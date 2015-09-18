package work;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GuidFinder {
    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s;
        StringBuilder stringBuilder = new StringBuilder();
        while (!"".equals(s = br.readLine())) {
            stringBuilder.append('"').append(s).append("@atropos.voodoo.com").append('"').append(',');
        }
        System.out.println(stringBuilder.toString());
    }
}
