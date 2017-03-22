package imal;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        String file1 = "data/reference_images/I01.bmp";
        String file2 = "data/distorted_images/I01_05_5.bmp";

        int numberOfPoints = 800;

        SURFGenerator surf1 = new SURFGenerator(numberOfPoints, file1);
        surf1.generate();
        //surf.binarize();

        SURFGenerator surf2 = new SURFGenerator(numberOfPoints, file2);
        surf2.generate();
        //surf.binarize();
        //BinPoint.printList("surf", RandomMethod.randDescriptors(surf.binPoints, 20));

        BRIEFGenerator brief1 = new BRIEFGenerator(surf1.getPoints(), file1);
        brief1.generate();
        brief1.binarize();

        BRIEFGenerator brief2 = new BRIEFGenerator(surf2.getPoints(), file2);
        brief2.generate();
        brief2.binarize();
        //BinPoint.printList("brief", RandomMethod.randDescriptors(brief.binPoints, 100));


//        SIFTGenerator sift = new SIFTGenerator(filePath);
//        sift.generate();
//        sift.binarize();

        Associate associate = new Associate();

        double averagePrecision = 0.0;
        double averageRecall = 0.0;
        double averageF1 = 0.0;
        int iterations = 100;

        for (int i = 0; i < iterations; i++) {
            Set<Integer> indexes = RandomMethod.randIndexes(brief1.binPoints.get(0).descriptors.size(), 100);
            List<BinPoint> randomDesc1 = BinPoint.applyIndexes(indexes, brief1.binPoints);
            List<BinPoint> randomDesc2 = BinPoint.applyIndexes(indexes, brief2.binPoints);


            Map<BinPoint, BinPoint> association = associate.findAssociate(randomDesc1, randomDesc2);
            associate.printResult();

            averagePrecision += associate.precision;
            averageRecall += associate.recall;
            averageF1 += associate.f1;
        }

        System.out.println("Average: ");
        System.out.println("precision: " + averagePrecision / iterations);
        System.out.println("recall: " + averageRecall / iterations);
        System.out.println("f1: " + averageF1 / iterations);

//        Painter painter = new Painter(file1, file2);
//        painter.paint(association, brief2.binPoints);

    }
}
