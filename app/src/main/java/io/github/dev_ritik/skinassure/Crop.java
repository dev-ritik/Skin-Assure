package io.github.dev_ritik.skinassure;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class Crop extends Fragment {

    CropImageView cropImageView;

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.crop, container, false);

        cropImageView = root.findViewById(R.id.cropImageView);

        String imageUri = getArguments().getString("Uri"); //if it's a string you stored.
        Log.i("point 24", imageUri);

        cropImageView.setImageUriAsync(Uri.parse(imageUri));

        Button uploadButton = root.findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap cropped = cropImageView.getCroppedImage();

                if (cropped == null) {
                    Toast.makeText(getActivity(), "cropping failed. Please take a new one", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("point 67", cropped.toString() + " " + cropped.getWidth() + " " + cropped.getHeight() + " " + cropped.getDensity());
                    Log.i("point 68", cropped.getByteCount() + "");

                    Uri croppedUri = bitmapToUriConverter(cropped);
                    Log.i("point 79", croppedUri + "");

                    //TODO size
                    Intent intent = new Intent(getActivity(), ServerActivity.class);
//                    intent.putExtra("bitmap", cropped);

                    intent.putExtra("croppedUri", croppedUri);

                    startActivity(intent);
                }
            }
        });


        return root;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        //TODO image degraded
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Uri bitmapToUriConverter(Bitmap mBitmap) {

        //TODO image degraded

        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
//            options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
//            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
//                    true);

            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "SkinAssure");
            if (!folder_gui.exists()) {
                folder_gui.mkdir();
            }
            Log.i("point 139", "in");
            File outputFile = new File(folder_gui, "cropped.jpg");

            FileOutputStream out = new FileOutputStream(outputFile);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = outputFile.getAbsolutePath();
            File f = new File(realPath);
            Log.i("point 149", "in");
            uri = Uri.fromFile(f);

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("point 151", e.getMessage());
        }
        Log.i("point 131", uri + "");
        return uri;
    }
}
