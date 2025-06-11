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

        Perlin p = new Perlin(seed, block);
        Perlin po = new Perlin(23424, block);
        Perlin pi = new Perlin(seed, block/2);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                System.out.println((int)Math.floor((p.retrieve(i,j) + Math.sqrt(2)/2)*255/Math.sqrt(2)) + ", " + t + ", " + p.retrieve(i,j));

                pixelWriter.setColor(i, j, Color.rgb((int)Math.floor((p.retrieve(i, j) + Math.sqrt(2)/2)*255/Math.sqrt(2)),0, 0));
                //                //(p.retrieve(i, j)+Math.pow(po.retrieve(i,j), 2)+pi.retrieve(i,j))
                // (int)Math.floor((po.retrieve(i, j) + Math.sqrt(2)/2)*255/Math.sqrt(2)), (int)Math.floor((pi.retrieve(i, j) + Math.sqrt(2)/2)*255/Math.sqrt(2)))
                t++;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}