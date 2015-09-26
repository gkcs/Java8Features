import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

class InputReader {
    private InputStream stream;
    private byte[] buf = new byte[1024];
    private int curChar;

    private int numChars;

    public InputReader(InputStream stream) {
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

class TaskManager {
    private List<Thread> tasks = new ArrayList<>();

    public void acceptTask(Runnable task) {
        tasks.add(new Thread(task));
    }

    private void startAllTasks() {
        tasks.stream().forEach(Thread::start);
    }

    private void waitForAllToComplete() {
        tasks.stream().forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void removeAllTasks() {
        tasks = new ArrayList<>();
    }

    public void completeAllTasks() {
        startAllTasks();
        waitForAllToComplete();
        removeAllTasks();
    }
}

public class Main {
    public static void main(String args[]) throws IOException {
        final InputReader reader = new InputReader(System.in);
        final int testCases = reader.readInt();
        final int[] inputNumber = new int[testCases];
        final Solver solver = new Solver();
        final TaskManager taskManager = new TaskManager();
        taskManager.acceptTask(solver::findPrimes);
        taskManager.acceptTask(() -> {
            for (int i = 0; i < testCases; i++) {
                inputNumber[i] = reader.readInt();
            }
        });
        taskManager.completeAllTasks();
        final Boolean[] results = new Boolean[testCases];
        final int noOfThreads = Runtime.getRuntime().availableProcessors();
        for (int thread = 0; thread < noOfThreads; thread++) {
            final int threadIndex = thread;
            taskManager.acceptTask(() -> {
                for (int i = threadIndex; i < testCases; i = i + noOfThreads) {
                    results[i] = solver.solve(inputNumber[i]);
                }
            });
        }
        taskManager.completeAllTasks();
        System.out.println(Arrays.stream(results).map(isPrime -> isPrime ? "YES" : "NO").collect(Collectors.joining("\n")));
    }

}

class Solver {
    private static final int NUMBER_RANGE = 1000000;
    private int primes[] = new int[78497];

    public void findPrimes() {
        final boolean sieve[] = new boolean[NUMBER_RANGE];
        int count = 0;
        for (int i = 3; i < NUMBER_RANGE; i = i + 2) {
            if (!sieve[i]) {
                primes[count++] = i;
                for (int j = i * 3; j < NUMBER_RANGE; j = j + (i << 1)) {
                    sieve[j] = true;
                }
            }
        }
        System.out.println(Arrays.toString(primes));
    }

    public boolean solve(final int val) {
        return Arrays.binarySearch(primes, val) >= 0;
    }
}
