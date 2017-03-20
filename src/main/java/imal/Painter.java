package imal;

import boofcv.gui.feature.FancyInterestPointRender;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Created by tomas on 2017-03-08.
 */
public class Painter {
    private BufferedImage image1;
    private BufferedImage image2;

    private static int offsetX = 10;
    private static int offsetY = 0;
    private static int strokeSize = 2;
    private static int circleRadius = 5;

    private Graphics2D graphics;
    private BufferedImage screen;
    private FancyInterestPointRender render;

    private Random rand;

    public Painter(String imagePath1, String imagePath2) {
        this.image1 = UtilImageIO.loadImage(imagePath1);
        this.image2 = UtilImageIO.loadImage(imagePath2);

        this.screen = new BufferedImage(this.image1.getWidth() + this.offsetX + this.image2.getWidth(),
                this.image1.getHeight() + this.offsetY, BufferedImage.TYPE_INT_ARGB);

        this.graphics = screen.createGraphics();
        this.graphics.setStroke(new BasicStroke(this.strokeSize));

        this.graphics.drawImage(this.image1, 0, 0, null);
        this.graphics.drawImage(this.image2, this.image1.getWidth() + this.offsetX, this.offsetY, null);

        this.render = new FancyInterestPointRender();

        this.rand = new Random();
    }


    public void paint(Map<BinPoint, BinPoint> points, List<BinPoint> notAssociatedPoints) {
        this.drawPoints(notAssociatedPoints);
        this.drawPoints(points);

        this.render.draw(this.graphics);
        ShowImages.showWindow(this.screen, "Image comparision", true);
    }

    private void drawPoints(Map<BinPoint, BinPoint> points) {
        for (BinPoint point : points.keySet()) {
            drawPoint(point, points.get(point), 0);
        }
    }

    private void drawPoints(List<BinPoint> points) {
        for (BinPoint point : points) {
            drawPoint(point, null, this.image1.getWidth() + this.offsetX);
        }
    }

    private void drawPoint(BinPoint point, BinPoint associatedPoint, int offsetX) {
        Color randomColor = getRandomColor();

        int x1 = (int) point.point.x + offsetX;
        int y1 = (int) point.point.y;

        render.addCircle(x1, y1, circleRadius, randomColor);

        if (associatedPoint != null) {

            int x2 = (int) associatedPoint.point.x + this.offsetX + this.image1.getWidth();
            int y2 = (int) associatedPoint.point.y + this.offsetY;
            render.addCircle(x2, y2, circleRadius, randomColor);
            graphics.setColor(randomColor);
            graphics.drawLine(x1, y1, x2, y2);
        }
    }

    private Color getRandomColor() {
        if (this.rand == null) {
            this.rand = new Random();
        }
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b);
    }

//    public static List<BinPoint> getNotAssociatedPoints(Map<BinPoint, BinPoint> pointsWithAssociation, List<BinPoint> points) {
//        Set<BinPoint> = new HashSet<>();
//        points.
//    }
}
