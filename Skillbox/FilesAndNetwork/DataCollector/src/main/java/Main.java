public class Main {
    private static final String LINK = "https://skillbox-java.github.io/";
    private static final String FILE = "D:/application/java/a/FilesAndNetwork/DataCollector/data/";
    private static final String MAP_FILE = "D:/application/java/a/FilesAndNetwork/DataCollector/src/main/resources/map.json";
    private static final String STATIONS_FILE = "D:/application/java/a/FilesAndNetwork/DataCollector/src/main/resources/stations.json";

    public static void main(String[] args) {

        Index index = new Index();
        try {
            index.parsingFiles(FILE);
            index.parsingHtml(LINK);
            ParsingInFileMap parsingInFileMap = new ParsingInFileMap(MAP_FILE, index.station, index.line);
            parsingInFileMap.parsingIn();
            ParsingInFileStations parsingInFileStations = new ParsingInFileStations(
                    index.hasConnection,
                    index.station,
                    index.line,
                    index.nameAndDate,
                    index.nameAndDepth,
                    STATIONS_FILE);
            parsingInFileStations.parsingIn();
            ParsingInConsole parsingCount = new ParsingInConsole(index.line, MAP_FILE);
            parsingCount.parsingIn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
