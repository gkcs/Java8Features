package ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private final List<Passenger> passengerList = new ArrayList<>();

    //PassengerId,Survived,Pclass,Name,Sex,Age,SibSp,Parch,Ticket,Fare,Cabin,Embarked
    public CSVReader() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/gaurav.se/Downloads/Titanic Kaggle/train.csv"));
        System.out.println(bufferedReader.readLine());
        for (String s = bufferedReader.readLine(); s != null && !s.equals(""); s = bufferedReader.readLine()) {
            System.out.println(s);
            String attributes[] = s.split(",");
            passengerList.add(new Passenger(Integer.parseInt(attributes[0]),
                    Integer.parseInt(attributes[1]),
                    Integer.parseInt(attributes[2]),
                    attributes[3] + attributes[4],
                    attributes[5].equalsIgnoreCase("male") ? 0 : 1,
                    Double.parseDouble(attributes[6].equals("") ? "0" : attributes[6]),
                    Integer.parseInt(attributes[7]),
                    Integer.parseInt(attributes[8]),
                    attributes[9],
                    Double.parseDouble(attributes[10]),
                    attributes[11],
                    (attributes.length <= 12 || attributes[12] == null) ? 0 : (attributes[12].equals("S") ? 0 : (attributes[12].equals("Q") ? 1 : 2))
            ));
        }
    }

    public double findNameCorrelation() {
        final CorrelationFinder correlationFinder = new CorrelationFinder();
        //final int attribute[] = new int[passengerList.size()];
        final double attribute[] = new double[passengerList.size()];
        final int survived[] = new int[passengerList.size()];
        for (int i = 0; i < passengerList.size(); i++) {
            attribute[i] = passengerList.get(i).getName().indexOf('(') > 0 ? 1 : 0;
            survived[i] = passengerList.get(i).getSurvived();
        }
        return correlationFinder.findCorrelation(attribute, survived);
    }
}

class Passenger {
    private final int id;
    private final int survived;
    private final int passengerClass;
    private final String name;
    private final int sex;
    private final double age;
    private final int sibSp;

    public int getId() {
        return id;
    }

    public int getSurvived() {
        return survived;
    }

    public int getPassengerClass() {
        return passengerClass;
    }

    public String getName() {
        return name;
    }

    public int getSex() {
        return sex;
    }

    public double getAge() {
        return age;
    }

    public int getSibSp() {
        return sibSp;
    }

    public int getpArch() {
        return pArch;
    }

    public String getTicket() {
        return ticket;
    }

    public double getFare() {
        return fare;
    }

    public String getCabin() {
        return cabin;
    }

    public int getEmbarked() {
        return embarked;
    }

    private final int pArch;
    private final String ticket;
    private final double fare;
    private final String cabin;
    private final int embarked;

    Passenger(int id, int survived, int passengerClass,
              String name, int sex, double age, int sibSp,
              int pArch, String ticket, double fare,
              String cabin, int embarked) {
        this.id = id;
        this.survived = survived;
        this.passengerClass = passengerClass;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.sibSp = sibSp;
        this.pArch = pArch;
        this.ticket = ticket;
        this.fare = fare;
        this.cabin = cabin;
        this.embarked = embarked;
    }
}