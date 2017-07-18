package imal;

import boofcv.struct.feature.ScalePoint;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by tomas on 2017-02-04.
 */

public class BinPoint {
    public ScalePoint point;
    public int[] descriptors;

    BinPoint(ScalePoint point, int[] desc) {
        this.point = point;
        this.descriptors = desc;
    }

    public BinPoint getGivenIndices(int[] indices) {
        int descSize = this.descriptors.length;
        int indicesSize = indices.length;
        if (indicesSize > descSize) {
            throw new IllegalArgumentException("Indices size: " + indicesSize + " can't be bigger than descriptors size: " + descSize);
        }

        int[] newDescripotrs = new int[indicesSize];
        for (int i = 0; i < indicesSize; i++) {
            newDescripotrs[i] = this.descriptors[indices[i]];
        }
        return new BinPoint(this.point, newDescripotrs);
    }

    @Override
    public String toString() {
        StringBuilder descriptorsString = new StringBuilder();
        for (Integer desc : this.descriptors) {
            descriptorsString.append(desc.toString() + ", ");
        }
        return "(x: " + point.x + ", y: " + point.y + ") - desc(" + this.descriptors.length + ")= " + descriptorsString;
    }

    static List<BinPoint> applyIndices(int[] indices, List<BinPoint> points) {
        List<BinPoint> result = new ArrayList<>(indices.length);
        for (BinPoint point : points) {
            result.add(point.getGivenIndices(indices));
        }
        return result;
        //return points.stream().map(point -> point.getGivenIndices(indices)).collect(Collectors.toList());
    }

    static void printList(String method, List<BinPoint> points) {
        System.out.println("Number of " + method + " points: " + points.size());
        points.forEach(point -> System.out.println(point));
    }
}