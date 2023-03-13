/*
 * @author GennadySX
 * @created at 2023
 **/

package com.mone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.mone.modules.ImageFilter;
import com.mone.modules.ImageFilters;
import com.mone.modules.ImageUtils;

public class MoneFilterModule extends ReactContextBaseJavaModule {

    public MoneFilterModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return "MoneFilterModule";
    }

    @ReactMethod
    public void imageFilter(String path, ImageFilters filter, Promise promise) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            Bitmap result = ImageFilter.applyFilter(bitmap, filter);
            String resultPath = saveImage(result);
            promise.resolve(resultPath);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    private String saveImage(Bitmap bitmap) {
        return ImageUtils.bitmapToBase64(bitmap);
    }

    @ReactMethod
    public void getFilters(Promise promise) {
        try {
            String[] filters = ImageFilter.getFilters();
            promise.resolve(filters);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

}
