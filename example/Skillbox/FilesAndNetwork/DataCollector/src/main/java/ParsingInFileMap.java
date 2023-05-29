import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.Lines;
import core.Stations;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ParsingInFileMap implements ParsingIn {

    private String map;
    private List<Stations> station;
    private List<Lines> line;

    public ParsingInFileMap(String map, List<Stations> station, List<Lines> line) {
        this.map = map;
        this.station = station;
        this.line = line;
    }

    @Override
    public void parsingIn() throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JSONObject mapAdd = new JSONObject();
        JSONObject lineAndStations = new JSONObject();
        List<Object> lines = new ArrayList<>();
        for (Lines lines1 : line) {
            JSONObject addLine = new JSONObject();
            addLine.put("name", lines1.getName());
            addLine.put("number", lines1.getNumber());
            lines.add(addLine);
            List<String> stationsList = new ArrayList<>();
            station.stream().filter(s -> lines1.getNumber().equals(s.getLineNumber()))
                    .forEach(s -> stationsList.add(s.getName()));
            lineAndStations.put(lines1.getNumber(), stationsList);
        }
        mapAdd.put("Stations", lineAndStations);
        mapAdd.put("lines", lines);
        PrintWriter out = new PrintWriter(new FileWriter(map));
        out.write(gson.toJson(mapAdd));
        out.close();
    }
}
