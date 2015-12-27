package decisionTree;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static decisionTree.DecisionTreeAlgorithm.NO_OF_DIFFERENT_ATTRIBUTES;
import static decisionTree.DecisionTreeAlgorithm.NO_OF_DIFFERENT_OUTCOMES;

public class ID3 {
    public static void main(String args[]) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/Users/gaurav.se/Downloads/train.csv")));
        DecisionTreeAlgorithm decisionTreeAlgorithm = new DecisionTreeAlgorithm();
        decisionTreeAlgorithm.makeDecisionTree(bufferedReader);
        bufferedReader.close();
        decisionTreeAlgorithm.classifyElements();
    }
}

class DecisionTreeAlgorithm {
    public static int NO_OF_DIFFERENT_OUTCOMES = 8 + 1;
    public static int NO_OF_DIFFERENT_ATTRIBUTES = 127;
    private DecisionTree decisionTree;

    public void makeDecisionTree(final BufferedReader bufferedReader) throws IOException {
        final ArrayList<String[]> elements = new ArrayList<>();
        bufferedReader.readLine();
        String line = bufferedReader.readLine();
        while (line != null && !"".equals(line)) {
            elements.add(line.split(","));
            line = bufferedReader.readLine();
        }
        preprocessing(elements);
        decisionTree = new DecisionTree(elements, inspectData());
//        System.out.println(Node.totalNodes);
//        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream("/Users/gaurav.se/Downloads/outputTree")));
//        printWriter.println(decisionTree);
    }

    public void classifyElements() throws IOException {
        BufferedReader queries = new BufferedReader(new FileReader(new File("/Users/gaurav.se/Downloads/test.csv")));
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File("/Users/gaurav.se/Downloads/classification.csv")));
        queries.readLine();
        printWriter.println("Id,Response");
        String s = queries.readLine();
        while (s != null && !s.equals("")) {
            String blocks[] = s.split(",");
            printWriter.println(blocks[0] + "," + decisionTree.classify(blocks, decisionTree.getRoot()));
            s = queries.readLine();
        }
        printWriter.close();
    }

    public int[] inspectData() throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/Users/gaurav.se/Downloads/train.csv")));
        final HashMap[] hashMaps = new HashMap[NO_OF_DIFFERENT_ATTRIBUTES + 1];
        for (int i = 0; i < hashMaps.length; i++) {
            hashMaps[i] = new HashMap<String, Integer>();
        }
        String s = bufferedReader.readLine();
        showPositions(s);
        s = bufferedReader.readLine();
        while (s != null && !"".equals(s)) {
            final String[] fields = s.split(",");
            for (int i = 1; i < fields.length; i++) {
                final boolean notContinious = i != 4 && i != 8 && i != 9 && i != 10 && i != 11 && i != 12 && i != 15 && i != 17 && i != 29 && !(i <= 37 && i >= 34);
                final boolean notdiscrete = !(i <= 126 && i >= 79) && i != 69 && i != 61 && i != 52 && i != 38;
                if (notContinious && notdiscrete) {
                    Object o = hashMaps[i].get(fields[i]);
                    hashMaps[i].put(fields[i], (o == null ? 0 : (Integer) o) + 1);
                } else {
                    if (!"".equals(fields[i])) {
                        Object o = hashMaps[i].get(((int) (Double.parseDouble(fields[i]) * 10)));
                        hashMaps[i].put((int) (Double.parseDouble(fields[i]) * 10), (o == null ? 0 : (Integer) o) + 1);
                    } else {
                        Object o = hashMaps[i].get(fields[i]);
                        hashMaps[i].put(fields[i], (o == null ? 0 : (Integer) o) + 1);
                    }
                }
            }
            s = bufferedReader.readLine();
        }
        final int counts[] = new int[hashMaps.length];
        for (int i = 1; i < NO_OF_DIFFERENT_ATTRIBUTES; i++) {
            counts[i] = hashMaps[i].keySet().size();
        }
        bufferedReader.close();
        return counts;
    }

    public void findDistinctValuesInData() throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/Users/gaurav.se/Downloads/train.csv")));
        final HashMap[] hashMaps = new HashMap[NO_OF_DIFFERENT_ATTRIBUTES + 1];
        for (int i = 0; i < hashMaps.length; i++) {
            hashMaps[i] = new HashMap<String, Integer>();
        }
        String s = bufferedReader.readLine();
        showPositions(s);
        s = bufferedReader.readLine();
        while (s != null && !"".equals(s)) {
            final String[] fields = s.split(",");
            for (int i = 1; i < fields.length; i++) {
                final boolean notContinious = i != 4 && i != 8 && i != 9 && i != 10 && i != 11 && i != 12 && i != 15 && i != 17 && i != 29 && !(i <= 37 && i >= 34);
                final boolean notdiscrete = !(i <= 126 && i >= 79) && i != 69 && i != 61 && i != 52 && i != 38;
                if (notContinious && notdiscrete) {
                    Object o = hashMaps[i].get(fields[i]);
                    hashMaps[i].put(fields[i], (o == null ? 0 : (Integer) o) + 1);
                } else {
                    if (!"".equals(fields[i])) {
                        Object o = hashMaps[i].get(((int) (Double.parseDouble(fields[i]) * 10)));
                        hashMaps[i].put((int) (Double.parseDouble(fields[i]) * 10), (o == null ? 0 : (Integer) o) + 1);
                    } else {
                        Object o = hashMaps[i].get(fields[i]);
                        hashMaps[i].put(fields[i], (o == null ? 0 : (Integer) o) + 1);
                    }
                }
            }
            s = bufferedReader.readLine();
        }
        solve(hashMaps);
    }

    private void solve(final HashMap[] hashMaps) {
        for (int i = 0; i < hashMaps.length; i++) {
            if (!hashMaps[i].isEmpty()) {
                System.out.println("INDEX: " + i);
            }
            for (final Map.Entry entry : (Set<Map.Entry>) hashMaps[i].entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }
    }

    private void showPositions(final String s) {
        final String[] fields = s.split(",");
        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i] + " " + i);
        }
    }

    private void sortAndPrint(List<String[]> elements) throws FileNotFoundException {
        final String[][] strings = elements.toArray(new String[elements.size()][]);
        Arrays.sort(strings, (o1, o2) -> {
            if (!o1[o1.length - 1].equals(o2[o1.length - 1])) {
                return o1[o1.length - 1].compareTo(o2[o1.length - 1]);
            }
            for (int i = 1; i < o1.length - 1; i++) {
                if (!o1[i].equals(o2[i])) {
                    return o1[i].compareTo(o2[i]);
                }
            }
            return 0;
        });
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream("/Users/gaurav.se/Downloads/outputSorted")));
        for (String[] a : strings) {
            printWriter.println(Arrays.stream(a).collect(Collectors.joining(",")));
        }
    }

    private void preprocessing(List<String[]> elements) {
        for (final String fields[] : elements) {
            for (int i = 1; i < fields.length; i++) {
                final boolean notContinious = i != 4 && i != 8 && i != 9 && i != 10 && i != 11 && i != 12 && i != 15 && i != 17 && i != 29 && !(i <= 37 && i >= 34);
                final boolean notdiscrete = !(i <= 126 && i >= 79) && i != 69 && i != 61 && i != 52 && i != 38;
                if ((!notContinious || !notdiscrete) && !"".equals(fields[i])) {
                    fields[i] = String.valueOf((int) (Double.parseDouble(fields[i]) * 10));
                }
            }
        }
    }
}

class DecisionTree {

    private static final double log2 = Math.log(2);

    public Node getRoot() {
        return root;
    }

    private final Node root = new Node();
    private int[] sizes;

    public DecisionTree(final List<String[]> elements, final int sizes[]) throws IOException {
        this.sizes = sizes;
        buildTree(root, elements);
    }

    private void buildTree(final Node node, final List<String[]> elements) {
        int countOfNonZeroNodes = 0;
        for (final String[] element : elements) {
            node.candidates[Integer.parseInt(element[NO_OF_DIFFERENT_ATTRIBUTES])]++;
        }
        for (final int candidate : node.candidates) {
            if (candidate > 0) {
                countOfNonZeroNodes++;
            }
        }
        if (countOfNonZeroNodes > 1) {
            node.branchingAttribute = findAttributeWithMinimumEntropy(elements, node.attributesUsed);
            node.attributesUsed[node.branchingAttribute] = true;
            final List<String> values = getDistinctAttributeValues(elements, node.branchingAttribute);
            node.links = new Node[values.size()];
            node.mappings = values;
            for (int i = 0; i < node.links.length; i++) {
                node.links[i] = new Node();
                boolean x[] = new boolean[node.attributesUsed.length];
                System.arraycopy(node.attributesUsed, 0, x, 0, node.attributesUsed.length);
                node.links[i].attributesUsed = x;
                buildTree(node.links[i], getFilteredElements(elements, node.branchingAttribute, values.get(i)));
            }
        }
    }

    private List<String[]> getFilteredElements(List<String[]> elements, int branchingAttribute, String value) {
        return elements
                .stream()
                .filter(line -> line[branchingAttribute].equals(value))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private int[][] getMappedElements(List<String[]> elements, int branchingAttribute) {
        int[][] result = new int[sizes[branchingAttribute]][NO_OF_DIFFERENT_OUTCOMES];
        HashMap<String, Integer> indexes = new HashMap<>();
        int count = 0;
        for (String[] line : elements) {
            if (!indexes.containsKey(line[branchingAttribute])) {
                indexes.put(line[branchingAttribute], count++);
            }
            result[indexes.get(line[branchingAttribute])][Integer.parseInt(line[NO_OF_DIFFERENT_ATTRIBUTES])]++;
        }
        return result;
    }

    private List<String> getDistinctAttributeValues(List<String[]> elements, int branchingAttribute) {
        return elements
                .stream()
                .map(element -> element[branchingAttribute])
                .distinct()
                .collect(Collectors.toList());
    }

    private int findAttributeWithMinimumEntropy(final List<String[]> elements, final boolean[] attributesUsed) {
        double minimumEntropy = Integer.MAX_VALUE;
        int minimumEntropyAttribute = 0;
        for (int i = 1; i < NO_OF_DIFFERENT_ATTRIBUTES; i++) {
            if (!attributesUsed[i]) {
                double totalEntropy = 0;
                double population = 0;
                final int[][] mappedElements = getMappedElements(elements, i);
                for (int[] from : mappedElements) {
                    for (final int to : from) {
                        population += to;
                    }
                }
                if (population == 0) {
                    totalEntropy = Integer.MAX_VALUE;
                } else {
                    for (int[] from : mappedElements) {
                        double entropy = 0;
                        double total = 0;
                        for (final int to : from) {
                            total = total + to;
                        }
                        for (final int to : from) {
                            double v = to / total;
                            if (v > 0 && v < 1) {
                                entropy = entropy - v * Math.log(v) / log2;
                            }
                        }
                        totalEntropy = totalEntropy + (entropy * total / population);
                    }
                }
                if (totalEntropy < minimumEntropy) {
                    minimumEntropy = totalEntropy;
                    minimumEntropyAttribute = i;
                }
            }
        }
        if (minimumEntropy == Integer.MAX_VALUE) {
            for (String s[] : elements) {
                System.out.println(Arrays.toString(s));
            }
        }
        System.out.println(minimumEntropy + " " + minimumEntropyAttribute + " " + elements.size());
        return minimumEntropyAttribute;
    }

    public int classify(final String[] line, final Node node) {
        if (node.branchingAttribute == 0) {
            double random = Math.random();
            double sum = Arrays.stream(node.candidates).sum();
            int i = 0;
            for (; i < node.candidates.length; i++) {
                if (random <= node.candidates[i] / sum) {
                    break;
                } else {
                    random -= node.candidates[i] / sum;
                }
            }
            return i;
        } else {
            for (int i = 0; i < node.mappings.size(); i++) {
                if (node.mappings.get(i).equals(line[node.branchingAttribute])) {
                    return classify(line, node.links[i]);
                }
            }
            return classify(line, node.links[0]);
        }
    }

    @Override
    public String toString() {
        return "DecisionTree{" +
                "root=" + root +
                '}';
    }
}

class Node {
    public static int totalNodes;
    boolean attributesUsed[] = new boolean[NO_OF_DIFFERENT_ATTRIBUTES];
    int branchingAttribute;
    final int candidates[] = new int[NO_OF_DIFFERENT_OUTCOMES];
    Node links[];
    List<String> mappings;

    public Node() {
        totalNodes++;
    }

    @Override
    public String toString() {
        return "Node{" +
                "attributesUsed=" + getAsBits(attributesUsed) +
                ", branchingAttribute=" + branchingAttribute +
                ", candidates=" + Arrays.toString(candidates) +
                ", links=" + Arrays.toString(links) +
                '}';
    }

    private String getAsBits(boolean[] attributesUsed) {
        long first = 0, second = 0;
        for (int i = 0; i < attributesUsed.length; i++) {
            if (attributesUsed[i]) {
                if (i < 64) {
                    first = first | (1 << i);
                } else {
                    second = second | (1 << i);
                }
            }
        }
        return String.format("%64s", Long.toBinaryString(second)).replace(' ', '0') + String.format("%64s", Long.toBinaryString(first)).replace(' ', '0');
    }
}