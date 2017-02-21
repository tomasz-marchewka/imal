package imal;

import java.util.*;

/**
 * Created by tomas on 2017-02-15.
 */
public class RandomMethod {

    static List<BinPoint> randDescriptors(List<BinPoint> points, int numbersOfIndexes) {
        int listSize = points.size();
        if (listSize <= numbersOfIndexes) {
            return points;
        }
        Set<Integer> randomIndexes = randIndexes(points.get(0).descriptors.size(), numbersOfIndexes);
        List<BinPoint> newPoints = new ArrayList<>();
        for (BinPoint point : points) {
            newPoints.add(point.getGivenIndexes(randomIndexes));
        }
        return newPoints;

    }

    static Set<Integer> randIndexes(int maxIndex, int indexesCount) {
        if (maxIndex < indexesCount) {
            throw new IllegalArgumentException("Can't ask for more numbers than are available");
        }
        Random rng = new Random(); // Ideally just create one instance globally
        // Note: use LinkedHashSet to maintain insertion order
        Set<Integer> generated = new LinkedHashSet<Integer>();
        while (generated.size() < indexesCount) {
            Integer next = rng.nextInt(maxIndex);
            // As we're adding to a set, this will automatically do a containment check
            generated.add(next);
        }
        return generated;
    }
}
