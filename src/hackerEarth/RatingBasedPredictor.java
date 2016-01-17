package hackerEarth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RatingBasedPredictor {
    //// TODO: 17/01/16 Set skill level based on profile details
    private final double skill = 0;
    private final double weightExponent = 2;
    private final double theta = 0.602;
    public final double maxIterationsForOptimization = 50;
    private final double lambda = 0.77;

    public double probabiltyOfSolving(final double firstPlayerRating, final double secondPlayerRating) {
        return 1 / (1 + Math.expm1(secondPlayerRating - firstPlayerRating - skill));
    }

    public double weightOfProblem(final double currentTimestamp, final double factor, final double firstGameTimeStamp, final double lastGameTimeStamp) {
        return Math.pow((1 + (currentTimestamp - firstGameTimeStamp) * factor)
                / (1 + (lastGameTimeStamp - firstGameTimeStamp) * factor), weightExponent);
    }

    public double getLearningRate(final int currentIteration) {
        return Math.pow((0.1 * maxIterationsForOptimization)
                / (currentIteration + 0.1 * maxIterationsForOptimization), theta);
    }

    public double getLoss(final double weights[],
                          final double outcomes[],
                          final double predictions[],
                          final double ratings[],
                          final double neighborRatings[]) {
        double loss = 0;
        for (int i = 0; i < outcomes.length; i++) {
            loss += weights[i] * (predictions[i] - outcomes[i]);
        }
        for (int i = 0; i < ratings.length; i++) {
            loss += lambda * Math.pow((ratings[i] - neighborRatings[i]), 2);
        }
        return loss;
    }

    public double getRatingDelta(final int currentIteration,
                                 final double weight,
                                 final double prediction,
                                 final double outcome,
                                 final int neighbors,
                                 final double rating,
                                 final double neighborRating) {
        double delta = -getLearningRate(currentIteration)
                * (weight * (prediction - outcome) * prediction
                * (1 - prediction) +
                (lambda / neighbors) * (rating - neighborRating));
        if (delta != 0) {
            System.out.println(delta);
        }
        return delta;
    }

}

class Predictor {

    private static final double timeSpan = 100;
    private static final RatingBasedPredictor ratingBasedPredictor = new RatingBasedPredictor();

    public static void main(String args[]) throws IOException {
        final List<String[]> csv = new ArrayList<>();
        final BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/gaurav.se/Documents/will_bill_solve_it/train/submissions.csv"));
        bufferedReader.readLine();
        String s = bufferedReader.readLine();
        while (s != null && !s.equals("")) {
            csv.add(s.split(","));
            s = bufferedReader.readLine();
        }
        final double weights[] = getWeights(csv);
        final double outcomes[] = getOutcomes(csv);
        final double ratings[] = new double[getRatingSize(csv)];
        final HashMap<String, Integer> players = getPlayers(csv);
        final HashMap<String, Integer> initialRatings = getInitialRatings();
        final int[][][] neighbors = getNeighbors(players, csv);
        final int neighborhood[] = new int[neighbors.length];
        for (int i = 0; i < neighbors.length; i++) {
            neighborhood[i] = neighbors[i].length;
        }
        initialRatings.entrySet().stream()
                .filter(initialRating -> players.containsKey(initialRating.getKey()))
                .forEach(initialRating -> ratings[players.get(initialRating.getKey())] = initialRating.getValue());
        final Submission submissions[] = getSubmissions(csv);
        for (int k = 0; k < ratingBasedPredictor.maxIterationsForOptimization; k++) {
            System.out.println("ITERATION: " + k);
            final double[] neighborRating = getNeighborRatings(weights, ratings, neighbors);
            final double predictions[] = getPredictions(submissions, ratings, players);
            for (int i = 0; i < submissions.length; i++) {
                ratings[players.get(submissions[i].userId)] +=
                        ratingBasedPredictor.getRatingDelta(k,
                                weights[i],
                                predictions[i],
                                outcomes[i],
                                neighborhood[players.get(submissions[i].userId)],
                                ratings[players.get(submissions[i].userId)],
                                neighborRating[players.get(submissions[i].userId)]);
                ratings[players.get(submissions[i].problemId)] += ratingBasedPredictor.getRatingDelta(k,
                        -weights[i],
                        predictions[i],
                        outcomes[i],
                        neighborhood[players.get(submissions[i].problemId)],
                        ratings[players.get(submissions[i].problemId)],
                        neighborRating[players.get(submissions[i].problemId)]);
            }
            System.out.println(ratingBasedPredictor.getLoss(weights, outcomes, predictions, ratings, neighborRating));
        }
        final double[] predictions = getPredictions(submissions, ratings, players);
        System.out.println(Arrays.toString(predictions));
        System.out.println(Arrays.toString(ratings));
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("user_id")
                .append(',')
                .append("problem_id")
                .append(',')
                .append("solved_status")
                .append('\n');
        for (int i = 0; i < predictions.length; i++) {
            predictions[i] = predictions[i] >= 0.6 ? 1 : 0;
            stringBuilder.append(submissions[i].userId)
                    .append(',')
                    .append(submissions[i].problemId)
                    .append(',')
                    .append(predictions[i])
                    .append('\n');
        }
        //System.out.println(stringBuilder.toString());
    }

    private static double[] getNeighborRatings(final double[] weights, final double[] ratings, final int[][][] neighbors) {
        final double neighborRating[] = new double[neighbors.length];
        for (int i = 0; i < neighbors.length; i++) {
            double neighborTotalWeight = 0;
            System.out.println(neighbors[i].length);
            for (int j = 0; j < neighbors[i].length; j++) {
                neighborRating[i] += weights[neighbors[i][j][1]] * ratings[neighbors[i][j][0]];
                neighborTotalWeight += weights[neighbors[i][j][1]];
            }
            neighborRating[i] /= neighborTotalWeight;
            if (neighborRating[i] != 0) {
                throw new RuntimeException("HEHEHEH");
            }
        }
        return neighborRating;
    }

    private static int[][][] getNeighbors(final HashMap<String, Integer> players, final List<String[]> csv) {
        final HashMap<Integer, List<Integer>> neighbors = new HashMap<>();
        final HashMap<Integer, List<Integer>> neighborDates = new HashMap<>();
        for (int i = 0; i < csv.size(); i++) {
            String[] line = csv.get(i);
            if (!neighbors.containsKey(players.get(line[0]))) {
                neighbors.put(players.get(line[0]), new ArrayList<>());
                neighborDates.put(players.get(line[0]), new ArrayList<>());
            }
            if (!neighbors.containsKey(players.get(line[1]))) {
                neighbors.put(players.get(line[1]), new ArrayList<>());
                neighborDates.put(players.get(line[1]), new ArrayList<>());
            }
            neighbors.get(players.get(line[0])).add(players.get(line[1]));
            neighborDates.get(players.get(line[0])).add(i);
            neighbors.get(players.get(line[1])).add(players.get(line[0]));
            neighborDates.get(players.get(line[1])).add(i);
        }
        final int neighborhood[][][] = new int[neighbors.size()][][];
        for (int i = 0; i < neighborhood.length; i++) {
            neighborhood[i] = new int[neighbors.get(i).size()][2];
            final Integer[] nn = neighbors.get(i).toArray(new Integer[neighborhood[i].length]);
            final Integer[] dd = neighborDates.get(i).toArray(new Integer[neighborhood[i].length]);
            for (int j = 0; j < neighborhood[i].length; j++) {
                neighborhood[i][j][0] = nn[j];
                neighborhood[i][j][1] = dd[j];
            }
        }
        return neighborhood;
    }

    private static HashMap<String, Integer> getPlayers(final List<String[]> csv) {
        final HashMap<String, Integer> players = new HashMap<>();
        int count = 0;
        for (String a[] : csv) {
            a[0] = "U" + a[0];
            a[1] = "P" + a[1];
            if (!players.containsKey(a[0])) {
                players.put(a[0], count++);
            }
            if (!players.containsKey(a[1])) {
                players.put(a[1], count++);
            }
        }
        return players;
    }

    private static Submission[] getSubmissions(final List<String[]> csv) {
        final Submission[] submissions = new Submission[csv.size()];
        for (int i = 0; i < submissions.length; i++) {
            submissions[i] = new Submission(csv.get(i)[0], csv.get(i)[1]);
        }
        return submissions;
    }

    private static int getRatingSize(final List<String[]> csv) {
        final int users = csv.stream().map(a -> a[0]).collect(Collectors.toCollection(HashSet::new)).size();
        final int problems = csv.stream().map(a -> a[1]).collect(Collectors.toCollection(HashSet::new)).size();
        return users + problems;
    }

    public static double[] getWeights(final List<String[]> csv) {
        final int length = csv.size();
        final double factor = timeSpan / length;
        final double weights[] = new double[length];
        for (int i = 0; i < length; i++) {
            weights[i] = ratingBasedPredictor.weightOfProblem(i, factor, 0, length);
        }
        return weights;
    }

    public static double[] getOutcomes(final List<String[]> csv) {
        int length = csv.size();
        double outcomes[] = new double[length];
        for (int i = 0; i < length; i++) {
            outcomes[i] = csv.get(i)[2].equals("SO") ? (csv.get(i)[3].equals("AC") ? 1 : 0.2) : 0;
        }
        return outcomes;
    }

    public static double[] getPredictions(final Submission[] submissions,
                                          final double[] ratings,
                                          final HashMap<String, Integer> playerMap) {
        double predictions[] = new double[submissions.length];
        for (int i = 0; i < submissions.length; i++) {
            predictions[i] = ratingBasedPredictor.probabiltyOfSolving(ratings[playerMap.get(submissions[i].userId)], ratings[playerMap.get(submissions[i].problemId)]);
        }
        return predictions;
    }

    public static HashMap<String, Integer> getInitialRatings() throws IOException {
        final HashMap<String, Integer> initialRatings = new HashMap<>();
        final BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/gaurav.se/Documents/will_bill_solve_it/train/problems.csv"));
        bufferedReader.readLine();
        String s = bufferedReader.readLine();
        while (s != null && !s.equals("")) {
            String[] split = s.split(",");
            initialRatings.put(split[0], getCorrespondingRating(split[1]));
            s = bufferedReader.readLine();
        }
        return initialRatings;
    }

    private static Integer getCorrespondingRating(String s) {
        switch (s) {
            case "E-M":
                return 300;
            case "E":
                return 0;
            case "M":
                return 600;
            case "M-H":
                return 900;
            case "H":
                return 1200;
            default:
                return -300;
        }
    }
}