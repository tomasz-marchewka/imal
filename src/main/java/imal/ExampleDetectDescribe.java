package imal;

import boofcv.abst.feature.associate.AssociateDescription;
import boofcv.abst.feature.associate.ScoreAssociation;
import boofcv.abst.feature.describe.ConfigBrief;
import boofcv.abst.feature.describe.DescribeRegionPoint;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.abst.feature.detect.interest.ConfigFastHessian;
import boofcv.abst.feature.detect.interest.ConfigGeneralDetector;
import boofcv.abst.feature.detect.interest.InterestPointDetector;
import boofcv.alg.feature.detect.interest.GeneralFeatureDetector;
import boofcv.alg.filter.derivative.GImageDerivativeOps;
import boofcv.factory.feature.associate.FactoryAssociation;
import boofcv.factory.feature.describe.FactoryDescribeRegionPoint;
import boofcv.factory.feature.detdesc.FactoryDetectDescribe;
import boofcv.factory.feature.detect.interest.FactoryDetectPoint;
import boofcv.factory.feature.detect.interest.FactoryInterestPoint;
import boofcv.io.UtilIO;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.feature.TupleDesc;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.ImageGray;

import java.awt.image.BufferedImage;

/**
 * {@link DetectDescribePoint} provides a single unified interface for detecting interest points inside of images
 * and describing the features. For some features (e.g. SIFT) it can be much faster than the alternative approach
 * where individual algorithms are used for feature detection, orientation estimation, and describe.  It also
 * simplifies the code.
 * <p>
 * This example demonstrates how to create instances, but the {@link ExampleAssociatePoints} demonstrates how
 * to use the interface.
 *
 * @author Peter Abeles
 */
public class ExampleDetectDescribe {

    /**
     * For some features, there are pre-made implementations of DetectDescribePoint.  This has only been done
     * in situations where there was a performance advantage or that it was a very common combination.
     */
    public static <T extends ImageGray, TD extends TupleDesc>
    DetectDescribePoint<T, TD> createFromPremade(Class<T> imageType) {
        return (DetectDescribePoint) FactoryDetectDescribe.surfStable(
                new ConfigFastHessian(1, 2, 200, 1, 9, 4, 4), null, null, imageType);
//		return (DetectDescribePoint)FactoryDetectDescribe.sift(new ConfigCompleteSift(-1,5,300));
    }

    /**
     * Any arbitrary implementation of InterestPointDetector, OrientationImage, DescribeRegionPoint
     * can be combined into DetectDescribePoint.  The syntax is more complex, but the end result is more flexible.
     * This should only be done if there isn't a pre-made DetectDescribePoint.
     */
    public static <T extends ImageGray, TD extends TupleDesc>
    DetectDescribePoint<T, TD> createFromComponents(Class<T> imageType) {
        // create a corner detector
        Class derivType = GImageDerivativeOps.getDerivativeType(imageType);
        GeneralFeatureDetector corner = FactoryDetectPoint.createShiTomasi(new ConfigGeneralDetector(1000, 5, 1), false, derivType);
        InterestPointDetector detector = FactoryInterestPoint.wrapPoint(corner, 1, imageType, derivType);

        // describe points using BRIEF
        DescribeRegionPoint describe = FactoryDescribeRegionPoint.brief(new ConfigBrief(true), imageType);

        // Combine together.
        // NOTE: orientation will not be estimated
        return FactoryDetectDescribe.fuseTogether(detector, null, describe);
    }

    public static void main(String args[]) {

        Class imageType = GrayF32.class;

      //  DetectDescribePoint detDesc = createFromPremade(imageType);
		DetectDescribePoint detDesc = createFromComponents(imageType);

        // Might as well have this example do something useful, like associate two images
        ScoreAssociation scorer = FactoryAssociation.defaultScore(detDesc.getDescriptionType());
        AssociateDescription associate = FactoryAssociation.greedy(scorer, Double.MAX_VALUE, true);

        // load and match images
        ExampleAssociatePoints app = new ExampleAssociatePoints(detDesc, associate, imageType);

        BufferedImage imageA = UtilImageIO.loadImage("data/zamek.jpg");
        BufferedImage imageB = UtilImageIO.loadImage("data/zamek.jpg");

        app.associate(imageA, imageB);
    }
}