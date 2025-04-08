package com.example.spaceimages;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity {
    private ImageView fullImage;
    private TextView title, positionNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ImageActivity", "Oncreate started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        fullImage = findViewById(R.id.iv_full_image);
        title = findViewById(R.id.tv_title);
        positionNumber = findViewById(R.id.tv_position);

        String filename = getIntent().getStringExtra("filename");
        DataSingleton.getInstance().logImageAccess(filename);
        int position = getIntent().getIntExtra("position", 1);
        Log.d("ImageActivity", "Info filename & position cargada");

        DataSingleton.ImageData data = DataSingleton.getInstance().getImageData(filename);

        if (data == null) {
            showErrorAndFinish("Downloading Image Data");
        }

        else{
            // Set initial data
            positionNumber.setText(String.valueOf(position));
            title.setText(data.title);


            // Load image
            if (data.fullImage != null) {
                fullImage.setImageBitmap(data.fullImage);
                Log.d("ImageActivity", "Imagen cargada");
            } else {
                loadFullImage(data);
                Log.d("ImageActivity", "Imagen cargada");
            }
        }
    }

    private void showErrorAndFinish(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void loadFullImage(DataSingleton.ImageData data) {
        new FullImageDownloadTask(fullImage, data).execute();
    }

}
