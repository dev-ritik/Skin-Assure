package io.github.dev_ritik.skinassure;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.theartofdev.edmodo.cropper.CropImageView;

public class ImageActivity extends AppCompatActivity {

    CropImageView cropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        cropImageView = findViewById(R.id.cropImageView);

        Intent intent = getIntent();
        String imageUri = intent.getStringExtra("Uri"); //if it's a string you stored.
//        Log.i("point 24", imageUri);
        Log.i("point 25", Uri.parse(imageUri)+"");

        cropImageView.setImageUriAsync(Uri.parse(imageUri));

        Bitmap cropped = cropImageView.getCroppedImage();


    }
}
