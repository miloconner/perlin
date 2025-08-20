package perlin;

import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import util.Perlin;

public class App extends Application {
    int z = 0;

    public App() {
        super();
    }

    public static double getValueAt(int x, int y, long seed) {
        Random rand = new Random(seed ^ (x * 73856093L) ^ (y * 19349663L));
        return rand.nextDouble() * 2 * Math.PI;
    }

    public static void main(String[] args) {
        javafx.application.Application.launch(args);
    }

    public static double smooth(double x) {
        return ((6 * Math.pow(x, 5)) - (15 * Math.pow(x, 4))) + (10 * Math.pow(x, 3));
        // return 3 * Math.pow(x, 2) - 2 * Math.pow(x, 3);
        // System.out.println("x is " + x);
        // System.out.println(8 * Math.pow(x, 7) - 7*Math.pow(x, 8));
        // if (x >= 1) {
        // System.out.println(x);
        // } else if (x < 0) {
        // System.out.println("Negative x: " + x);
        // }
        // return 8 * Math.pow(x, 7) - 7*Math.pow(x, 8);
    }

    /**
     * Uses the specified {@link PixelWriter} to fill an image of size x size pixels
     * with Perlin
     * noise.
     * <p>
     * This uses the algorithm in {@link ImprovedNoise} to generate the Perlin
     * noise.
     * 
     * @param size        the image is a square with this width and length in pixels
     * @param pixelWriter the {@link PixelWriter} use to set the pixels in the
     *                    image.
     */
    public void mikeNoise(int size, PixelWriter pixelWriter) {
        /*
         * Unit square dimension in pixels
         */
        double unitSquareDim = 50;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double x = i / unitSquareDim;
                double y = j / unitSquareDim;
                double finalValue = ImprovedNoise.noise(x, y, 0.0);
                pixelWriter.setColor(i, j, Color.rgb(toRGBValue(finalValue), 0, 0));
            }
        }
    }

    public void miloNoise(int size, PixelWriter pixelWriter) {
        z++;
        Perlin p = new Perlin(12345L, 50, 3);
        int t = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double finalValue = p.retrieve(i, j, z);
                pixelWriter.setColor(i, j, Color.rgb(toRGBValue(finalValue), 0, 0));
                t = t + 1;
            }
        }
    }

    public double noiseAt(int i, int j) {
        /*
         * The block size determines the granularity of the Perlin noise. A smaller
         * block size will
         * result in more detail, while a larger block size will produce a smoother
         * image.
         */
        double block = 50.0;
        long seedx = 12345L;
        int corner00 = (int) Math.floor(i / block);
        int corner01 = corner00 + 1;
        int corner10 = (int) Math.floor(j / block);
        int corner11 = corner10 + 1;
        double theta1 = getValueAt(corner00, corner10, seedx);
        Vec2 gradVec1 = new Vec2(Math.cos(theta1), Math.sin(theta1));
        Vec2 offVec1 = new Vec2((i / block) - corner00, (j / block) - corner10);
        double theta2 = getValueAt(corner01, corner10, seedx);
        Vec2 gradVec2 = new Vec2(Math.cos(theta2), Math.sin(theta2));
        Vec2 offVec2 = new Vec2((i / block) - corner01, (j / block) - corner10);
        double theta3 = getValueAt(corner00, corner11, seedx);
        Vec2 gradVec3 = new Vec2(Math.cos(theta3), Math.sin(theta3));
        Vec2 offVec3 = new Vec2((i / block) - corner00, (j / block) - corner11);
        double theta4 = getValueAt(corner01, corner11, seedx);
        Vec2 gradVec4 = new Vec2(Math.cos(theta4), Math.sin(theta4));
        Vec2 offVec4 = new Vec2((i / block) - corner01, (j / block) - corner11);
        // System.out.println(d1);
        double sx = (i / block) - corner00;
        double sy = (j / block) - corner10;
        double f1 = smooth(sx);
        double f2 = smooth(sy);
        // double f1 = 6*Math.pow(sx, 5) - 15*Math.pow(sx, 4) + 10*Math.pow(sx, 3);
        // double f2 = 6*Math.pow(sy, 5) - 15*Math.pow(sy, 4) + 10*Math.pow(sy, 3);
        double d1 = gradVec1.dot(offVec1);
        double d2 = gradVec2.dot(offVec2);
        double d3 = gradVec3.dot(offVec3);
        double d4 = gradVec4.dot(offVec4);
        double i1 = d1 + (f1 * (d2 - d1));
        double i2 = d3 + (f1 * (d4 - d3));
        double finalValue = i1 + (f2 * (i2 - i1));
        // finalValue = finalValue * 100;
        // finalValue = Math.round(finalValue);
        // finalValue = finalValue / 100;
        return finalValue;
    }

    @Override
    public void start(Stage primaryStage) {
        /*
         * The size of the image to be generated.
         */
        int size = 400;
        /*
         * WritableImage is used to create an image that can be modified pixel by pixel.
         * PixelWriter is used to write pixels to the WritableImage.
         */
        WritableImage image = new WritableImage(size, size);
        PixelWriter pixelWriter = image.getPixelWriter();
        // PixelWriter is set up but not used yet
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(false);
        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, size, size);
        primaryStage.setTitle("Perlin FX");
        primaryStage.setScene(scene);
        primaryStage.show();
        miloNoise(size, pixelWriter);
        // mikeNoise(size, pixelWriter);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                miloNoise(size, pixelWriter);
                System.out.println("SPACE");
            }
        });
    }

    public int toRGBValue(double noiseValue) {
        int rgbVal = (int) Math.floor(((noiseValue + (Math.sqrt(3) / 2)) * 255) / Math.sqrt(3));
        return rgbVal;
    }
}
