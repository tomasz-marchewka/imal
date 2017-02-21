package imal;

public class Main {

    public static void main(String[] args) {
        String filePath = "data/zamek.jpg";

        int numberOfPoints = 800;

        SURFGenerator surf = new SURFGenerator(numberOfPoints, filePath);
        surf.generate();
        surf.binarize();
        BinPoint.printList("surf", RandomMethod.randDescriptors(surf.binPoints, 20));

        BRIEFGenerator brief = new BRIEFGenerator(surf.getPoints(), filePath);
        brief.generate();
        brief.binarize();
        BinPoint.printList("brief", RandomMethod.randDescriptors(brief.binPoints, 100));

        SIFTGenerator sift = new SIFTGenerator(filePath);
        sift.generate();
        sift.binarize();
        BinPoint.printList("sift", RandomMethod.randDescriptors(sift.binPoints, 100));
    }
}
