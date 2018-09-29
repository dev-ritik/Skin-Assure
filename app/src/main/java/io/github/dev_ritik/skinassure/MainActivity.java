package io.github.dev_ritik.skinassure;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Camera camera1;
    FrameLayout frameLayout;
    ShowCamera showCamera;
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File picture_File = getOutputMediaFile();

            if (picture_File == null) {
                return;
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(picture_File);
                    fos.write(data);
                    fos.close();

                    camera.startPreview();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.frame);

        Log.i("point 24", "camera" + camera1);

        if (camera1 != null) {
            camera1.release();
            camera1 = null;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            //ask for authorisation
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
        else {
            //start your camera

            camera1 = Camera.open();

            showCamera = new ShowCamera(this, camera1);
            //        if(checkCameraHardware(getApplicationContext())){
//            Camera camera =getCameraInstance(getApplicationContext());
//            Log.i("point 19",camera.getParameters().toString());
//            Log.i("point 19",camera.getNumberOfCameras()+"");
//        }
            frameLayout.addView(showCamera);
        }


    }

    private File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED))
            return null;
        else {
            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");
            if (!folder_gui.exists()) {
                folder_gui.mkdir();
            }
            File outputFile = new File(folder_gui, "temp.jpg");

            return outputFile;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 50: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.i("point 63", "request successful");

                } else {
                    // permission denied
                    Toast.makeText(getApplicationContext(), "Please grant camera permissions", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera1.release();
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            Toast.makeText(context, "Camera not found!!", Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public Camera getCameraInstance(Context context) {
        Camera c = null;
//        try {
        c = Camera.open(); // attempt to get a Camera instance
//        }
//        catch (Exception e){
        // Camera is not available (in use or does not exist)
//            e.printStackTrace();
        Log.i("point 47", "exception");
        Toast.makeText(context, "Camera not found!!", Toast.LENGTH_SHORT).show();
//        }
        return c; // returns null if camera is unavailable
    }

    public void capture(View view) {
        camera1.takePicture(null, null, mPictureCallback);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // respond to touch events
        showCamera.focusOnTouch(event);
        return false;
    }
}
