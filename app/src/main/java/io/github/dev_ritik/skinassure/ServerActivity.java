package io.github.dev_ritik.skinassure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

public class ServerActivity extends AppCompatActivity {
    ImageView image;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        image = findViewById(R.id.image);

        Intent intent = getIntent();
//        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("bitmap");
        Uri myUri = intent.getParcelableExtra("croppedUri");

        if (myUri == null) {
            Toast.makeText(this, "image not found", Toast.LENGTH_SHORT).show();
        } else {
            image.setImageURI(myUri);
        }
        
        startUploadTask();
//
//        progressdialog = new ProgressDialog(getApplicationContext());
//        progressdialog.setMessage("Please Wait....");
//        progressdialog.show();

    }

    private void startUploadTask() {
    }
}
