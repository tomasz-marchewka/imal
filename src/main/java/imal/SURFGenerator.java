package imal;

import boofcv.abst.feature.detect.extract.ConfigExtract;
import boofcv.abst.feature.detect.extract.NonMaxSuppression;
import boofcv.abst.feature.orientation.OrientationIntegral;
import boofcv.alg.feature.describe.DescribePointSurf;
import boofcv.alg.feature.detect.interest.FastHessianFeatureDetector;
import boofcv.alg.transform.ii.GIntegralImageOps;
import boofcv.core.image.GeneralizedImageOps;
import boofcv.factory.feature.describe.FactoryDescribePointAlgs;
import boofcv.factory.feature.detect.extract.FactoryFeatureExtractor;
import boofcv.factory.feature.orientation.FactoryOrientationAlgs;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.BoofDefaults;
import boofcv.struct.feature.BrightFeature;
import boofcv.struct.feature.ScalePoint;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.ImageGray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomas on 27.07.2016.
 */
public class SURFGenerator {
    private int numberOfPoints;
    private String imagePath;

    private List<BrightFeature> descriptors;
    private List<ScalePoint> points;
    public List<BinPoint> binPoints;

    public SURFGenerator(int numberOfPoints, String imagePath) {
        this.numberOfPoints = numberOfPoints;
        this.imagePath = imagePath;
        this.binPoints = new ArrayList<>();
    }

    public <II extends ImageGray> void generate() {
        GrayF32 image = UtilImageIO.loadImage(this.imagePath, GrayF32.class);

        Class<II> integralType = GIntegralImageOps.getIntegralType(GrayF32.class);

        NonMaxSuppression extractor = FactoryFeatureExtractor.nonmax(new ConfigExtract(2, 0, 5, true));
        FastHessianFeatureDetector<II> detector = new FastHessianFeatureDetector<II>(extractor, this.numberOfPoints, 2, 9, 4, 4, 6);

        OrientationIntegral<II> orientation = FactoryOrientationAlgs.sliding_ii(null, integralType);

        DescribePointSurf<II> descriptor = FactoryDescribePointAlgs.<II>surfStability(null, integralType);

        II integral = GeneralizedImageOps.createSingleBand(integralType, image.width, image.height);
        GIntegralImageOps.transform(image, integral);

        detector.detect(integral);

        orientation.setImage(integral);
        descriptor.setImage(integral);

        List<ScalePoint> points = detector.getFoundPoints();

        List<BrightFeature> descriptors = new ArrayList<>();

        for (ScalePoint point : points) {
            orientation.setObjectRadius(point.scale * BoofDefaults.SURF_SCALE_TO_RADIUS);
            double angle = orientation.compute(point.x, point.y);

            BrightFeature desc = descriptor.createDescription();
            descriptor.describe(point.x, point.y, angle, point.scale, desc);

            descriptors.add(desc);
        }

        this.descriptors = descriptors;
        this.points = points;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    public List<BrightFeature> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<BrightFeature> descriptors) {
        this.descriptors = descriptors;
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
                this.binPoints.add(new BinPoint(this.points.get(i), SURFGenerator.binarizePoint(this.descriptors.get(i).getValue())));
            }
        }
    }

    public static int[] binarizePoint(double[] point) {
        int[] binPoint = new int[point.length];
        for (int i = 0; i < point.length; i++) {
            binPoint[i] = point[i] > 0 ? 1 : 0;
        }
        return binPoint;
    }
}
