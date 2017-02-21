package imal;

import boofcv.struct.feature.ScalePoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public BinPoint getGivenIndexes(Set<Integer> indexes) throws IllegalArgumentException {
        int descSize = this.descriptors.size();
        if(indexes.size() >= descSize) {
            throw new IllegalArgumentException("Indexes size: " + indexes.size() + " can't be bigger than descriptors size: " + descSize);
        }
        List<Integer> newDescripotrs = new ArrayList<>();
        for (int index : indexes) {
            if(index >= descSize ) {
                throw new IllegalArgumentException("Given index: " + index + " is bigger than descriptors size: " + descSize);
            }
            newDescripotrs.add(this.descriptors.get(index));
        }
        return new BinPoint(this.point, newDescripotrs);
    }

    @Override
    public String toString() {
        StringBuilder descriptorsString = new StringBuilder();
        for (Integer desc: this.descriptors) {
            descriptorsString.append(desc.toString() + ", ");
        }
        return "(x: " + point.x + ", y: " + point.y + ") - desc(" + this.descriptors.size() + ")= "+ descriptorsString;
    }

    static void printList(String method, List<BinPoint> points) {
        System.out.println("Number of " + method + " points: " + points.size());
        points.forEach(point -> System.out.println(point));
    }
}