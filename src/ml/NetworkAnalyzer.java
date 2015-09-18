package ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class NetworkAnalyzer {

    public static final int size = 200000;

    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/Users/gaurav.se/NetworkAnalysis.txt"));
        br.readLine();
        String s = br.readLine();
        String input[] = new String[size];
        int count = 0;
        do {
            input[count++] = s;
            s = br.readLine();
        }
        while (s != null);
        HashMap<String, User> users = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String[] fields = input[i].split("\\s+");
            if (fields[2].equals("0")) {
                User caller = users.get(fields[0]);
                if (caller == null) {
                    caller = new User(fields[0]);
                    caller.numberOfCalls++;
                    users.put(fields[0], caller);
                } else {
                    if (caller.numberOfCalls == 0) {
                        System.out.println(caller.phoneNumber
                                + " made his first call after being called "
                                + caller.numberOfReceptions + " times");
                    }
                    caller.numberOfCalls++;
                }
                User user = users.get(fields[1]);
                if (user == null) {
                    user = new User(fields[1]);
                    user.numberOfReceptions++;
                    users.put(fields[1], user);
                } else {
                    user.numberOfReceptions++;
                }
            }
        }

        System.out.println("USERS:");
        StringBuilder sb = new StringBuilder();
        for (HashMap.Entry<String, User> entry : users.entrySet()) {

            sb.append(entry.getKey()).append(" ").append(((double) entry.getValue().numberOfCalls) / (entry.getValue().numberOfCalls + entry.getValue().numberOfReceptions)).append('\n');
        }
        System.out.println(sb);
        //PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File("/Users/gaurav.se/NetworkAnalysis.txt")));
        br.close();
        //printWriter.close();
    }
}

