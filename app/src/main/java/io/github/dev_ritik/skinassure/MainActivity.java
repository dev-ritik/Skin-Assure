package io.github.dev_ritik.skinassure;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    CameraView camera;
    ImageView image;
    RelativeLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            //ask for authorisation
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
        else {
            camera = findViewById(R.id.camera);
            camera.setLifecycleOwner(this);
            image = findViewById(R.id.image);
            frame = findViewById(R.id.frame);

            camera.mapGesture(Gesture.PINCH, GestureAction.ZOOM); // Pinch to zoom!
            camera.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER); // Tap to focus!
            camera.mapGesture(Gesture.LONG_TAP, GestureAction.CAPTURE); // Long tap to shoot!
            camera.addCameraListener(new CameraListener() {

                /**
                 * Notifies that the camera was opened.
                 * The options object collects all supported options by the current camera.
                 */
                @Override
                public void onCameraOpened(CameraOptions options) {
                }

                /**
                 * Notifies that the camera session was closed.
                 */
                @Override
                public void onCameraClosed() {
                }

                /**
                 * Notifies about an error during the camera setup or configuration.
                 * At the moment, errors that are passed here are unrecoverable. When this is called,
                 * the camera has been released and is presumably showing a black preview.
                 *if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                 ask for authorisation
                 ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
                 else {
                 * This is the right moment to show an error dialog to the user.
                 */
                @Override
                public void onCameraError(CameraException error) {
                }

                /**
                 * Notifies that a picture previously captured with capturePicture()
                 * or captureSnapshot() is ready to be shown or saved.
                 *
                 * If planning to get a bitmap, you can use CameraUtils.decodeBitmap()
                 * to decode the byte array taking care about orientation.
                 */
                @Override
                public void onPictureTaken(byte[] picture) {
                }

                /**
                 * Notifies that a video capture has just ended. The file parameter is the one that
                 * was passed to startCapturingVideo(File), or a fallback video file.
                 */
                @Override
                public void onVideoTaken(File video) {
                }

                /**
                 * Notifies that the device was tilted or the window offset changed.
                 * The orientation passed can be used to align views (e.g. buttons) to the current
                 * camera viewport so they will appear correctly oriented to the user.
                 */
                @Override
                public void onOrientationChanged(int orientation) {
                }

                /**
                 * Notifies that user interacted with the screen and started focus with a gesture,
                 * and the autofocus is trying to focus around that area.
                 * This can be used to draw things on screen.
                 */
                @Override
                public void onFocusStart(PointF point) {
                }

                /**
                 * Notifies that a gesture focus event just ended, and the camera converged
                 * to a new focus (and possibly exposure and white balance).
                 */
                @Override
                public void onFocusEnd(boolean successful, PointF point) {
                }

                /**
                 * Noitifies that a finger gesture just caused the camera zoom
                 * to be changed. This can be used, for example, to draw a seek bar.
                 */
                @Override
                public void onZoomChanged(float newValue, float[] bounds, PointF[] fingers) {
                }

                /**
                 * Noitifies that a finger gesture just caused the camera exposure correction
                 * to be changed. This can be used, for example, to draw a seek bar.
                 */
                @Override
                public void onExposureCorrectionChanged(float newValue, float[] bounds, PointF[] fingers) {
                }

            });

            camera.addCameraListener(new CameraListener() {
                @Override
                public void onPictureTaken(byte[] picture) {
                    Log.i("point 158", "taken");
                    // Create a bitmap or a file...
                    // CameraUtils will read EXIF orientation for you, in a worker thread.
                    File picture_File = getOutputMediaFile();

                    if (picture_File == null) {
                        return;
                    } else {
                        try {
                            FileOutputStream fos = new FileOutputStream(picture_File);
                            fos.write(picture);
                            fos.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

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
    protected void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.destroy();
    }

    public void capture(View view) {
        Log.i("point 180", "click");


        camera.capturePicture();

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

    public void amimFrame() {
        ObjectAnimator settleAnimator = new ObjectAnimator();
        settleAnimator = ObjectAnimator.ofFloat(frame, "translationY", 150);
        settleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.i("point 247", "anim start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i("point 252", "anim end");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("point 257", "anim cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.i("point 262", "anim repeat");

            }
        });


        settleAnimator.setDuration(1500);//millisec

//this will take start value and end value in pixels ie. (from, to)
        settleAnimator.setFloatValues(+300, 0);
        settleAnimator.start();

    }

    public void test(View view) {
        amimFrame();
    }
}

