package core;

public class Stations {

    private String name;
    private String lineNumber;

    public Stations(String name, String lineNumber) {
        this.name = name;
        this.lineNumber = lineNumber;
    }

    public String getName() {
        return name;
    }

    public String getLineNumber() {
        return lineNumber;
    }
}
