package imal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomas on 2017-03-13.
 */
public class Associate {

    public static double EPSILON = 5.0;
    public static double HAMMING_RATIO = 0.8;

    public static Map<BinPoint, BinPoint> findAssociate(List<BinPoint> points1, List<BinPoint> points2) {
        Map<BinPoint, BinPoint> result = new HashMap<>();
        for (BinPoint point1 : points1) {
            int hammingBest1 = Integer.MAX_VALUE;
            int hammingBest2 = Integer.MAX_VALUE;
            BinPoint best1 = null;
            BinPoint best2 = null;
            for (BinPoint point2 : points2) {
                if (Associate.isInEpsilon(point1, point2)) {
                    int currentHamming = Associate.countHamming(point1, point2);
                    if (currentHamming < hammingBest1) {
                        hammingBest2 = hammingBest1;
                        hammingBest1 = currentHamming;
                        best2 = best1;
                        best1 = point2;
                    }
                }
            }
            if (best1 != null && best2 != null && hammingBest2 != 0 && (double) hammingBest1 / hammingBest2 <= HAMMING_RATIO) {
                result.put(point1, best1);
            } else {
                result.put(point1, null);
            }
        }
        return result;
    }

    public static boolean isInEpsilon(BinPoint point1, BinPoint point2) {
        double x1 = point1.point.x;
        double y1 = point1.point.y;
        double x2 = point2.point.x;
        double y2 = point2.point.y;

        double deltaX = Math.abs(x1 - x2);
        double deltaY = Math.abs(y1 - y2);
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        return (distance < EPSILON);
    }

    public static int countHamming(BinPoint point1, BinPoint point2) throws IllegalArgumentException {
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
