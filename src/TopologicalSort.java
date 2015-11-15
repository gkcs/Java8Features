import java.util.Arrays;

class Graph {
    private final boolean adj[][];
    private int n;
    private final int stack[];
    private int top = -1;
    private final boolean visited[];

    public Graph(int n, int[] a, int[] b) {
        this.n = n;
        adj = new boolean[n][n];
        for (int i = 0; i < a.length; i++) {
            adj[a[i] - 1][b[i] - 1] = true;
        }
        stack = new int[n];
        visited = new boolean[n];
    }

    void topologicalSortUtil(int v) {
        visited[v] = true;
        for (int i = 0; i < n; i++) {
            if (adj[v][i] && !visited[i]) {
                topologicalSortUtil(i);
            }
        }
        stack[++top] = v;
    }

    public int[] topologicalSort() {
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                topologicalSortUtil(i);
            }
        }
        final int half = (stack.length + 1) >> 1;
        for (int i = 0; i < half; i++) {
            int temp = stack[i] + 1;
            stack[i] = stack[n - i - 1] + 1;
            stack[n - i - 1] = temp;
        }
        return stack;
    }

    public static void main(String args[]) {
        int n = 5;
        int[] a = {1, 2, 2, 3};
        int[] b = {3, 5, 3, 4};
        int[] sortedGraph = new Graph(n, a, b).topologicalSort();
        System.out.println(Arrays.toString(sortedGraph));
    }
}
