import java.io.IOException;

public class BichromeBoard {
    public static void main(String args[]) throws IOException {
        final InputReader br = new InputReader(System.in);
        final int cities = br.readInt(), roads = br.readInt();
        final int[] from = new int[roads], to = new int[roads], distance = new int[roads];
        for (int i = 0; i < roads; i++) {
            from[i] = br.readInt();
            to[i] = br.readInt();
            distance[i] = br.readInt();
        }
        final int orders = br.readInt();
        final int[] source = new int[orders], destination = new int[orders], weight = new int[orders], income = new int[orders];
        for (int i = 0; i < orders; i++) {
            source[i] = br.readInt();
            destination[i] = br.readInt();
            weight[i] = br.readInt();
            income[i] = br.readInt();
        }
        final int STARTING_POINT = br.readInt(), FUEL = br.readInt(), CAPACITY = br.readInt();
        System.out.println(0);
    }

}