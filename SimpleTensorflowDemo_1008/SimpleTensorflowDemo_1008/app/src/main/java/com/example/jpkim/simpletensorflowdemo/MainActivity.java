package com.example.jpkim.simpletensorflowdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

//import com.flurgle.camerakit.CameraKit;
//import com.flurgle.camerakit.CameraListener;
//import com.flurgle.camerakit.CameraView;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraListener;
import com.wonderkiln.camerakit.CameraView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yongchun.library.view.ImageSelectorActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // INPUT SIZE, MEAN, STD values are taken from label_image source
    private static final int INPUT_SIZE = 299;
    private static final int IMAGE_MEAN = 0;
    private static final float IMAGE_STD = 255.0f;
    private static final String INPUT_NAME = "Mul";
    private static final String OUTPUT_NAME = "final_result";

    private static final String MODEL_FILE = "file:///android_asset/graph.pb";
    private static final String LABEL_FILE =
            "file:///android_asset/labels.txt";

    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();

    private CameraView cameraView;
    private TextView txtResult;
    private ImageView imgResult;
    private Button btnDetect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView = (CameraView)findViewById(R.id.cameraView);
        txtResult = (TextView)findViewById(R.id.txtResult);
        imgResult = (ImageView)findViewById(R.id.imgResult);

        Button btnGallery = (Button) findViewById(R.id.btnGallery);
        Button btnURL = (Button) findViewById(R.id.btnUrl);
        Button btnCamera = (Button) findViewById(R.id.btnCamera);

        btnDetect = (Button)findViewById(R.id.btnDetect);

        cameraView.setVisibility(View.INVISIBLE);  // hides cameraview on startup, shows when camera button clicked
        btnDetect.setVisibility(View.INVISIBLE);   // hide camera detect button on startup

        // btn events delegation
        btnGallery.setOnClickListener(this);
        btnURL.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnDetect.setOnClickListener(this);

        // initialize tensorflow async
        initTensorFlowAndLoadModel();

        // permission check & request if needed
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        // cameraview library has its own permission check method
        cameraView.setPermissions(CameraKit.Constants.PERMISSIONS_PICTURE);

        // invoke tensorflow inference when picture taken from camera

        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                super.onPictureTaken(picture);

                Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);

                recognize_bitmap(bitmap);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            IMAGE_MEAN,
                            IMAGE_STD,
                            INPUT_NAME,
                            OUTPUT_NAME);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        // define which methods to call when buttons in view clicked
        int id = v.getId();

        switch(id) {
            case R.id.btnGallery:
                LoadImageFromGallery();
                break;
            case R.id.btnUrl:
                LoadImageFromUrl();
                break;
            case R.id.btnCamera:
                DetectImageFromCamera();
                break;
            case R.id.btnDetect:
                cameraView.captureImage();
                break;
            default:
                break;
        }
    }

    // recognize image from camera roll.
    private void LoadImageFromGallery() {

        // make sure cameraview/dectect button invisible and stopped
        cameraView.setVisibility(View.INVISIBLE);
        btnDetect.setVisibility(View.INVISIBLE);
        cameraView.stop();

        // invoke image picker to get a single image to be inferenced
        ImageSelectorActivity.start(MainActivity.this, 1, ImageSelectorActivity.MODE_SINGLE, false,false,false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // pass the selected image from image picker to tensorflow
        // image picker returns image(s) in arrayList

        if(resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE){
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);

            // image decoded to bitmap, which can be recognized by tensorflow
            Bitmap bitmap = BitmapFactory.decodeFile(images.get(0));

            recognize_bitmap(bitmap);
        }
    }

    // retrieves image from entered url string and call tensorflow
    private void LoadImageFromUrl() {
        // make sure cameraview/dectect button invisible and stopped
        cameraView.setVisibility(View.INVISIBLE);
        btnDetect.setVisibility(View.INVISIBLE);
        cameraView.stop();

        // inflate alertdialog for url input and show it to the user
        LayoutInflater layoutinflater = LayoutInflater.from(this);
        View dialogView = layoutinflater.inflate(R.layout.dialog_prompt_url,null);

        final EditText editURL = (EditText)dialogView.findViewById(R.id.editURL);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(dialogView)
                .setTitle("Enter/Paste url of image to recognize")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        // read from url stream not allowed in main thread. so invoke it in different thread

                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String url = editURL.getText().toString();
                                    InputStream input = new java.net.URL(editURL.getText().toString()).openStream();
                                    //InputStream input = new java.net.URL(editURL.getText().toString()).openConnection().getInputStream();

                                        final Bitmap bitmap =  BitmapFactory.decodeStream(input);
                                    // recognize_bitmap needs to update the UI(imgResult, txtResult), so invoke it in runOnUiThread
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //
                                                recognize_bitmap(bitmap);
                                            }
                                        });


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    }
                })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .create()
                .show();
    }

    private void DetectImageFromCamera(){
        // show cameraview and detect button when source from camera button clicked
        cameraView.setVisibility(View.VISIBLE);
        btnDetect.setVisibility(View.VISIBLE);
        if(!cameraView.isActivated())cameraView.start();
    }

// recognize bitmap and get results
    private void recognize_bitmap(Bitmap bitmap) {

        // create a bitmap scaled to INPUT_SIZE
        bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);

        // returned value stores in Classifier.Recognition format
        // which provides various methods to parse the result,
        // but I'm going to show raw result here.
        final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
        final Bitmap finalBitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imgResult.setImageBitmap(finalBitmap);
            }
        });
        txtResult.setText(results.toString());
    }
}
