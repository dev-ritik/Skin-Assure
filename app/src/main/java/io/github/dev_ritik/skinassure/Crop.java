package io.github.dev_ritik.skinassure;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImageView;

public class Crop extends Fragment {

    CropImageView cropImageView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.crop, container, false);

//        Toolbar toolbar = root.findViewById(R.id.toolbar);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarId);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().onBackPressed();
//            }
//        });

//        ImageButton back = root.findViewById(R.id.back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("point 34", "back");
//
//                MainFragment mainFragment = new MainFragment();
//
//                getFragmentManager().beginTransaction().replace(R.id.listFragment, mainFragment).commit();
//
//            }
//        });
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
                    Log.i("point 67", cropped.toString()+" "+cropped.getWidth()+" "+cropped.getHeight()+" "+cropped.getDensity());
                    Log.i("point 68", cropped.getByteCount()+"");

                    //TODO size
                    Intent intent = new Intent(getActivity(), ServerActivity.class);
                    intent.putExtra("bitmap", cropped);
                    startActivity(intent);
                }
            }
        });


        return root;
    }
}
