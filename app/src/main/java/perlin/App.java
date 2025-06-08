package perlin;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.paint.Color;


import java.util.Random;

public class App extends Application {
    public static double getValueAt(int x, int y, long seed) {
        Random rand = new Random(seed ^ (x * 73856093L) ^ (y * 19349663L));
        return rand.nextDouble() * 2 * Math.PI;
    }

    public static double smooth(double x) {
        return 6*Math.pow(x, 5) - 15*Math.pow(x, 4) + 10*Math.pow(x, 3);
        // return 3 * Math.pow(x, 2) - 2 * Math.pow(x, 3);
        // System.out.println("x is " + x);
        // System.out.println(8 * Math.pow(x, 7) - 7*Math.pow(x, 8));
        // if (x >= 1) {
        //     System.out.println(x);
        // } else if (x < 0) {
        //     System.out.println("Negative x: " + x);
        // }
        // return 8 * Math.pow(x, 7) - 7*Math.pow(x, 8);
    }

    @Override
    public void start(Stage primaryStage) {
        int size = 400;
        double block = 50.0;
        long seed = 12345L;

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

        // // Console output and math logic (unchanged)
        // System.out.println("Value at 0,0: " + getValueAt(0, 0, seed));
        // System.out.println("Value at 9999,9999: " + getValueAt(9999, 9999, seed));
        // System.out.println("Value at 9999,9999: " + getValueAt(9999, 9999, seed));
        // System.out.println("Value at 9999,9999: " + getValueAt(9999, 9999, seed));

        int t = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int c1 = (int)Math.floor(i/block);
                int c2 = c1 + 1;
                int c3 = (int)Math.floor(j/block);
                int c4 = c3 + 1;
                double v1x = Math.cos(getValueAt(c1,c3, seed));
                double v1y = Math.sin(getValueAt(c1,c3, seed));
                double v2x = Math.cos(getValueAt(c2,c3, seed));
                double v2y = Math.sin(getValueAt(c2,c3, seed));
                double v3x = Math.cos(getValueAt(c1,c4, seed));
                double v3y = Math.sin(getValueAt(c1,c4, seed));
                double v4x = Math.cos(getValueAt(c2,c4, seed));
                double v4y = Math.sin(getValueAt(c2,c4, seed));
                double d1 = v1x * (i/block - c1) + v1y * (j/block - c3);
                double d2 = v2x * (i/block - c2) + v2y * (j/block - c3);
                double d3 = v3x * (i/block - c1) + v3y * (j/block - c4);
                double d4 = v4x * (i/block - c2) + v4y * (j/block - c4);
                //System.out.println(d1);

                double sx = i/block - c1;
                double sy = j/block - c3;

                double f1 = smooth(sx);
                double f2 = smooth(sy);

                // double f1 = 6*Math.pow(sx, 5) - 15*Math.pow(sx, 4) + 10*Math.pow(sx, 3);
                // double f2 = 6*Math.pow(sy, 5) - 15*Math.pow(sy, 4) + 10*Math.pow(sy, 3);

                double i1 = d1 + f1*(d2 - d1);
                double i2 = d3 + f1*(d4 - d3);
                double finalValue = i1 + f2*(i2 - i1);
                // finalValue = finalValue * 100;
                // finalValue = Math.round(finalValue);
                // finalValue = finalValue / 100;

                System.out.println((int)Math.floor((finalValue + Math.sqrt(2)/2)*255/Math.sqrt(2)) + ", " + t + ", " + finalValue);

                pixelWriter.setColor(i, j, Color.rgb((int)Math.floor((finalValue + Math.sqrt(2)/2)*255/Math.sqrt(2)), 0, 0));
                t++;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}