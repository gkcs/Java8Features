package decisionTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CsvConvertor {
    public static void main(String args[]) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String s = bufferedReader.readLine();
        while (s != null && !"".equals(s)) {
            String[] split = s.split(",");
            for (int i = 0; i < split.length; i++) {
                if (split[i].length() > 0 && split[i].charAt(0) != '\"') {
                    split[i] = "\"" + split[i] + "\"";
                } else if (split[i].length() == 0) {
                    split[i] = "\"\"";
                }
            }
            System.out.print("elements.add(new String[]{");
            System.out.print(Arrays.stream(split).collect(Collectors.joining(",")));
            System.out.println("});");
            s = bufferedReader.readLine();
        }
    }
}

//        2,1,"D3",10,0.076923077,2,1,1,0.641791045,0.581818182,0.148535565,0.323007976,0.028,12,1,0,3,,1,2,6,3,1,2,1,1,1,3,1,0.000666667,1,1,2,2,,0.598039216,,0.526785714,4,112,2,1,1,3,2,2,1,,3,2,3,3,240,3,3,1,1,2,1,2,3,,1,3,3,1,3,2,3,,1,3,1,2,2,1,3,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8
//        5,1,"A1",26,0.076923077,2,3,1,0.059701493,0.6,0.131799163,0.272287744,0,1,3,0,2,0.0018,1,2,6,3,1,2,1,2,1,3,1,0.000133333,1,3,2,2,0.188405797,,0.084507042,,5,412,2,1,1,3,2,2,1,,3,2,3,3,0,1,3,1,1,2,1,2,3,,1,3,3,1,3,2,3,,3,1,1,2,2,1,3,3,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4
//        6,1,"E1",26,0.076923077,2,3,1,0.029850746,0.745454545,0.288702929,0.428780429,0.03,9,1,0,2,0.03,1,2,8,3,1,1,1,2,1,1,3,,3,2,3,3,0.304347826,,0.225352113,,10,3,2,2,1,3,2,2,2,,3,2,3,3,,1,3,1,1,2,1,2,3,,2,2,3,1,3,2,3,,3,3,1,3,2,1,3,3,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8