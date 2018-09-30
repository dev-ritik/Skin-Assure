package io.github.dev_ritik.skinassure;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ServerActivity extends AppCompatActivity {
    protected Interpreter tflite;
    ImageView image;
    ProgressDialog progressdialog;
    ByteBuffer imgData;
    int[] intValues;

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int height = (int) (newHeight * densityMultiplier);
        int width = (int) (height * photo.getWidth() / ((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, width, height, true);

        return photo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        image = findViewById(R.id.image);


    }

    private void startUploadTask(Bitmap inputBitmap) {
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);
        progressdialog.show();

        try {
            tflite = new Interpreter(loadModelFile(this));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "error!!", Toast.LENGTH_SHORT).show();
            progressdialog.cancel();
        }
        convertBitmapToByteBuffer(inputBitmap);

        float[][] labelProbArray = new float[1][10];
        tflite.run(imgData, labelProbArray);
        progressdialog.cancel();

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle("output")
                .setMessage("Output: basal cell carcinoma" + labelProbArray[0][0] +
                        "\nlentigo" + labelProbArray[0][1] +
                        "\nmalignant melanoma" + labelProbArray[0][2] +
                        "\nseborrheic keratosis" + labelProbArray[0][3] +
                        "\nskin" + labelProbArray[0][4] +
                        "\nwart" + labelProbArray[0][5] +
                        "\ndermatofibroma" + labelProbArray[0][6] +
                        "\nhemangioma" + labelProbArray[0][7] +
                        "\nnevus" + labelProbArray[0][8] +
                        "\npyogenic granuloma" + labelProbArray[0][0])
                .show();

        for (float[] asd : labelProbArray)
            for (float asdf : asd)
                Log.i("point 78", asdf + "");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Bitmap cropped = (Bitmap) intent.getParcelableExtra("bitmap");
        Log.i("point 51", cropped.toString() + " " + cropped.getWidth() + " " + cropped.getHeight() + " " + cropped.getDensity());
        Log.i("point 52", cropped.getByteCount() + "");
//        Uri myUri = intent.getParcelableExtra("croppedUri");


        if (cropped == null) {
            Toast.makeText(this, "image not found", Toast.LENGTH_SHORT).show();
        } else {
            image.setImageBitmap(cropped);
        }

        startUploadTask(cropped);

    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd(getModelPath());
//        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(getModelPath());
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private String getModelPath() {
        return "optimized_graph.tflite";
    }

    private void convertBitmapToByteBuffer(Bitmap bm) {
        imgData = ByteBuffer.allocateDirect(1 * 128 * 128 * 3 * 4);
        intValues = new int[bm.getHeight() * bm.getWidth()];
        imgData.rewind();
        bm.getPixels(intValues, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight());

        int pixels = 0;
        for (int i = 0; i < bm.getWidth(); ++i) {
            for (int j = 0; j < bm.getHeight(); j++) {
                final int val = intValues[pixels++];
                Log.i("point 111", "loop" + val + " " + i + " " + j);

                imgData.putFloat((val >> 16) & 0xFF);
                imgData.putFloat((val >> 8) & 0xFF);
                imgData.putFloat((val) & 0xFF);
            }
        }
    }
}
