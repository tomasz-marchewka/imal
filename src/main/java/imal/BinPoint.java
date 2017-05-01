package imal;

import boofcv.struct.feature.ScalePoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by tomas on 2017-02-04.
 */

public class BinPoint {
    public ScalePoint point;
    public List<Integer> descriptors;

    BinPoint(ScalePoint point, List<Integer> desc) {
        this.point = point;
        this.descriptors = desc;
    }

    public BinPoint getGivenIndices(Set<Integer> indices) throws IllegalArgumentException {
        int descSize = this.descriptors.size();
        if (indices.size() > descSize) {
            throw new IllegalArgumentException("Indices size: " + indices.size() + " can't be bigger than descriptors size: " + descSize);
        }
        List<Integer> newDescripotrs = new ArrayList<>();
        for (int index : indices) {
            if (index >= descSize) {
                throw new IllegalArgumentException("Given index: " + index + " is bigger than descriptors size: " + descSize);
            }
            newDescripotrs.add(this.descriptors.get(index));
        }
        return new BinPoint(this.point, newDescripotrs);
    }

    @Override
    public String toString() {
        StringBuilder descriptorsString = new StringBuilder();
        for (Integer desc : this.descriptors) {
            descriptorsString.append(desc.toString() + ", ");
        }
        return "(x: " + point.x + ", y: " + point.y + ") - desc(" + this.descriptors.size() + ")= " + descriptorsString;
    }

    static List<BinPoint> applyIndices(Set<Integer> indices, List<BinPoint> points) {
        return points.stream().map(point -> point.getGivenIndices(indices)).collect(Collectors.toList());
    }

    static void printList(String method, List<BinPoint> points) {
        System.out.println("Number of " + method + " points: " + points.size());
        points.forEach(point -> System.out.println(point));
    }
}