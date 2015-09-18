package ml;

import java.io.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;

class LCSin {
    private InputStream stream;
    private byte[] buf = new byte[1024];
    private int curChar;
    private int numChars;

    public LCSin(InputStream stream) {
        this.stream = stream;
    }

    public int read() {
        if (numChars == -1)
            throw new InputMismatchException();
        if (curChar >= numChars) {
            curChar = 0;
            try {
                numChars = stream.read(buf);
            } catch (IOException e) {
                throw new InputMismatchException();
            }
            if (numChars <= 0)
                return -1;
        }
        return buf[curChar++];
    }

    public int readInt() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        int sgn = 1;
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        int res = 0;
        do {
            if (c < '0' || c > '9')
                throw new InputMismatchException();
            res *= 10;
            res += c - '0';
            c = read();
        } while (!isSpaceChar(c));
        return res * sgn;
    }

    public boolean isSpaceChar(int c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
    }

    public String readString() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        StringBuilder res = new StringBuilder();
        do {
            res.appendCodePoint(c);
            c = read();
        }
        while (!isSpaceChar(c));
        return res.toString();
    }

    public long readLong() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        int sgn = 1;
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        long res = 0;
        do {
            if (c < '0' || c > '9')
                throw new InputMismatchException();
            res *= 10;
            res += c - '0';
            c = read();
        }
        while (!isSpaceChar(c));
        return res * sgn;
    }
}

public class FileFormatter {
    private final static Calendar calendar = Calendar.getInstance();

    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/Users/gaurav.se/Documents/machine-learning.txt"));
        br.readLine();
        String s = br.readLine();
        Call calls[] = new Call[482797];
        int count = 0;
        for (int i = 0; i < 500000; i++) {
            String[] fields = s.split("\\s+");
            if (!fields[2].equals("NULL")) {
                if (fields[5].equals("NULL")) {
                    fields[5] = "";
                }
                if (fields[9].equals("NULL")) {
                    fields[9] = "0";
                }
                calendar.setTime(new Date(Long.parseLong(fields[4]) * 1000));
                final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                calls[count++] = new Call(fields[1].equals(fields[11]),
                        Double.parseDouble(fields[2]),
                        dayOfWeek != 1 && dayOfWeek != 7,
                        fields[12].equals(fields[5]),
                        fields[7],
                        fields[6],
                        fields[2],
                        fields[11],
                        Integer.parseInt(fields[9]));
            }
            s = br.readLine();
        }
        System.out.println(count);
        Arrays.parallelSort(calls);
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File("/Users/gaurav.se/Documents/DataDump.txt")));
        StringBuilder stringBuilder = new StringBuilder();
        for (Call call : calls) {
            stringBuilder.append(call.toString()).append('\n');
        }
        printWriter.write(stringBuilder.toString());
        br.close();
        printWriter.close();
    }
}

class Call implements Comparable<Call> {
    private final boolean localCall;
    private final double sellingPrice;
    private final boolean weekDay;
    private final boolean callerIdRecognized;
    private final int total_talk_time;
    private final String status;
    private final String call_flow;
    private final String source_country;
    private final String destination_country;

    public Call(boolean localCall,
                double sellingPrice,
                boolean weekDay,
                boolean callerIdRecognized,
                String status,
                String call_flow,
                String source_country,
                String destination_country,
                int total_talk_time) {
        this.localCall = localCall;
        this.sellingPrice = sellingPrice;
        this.weekDay = weekDay;
        this.callerIdRecognized = callerIdRecognized;
        this.status = status;
        this.call_flow = call_flow;
        this.source_country = source_country;
        this.destination_country = destination_country;
        this.total_talk_time = total_talk_time;
    }

    @Override
    public String toString() {
        return (localCall ? 1 : 0)
                + ", " + sellingPrice
                + ", " + (weekDay ? 1 : 0)
                + ", " + (callerIdRecognized ? 1 : 0)
                + ", " + total_talk_time
                + ", " + status
                + ", " + call_flow
                + ", " + source_country
                + ", " + destination_country;
    }

    @Override
    public int compareTo(Call other) {
        if (status == null || status.equals(other.status)) {
            if (call_flow == null || call_flow.equals(other.call_flow)) {
                if (source_country == null || source_country.equals(other.source_country)) {
                    return destination_country == null ? 0 : destination_country.compareTo(other.destination_country);
                } else {
                    return source_country.compareTo(other.source_country);
                }
            } else {
                return call_flow.compareTo(other.call_flow);
            }
        } else {
            return status.compareTo(other.status);
        }
    }
}
