package imal;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String file1 = "data/reference_images/I01.bmp";
        String file2 = "data/distorted_images/I01_24_5.bmp";

        int numberOfPoints = 50;

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
//        BinPoint.printList("sift", RandomMethod.randDescriptors(sift.binPoints, 100));

        Associate associate = new Associate();

        Map<BinPoint, BinPoint> association = associate.findAssociate(brief1.binPoints, brief2.binPoints);

        Painter painter = new Painter(file1, file2);
        painter.paint(association, brief2.binPoints);

    }
}
