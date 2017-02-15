package imal;

public class Main {

    public static void main(String[] args) {
        String filePath = "data/zamek.jpg";

        int numberOfPoints = 800;
        SURFGenerator surf = new SURFGenerator(numberOfPoints, filePath);
        surf.generate();
        surf.binarize();

        int surfPointsSize = surf.getPoints().size();
        System.out.println("Number of surf points: " + surfPointsSize);
        for (int i = 0; i < surfPointsSize; i++) {
            System.out.println("\n" + i + ") " + surf.getPoints().get(i));
            for (double value : surf.getDescriptors().get(i).value) {
                System.out.print(value + " ");
            }
        }
        System.out.println();

        BRIEFGenerator brief = new BRIEFGenerator(surf.getPoints(), filePath);
        brief.generate();
        brief.binarize();

        int briefPointsSize = brief.getPoints().size();
        System.out.println("Number of brief points: " + briefPointsSize);
        for (int i = 0; i < briefPointsSize; i++) {
            System.out.println("\n" + i + ") " + brief.getPoints().get(i));
            for (int value : brief.getDescriptors().get(i)) {
                System.out.print(value + " ");
            }
        }
        System.out.println();

        SIFTGenerator sift = new SIFTGenerator(filePath);
        sift.generate();
        sift.binarize();

        int siftPointsSize = sift.getPoints().size();
        System.out.println("Number of sift points: " + siftPointsSize);
        for (int i = 0; i < siftPointsSize; i++) {
            System.out.println("\n" + i + ") " + sift.getPoints().get(i));
            for (double value : sift.getDescriptors().get(i).value) {
                System.out.print(value + " ");
            }
        }
    }
}
