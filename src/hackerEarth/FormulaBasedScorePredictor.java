package hackerEarth;

import java.util.Arrays;

public class FormulaBasedScorePredictor {

    public double getRating(final double lineNumber[], final double opponent_rating[], double result[]) {
        final double weightTieToDefinedRatingLevel = 2.5;
        final double weightOpponentRating = 12.5;
        final double gameWeight[] = new double[opponent_rating.length];
        for (int i = 0; i < opponent_rating.length; i++) {
            gameWeight[i] = Math.pow((1 / (1 + (100 - lineNumber[i]) / 48)), 2);
        }
        final double sumGameWeight = Arrays.stream(gameWeight).sum();
        double weighedPerformance = 0;
        double weighedOpponentRating = 0;
        final int extention = 850;
        for (int i = 0; i < opponent_rating.length; i++) {
            weighedPerformance += ((opponent_rating[i] + extention * (result[i] - 0.5)) * gameWeight[i]);
            weighedOpponentRating += weightOpponentRating * (opponent_rating[i] * gameWeight[i]) / sumGameWeight;
        }
        final double defined_rating_level = 1500;
        final double extraPoints = 24.5;
        final double weighedTieToDefinedRatingLevel = weightTieToDefinedRatingLevel * defined_rating_level;
        final double rating = (weighedPerformance + weighedOpponentRating + weighedTieToDefinedRatingLevel + extraPoints)
                / (sumGameWeight + weightOpponentRating + weightTieToDefinedRatingLevel);
        return rating;
    }

    public double getWinningProbabilty(double rating, double opponent_rating) {
        final int extention = 850;
        return (rating - opponent_rating) / extention + 0.5;
    }
}
