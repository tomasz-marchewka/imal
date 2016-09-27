package imal;

public class Main {

    public static void main(String[] args) {
        String filePath = "data/zamek.jpg";

        int numberOfPoints = 800;
        SURFGenerator surf = new SURFGenerator(numberOfPoints, filePath);
        surf.generate();
        System.out.println("Number of points: " + surf.getPoints().size());
        int pointsSize = surf.getPoints().size();
        for(int i = 0; i < pointsSize; i++) {
            System.out.println(i +") " + surf.getPoints().get(i));
            System.out.println(surf.getDescriptions().get(i).value + "\n");
        }
    }
}
