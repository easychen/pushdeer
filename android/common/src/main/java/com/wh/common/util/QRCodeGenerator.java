package com.wh.common.util;


import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
    private final Map<EncodeHintType, String> hints;
    private Bitmap bitmap = null;
    private final String str;
    private final int WIDTH;
    private final int HEIGHT;

    public QRCodeGenerator(String str, int WIDTH, int HEIGHT){
        this.str = (str.length()>300)?"str is too long":str;
        this.WIDTH=WIDTH;
        this.HEIGHT=HEIGHT;
        this.hints = new HashMap<>();
        this.hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
    }

    public Bitmap getQRCode() {
        try {
            BitMatrix Result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);//通过字符串创建二维矩阵
            int width = Result.getWidth();
            int height = Result.getHeight();

            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = Result.get(x, y) ? BLACK : WHITE;//根据二维矩阵数据创建数组
                }
            }
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//创建位图
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);//将数组加载到位图中
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}