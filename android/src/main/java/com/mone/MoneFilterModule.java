/*
 * @author GennadySX
 * @created at 2023
 **/

package com.mone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.mone.modules.ImageFilter;
import com.mone.modules.ImageFilters;
import com.mone.modules.ImageUtils;

public class MoneFilterModule extends ReactContextBaseJavaModule {

    ReactApplicationContext context;
    public MoneFilterModule(ReactApplicationContext reactContext) {

        super(reactContext);
        context = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "MoneFilterModule";
    }

      @ReactMethod
      public void filterImage(String path, String filter, Callback callback) {
           try {
               Glide.with(context)
                       .asBitmap()
                       .load(path)
                       .into(new SimpleTarget<Bitmap>() {
                           @Override
                           public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                               Bitmap result = ImageFilter.applyFilter(resource, ImageFilters.valueOf(filter));
                               String resultPath = saveImage(result);
                               callback.invoke( resultPath);
                           }
                       });

          } catch (Exception e) {
              callback.invoke(null, e);
          }
      }

    private String saveImage(Bitmap bitmap) {
        return ImageUtils.bitmapToBase64(bitmap);
    }

}
