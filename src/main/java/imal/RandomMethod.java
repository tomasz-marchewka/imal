package imal;

import java.util.*;

/**
 * Created by tomas on 2017-02-15.
 */
public class RandomMethod {

    static List<BinPoint> randDescriptors(List<BinPoint> points, int numbersOfIndices) {
        int listSize = points.size();
        if (listSize <= numbersOfIndices) {
            return points;
        }
        Set<Integer> randomIndices = randIndices(points.get(0).descriptors.size(), numbersOfIndices);
        List<BinPoint> newPoints = new ArrayList<>();
        for (BinPoint point : points) {
            newPoints.add(point.getGivenIndices(randomIndices));
        }
        return newPoints;

    }

    static Set<Integer> randIndices(int maxIndex, int indicesCount) {
        if (maxIndex < indicesCount) {
            throw new IllegalArgumentException("Can't ask for more numbers than are available");
        }
        Random rng = new Random(); // Ideally just create one instance globally
        // Note: use LinkedHashSet to maintain insertion order
        Set<Integer> generated = new LinkedHashSet<Integer>();
        while (generated.size() < indicesCount) {
            Integer next = rng.nextInt(maxIndex);
            // As we're adding to a set, this will automatically do a containment check
            generated.add(next);
        }
        return generated;
    }
}
