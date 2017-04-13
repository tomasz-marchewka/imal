package imal;

public class Main {

    public static int  NUMBER_OF_POINTS = 800;

    public static void main(String[] args) {
        useTid(1);

//        String file1 = "data/reference_images/I01.bmp";
//        String file2 = "data/distorted_images/I01_05_1.bmp";
//
//        SURFGenerator surf1 = new SURFGenerator(NUMBER_OF_POINTS, file1);
//        surf1.generate();
//        //surf.binarize();
//
//        SURFGenerator surf2 = new SURFGenerator(NUMBER_OF_POINTS, file2);
//        surf2.generate();
//        //surf.binarize();
//        //BinPoint.printList("surf", RandomMethod.randDescriptors(surf.binPoints, 20));
//
//        BRIEFGenerator brief1 = new BRIEFGenerator(surf1.getPoints(), file1);
//        brief1.generate();
//        brief1.binarize();
//
//        BRIEFGenerator brief2 = new BRIEFGenerator(surf2.getPoints(), file2);
//        brief2.generate();
//        brief2.binarize();
        //BinPoint.printList("brief", RandomMethod.randDescriptors(brief.binPoints, 100));


//        SIFTGenerator sift = new SIFTGenerator(filePath);
//        sift.generate();
//        sift.binarize();

//        Associate associate = new Associate();
//
//        double averagePrecision = 0.0;
//        double averageRecall = 0.0;
//        double averageF1 = 0.0;
//        int iterations = 10;
//
//        for (int i = 0; i < iterations; i++) {
//            Set<Integer> indexes = RandomMethod.randIndexes(brief1.binPoints.get(0).descriptors.size(), 520);
//            List<BinPoint> randomDesc1 = BinPoint.applyIndexes(indexes, brief1.binPoints);
//            List<BinPoint> randomDesc2 = BinPoint.applyIndexes(indexes, brief2.binPoints);
//
//            Map<BinPoint, BinPoint> association = associate.findAssociate(randomDesc1, randomDesc2);
//            associate.printResult();
//
//            averagePrecision += associate.precision;
//            averageRecall += associate.recall;
//            averageF1 += associate.f1;
//        }
//
//        System.out.println("Average: ");
//        System.out.println("precision: " + averagePrecision / iterations);
//        System.out.println("recall: " + averageRecall / iterations);
//        System.out.println("f1: " + averageF1 / iterations);

//        System.out.println("Entropy: ");
//        System.out.println(EntropyMethod.countEntropy(brief1.binPoints));
//        System.out.println(EntropyMethod.countEntropy(brief2.binPoints));
//        System.out.println("Get indexes: ");
//        Set<Integer> indexes = EntropyMethod.getDescriptors(brief1.binPoints, 50);
//        List<BinPoint> entropyDesc1 = BinPoint.applyIndexes(indexes, brief1.binPoints);
//        List<BinPoint> entropyDesc2 = BinPoint.applyIndexes(indexes, brief2.binPoints);
//        System.out.println("New entropy = " + EntropyMethod.countEntropy(entropyDesc1));
//        System.out.println("New entropy = " + EntropyMethod.countEntropy(entropyDesc2));
//        Map<BinPoint, BinPoint> association = associate.findAssociate(entropyDesc1, entropyDesc2);
//        associate.printResult();


//        Painter painter = new Painter(file1, file2);
//        painter.paint(association, brief2.binPoints);

    }
    
    static void useTid(int imageNumber) {
        String refImageNumber = imageNumber < 10 ? "0" + imageNumber : "" + imageNumber;
        String referenceImage = "data/reference_images/I" + refImageNumber + ".bmp";

        SURFGenerator surfReference = new SURFGenerator(NUMBER_OF_POINTS, referenceImage);
        surfReference.generate();

        BRIEFGenerator briefReference = new BRIEFGenerator(surfReference.getPoints(), referenceImage);
        briefReference.generate();
        briefReference.binarize();

        Associate associate = new Associate();

        String pathPatternDistoredImages = "data/distorted_images/I" + refImageNumber + "_";
        int maxDistortedType = 24;
        int maxDistortedLevel = 5;

        for(int i = 1; i <= maxDistortedType; i++) {
            for(int j = 1; j <= maxDistortedLevel; j++) {
                String distortedTypeNumber = i < 10 ? "0" + i : "" + i;
                String distortedImagePath = pathPatternDistoredImages + distortedTypeNumber + "_" + j + ".bmp";

                SURFGenerator surfDistorted = new SURFGenerator(NUMBER_OF_POINTS, distortedImagePath);
                surfDistorted.generate();

                BRIEFGenerator briefDistorted = new BRIEFGenerator(surfDistorted.getPoints(), distortedImagePath);
                briefDistorted.generate();
                briefDistorted.binarize();

                associate.findAssociate(briefReference.binPoints, briefDistorted.binPoints);

                System.out.println("i: " + i + ", j: " + j + " - " + distortedImagePath);
                associate.printResult();
            }
        }

    }
}
