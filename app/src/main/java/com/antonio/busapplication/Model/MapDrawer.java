package com.antonio.busapplication.Model;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 5/26/2017.
 */

public class MapDrawer {
    private GoogleMap map;
    ValueAnimator valueAnimator;

    public MapDrawer(GoogleMap map) {
        this.map = map;
    }


    public void drawCircle(LatLng center) {
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.OVAL);
        d.setSize(500,500);
        d.setColor(0x555751FF);
        d.setStroke(5, Color.TRANSPARENT);

        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth()
                , d.getIntrinsicHeight()
                , Bitmap.Config.ARGB_8888);

        // Convert the drawable to bitmap
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);

        // Radius of the circle
        final int radius = 1000;

        // Add the circle to the map
        final GroundOverlay circle = map.addGroundOverlay(new GroundOverlayOptions()
                .position(center, 2 * radius).image(BitmapDescriptorFactory.fromBitmap(bitmap)));

        valueAnimator = new ValueAnimator();
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setIntValues(0, radius);
        valueAnimator.setDuration(3000);
        valueAnimator.setEvaluator(new IntEvaluator());
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                circle.setDimensions(animatedFraction * radius * 2);
            }
        });

        valueAnimator.start();
    }

    public void stopDrawing() {
        if(valueAnimator != null)
        valueAnimator.cancel();
    }
}
