package ml;

import java.io.IOException;

public class Titanic {
    public static void main(String args[]) throws IOException {
        CSVReader csvReader = new CSVReader();
        System.out.println(csvReader.findNameCorrelation());
    }
}
