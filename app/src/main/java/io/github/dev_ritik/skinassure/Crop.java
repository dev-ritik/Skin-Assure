package io.github.dev_ritik.skinassure;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theartofdev.edmodo.cropper.CropImageView;

public class Crop extends Fragment {

    CropImageView cropImageView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.crop, container, false);

        cropImageView = root.findViewById(R.id.cropImageView);
        Log.i("point 27", getArguments()+"");

        String imageUri =  getArguments().getString("Uri"); //if it's a string you stored.
        Log.i("point 24", imageUri);
        Log.i("point 25", Uri.parse(imageUri) + "");

        cropImageView.setImageUriAsync(Uri.parse(imageUri));

        Bitmap cropped = cropImageView.getCroppedImage();

        return root;
    }
}
