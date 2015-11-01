package ml;

public class LCMFinder {
    public static void main(String args[]) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 6284; i < 12504; i++) {
            stringBuilder.append(i).append(',').append('A').append('\n');
        }
        System.out.println(stringBuilder);
    }
}