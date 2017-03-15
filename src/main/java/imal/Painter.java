package imal;

import boofcv.gui.feature.FancyInterestPointRender;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.feature.ScalePoint;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 * Created by tomas on 2017-03-08.
 */
public class Painter {
    public static void drawImages(String imagePath1, String imagePath2, List<ScalePoint> points1, List<ScalePoint> points2) {
        BufferedImage image1 = UtilImageIO.loadImage(imagePath1);
        BufferedImage image2 = UtilImageIO.loadImage(imagePath2);

        int border = 10;

        BufferedImage screen = new BufferedImage(image1.getWidth() + border + image2.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = screen.createGraphics();

        graphics.drawImage(image1, 0, 0, null);
        graphics.drawImage(image2, image1.getWidth() + border, 0, null);

        FancyInterestPointRender render = new FancyInterestPointRender();

        drawPoints(render, points1, 0);
        drawPoints(render, points2, image1.getWidth() + border);

        graphics.setStroke(new BasicStroke(1));

        render.draw(graphics);

        ShowImages.showWindow(screen, "Image comparision", true);
    }

    public static void drawPoints(FancyInterestPointRender render, List<ScalePoint> points, int offsetX) {
        Random rand = new Random();
        for (ScalePoint point : points) {
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b);
            render.addCircle((int) point.x + offsetX, (int) point.y, 5, randomColor);
        }
    }
}
