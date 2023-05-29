import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.Lines;
import core.Stations;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParsingInFileStations implements ParsingIn {

    private List<Stations> station;
    private List<Lines> line;
    private Map<String, String> nameAndDate;
    private Map<String, String> nameAndDepth;
    private String hasConnection;
    private String stations;
    private static final String REGEX_DOUBLE = "[-]?[0-9]{1,2}[','][0-9]{0,}";
    private static final String REGEX_LONG = "[-]?[0-9]{1,2}";

    public ParsingInFileStations(String hasConnection,
                                 List<Stations> station,
                                 List<Lines> line,
                                 Map<String, String> nameAndDate,
                                 Map<String, String> nameAndDepth,
                                 String stations) {
        this.station = station;
        this.line = line;
        this.nameAndDate = nameAndDate;
        this.nameAndDepth = nameAndDepth;
        this.hasConnection = hasConnection;
        this.stations = stations;
    }

    @Override
    public void parsingIn() throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JSONObject stationParameters = new JSONObject();
        List<Object> parameters = new ArrayList<>();
        for (Stations stations1 : station) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", stations1.getName());
            line.stream().filter(l -> stations1.getLineNumber().equals(l.getNumber()))
                    .forEach(l -> jsonObject.put("line", l.getName()));
            if (nameAndDate.containsKey(stations1.getName())) {
                jsonObject.put("date", nameAndDate.get(stations1.getName()));
            }
            if (nameAndDepth.containsKey(stations1.getName())) {
                String depth = nameAndDepth.get(stations1.getName()).replaceAll("[^\\−\\d\\,]+", "")
                        .replaceAll("[\\−]", "-");
                if (depth.matches(REGEX_DOUBLE)) {
                    double d = Double.parseDouble(depth.replace(',', '.'));
                    jsonObject.put("depth", d);
                }
                if (depth.matches(REGEX_LONG)) {
                    long d = Long.parseLong(depth);
                    jsonObject.put("depth", d);
                }
            }

            if (hasConnection.contains(stations1.getName())) {
                jsonObject.put("hasConnection", true);
            } else {
                jsonObject.put("hasConnection", false);
            }
            parameters.add(jsonObject);
            stationParameters.put("Stations", parameters);
        }
        PrintWriter out = new PrintWriter(new FileWriter(stations));
        out.write(gson.toJson(stationParameters));
        out.close();
    }
}
