/*
 * @author GennadySX
 * @created at 2023
 **/

package com.mone.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mone.modules.ImageFilter;
import com.mone.modules.ImageFilters;

@SuppressLint("ViewConstructor")
public class ResizeImageView extends FrameLayout {

    ImageView myImageView;
    Bitmap bitmap;
    public ResizeImageView(@NonNull Context context, Bundle args) {
        super(context);

        String url = args.getString("uri");
        String filterString = args.getString("filter");

        int width = args.getInt("width");
        int height = args.getInt("height");

        myImageView = new ImageView(context);
        this.onChangeParams(url, width, height, ImageFilters.valueOf(filterString));
        addView(myImageView);
    }


    /**
     * Change image params
     * @param uri
     * @param width
     * @param height
     * @param filter
     */
    public void onChangeParams(String uri, int width, int height, ImageFilters filter) {

        Log.d("ResizeImageView", "onChangeParams: " + uri + " " + width + " " + height + " " + filter);

        Glide.with(getContext())
                .asBitmap()
                .override(width, height)
                .load(uri)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bitmap = resource;
                        Bitmap bitmap = ImageFilter.applyFilter(resource, filter);
                        //scale image
                        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                        myImageView.setImageBitmap(bitmap);
                    }
                });
        myImageView.setLayoutParams(new LayoutParams(width, height));
    }


}
