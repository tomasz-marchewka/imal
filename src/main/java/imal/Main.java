package imal;

public class Main {

    public static void main(String[] args) {
        String filePath = "data/zamek.jpg";

        int numberOfPoints = 800;
        SURFGenerator surf = new SURFGenerator(numberOfPoints, filePath);
        surf.generate();

        int surfPointsSize = surf.getPoints().size();
        System.out.println("Number of surf points: " + surfPointsSize);
        for(int i = 0; i < surfPointsSize; i++) {
            System.out.println("\n" + i +") " + surf.getPoints().get(i));
            for(double value : surf.getDescriptions().get(i).value) {
                System.out.print(value + " ");
            }
        }
        BRIEFGenerator brief = new BRIEFGenerator(surf.getPoints(), filePath);
        brief.generate();
        int briefPointsSize = brief.getPoints().size();
        System.out.println("Number of brief points: " + briefPointsSize);
        for(int i = 0; i < briefPointsSize; i++) {
            System.out.println("\n" + i +") " + brief.getPoints().get(i));
            for(int value : brief.getDescriptors().get(i)) {
                System.out.print(value + " ");
            }
        }
    }
}
