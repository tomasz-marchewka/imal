package imal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomas on 2017-03-13.
 */
public class Associate {

    private static double EPSILON = 5.0;
    private static double HAMMING_RATIO = 0.8;
    public double precision;
    public double recall;
    public double f1;

    public Map<BinPoint, BinPoint> findAssociate(List<BinPoint> points1, List<BinPoint> points2) {
        Map<BinPoint, BinPoint> result = new HashMap<>();
        int numberOfAssociate = 0;
        int numberOfAllAssociate = 0;
        for (BinPoint point1 : points1) {
            int hammingBest1 = Integer.MAX_VALUE;
            int hammingBest2 = Integer.MAX_VALUE;
            BinPoint bestPoint = null;
            for (BinPoint point2 : points2) {
                int currentHamming = this.countHamming(point1, point2);
                if (currentHamming < hammingBest1) {
                    bestPoint = point2;
                    hammingBest2 = hammingBest1;
                    hammingBest1 = currentHamming;
                }
            }
            if (bestPoint != null && hammingBest2 != 0 && (double) hammingBest1 / hammingBest2 <= HAMMING_RATIO) {
                if (this.isInEpsilon(point1, bestPoint)) {
                    result.put(point1, bestPoint);
                    numberOfAssociate++;
                }
                numberOfAllAssociate++;
            } else {
                result.put(point1, null);
            }
        }
        this.recall = (double) numberOfAssociate / points1.size();
        this.precision = (double) numberOfAssociate / numberOfAllAssociate;
        if (this.precision + this.recall != 0.0) {
            this.f1 = 2 * (this.precision * this.recall) / (this.precision + this.recall);
        } else {
            this.f1 = 0.0;
        }
        return result;
    }

    public void printResult() {
        System.out.println("precision: " + this.precision);
        System.out.println("recall: " + this.recall);
        System.out.println("f1: " + this.f1);
    }

    private boolean isInEpsilon(BinPoint point1, BinPoint point2) {
        double x1 = point1.point.x;
        double y1 = point1.point.y;
        double x2 = point2.point.x;
        double y2 = point2.point.y;

        double deltaX = Math.abs(x1 - x2);
        double deltaY = Math.abs(y1 - y2);
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        return (distance < EPSILON);
    }

    private int countHamming(BinPoint point1, BinPoint point2) throws IllegalArgumentException {
        if (point1.descriptors.size() != point2.descriptors.size()) {
            throw new IllegalArgumentException("Point1 size: " + point1.descriptors.size() + " is not equal to point2 size: " + point2.descriptors.size());
        }
        int hamming = 0;
        for (int i = 0; i < point1.descriptors.size(); i++) {
            hamming += point1.descriptors.get(i) ^ point2.descriptors.get(i);
        }
        return hamming;
    }
}
