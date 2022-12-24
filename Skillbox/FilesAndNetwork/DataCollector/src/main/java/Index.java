import core.Lines;
import core.Stations;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Index {
    public final List<Stations> station;
    public final List<Lines> line;
    public final List<File> fileList;
    public final Map<String, String> nameAndDate;
    public final Map<String, String> nameAndDepth;
    public String hasConnection = "";
    private static final String TAG_NAME = "name";
    private static final String TAG_DATE = "date";
    private static final String TAG_DEPTH = "depth";
    private static final String TAG_DEPTH_METERS = "depth_meters";
    private static final String TAG_STATION_NAME = "station_name";
    private static final String TAG_JSON = ".json";
    private static final String TAG_CSV = ".csv";
    private static final String REGEX = "[0-9]{2}[.][0-9]{2}[.][0-9]{4}";

    public Index() {
        station = new ArrayList<>();
        line = new ArrayList<>();
        fileList = new ArrayList<>();
        nameAndDate = new HashMap<>();
        nameAndDepth = new HashMap<>();
    }

    public void parsingHtml(String link) throws Exception {
        Document document = Jsoup.connect(link).get();
        Elements map = document.select("#metrodata");
        Elements lines = map.select("div[data-depend]");

        for (int i = 0; i < lines.size(); i++) {
            Element nameLine = lines.get(i);
            String lineNumber = nameLine.select("span[data-line]").attr("data-line");
            Elements stationsName = map.select("div[data-line=" + lineNumber + "]")
                    .select("span[class=name]");
            hasConnection += map.select("div[data-line=" + lineNumber + "]")
                    .select("span[title]").attr("title");
            stationsName.forEach(s -> station.add(new Stations(s.text(), lineNumber)));
            line.add(new Lines(nameLine.text(), lineNumber));
        }
    }

    private void parsingJsonFile(File file) throws Exception {
        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader(file);
        JSONArray jsonArray = (JSONArray) parser.parse(reader);
        for (Object it : jsonArray) {
            JSONObject jsonObject = (JSONObject) it;
            if (jsonObject.get(TAG_NAME) != null && jsonObject.get(TAG_DATE) != null) {
                String name = (String) jsonObject.get(TAG_NAME);
                String date = (String) jsonObject.get(TAG_DATE);
                nameAndDate.put(name, date);
            } else if (jsonObject.get(TAG_STATION_NAME) != null && jsonObject.get(TAG_DEPTH_METERS) != null) {
                String station_name = (String) jsonObject.get(TAG_STATION_NAME);
                Object depth_meters = jsonObject.get(TAG_DEPTH_METERS);
                nameAndDepth.put(station_name, depth_meters.toString());
            } else if (jsonObject.get(TAG_NAME) != null && jsonObject.get(TAG_DEPTH) != null) {
                String name = (String) jsonObject.get(TAG_NAME);
                Object depth = jsonObject.get(TAG_DEPTH);
                nameAndDepth.put(name, depth.toString());
            }
        }
    }

    private String parsingCsvFiles(File file) throws Exception {
        File file1 = new File(file.toURI());
        Scanner scanner = new Scanner(file1);
        while (scanner.hasNextLine()) {
            String string = scanner.nextLine();
            String[] strings = string.split(",");
            if (strings[1].matches(REGEX)) {
                return nameAndDate.put(strings[0], strings[1]);
            }
            return nameAndDepth.put(strings[0], strings[1]);
        }
        scanner.close();
        return null;
    }

    public void parsingFiles(String file) throws Exception {
        File files = new File(file);
        searchFile(files);
        formatFiles();
    }

    private void searchFile(File file) {
        if (file.isDirectory()) {
            File[] fileDirectory = file.listFiles();
            if (fileDirectory != null) {
                for (File isFile : fileDirectory) {
                    if (isFile.isDirectory()) {
                        searchFile(isFile);
                    } else {
                        if ((isFile.getName().toLowerCase().endsWith(TAG_JSON)) ||
                                (isFile.getName().toLowerCase().endsWith(TAG_CSV))) {
                            fileList.add(isFile);
                        }
                    }
                }
            }
        }
    }

    private void formatFiles() throws Exception {
        for (File oneFile : fileList) {
            if (oneFile.getName().toLowerCase().endsWith(TAG_JSON)) {
                parsingJsonFile(oneFile);
            } else {
                parsingCsvFiles(oneFile);
            }
        }
    }
}
