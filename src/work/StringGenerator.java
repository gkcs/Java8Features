package work;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class StringGenerator {

    private SecureRandom prng;
    private char[] alphanums = new char[36];

    public StringGenerator() throws NoSuchAlgorithmException {
        prng = SecureRandom.getInstance("SHA1PRNG");
        for (int i = 0; i < 26; i++) {
            alphanums[i] = (char) ('a' + i);
            if (i < 10) {
                alphanums[26 + i] = (char) (i + '0');
            }
        }
    }

    protected synchronized String getRandom(char[] chars, int len) {

        int limit = (int) Math.pow(2, Math.log(chars.length));
        int diff = chars.length - limit;

        for (int i = 0; i < diff; i++) {
            int idx = prng.nextInt(limit);
            char temp = chars[limit + i];
            chars[limit + i] = chars[idx];
            chars[idx] = temp;
        }

        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = chars[prng.nextInt(limit)];
        }
        return new String(str);
    }

    public String randomAlphanum(int length) {
        return getRandom(alphanums, length);
    }

    public static void main(String args[]) throws NoSuchAlgorithmException {
        Alphabet a[] = new Alphabet[36];
        for (int i = 0; i < a.length; i++) {
            a[i] = new Alphabet();
            a[i].index = i;
        }
        StringGenerator stringGenerator = new StringGenerator();
        for (int i = 0; i < 100000; i++) {
            String token = stringGenerator.randomAlphanum(32);
            for (int j = 0; j < token.length(); j++) {
                if (token.charAt(j) <= 'z' && token.charAt(j) >= 'a') {
                    a[token.charAt(j) - 'a'].val++;
                } else {
                    a[token.charAt(j) - '0' + 26].val++;
                }
            }
        }
        Arrays.parallelSort(a);
        System.out.println(Arrays.toString(a));
    }
}

class Alphabet implements Comparable<Alphabet> {
    int val, index;

    @Override
    public String toString() {
        return "{" +
                "val=" + val +
                ", index=" + (index < 26 ? String.valueOf((char) ('a' + index)) : String.valueOf((char) ('0' + index - 26))) +
                '}';
    }

    @Override
    public int compareTo(Alphabet other) {
        return val - other.val;
    }
}