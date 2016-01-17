package work;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GuidFinder {
    public static void main(String args[]) throws IOException {
//        System.out.println(Math.random());
        BufferedReader br = new BufferedReader(new FileReader("/Users/gaurav.se/Documents/cricket_dates.txt"));
        String s = br.readLine();
        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, Integer> months = new HashMap<>();
        months.put("January", 1);
        months.put("February", 2);
        months.put("March", 3);
        months.put("April", 4);
        months.put("May", 5);
        months.put("June", 6);
        months.put("July", 7);
        months.put("August", 8);
        months.put("September", 9);
        months.put("October", 10);
        months.put("November", 11);
        months.put("December", 12);
        int monthArray[][] = new int[6][13];
        boolean flag = false;
        int count = 0;
        int population = 0;
        while (s != null && !"".equals(s)) {
            if (flag) {
                //stringBuilder.append(s.substring(s.indexOf("wiki/") + 5, s.indexOf("title") - 2)).append('\n');
                String[] split = s.split(" ");
                List<String> collect = Arrays.stream(split).filter(c -> !c.contains("df")).filter(c -> !c.contains("in")).collect(Collectors.toList());
                split = collect.toArray(new String[collect.size()]);
                try {
                    int first = Integer.parseInt(split[0]);
                    if (first > 1000) {
                        int m = Integer.parseInt(split[1]);
                        int d = Integer.parseInt(split[2].substring(0, 1));
                        int y = Integer.parseInt(split[0]);
                        stringBuilder.append(y).append(',').append(m).append(',').append(d).append('\n');
                        int decade = ((y % 100) / 15) - 2;
                        //System.out.println(decade);
                        monthArray[decade][m]++;
                        population++;
                    } else {
                        Integer m = months.get(split[1]);
                        int d = Integer.parseInt(split[0]);
                        int y = Integer.parseInt(split[2].substring(0, 4));
                        stringBuilder.append(y).append(',').append(m).append(',').append(d).append('\n');
                        int decade = ((y % 100) / 15) - 2;
                        //System.out.println(decade);
                        monthArray[decade][m]++;
                        population++;
                    }
                    count++;
                } catch (Exception ignored) {
                }
            }
            s = br.readLine();
            flag = !flag;
        }
        System.out.println(stringBuilder);
        int next=0;
        for (int i = 0; i < monthArray.length; i++) {
            for (int j = 0; j < monthArray[i].length; j++) {
                System.out.println((++next) + "," + monthArray[i][j]);
            }
        }
        System.out.println(count);
        System.out.println(population);
//        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File("/Users/gaurav.se/Documents/cricketer_links.txt"))));
//        printWriter.println(stringBuilder.toString());
//        printWriter.close();
    }
}
