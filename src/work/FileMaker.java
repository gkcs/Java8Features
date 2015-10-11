package work;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public class FileMaker {

    public static void main(String args[]) throws IOException, ParseException {
        final BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/gaurav.se/Documents/Guids.txt"));
        final StringBuilder sb = new StringBuilder();
        for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) {
            if (s.length() > 0 && s.charAt(0) == '|') {
                String s1 = s.split("@a")[0];
                sb.append(s1.split(" ")[1]).append('\n');
            } else {
                sb.append(s).append('\n');
            }
        }
        bufferedReader.close();
        System.out.println(sb);
    }

    public static class Record {
        private final String time;
        private final boolean entry;

        public Record(boolean entry, String time) {
            this.time = time;
            this.entry = entry;
        }

        @Override
        public String toString() {
            return "Record{" +
                    "time='" + time + '\'' +
                    ", entry=" + entry +
                    '}';
        }
    }
}
//    for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) {
//        String csv[] = call.split(",");
//        String records[] = s.replaceAll("\\s+", "").split(",");
//        Date d = df.parse(records[6]);
//        Long time = d.getTime();
//        time += Long.parseLong(records[2]) * 1000;
//        String date = df2.format(new Date(time));
//        csv[1] = "\"" + records[4] + "\"";
//        csv[2] = "\"" + records[5] + "\"";
//        csv[4] = "\"" + df2.format(d) + "\"";
//        csv[5] = "\"" + df2.format(d) + "\"";
//        csv[6] = "\"" + date + "\"";
//        csv[7] = "\"" + records[2] + "\"";
//        csv[8] = "\"" + records[2] + "\"";
//        csv[10] = "\"" + records[8] + "\"";
//        csv[11] = "\"" + records[7] + "\"";
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(csv[0]);
//        for (int i = 1; i < csv.length; i++) {
//            stringBuilder.append(',');
//            stringBuilder.append(csv[i]);
//        }
//        bufferedWriter.write(stringBuilder.toString());
//    }

//        String previous = bufferedReader.readLine().replaceAll("\\s+", "").split(",")[7];
//        csv[0] = "\"" + records[10] + "\"";
//        csv[1] = "\"" + previous + "\"";
//        csv[2] = "\"" + records[7] + "\"";
//        previous = records[7];
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(csv[0]);
//        for (int i = 1; i < csv.length; i++) {
//        stringBuilder.append(',');
//        stringBuilder.append(csv[i]);
//        }
//        bufferedWriter.write(stringBuilder.toString());
//        bufferedWriter.write('\n');

//StringBuilder stringBuilder = new StringBuilder();
//for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) {
//final String[] split = s.replace('|', ',').replaceAll("\\s+", "").split(",");
//        stringBuilder.append(split[0]).append(',');
//        stringBuilder.append(split[1]).append(',');
//        stringBuilder.append(split[2]).append(',');
//        stringBuilder.append(userReader.readLine().replace('|', ',').replaceAll("\\s+", "").split(",")[2].split("@")[0]).append('\n');
//        }
//        bufferedWriter.write(stringBuilder.toString());