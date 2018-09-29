package io.github.dev_ritik.skinassure;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceHolder holder;

    public ShowCamera(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
    }

    public ShowCamera(Context context) {
        super(context);
    }

    public ShowCamera(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowCamera(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("point 39", "created");

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("point 45", "changed");
        Camera.Parameters params = camera.getParameters();

        List<Camera.Size> sizes = params.getSupportedPictureSizes();


        for (Camera.Size size : sizes)
            Log.i("point 48", "" + size.width);
//        if(this.getResources().getConfiguration().orientation!=Configuration.ORIENTATION_LANDSCAPE){
//
//        }   //code to change orientation!!


//        camera.setParameters(params);

//        params.setPictureSize(sizes.get(sizes.size()-1).width,sizes.get(sizes.size()-1).height);
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("point 67", "preview error");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("point 72", "destroyed");
        camera.stopPreview();
    }

    private Rect calculateTapArea(float x, float y, float coefficient) {
        int areaSize = Float.valueOf(210 * coefficient).intValue();

        int left = clamp((int) x - areaSize / 2, 0, getWidth() - areaSize);
        int top = clamp((int) y - areaSize / 2, 0, getHeight() - areaSize);

        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
//        matrix.mapRect(rectF);

        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    protected void focusOnTouch(MotionEvent event) {
        if (camera != null) {

            camera.cancelAutoFocus();
            Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f);
            Rect meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f);

            Camera.Parameters parameters = camera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            parameters.setFocusAreas(Lists.newArrayList(new Camera.Area(focusRect, 1000)));

//            if (meteringAreaSupported) {
//                parameters.setMeteringAreas(Lists.newArrayList(new Camera.Area(meteringRect, 1000)));
//            }

            camera.setParameters(parameters);
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    Log.i("point 121", "autofocus");

                }
            });
        }
    }

}
