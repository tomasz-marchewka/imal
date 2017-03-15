package imal;

import boofcv.abst.feature.describe.ConfigSiftScaleSpace;
import boofcv.abst.feature.detect.interest.ConfigSiftDetector;
import boofcv.abst.filter.derivative.ImageGradient;
import boofcv.alg.feature.describe.DescribePointSift;
import boofcv.alg.feature.detect.interest.SiftDetector;
import boofcv.alg.feature.detect.interest.SiftScaleSpace;
import boofcv.alg.feature.orientation.OrientationHistogramSift;
import boofcv.alg.filter.derivative.DerivativeType;
import boofcv.factory.feature.detect.interest.FactoryInterestPointAlgs;
import boofcv.factory.filter.derivative.FactoryDerivative;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.feature.BrightFeature;
import boofcv.struct.feature.ScalePoint;
import boofcv.struct.feature.SurfFeatureQueue;
import boofcv.struct.image.GrayF32;
import georegression.struct.point.Point2D_F64;
import org.ddogleg.struct.FastQueue;
import org.ddogleg.struct.GrowQueue_F64;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SIFTGenerator {

    private String imagePath;
    private List<BrightFeature> descriptors;
    private List<ScalePoint> points;
    public List<BinPoint> binPoints;

    public SIFTGenerator(String imagePath) {
        this.imagePath = imagePath;
        this.points = new ArrayList<>();
        this.binPoints = new ArrayList<>();
    }

    public void generate() {
        this.descriptors = new ArrayList<>();

        GrayF32 image = UtilImageIO.loadImage(this.imagePath, GrayF32.class);
        //SiftScaleSpace scaleSpace = new SiftScaleSpace(0, 3, 5, 1.6);

        ConfigSiftScaleSpace configSiftScaleSpace = new ConfigSiftScaleSpace();
        configSiftScaleSpace.sigma0 = 1.6f;
        configSiftScaleSpace.numScales = 5;
        configSiftScaleSpace.firstOctave = 1;
        configSiftScaleSpace.lastOctave = 5;
        ConfigSiftDetector configSiftDetector = new ConfigSiftDetector();
        configSiftDetector.extract.radius = 2;
        configSiftDetector.extract.threshold = 1.0f;
        configSiftDetector.extract.ignoreBorder = 1;
        configSiftDetector.maxFeaturesPerScale = -1;
        configSiftDetector.edgeR = 5.0d;
        SiftDetector detector = FactoryInterestPointAlgs.sift(configSiftScaleSpace, configSiftDetector);

        OrientationHistogramSift<GrayF32> orientation = new OrientationHistogramSift<GrayF32>(32, 1.5, GrayF32.class);
        DescribePointSift describe = new DescribePointSift(8, 4, 8, 2.5, 0.5, 0.2D, GrayF32.class);

        SurfFeatureQueue features = new SurfFeatureQueue(describe.getDescriptorLength());
        GrowQueue_F64 featureScales = new GrowQueue_F64(100);
        GrowQueue_F64 featureAngles = new GrowQueue_F64(100);
        FastQueue<Point2D_F64> location = new FastQueue<>(100, Point2D_F64.class, true);

        features.reset();
        featureScales.reset();
        featureAngles.reset();
        location.reset();

        //scaleSpace.initialize(image);
        //scaleSpace.constructPyramid(image);
        //scaleSpace.computeFeatureIntensity();
        //scaleSpace.computeDerivatives();

        detector.process(image);
        //orientation.setScaleSpace(scaleSpace);

        ImageGradient imageGradient = FactoryDerivative.gradient(DerivativeType.THREE, image.imageType, image.imageType);
        GrayF32 derivX = UtilImageIO.loadImage(this.imagePath, GrayF32.class);
        GrayF32 derivY = UtilImageIO.loadImage(this.imagePath, GrayF32.class);
        imageGradient.process(image, derivX, derivY);
        orientation.setImageGradient(derivX, derivY);

        //describe.setScaleSpace(scaleSpace);
        describe.setImageGradient(derivX, derivY);

        FastQueue<ScalePoint> found = detector.getDetections();

        for (int i = 0; i < found.size; i++) {
            ScalePoint scalePoint = found.data[i];
            orientation.process(scalePoint.x, scalePoint.y, scalePoint.scale);

            GrowQueue_F64 angles = orientation.getOrientations();
            //int imageIndex = orientation.getImageIndex();
            //double pixelScale = orientation.getPixelScale();

            for (int j = 0; j < angles.size; j++) {
                BrightFeature desc = features.grow();
                double yaw = angles.data[j];
                describe.process(scalePoint.x, scalePoint.y, scalePoint.scale, yaw, desc);
                //desc.laplacianPositive = scalePoint.white;
                desc.white = scalePoint.white;
                featureScales.push(scalePoint.scale);
                featureAngles.push(yaw);
                location.grow().set(scalePoint.x, scalePoint.y);
                this.descriptors.add(desc);
            }
            this.points.add(scalePoint);
        }

    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
        if (this.descriptors != null && this.points != null && this.descriptors.size() >= this.points.size()) {
            double middle = getMiddle();

            for(int i = 0; i < this.points.size(); i++) {
                this.binPoints.add(new BinPoint(this.points.get(i), SIFTGenerator.binarizePoint(this.descriptors.get(i).getValue(), middle)));
            }
        }
    }

    private double getMiddle() {
        int size = this.getDescriptors().size() * this.getDescriptors().get(0).getValue().length;
        double[] array = new double[size];

        int i = 0;
        for (BrightFeature desc : this.getDescriptors()) {
            for (double value : desc.getValue()) {
                array[i++] = value;
            }
        }

        Arrays.sort(array);

        int middleIndex = (size % 2 == 0) ? (size+1) /2 : size /2;
        return array[middleIndex];
    }

    public static List<Integer> binarizePoint(double[] point, double middle) {
        List<Integer> binPoint = new ArrayList<>(point.length);
        for (int i = 0; i < point.length; i++) {
            binPoint.add(i, point[i] > middle ? 1 : 0);
        }
        return binPoint;
    }
}
