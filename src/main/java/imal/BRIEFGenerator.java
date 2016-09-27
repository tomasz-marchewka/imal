package imal;

import java.util.List;

/**
 * Created by tomas on 31.07.2016.
 */
public class BRIEFGenerator {

    private String imagePath;
    private List<List<Integer>> output;

    public BRIEFGenerator(String imagePath) {
        this.imagePath = imagePath;
    }

    public void generate() {}

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
