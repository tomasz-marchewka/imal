package imal;

import boofcv.alg.feature.describe.DescribePointBrief;
import boofcv.alg.feature.describe.brief.FactoryBriefDefinition;
import boofcv.factory.feature.describe.FactoryDescribePointAlgs;
import boofcv.factory.filter.blur.FactoryBlurFilter;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.feature.ScalePoint;
import boofcv.struct.feature.TupleDesc_B;
import boofcv.struct.image.GrayF32;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tomas on 31.07.2016.
 */
public class BRIEFGenerator {

    private String imagePath;
    private List<List<Integer>> descriptors;
    private List<ScalePoint> points;
    public List<BinPoint> binPoints;

    public BRIEFGenerator(List<ScalePoint> points, String imagePath) {
        this.imagePath = imagePath;
        this.points = points;
        this.binPoints = new ArrayList<>();
    }

    public void generate() {
        this.descriptors = new ArrayList<>();
        BufferedImage tmp = null;
        try {
            tmp = ImageIO.read(new File(this.imagePath));
        } catch (IOException e) {
            System.out.println(e + " | " + this.imagePath + "  IOError");
        }

        GrayF32 input = ConvertBufferedImage.convertFromSingle(tmp, null, GrayF32.class);


        DescribePointBrief<GrayF32> brief = FactoryDescribePointAlgs.brief(FactoryBriefDefinition.gaussian(new Random(123), 16, 512),
                FactoryBlurFilter.gaussian(GrayF32.class, 0, 4));


        brief.setImage(input);
        TupleDesc_B f = brief.createFeature();


        for (ScalePoint point : this.points) {

            brief.process(point.x, point.y, f);

            int[] descriptor = new int[520];

            for (int i = 0; i < f.numBits; i++) {
                if (f.isBitTrue(i)) {
                    descriptor[i] = 1;
                }
            }
            this.descriptors.add(Ints.asList(descriptor));
        }
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<List<Integer>> getDescriptors() {
        return descriptors;
    }

    public List<ScalePoint> getPoints() {
        return points;
    }

    public void setPoints(List<ScalePoint> points) {
        this.points = points;
    }

    public void binarize() {
        if (this.descriptors != null && this.points != null && this.descriptors.size() == this.points.size()) {
            for (int i = 0; i < this.points.size(); i++) {
                this.binPoints.add(new BinPoint(this.points.get(i), Ints.toArray(this.descriptors.get(i))));
            }
        }
    }

}
