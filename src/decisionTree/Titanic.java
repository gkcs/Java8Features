package decisionTree;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Titanic {
    public static void main(String args[]) throws IOException {
        Titanic titanic = new Titanic();
        //titanic.findDistinctValuesInData();
        DecisionTreeAlgorithm.NO_OF_DIFFERENT_ATTRIBUTES = 11;
        DecisionTreeAlgorithm.NO_OF_DIFFERENT_OUTCOMES = 2;
        titanic.makeDecisionTree(new BufferedReader(new FileReader(new File("/Users/gaurav.se/Downloads/Titanic Kaggle/train.csv"))));
    }


    public void makeDecisionTree(final BufferedReader bufferedReader) throws IOException {
        final ArrayList<String[]> elements = new ArrayList<>();
        bufferedReader.readLine();
        String line = bufferedReader.readLine();
        while (line != null && !"".equals(line)) {
            StringBuilder sb = new StringBuilder();
            int index = 0;
            for (; index < line.length() && line.charAt(index) != '\"'; index++) {
                sb.append(line.charAt(index));
            }
            index++;
            for (; index < line.length() && line.charAt(index) != '\"'; index++) {
                if (line.charAt(index) != ',') {
                    sb.append(line.charAt(index));
                }
            }
            index++;
            for (; index < line.length(); index++) {
                sb.append(line.charAt(index));
            }
            line = sb.toString();
            while (line.split(",").length < 12) {
                line = line + "S,";
            }
            String[] split = line.split(",");
            elements.add(split);
            line = bufferedReader.readLine();
        }
        preprocessing(elements);
        DecisionTree decisionTree = new DecisionTree(elements, inspectData());
        System.out.println(decisionTree);
        System.out.println(Node.totalNodes);
        bufferedReader.close();
        BufferedReader queries = new BufferedReader(new FileReader(new File("/Users/gaurav.se/Downloads/Titanic Kaggle/test.csv")));
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File("/Users/gaurav.se/Downloads/Titanic Kaggle/classification.csv")));
        queries.readLine();
        printWriter.println("PassengerId,Survived");
        String s = queries.readLine();
        while (s != null && !s.equals("")) {
            String blocks[] = s.split(",");
            printWriter.println(blocks[0] + "," + decisionTree.classify(cleanData(blocks), decisionTree.getRoot()));
            s = queries.readLine();
        }
        printWriter.close();
    }

    private void preprocessing(ArrayList<String[]> elements) {
        for (int i = 0; i < elements.size(); i++) {
            elements.add(cleanData(elements.remove(0)));
        }
        for (String[] fields : elements) {
            String temp = fields[DecisionTreeAlgorithm.NO_OF_DIFFERENT_ATTRIBUTES];
            fields[DecisionTreeAlgorithm.NO_OF_DIFFERENT_ATTRIBUTES] = fields[1];
            fields[1] = temp;
            if ("".equals(fields[DecisionTreeAlgorithm.NO_OF_DIFFERENT_ATTRIBUTES])) {
                fields[DecisionTreeAlgorithm.NO_OF_DIFFERENT_ATTRIBUTES] = "0";
            }
        }
    }

    private String[] cleanData(String[] fields) {
        for (int i = 1; i < fields.length; i++) {
            //PassengerId,Survived,Pclass,Name,Sex,Age,SibSp,Parch,Ticket,Fare,Cabin,Embarked
            if (i == 3) {
                fields[i] = String.valueOf((fields[i].length() / 5));
            } else if (i == 4) {
                fields[i] = String.valueOf(fields[i].equals("male") ? 1 : 0);
            } else if (i == 5) {
                if (!"".equals(fields[i])) {
                    fields[i] = String.valueOf((int) (Double.parseDouble(fields[i]) / 8));
                }
            } else if (i == 8) {
                if (!"".equals(fields[i])) {
                    fields[i] = String.valueOf(fields[i].charAt(0));
                }
            } else if (i == 9) {
                if (!"".equals(fields[i])) {
                    fields[i] = String.valueOf((int) (Double.parseDouble(fields[i]) / 10));
                }
            } else if (i == 10) {
                if (!"".equals(fields[i])) {
                    fields[i] = fields[i].substring(0, 2 > fields[i].length() ? fields[i].length() : 2);
                }
            }
        }
        return fields;
    }

    private void showPositions(final String s) {
        final String[] fields = s.split(",");
        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i] + " " + i);
        }
    }

    public void findDistinctValuesInData() throws IOException {
        solve(getHashMaps());
    }

    public int[] inspectData() throws IOException {
        HashMap[] hashMaps = getHashMaps();
        final int counts[] = new int[hashMaps.length];
        for (int i = 1; i < DecisionTreeAlgorithm.NO_OF_DIFFERENT_ATTRIBUTES; i++) {
            counts[i] = hashMaps[i].keySet().size();
        }
        return counts;
    }

    private HashMap[] getHashMaps() throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/Users/gaurav.se/Downloads/Titanic Kaggle/train.csv")));
        final HashMap[] hashMaps = new HashMap[DecisionTreeAlgorithm.NO_OF_DIFFERENT_ATTRIBUTES + 1];
        for (int i = 0; i < hashMaps.length; i++) {
            hashMaps[i] = new HashMap<String, Integer>();
        }
        String s = bufferedReader.readLine();
        showPositions(s);
        s = bufferedReader.readLine();
        while (s != null && !"".equals(s)) {
            StringBuilder sb = new StringBuilder();
            int index = 0;
            for (; index < s.length() && s.charAt(index) != '\"'; index++) {
                sb.append(s.charAt(index));
            }
            index++;
            for (; index < s.length() && s.charAt(index) != '\"'; index++) {
                if (s.charAt(index) != ',') {
                    sb.append(s.charAt(index));
                }
            }
            index++;
            for (; index < s.length(); index++) {
                sb.append(s.charAt(index));
            }
            s = sb.toString();
            final String[] fields = cleanData(s.split(","));
            for (int i = 1; i < fields.length; i++) {
                //PassengerId,Survived,Pclass,Name,Sex,Age,SibSp,Parch,Ticket,Fare,Cabin,Embarked
                if (!"".equals(fields[i]) && !(i == 8 || i == 10 || i == 11)) {
                    Object o = hashMaps[i].get(((int) (Double.parseDouble(fields[i]))));
                    hashMaps[i].put((int) (Double.parseDouble(fields[i])), (o == null ? 0 : (Integer) o) + 1);
                } else {
                    Object o = hashMaps[i].get(fields[i]);
                    hashMaps[i].put(fields[i], (o == null ? 0 : (Integer) o) + 1);
                }
            }
            s = bufferedReader.readLine();
        }
        HashMap temp = hashMaps[DecisionTreeAlgorithm.NO_OF_DIFFERENT_ATTRIBUTES];
        hashMaps[DecisionTreeAlgorithm.NO_OF_DIFFERENT_ATTRIBUTES] = hashMaps[1];
        hashMaps[1] = temp;
        return hashMaps;
    }

    private void solve(final HashMap[] hashMaps) {
        for (int i = 0; i < hashMaps.length; i++) {
            if (!hashMaps[i].isEmpty()) {
                System.out.println("INDEX: " + i);
            }
            for (final Map.Entry entry : (Set<Map.Entry>) hashMaps[i].entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }
    }
}
