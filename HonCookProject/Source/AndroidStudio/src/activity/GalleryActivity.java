package org.tensorflow.demo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.demo.Classifier;
import org.tensorflow.demo.R;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class GalleryActivity extends AppCompatActivity {

    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    private static final int INPUT_SIZE = 50;
    private Executor executor = Executors.newSingleThreadExecutor();
    private Classifier classifier;

    private ImageView imageResult;
    private TextView textResult;

    private Button btnDetectObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        btnDetectObject = findViewById(R.id.btnDetect);
        imageResult = findViewById(R.id.imageResult);
        textResult = findViewById(R.id.textResult);
        textResult.setMovementMethod(new ScrollingMovementMethod());

        imageview = (ImageView) findViewById(R.id.imageView);
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        btnDetectObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 이미지피커에서 선택된 이미지를 텐서플로우로 넘깁니다.
        // 이미지피커는 ArrayList 로 값을 리턴합니다.
        if (resultCode == RESULT_OK && requestCode == GET_GALLERY_IMAGE) {
            Uri selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);
        }
    }

    private void recognize_bitmap(Bitmap bitmap) {

        bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);

        final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
        final Bitmap finalBitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageResult.setImageBitmap(finalBitmap);
            }
        });
        textResult.setText(results.toString());
    }
}
