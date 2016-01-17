package hackerEarth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
        return Math.pow((1 + 0.1 * maxIterationsForOptimization)
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
        return -getLearningRate(currentIteration)
                * (weight * (prediction - outcome) * prediction
                * (1 - prediction) +
                (lambda / neighbors) * (rating - neighborRating));
    }

}

class Predictor {

    private final double timeSpan = 100;
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
        final int[][][] neighbors = getNeighbors(players, csv);
        final int neighborhood[] = new int[neighbors.length];
        for (int i = 0; i < neighbors.length; i++) {
            neighborhood[i] = neighbors[i].length;
        }
        final Submission submissions[] = getSubmissions(csv);
        for (int k = 0; k < ratingBasedPredictor.maxIterationsForOptimization; k++) {
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
        }
        final double[] predictions = getPredictions(submissions, ratings, players);
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("user_id")
                .append(',')
                .append("problem_id")
                .append(',')
                .append("solved_status")
                .append('\n');
        for (int i = 0; i < predictions.length; i++) {
            predictions[i] = predictions[i] >= 0.5 ? 1 : 0;
            stringBuilder.append(submissions[i].userId)
                    .append(',')
                    .append(submissions[i].problemId)
                    .append(',')
                    .append(predictions[i])
                    .append('\n');
        }
        System.out.println(stringBuilder.toString());
    }

    private static double[] getNeighborRatings(double[] weights, double[] ratings, int[][][] neighbors) {
        final double neighborRating[] = new double[ratings.length];
        for (int i = 0; i < neighbors.length; i++) {
            double neighborTotalWeight = 0;
            for (int j = 0; j < neighbors[i].length; j++) {
                neighborRating[i] += weights[neighbors[i][j][1]] * ratings[neighbors[i][j][0]];
                neighborTotalWeight += weights[neighbors[i][j][1]];
            }
            neighborRating[i] /= neighborTotalWeight;
        }
        return neighborRating;
    }

    private static int[][][] getNeighbors(final HashMap<String, Integer> players, final List<String[]> csv) {
        HashMap<Integer, List<Integer>> neighbors = new HashMap<>();
        HashMap<Integer, List<Integer>> neighborDates = new HashMap<>();
        for (int i = 0; i < csv.size(); i++) {
            String[] line = csv.get(i);
            if (!neighbors.containsKey(players.get(line[0]))) {
                neighbors.put(players.get(line[0]), new ArrayList<>());
                neighborDates.put(players.get(line[0]), new ArrayList<>());
            }
            neighbors.get(players.get(line[0])).add(players.get(line[1]));
            neighborDates.get(players.get(line[0])).add(i);
        }
        final int neighborhood[][][] = new int[neighbors.size()][][];
        for (int i = 0; i < neighborhood.length; i++) {
            neighborhood[i] = new int[neighbors.get(i).size()][2];
            Integer[] nn = neighbors.get(i).toArray(new Integer[neighborhood[i].length]);
            Integer[] dd = neighborDates.get(i).toArray(new Integer[neighborhood[i].length]);
            for (int j = 0; j < neighborhood[i].length; j++) {
                neighborhood[i][j][0] = nn[j];
                neighborhood[i][j][1] = dd[j];
            }
        }
        return neighborhood;
    }

    private static HashMap<String, Integer> getPlayers(final List<String[]> csv) {
        HashMap<String, Integer> players = new HashMap<>();
        int count = 0;
        for (String a[] : csv) {
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
        Submission[] submissions = new Submission[csv.size()];
        for (int i = 0; i < submissions.length; i++) {
            submissions[i] = new Submission(csv.get(i)[0], csv.get(i)[1]);
        }
        return submissions;
    }

    private static int getRatingSize(final List<String[]> csv) {
        int users = csv.stream().map(a -> a[0]).collect(Collectors.toCollection(HashSet::new)).size();
        int problems = csv.stream().map(a -> a[1]).collect(Collectors.toCollection(HashSet::new)).size();
        return users + problems;
    }

    public static double[] getWeights(final List<String[]> csv) {
        int length = csv.size();
        double factor = timeSpan / length;
        double weights[] = new double[length];
        for (int i = 0; i < length; i++) {
            weights[i] = ratingBasedPredictor.weightOfProblem(i, factor, 0, length);
        }
        return weights;
    }

    public static double[] getOutcomes(final List<String[]> csv) {
        int length = csv.size();
        double outcomes[] = new double[length];
        for (int i = 0; i < length; i++) {
            outcomes[i] = csv.get(i)[2].equals("SO") ? (csv.get(i)[3].equals("AC") ? 1 : 0.5) : 0;
        }
        return outcomes;
    }

    public static double[] getPredictions(final Submission[] submissions,
                                          final double[] ratings,
                                          final HashMap<String, Integer> playerMap) {
        double predicitons[] = new double[submissions.length];
        for (int i = 0; i < submissions.length; i++) {
            predicitons[i] = ratingBasedPredictor.probabiltyOfSolving(ratings[playerMap.get(submissions[i].userId)], ratings[playerMap.get(submissions[i].problemId)]);
        }
        return predicitons;
    }

}