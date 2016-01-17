package work;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileFormatter {
    public static void main(String args[]) throws IOException {
        boolean flag = false;
        String clotho = flag ? "-clotho" : "";
        BufferedReader br = new BufferedReader(new FileReader("/Users/gaurav.se/records" + clotho + ".txt"));
        StringBuilder stringBuilder = new StringBuilder();
        String s = br.readLine();
        stringBuilder.append(Arrays.stream(s.split("\t")).collect(Collectors.joining(","))).append('\n');
        s = br.readLine();
        int lineNumber = 0;
        while (s != null && !"".equals(s)) {
            lineNumber++;
            if (!(s.contains("init_failed") || s.contains("stuck_cal") || s.contains("terminated_us") || s.contains("manual_termination") || s.contains("manually_terminated"))
                    && isNumber(s.split("\t")[2])
                    && Integer.parseInt(s.split("\t")[2]) > 0) {
                //System.out.println(lineNumber);
                if (s.contains("stuck_cal") || s.contains("terminated_us") || s.contains("manual_termination") || s.contains("manually_terminated")) {
                    String vals[] = s.split("\t");
                    List<String> list = new ArrayList<>();
                    if (!isNumber(vals[0])) {
                        vals[0] = "NULL";
                    }
                    if (!isNumber(vals[1])) {
                        vals[1] = "NULL";
                    }
                    if (!isNumber(vals[2]) || Integer.parseInt(vals[2]) == 0) {
                        vals[2] = "NULL";
                    }
                    int i = 0;
                    for (; i < vals.length && !(vals[i].contains("stuck_cal") || vals[i].contains("terminated_us") || vals[i].contains("manual_termination") || vals[i].contains("manually_terminated")); i++) {
                        list.add(vals[i]);
                    }
                    for (; i < vals.length && (vals[i].contains("stuck_cal") || vals[i].contains("terminated_us") || vals[i].contains("manual_termination") || vals[i].contains("manually_terminated")); i++) {
                    }
                    if (i < vals.length) {
                        list.add("TRUE");
                    }
                    for (; i < vals.length; i++) {
                        list.add(vals[i]);
                    }
                    stringBuilder.append(list.stream().collect(Collectors.joining(","))).append('\n');
                } else {
                    stringBuilder.append(Arrays.stream(s.split("\t")).collect(Collectors.joining(","))).append('\n');
                }
            }
            s = br.readLine();
        }
//        System.out.println(stringBuilder);
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File("/Users/gaurav.se/calls" + clotho + ".txt")));
        printWriter.write(stringBuilder.toString());
        br.close();
        printWriter.close();
    }

    private static boolean isNumber(String val) {
        try {
            int parse = Integer.parseInt(val);
            if (parse < 0) {
                throw new RuntimeException("Negative value");
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
