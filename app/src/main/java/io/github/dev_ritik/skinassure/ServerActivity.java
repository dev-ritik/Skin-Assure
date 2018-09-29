package io.github.dev_ritik.skinassure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ServerActivity extends AppCompatActivity {
    ImageView image;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        image=findViewById(R.id.image);

        Intent intent=getIntent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("bitmap");
        image.setImageBitmap(bitmap);
//
//        progressdialog = new ProgressDialog(getApplicationContext());
//        progressdialog.setMessage("Please Wait....");
//        progressdialog.show();

    }
}
