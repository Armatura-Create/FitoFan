package com.example.alex.fitofan.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class CompressImage {
    public static String getBase64FromBitmap(Bitmap source) {
        ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
        source.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStreamObject);
        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
        return Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
    }

    public static Bitmap compressImageFromBitmap(Bitmap source) {
        int actualWidth = source.getWidth();
        int actualHeight = source.getHeight();
        float maxHeight = 816.0f;
        float maxWidth = 816.0f;
        if(maxWidth>actualHeight){
            return source; }
        float scaleVal = 1;
        scaleVal = maxWidth/actualWidth;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleVal,scaleVal);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                source, 0, 0, actualWidth, actualHeight, matrix, false);
        source.recycle();
        return resizedBitmap;
    }
}
