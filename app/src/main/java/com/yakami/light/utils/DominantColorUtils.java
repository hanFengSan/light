package com.yakami.light.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yakami on 2016/8/1, enjoying it!
 */

public class DominantColorUtils {

    protected final static String TAG = "DominantColorUtils";

    public static int getDominantColor2(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    private static boolean isGrey(int r, int g, int b) {

        int rgDiff = r - g;
        int rbDiff = r - b;

        int tolerance = 100;

        if (rgDiff > tolerance || rgDiff < -tolerance)
            if (rbDiff > tolerance || rbDiff < -tolerance)
                return false;
        return true;
    }


    /**
     * Calculates the dominat color of a bitmap
     * WARNING: Slow
     *
     * @param bitmap
     * @return
     */
    public static int getDominantColor(Bitmap bitmap) {

        if (bitmap == null)
            throw new NullPointerException();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = width * height;
        int pixels[] = new int[size];

        Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);

        bitmap2.getPixels(pixels, 0, width, 0, 0, width, height);

        final List<HashMap<Integer, Integer>> colorMap = new ArrayList<HashMap<Integer, Integer>>();
        colorMap.add(new HashMap<Integer, Integer>());
        colorMap.add(new HashMap<Integer, Integer>());
        colorMap.add(new HashMap<Integer, Integer>());

        int color = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        Integer rC, gC, bC;
        for (int i = 0; i < pixels.length; i++) {
            color = pixels[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            if (isGrey(r, g, b))
                continue;
            rC = colorMap.get(0).get(r);
            if (rC == null)
                rC = 0;
            colorMap.get(0).put(r, ++rC);

            gC = colorMap.get(1).get(g);
            if (gC == null)
                gC = 0;
            colorMap.get(1).put(g, ++gC);

            bC = colorMap.get(2).get(b);
            if (bC == null)
                bC = 0;
            colorMap.get(2).put(b, ++bC);
        }

        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            int max = 0;
            int val = 0;
            for (Map.Entry<Integer, Integer> entry : colorMap.get(i).entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    val = entry.getKey();
                }
            }
            rgb[i] = val;
        }

        int dominantColor = Color.rgb(rgb[0], rgb[1], rgb[2]);

        return dominantColor;
    }

    /**
     * Gets the average color in a drawable
     * This is calculated by sampling a subset of pixels
     * All calculations are done in HSV color space
     * <p>
     * Written by David Webb (http://makingmoneywithandroid.com/)
     *
     * @param image The drawable to sample
     * @return An integer representing the average Color of the sampled pixels
     */
    public static int getAverageColor(Drawable image) {
        //Setup initial variables
        int hSamples = 40;            //Number of pixels to sample on horizontal axis
        int vSamples = 40;            //Number of pixels to sample on vertical axis
        int sampleSize = hSamples * vSamples; //Total number of pixels to sample
        float[] sampleTotals = {0, 0, 0};   //Holds temporary sum of HSV values

        //If white pixels are included in sample, the average color will
        //  often have an unexpected shade. For this reason, we set a
        //  minimum saturation for pixels to be included in the sample set.
        //  Saturation < 0.1 is very close to white (see http://mkweb.bcgsc.ca/color_summarizer/?faq)
        float minimumSaturation = 0.1f;     //Saturation range is 0...1

        //By the same token, we should ignore transparent pixels
        //  (pixels with low alpha value)
        int minimumAlpha = 200;         //Alpha range is 0...255

        //Get bitmap
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        int width = b.getWidth();
        int height = b.getHeight();

        //Loop through pixels horizontally
        float[] hsv = new float[3];
        int sample;
        for (int i = 0; i < width; i += (width / hSamples)) {
            //Loop through pixels vertically
            for (int j = 0; j < height; j += (height / vSamples)) {
                //Get pixel & convert to HSV format
                sample = b.getPixel(i, j);
                Color.colorToHSV(sample, hsv);

                //Check pixel matches criteria to be included in sample
                if ((Color.alpha(sample) > minimumAlpha) && (hsv[1] >= minimumSaturation)) {
                    //Add sample values to total
                    sampleTotals[0] += hsv[0];  //H
                    sampleTotals[1] += hsv[1];  //S
                    sampleTotals[2] += hsv[2];  //V
                } else {
                    Log.v(TAG, "Pixel rejected: Alpha " + Color.alpha(sample) + ", H: " + hsv[0] + ", S:" + hsv[1] + ", V:" + hsv[1]);
                }
            }
        }

        //Divide total by number of samples to get average HSV values
        float[] average = new float[3];
        average[0] = sampleTotals[0] / sampleSize;
        average[1] = sampleTotals[1] / sampleSize;
        average[2] = sampleTotals[2] / sampleSize;

        //Return average tuplet as RGB color
        return Color.HSVToColor(average);
    }
}
