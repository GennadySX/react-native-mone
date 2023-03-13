/*
 * @author GennadySX
 * @created at 2023
 **/

package com.mone.modules;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class ImageFilter {
    public static Bitmap applyFilter(Bitmap bitmap, ImageFilters filter) {
        switch (filter) {
            case BLUR:
                return filterBlur(bitmap, 15, 0.3f);
            case GRAYSCALE:
                return filterGrayScale(bitmap);
            case INVERT:
                return filterInvert(bitmap);
            case SEPIA:
                return filterSepia(bitmap);
            case SHARPEN:
                return filterSharpen(bitmap);
            case THRESHOLD:
                return filterThreshold(bitmap, 127);
            default:
                return bitmap;
        }
    }






    public static Bitmap filterBlur(Bitmap sentBitmap, int radius, float scale) {
        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int[] r = new int[wh];
        int[] g = new int[wh];
        int[] b = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int[] vmin = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int[] dv = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return bitmap;
    }


    public static Bitmap filterGrayScale(Bitmap bitmap) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        int A, R, G, B;
        int pixel;
        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                pixel = bitmap.getPixel(x, y);
                A = (pixel >> 24) & 0xff;
                R = (pixel >> 16) & 0xff;
                G = (pixel >> 8) & 0xff;
                B = (pixel) & 0xff;
                R = G = B = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                result.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return result;


    }

    public static Bitmap filterSepia(Bitmap bitmap) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        int A, R, G, B;
        int pixel;
        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                pixel = bitmap.getPixel(x, y);
                A = (pixel >> 24) & 0xff;
                R = (pixel >> 16) & 0xff;
                G = (pixel >> 8) & 0xff;
                B = (pixel) & 0xff;
                R = G = B = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                R += 40;
                if (R > 255) {
                    R = 255;
                }
                G += 20;
                if (G > 255) {
                    G = 255;
                }
                B -= 20;
                if (B < 0) {
                    B = 0;
                }
                result.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return result;

    }

    public static Bitmap filterInvert(Bitmap bitmap) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        int A, R, G, B;
        int pixel;
        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                pixel = bitmap.getPixel(x, y);
                A = (pixel >> 24) & 0xff;
                R = (pixel >> 16) & 0xff;
                G = (pixel >> 8) & 0xff;
                B = (pixel) & 0xff;
                R = 255 - R;
                G = 255 - G;
                B = 255 - B;
                result.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return result;
    }


    public static Bitmap filterSharpen(Bitmap bitmap) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        int A, R, G, B;
        int pixel;
        int[][] pixels = new int[3][3];
        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                if (y - 1 >= 0) {
                    if (x - 1 >= 0) {
                        pixel = bitmap.getPixel(x - 1, y - 1);
                        A = (pixel >> 24) & 0xff;
                        R = (pixel >> 16) & 0xff;
                        G = (pixel >> 8) & 0xff;
                        B = (pixel) & 0xff;
                        pixels[0][0] = R;
                        pixels[0][1] = G;
                        pixels[0][2] = B;
                    }
                    pixel = bitmap.getPixel(x, y - 1);
                    A = (pixel >> 24) & 0xff;
                    R = (pixel >> 16) & 0xff;
                    G = (pixel >> 8) & 0xff;
                    B = (pixel) & 0xff;
                    pixels[1][0] = R;
                    pixels[1][1] = G;
                    pixels[1][2] = B;
                    if (x + 1 < bitmap.getWidth()) {
                        pixel = bitmap.getPixel(x + 1, y - 1);
                        A = (pixel >> 24) & 0xff;
                        R = (pixel >> 16) & 0xff;
                        G = (pixel >> 8) & 0xff;
                        B = (pixel) & 0xff;
                        pixels[2][0] = R;
                        pixels[2][1] = G;
                        pixels[2][2] = B;
                    }
                }
                if (x - 1 >= 0) {
                    pixel = bitmap.getPixel(x - 1, y);
                    A = (pixel >> 24) & 0xff;
                    R = (pixel >> 16) & 0xff;
                    G = (pixel >> 8) & 0xff;
                    B = (pixel) & 0xff;
                    pixels[0][0] = R;
                    pixels[0][1] = G;
                    pixels[0][2] = B;
                }
                pixel = bitmap.getPixel(x, y);
                A = (pixel >> 24) & 0xff;
                R = (pixel >> 16) & 0xff;
                G = (pixel >> 8) & 0xff;
                B = (pixel) & 0xff;
                pixels[1][0] = R;
                pixels[1][1] = G;
                pixels[1][2] = B;
                if (x + 1 < bitmap.getWidth()) {
                    pixel = bitmap.getPixel(x + 1, y);
                    A = (pixel >> 24) & 0xff;
                    R = (pixel >> 16) & 0xff;
                    G = (pixel >> 8) & 0xff;
                    B = (pixel) & 0xff;
                    pixels[2][0] = R;
                    pixels[2][1] = G;
                    pixels[2][2] = B;
                }
                if (y + 1 < bitmap.getHeight()) {
                    if (x - 1 >= 0) {
                        pixel = bitmap.getPixel(x - 1, y + 1);
                        A = (pixel >> 24) & 0xff;
                        R = (pixel >> 16) & 0xff;
                        G = (pixel >> 8) & 0xff;
                        B = (pixel) & 0xff;
                        pixels[0][0] = R;
                        pixels[0][1] = G;
                        pixels[0][2] = B;
                    }
                    pixel = bitmap.getPixel(x, y + 1);
                    A = (pixel >> 24) & 0xff;
                    R = (pixel >> 16) & 0xff;
                    G = (pixel >> 8) & 0xff;
                    B = (pixel) & 0xff;
                    pixels[1][0] = R;
                    pixels[1][1] = G;
                    pixels[1][2] = B;
                    if (x + 1 < bitmap.getWidth()) {
                        pixel = bitmap.getPixel(x + 1, y + 1);
                        A = (pixel >> 24) & 0xff;
                        R = (pixel >> 16) & 0xff;
                        G = (pixel >> 8) & 0xff;
                        B = (pixel) & 0xff;
                        pixels[2][0] = R;
                        pixels[2][1] = G;
                        pixels[2][2] = B;
                    }
                }

                R = (int) (pixels[0][0] * -0.125 + pixels[1][0] * -0.125 + pixels[2][0] * -0.125 + pixels[0][1] * -0.125 + pixels[1][1] * 2 + pixels[2][1] * -0.125 + pixels[0][2] * -0.125 + pixels[1][2] * -0.125 + pixels[2][2] * -0.125);
                G = (int) (pixels[0][0] * -0.125 + pixels[1][0] * -0.125 + pixels[2][0] * -0.125 + pixels[0][1] * -0.125 + pixels[1][1] * 2 + pixels[2][1] * -0.125 + pixels[0][2] * -0.125 + pixels[1][2] * -0.125 + pixels[2][2] * -0.125);
                B = (int) (pixels[0][0] * -0.125 + pixels[1][0] * -0.125 + pixels[2][0] * -0.125 + pixels[0][1] * -0.125 + pixels[1][1] * 2 + pixels[2][1] * -0.125 + pixels[0][2] * -0.125 + pixels[1][2] * -0.125 + pixels[2][2] * -0.125);

                if (R < 0) {
                    R = 0;
                }
                if (R > 255) {
                    R = 255;
                }
                if (G < 0) {
                    G = 0;
                }
                if (G > 255) {
                    G = 255;
                }
                if (B < 0) {
                    B = 0;
                }
                if (B > 255) {
                    B = 255;
                }

                result.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return result;
    }

    public static Bitmap filterThreshold(Bitmap bitmap, int threshold) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        int A, R, G, B;
        int pixel;

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                pixel = bitmap.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                if (R > threshold) {
                    R = 255;
                } else {
                    R = 0;
                }
                if (G > threshold) {
                    G = 255;
                } else {
                    G = 0;
                }
                if (B > threshold) {
                    B = 255;
                } else {
                    B = 0;
                }

                result.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return result;
    }


    public static String[] getFilters() {

        int enumsCount = ImageFilters.values().length;
        String[] filters = new String[enumsCount];

        for (int i = 0; i < enumsCount; i++) {
            filters[i] = ImageFilters.values()[i].toString();
        }

        return filters;
    }
}
