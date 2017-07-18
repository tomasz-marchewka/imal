package imal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tomas on 2017-04-03.
 */
public class EntropyMethod {


    static double countEntropy(List<BinPoint> points) {
        Set<Integer> emptySet = new HashSet<>();
        return EntropyMethod.countEntropy(points, emptySet);
    }

    static double countEntropy(List<BinPoint> points, Set<Integer> excludedIndices) {
        double probability1 = 0;
        double probability0 = 0;

        int size = 0;

        int count0 = 0;
        int count1 = 0;

        for (BinPoint point : points) {
            for (int i = 0; i < point.descriptors.length; i++) {
                if(!excludedIndices.contains(i)) {
                    if (point.descriptors[i] == 0) {
                        count0++;
                    } else {
                        count1++;
                    }
                    size++;
                }
            }
        }


        probability0 = (double) count0 / size;
        probability1 = (double) count1 / size;

        return -(probability0 * log2(probability0) + probability1 * log2(probability1));
    }

    public static Set<Integer> getDescriptors(List<BinPoint> points, int numbersOfIndices) {
        Set<Integer> excludedIndices = new HashSet<>();

        for (int i = 0; i < numbersOfIndices; i++) {

            double lowestEntropy = 1;
            int lowestEntropyIndex = 0;

            int descSize = points.get(0).descriptors.length;
            for (int j = 0; j < descSize; j++) {
                Set<Integer> internalExcluded = new HashSet<>(excludedIndices);
                internalExcluded.add(j);

                double currentEntropy = countEntropy(points, internalExcluded);
                if (currentEntropy < lowestEntropy) {
                    lowestEntropy = currentEntropy;
                    lowestEntropyIndex = j;
                }
            }
            excludedIndices.add(lowestEntropyIndex);
        }
        return getIndicesWithout(excludedIndices, points.get(0).descriptors.length);
    }

    private static Set<Integer> getIndicesWithout(Set<Integer> excludedIndices, int maxIndex) {
        Set<Integer> result = new HashSet<>();
        for (int i = 0; i < maxIndex; i++) {
            if(!excludedIndices.contains(i)) {
                result.add(i);
            }
        }
        return result;
    }

    private static double log2(double number) {
        return Math.log(number) / Math.log(2);
    }
}
