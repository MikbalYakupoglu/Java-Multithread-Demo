package demo6;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class App {

    // create resources file and add your photo with the name old-photo.jpg
    public static final String SOURCE_FILE = "./resources/old-photo.jpg";
    public static final String DESTINATION_FILE = "./out/new-photo.jpg";


    public static void main(String[] args) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage newImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        int threadAmount = 4;
        long startTime = System.currentTimeMillis();
//        reColorSingleThreadImage(originalImage, newImage, 0 , 0, originalImage.getWidth(), originalImage.getHeight());
        reColorMultiThreadImage(originalImage, newImage, threadAmount);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;


        File outputFile = new File(DESTINATION_FILE);
        ImageIO.write(newImage, "jpg", outputFile);

        System.out.println(duration);
    }

    public static void reColorMultiThreadImage(BufferedImage originalImage, BufferedImage newImage, int threadAmount) {
        List<Thread> threadList = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / threadAmount;

        for (int i = 0; i < threadAmount; i++) {
            final int startWidth = 0;
            final int startHeight = i * height;
            Thread thread = new Thread(() -> reColorSingleThreadImage(originalImage, newImage, startWidth, startHeight, width, height));
            threadList.add(thread);
        }

        for (Thread thread : threadList) {
            thread.start();
        }

        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void reColorSingleThreadImage(BufferedImage originalImage, BufferedImage newImage, int startWidth, int startHeight, int width, int height) {
        for (int x = startWidth; x < startWidth + width; x++) {
            for (int y = startHeight; y < startHeight + height; y++) {
                int rgb = originalImage.getRGB(x, y);

                if (isChangeableColor(rgb)) {
                    int newRed = 0;
                    int newGreen = 255;
                    int newBlue = 0;

                    int newColor = createNewColor(newRed, newGreen, newBlue);
                    newImage.setRGB(x, y, newColor);
                } else {
                    newImage.setRGB(x, y, originalImage.getRGB(x, y));
                }
            }
        }
    }

    public static boolean isChangeableColor(int rgb) {
        int red = findRed(rgb);
        int green = findGreen(rgb);
        int blue = findBlue(rgb);

        boolean redChangeRange = red > 115 && red < 185;
        boolean greenChangeRange = green > 65 && green < 105;
        boolean blueChangeRange = blue > 70 && blue < 140;

        return redChangeRange && greenChangeRange && blueChangeRange;
    }

    public static int createNewColor(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        return rgb;
    }

    public static int findRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    public static int findGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    public static int findBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
