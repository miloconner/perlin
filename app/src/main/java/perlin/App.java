package perlin;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import util.Vec2;


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
                double theta1 = getValueAt(c1,c3, seed);
                Vec2 gradVec1 = new Vec2(Math.cos(theta1), Math.sin(theta1));
                Vec2 offVec1 = new Vec2(i/block - c1, j/block - c3);
                double theta2 = getValueAt(c2,c3, seed);
                Vec2 gradVec2 = new Vec2(Math.cos(theta2), Math.sin(theta2));
                Vec2 offVec2 = new Vec2(i/block - c2, j/block - c3);
                double theta3 = getValueAt(c1,c4, seed);
                Vec2 gradVec3 = new Vec2(Math.cos(theta3), Math.sin(theta3));
                Vec2 offVec3 = new Vec2(i/block - c1, j/block - c4);
                double theta4 = getValueAt(c2,c4, seed);
                Vec2 gradVec4 = new Vec2(Math.cos(theta4), Math.sin(theta4));
                Vec2 offVec4 = new Vec2(i/block - c2, j/block - c4);
                //System.out.println(d1);

                double sx = i/block - c1;
                double sy = j/block - c3;

                double f1 = smooth(sx);
                double f2 = smooth(sy);

                // double f1 = 6*Math.pow(sx, 5) - 15*Math.pow(sx, 4) + 10*Math.pow(sx, 3);
                // double f2 = 6*Math.pow(sy, 5) - 15*Math.pow(sy, 4) + 10*Math.pow(sy, 3);

                double d1 = gradVec1.dot(offVec1);
                double d2 = gradVec2.dot(offVec2);
                double d3 = gradVec3.dot(offVec3);
                double d4 = gradVec4.dot(offVec4);
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