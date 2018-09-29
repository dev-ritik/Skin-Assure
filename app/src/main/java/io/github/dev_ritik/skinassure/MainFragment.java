package io.github.dev_ritik.skinassure;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {
    private static final int GALLERY_RESULT2 = 2;
    CameraView camera;
    ImageView image;
    RelativeLayout frameBottom;
    ImageButton galleryImage, flash;
    SeekBar zoom;
    LinearLayout flashTray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.main_fragment, container, false);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 50);
        else {
            camera = root.findViewById(R.id.camera);
            camera.setLifecycleOwner(this);
            image = root.findViewById(R.id.image);
            frameBottom = root.findViewById(R.id.frameButtom);
            galleryImage = root.findViewById(R.id.galleryImage);
            zoom = root.findViewById(R.id.seekBar);
            zoom.setMax(10);
            flashTray = root.findViewById(R.id.flashTray);
            flashTray.setVisibility(View.GONE);

            zoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    Toast.makeText(seekBar.getContext(), "Value: "+getConvertedValue(progress), Toast.LENGTH_SHORT).show();

                    if (fromUser)
                        camera.setZoom((float) progress / 10);
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
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
                    Log.i("point 143", "zoom" + newValue);
                    zoom.setProgress((int) (newValue * 10));
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

        Button test = root.findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amimFrame();
            }
        });
        Button capture = root.findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.capturePicture();
            }
        });
        ImageButton galleryImage = root.findViewById(R.id.galleryImage);
        galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/jpeg");
                startActivityForResult(intent, GALLERY_RESULT2);
            }
        });

        flash = root.findViewById(R.id.flash);
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("point 240", "flash");
                if (flashTray.getVisibility() == View.VISIBLE) flashTray.setVisibility(View.GONE);
                else flashTray.setVisibility(View.VISIBLE);
            }
        });
        ImageButton flashOn = root.findViewById(R.id.flashOn);
        flashOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFlash(R.drawable.round_flash_on_white_36, Flash.ON);
            }
        });
        ImageButton flashAuto = root.findViewById(R.id.flashAuto);
        flashAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFlash(R.drawable.round_flash_auto_white_36, Flash.AUTO);
            }
        });
        ImageButton flashOff = root.findViewById(R.id.flashOff);
        flashOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFlash(R.drawable.round_flash_off_white_36, Flash.OFF);
            }
        });

        camera.start();
        return root;

    }

    private void changeFlash(int drawable, Flash state) {
        flash.setImageDrawable(getResources().getDrawable(drawable));
        camera.setFlash(state);
        flashTray.setVisibility(View.GONE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_RESULT2) {
            if (resultCode == RESULT_OK) {

                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {

                    Crop cropFragment = new Crop();
                    Bundle args = new Bundle();
                    args.putString("Uri", selectedImageUri.toString());
                    cropFragment.setArguments(args);

//Inflate the fragment
                    getFragmentManager().beginTransaction().replace(R.id.listFragment, cropFragment).commit();

//                    Intent myIntent = new Intent(MainActivity.this, ImageActivity.class);
//                    myIntent.putExtra("Uri", selectedImageUri.toString()); //Optional parameters
//                    startActivity(myIntent);
                }

            } else {
                Toast.makeText(getActivity(), "error in getting picture", Toast.LENGTH_SHORT).show();
            }

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
                    Toast.makeText(getActivity(), "Please grant camera permissions", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "failed to write data!", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");
            if (!folder_gui.exists()) {
                folder_gui.mkdir();
            }
            File outputFile = new File(folder_gui, "temp.jpg");
//            Log.i("point 261", "" + Uri.fromFile(outputFile));
//            Intent myIntent = new Intent(MainActivity.this, ImageActivity.class);
//            myIntent.putExtra("Uri", Uri.fromFile(outputFile).toString());
//            startActivity(myIntent);

            Crop cropFragment = new Crop();

            Bundle args = new Bundle();
            args.putString("Uri", Uri.fromFile(outputFile).toString());
            cropFragment.setArguments(args);

            getFragmentManager().beginTransaction().replace(R.id.listFragment, cropFragment).commit();


            return outputFile;
        }
    }

    public void amimFrame() {
        ObjectAnimator settleAnimator = ObjectAnimator.ofFloat(frameBottom, "translationY", 150);
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


        settleAnimator.setDuration(500);//millisec

//this will take start value and end value in pixels ie. (from, to)
        settleAnimator.setFloatValues(+300, 0);
        settleAnimator.start();

    }

    public void uploadImage(View view) {
    }

}

