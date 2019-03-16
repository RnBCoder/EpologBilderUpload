package com.example.epologbilderupload;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import org.jibble.simpleftp.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    String BarcodeNr = "Kein Barcode eingelesen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btn_Kamera = findViewById(R.id.BTN_Kamera);        //assign BTN_Kamera to button object
                                                                        //OnClick Listener
        btn_Kamera.setOnClickListener(v -> {
            dispatchTakePictureIntent();                                //start Intent to take Photo
        });


        final Button btn_Barcode = findViewById(R.id.BTN_Barcode);      //assign BTN_Barcode to button object
                                                                        //OnClick Listener
        btn_Barcode.setOnClickListener(v -> {
            startActivityBarcode();                                     //start Intent to take Photo
        });


    }

    private void startActivityBarcode() {
        Intent intent = new Intent(this,BarcodeActivity.class).putExtra("temp", BarcodeNr);
        startActivity(intent);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                String path = photoURI.getPath();

            }
        }
    }


    private void ftpUpload(){                           //http://www.jibble.org/simpleftp/

        try {
            SimpleFTP ftp = new SimpleFTP();

            // Connect to an FTP server on port 21.
            ftp.connect("ftp.somewhere.net", 21, "username", "password");

            // Set binary mode.
            ftp.bin();

            // Change to a new working directory on the FTP server.
            ftp.cwd("web");

            // Upload some files.
            ftp.stor(new File(currentPhotoPath));


            // Quit from the FTP server.
            ftp.disconnect();
        }
        catch (IOException e) {

        }

    }


    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;



    }

}

    //String mString = "Test";                          https://stackoverflow.com/questions/27732781/how-to-write-exif-data-to-image-in-android
       // new ExifInterface(image.getAbsolutePath());
   // ExifInterface exif;
       // exif.setAttribute("UserComment", mString);
        //exif.saveAttributes();
