package io.github.dev_ritik.skinassure;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.otaliastudios.cameraview.CameraView;

public class MainActivity extends AppCompatActivity {
    private static final int GALLERY_RESULT2 = 2;
    CameraView camera;
    ImageView image;
    RelativeLayout frameBottom;
    ImageButton galleryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();

// add
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.mainFrame, new MainFragment()).addToBackStack( "tag" );
        ft.commit();

    }

//TODO camera destroy stop start
//    @Override
//    protected void onResume() {
//        super.onResume();
//        camera.start();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        camera.stop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        camera.destroy();
//    }

    public void capture(View view) {
        Log.i("point 180", "click");


        camera.capturePicture();

    }
}

