package ml;

import java.io.IOException;

public class Titanic {
    public static void main(String args[]) throws IOException {
        CSVReader csvReader = new CSVReader();
        System.out.println("SEX: ");
        System.out.println(csvReader.findCorrelation(Passenger::getSex));
        System.out.println("PASSENGER CLASS: ");
        System.out.println(csvReader.findCorrelation(Passenger::getPassengerClass));
        System.out.println("AGE:");
        System.out.println(csvReader.findCorrelation(Passenger::getAge));
        System.out.println("SIB SP:");
        System.out.println(csvReader.findCorrelation(Passenger::getSibSp));
        System.out.println("P ARCH:");
        System.out.println(csvReader.findCorrelation(Passenger::getpArch));
        System.out.println("FARE:");
        System.out.println(csvReader.findCorrelation(Passenger::getFare));
        System.out.println("EMBARKED:");
        System.out.println(csvReader.findCorrelation(Passenger::getEmbarked));
        System.out.println("NAME:");
        System.out.println(csvReader.findCorrelation(c -> c.getName().indexOf('(') >= 0 ? 1 : 0));

        System.out.println("NAME AND CLASS:");
        System.out.println(csvReader.findCorrelation(Passenger::getPassengerClass, Passenger::getSex));
    }
}
