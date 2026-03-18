package com.example.mobiletechapp2;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;

import com.google.mlkit.vision.common.InputImage;

public class MLKitActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 3000;

    private Uri imageFileUri;
    private ImageView imageView;
    private TextView textViewOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlkit);

        imageView = findViewById(R.id.imageViewMLKit);
        textViewOutput = findViewById(R.id.textViewMLKit);
    }

    private boolean checkPermission() {

        String permission = android.Manifest.permission.CAMERA;

        boolean grantCamera =
                ContextCompat.checkSelfPermission(this, permission)
                        == PackageManager.PERMISSION_GRANTED;

        if (!grantCamera) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission},
                    REQUEST_PERMISSION);
        }

        return grantCamera;
    }

    public void openCamera(View view) {

        if (!checkPermission())
            return;

        Intent takePhotoIntent =
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imageFileUri =
                getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new ContentValues());

        takePhotoIntent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                imageFileUri);

        activityResultLauncher.launch(takePhotoIntent);
    }

    public void loadImage(View view) {

        Intent galleryIntent =
                new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        activityResultLauncher.launch(galleryIntent);
    }

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {

                        @Override
                        public void onActivityResult(ActivityResult result) {

                            if (result.getResultCode() == RESULT_OK) {

                                if (result.getData() != null &&
                                        result.getData().getData() != null)

                                    imageFileUri = result.getData().getData();

                                imageView.setImageURI(imageFileUri);

                                textViewOutput.setText("");

                                InputImage image = null;

                                try {

                                    image = InputImage.fromFilePath(
                                            getBaseContext(),
                                            imageFileUri);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (image != null) {
                                    processImageFromBarcodeReader(image);
                                }
                            }
                        }
                    });

    public void processImageFromBarcodeReader(InputImage image) {

        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                        .build();

        BarcodeScanner scanner =
                BarcodeScanning.getClient(options);

        Task<List<Barcode>> result =
                scanner.process(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<Barcode>>() {

                                    @Override
                                    public void onSuccess(List<Barcode> barcodes) {

                                        textViewOutput.append(
                                                Html.fromHtml(
                                                        "<b>Detected barcode:</b><br>",
                                                        Html.FROM_HTML_MODE_LEGACY));

                                        String result = "";

                                        for (Barcode barcode : barcodes) {

                                            result = barcode.getRawValue();

                                            textViewOutput.append(
                                                    result + "\n");
                                        }

                                        if (result.length() < 2) {

                                            textViewOutput.append(
                                                    "Barcode not found.\n");
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {

                                    @Override
                                    public void onFailure(Exception e) {

                                        textViewOutput.setText("Failed");

                                    }
                                });
    }
}