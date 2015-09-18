import java.util.Arrays;

class SegmentTree {
    private final long a[];
    private int digit, n;

    public SegmentTree(int n) {
        this.n = n;
        digit = 0;
        while (n > (1 << digit)) {
            digit++;
        }
        a = new long[1 << (digit + 1)];
        int end = (1 << digit) + n, start = 1 << digit;
        while (start > 0) {
            start = start >> 1;
            end = ((end - 1) >> 1) + 1;
        }
    }

    public long[] getState() {
        return Arrays.copyOfRange(a, 1 << digit, (1 << digit) + n);
    }

    @Override
    public String toString() {
        return "SegmentTree{" +
                "a=" + Arrays.toString(a) +
                '}';
    }

    private void findMaxes(int node, int left, int right, int gleft, int gright) {
        if (left <= right) {
            if (gleft >= left && gright <= right) {
                a[node] = a[node] - 1;
            } else if ((left >= gleft && gright >= left) || (right >= gleft && gright >= right)) {
                findMaxes(node << 1, left, right, gleft, (gright + gleft) / 2);
                findMaxes((node << 1) + 1, left, right, ((gright + gleft) / 2) + 1, gright);
            }
        }
    }

    private void split(int node) {
        if ((node << 1) < a.length) {
            a[node << 1] += a[node];
            a[(node << 1) + 1] += a[node];
        }
    }

    public void updateTree(int node) {
        if ((node << 1) < a.length) {
            split(node);
            updateTree(node << 1);
            updateTree((node << 1) + 1);
        }
    }

    public void handleQuery(int l, int r) {
        findMaxes(1, (1 << digit) + l, (1 << digit) + r, 1 << digit, (1 << (digit + 1)) - 1);
    }
} 