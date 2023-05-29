import core.Lines;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.List;

public class ParsingInConsole implements ParsingIn {
    private static List<Lines> line;
    private static String map;

    public ParsingInConsole(List<Lines> line, String map) {
        this.line = line;
        this.map = map;
    }

    @Override
    public void parsingIn() throws Exception {
        FileReader fileReader = new FileReader(map);
        JSONParser jsonObject = new JSONParser();
        JSONObject parse = (JSONObject) jsonObject.parse(fileReader);
        JSONObject massiveStations = (JSONObject) parse.get("Stations");

        for (int i = 0; i < line.size(); i++) {
            JSONArray oneLine = (JSONArray) massiveStations.get(line.get(i).getNumber());
            System.out.println("line: " + line.get(i).getNumber() + "-" + line.get(i).getName());
            System.out.println("size: " + oneLine.size());
        }
        fileReader.close();
    }
}
