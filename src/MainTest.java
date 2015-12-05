import org.junit.Test;

public class MainTest {
    private final boolean randomize = true;

    @Test
    public void test() {
        final int board[][] = new int[][]{{5, 5, 5, 5, 5}, {3, 9, 5, 5, 3}, {6, 12, 5, 3, 10}, {3, 9, 5, 6, 12}, {12, 4, 7, 9, 3}};
        final Strategy strategy = new Strategy(board.length, randomize);
        System.out.println(strategy.input(board));
    }

    @Test
    public void testBoard() {
        final int board[][] = new int[][]{{5, 0, 0, 0, 0}, {1, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};
        final Strategy strategy = new Strategy(board.length, randomize);
        System.out.println(strategy.input(board));
    }

    @Test
    public void test3() {
        final int board[][] = new int[][]{{5, 5, 5}, {5, 5, 5}, {5, 5, 5}};
        final Strategy strategy = new Strategy(board.length, randomize);
        System.out.println(strategy.input(board));
    }

    @Test
    public void test4() {
        final int board[][] = new int[][]{{5, 5, 5, 5, 5}, {1, 1, 1, 1, 1}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};
        final Strategy strategy = new Strategy(board.length, randomize);
        System.out.println(strategy.input(board));
    }

    @Test
    public void test5() {
        final int board[][] = new int[][]{{5, 5, 5, 5, 6}, {3, 9, 5, 5, 1}, {2, 8, 1, 5, 0}, {0, 2, 12, 5, 6}, {8, 0, 5, 1, 1}};
        final Strategy strategy = new Strategy(board.length, randomize);
        System.out.println(strategy.input(board));
    }

    @Test
    public void test6() {
        final int board[][] = new int[][]{{5, 5, 5, 5, 5}, {3, 9, 3, 9, 5}, {12, 6, 12, 0, 3}, {15, 9, 3, 10, 10}, {5, 6, 12, 6, 12}};
        final Strategy strategy = new Strategy(board.length, randomize);
        System.out.println(strategy.input(board));
    }

    @Test
    public void test7() {
        final int board[][] = new int[][]{{5, 5, 5, 5, 5}, {5, 1, 5, 3, 9}, {1, 0, 3, 8, 6}, {0, 0, 2, 8, 1}, {0, 0, 0, 0, 4}};
        final Strategy strategy = new Strategy(board.length, randomize);
        System.out.println(strategy.input(board));
    }

    @Test
    public void test8() {
        final int board[][] = new int[][]{{15, 9, 7, 15, 15}, {15, 12, 7, 15, 15}, {15, 9, 3, 15, 15}, {5, 6, 10, 15, 15}, {15, 9, 6, 15, 15}};
        final Strategy strategy = new Strategy(board.length, randomize);
        System.out.println(strategy.input(board));
    }

    @Test
    public void test9() {
        final int board[][] = new int[][]{{15, 15, 15, 15, 15}, {15, 15, 15, 15, 15}, {15, 15, 15, 15, 15}, {15, 9, 3, 15, 15}, {15, 12, 6, 15, 15}};
        final Strategy strategy = new Strategy(board.length, randomize);
        System.out.println(strategy.input(board));
    }

    @Test
    public void test10() {
        final int board[][] = new int[][]{{15, 15, 10, 15, 15}, {15, 15, 10, 9, 5}, {9, 5, 6, 12, 3}, {10, 9, 5, 3, 12}, {6, 10, 9, 6, 15}};
        final Strategy strategy = new Strategy(board.length, randomize);
        System.out.println(strategy.input(board));
    }

    @Test
    public void test11() {
        final int board[][] = new int[][]{{15, 15, 15, 15, 15},{15, 15, 15, 15, 15},{15, 15, 15, 15, 15},{15, 15, 15, 15, 15},{15, 13, 3, 15, 15}};
        final Strategy strategy = new Strategy(board.length, randomize);
        Edge edge = strategy.input(board);
        System.out.println(edge.x + " " + edge.y + " " + edge.side);
    }

    @Test
    public void testStuck() {
        final int board[][] = new int[][]{{15, 9, 5, 6, 15}, {15, 12, 5, 5, 3}, {9, 5, 5, 5, 6}, {12, 5, 5, 5, 3}, {15, 15, 9, 7, 12}};
        final Strategy strategy = new Strategy(board.length, randomize);
        System.out.println(strategy.input(board));
    }

    @Test
    public void test12() {
        final int board[][] = new int[][]{{5, 5, 5, 3, 15}, {9, 5, 5, 4, 15}, {6, 15, 15, 15, 11}, {15, 15, 15, 15, 12}, {7, 15, 15, 15, 15}};
        final Strategy strategy = new Strategy(board.length, randomize);
        Edge edge = strategy.input(board);
        System.out.println(edge.x + " " + edge.y + " " + edge.side);
    }

    @Test
    public void test13() {
        final int board[][] = new int[][]{{15, 9, 3, 10, 15}, {9, 6, 12, 6, 11}, {12, 3, 9, 1, 6}, {5, 6, 10, 10, 15}, {15, 15, 12, 6, 15}};
        final Strategy strategy = new Strategy(board.length, randomize);
        Edge edge = strategy.input(board);
        System.out.println(edge.x + " " + edge.y + " " + edge.side);
    }
}