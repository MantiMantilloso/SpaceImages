package com.example.spaceimages;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleView, infoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = findViewById(R.id.full_image);
        titleView = findViewById(R.id.text_title);
        infoView = findViewById(R.id.text_info);

        String filename = getIntent().getStringExtra("filename");
        if (filename != null) {
            DataSingleton.ImageData data = DataSingleton.getInstance().getImageData(filename);
            if (data != null) {
                Bitmap fullImage = data.fullImage;
                if (fullImage != null) {
                    imageView.setImageBitmap(fullImage);
                }

                titleView.setText(data.title);
                infoView.setText("Dimensiones: " + data.width + " x " + data.height);
            }
        }
    }
}
