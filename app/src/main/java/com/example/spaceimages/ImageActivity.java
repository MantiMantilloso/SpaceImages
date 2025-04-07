//package com.example.spaceimages;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class ImageActivity extends AppCompatActivity {
//
//    private ImageView imageView;
//    private TextView titleView, infoView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image);
//
//        imageView = findViewById(R.id.full_image);
//        titleView = findViewById(R.id.text_title);
//        infoView = findViewById(R.id.text_info);
//
//        String filename = getIntent().getStringExtra("filename");
//        if (filename != null) {
//            DataSingleton.ImageData data = DataSingleton.getInstance().getImageData(filename);
//            if (data != null) {
//                Bitmap fullImage = data.fullImage;
//                if (fullImage != null) {
//                    imageView.setImageBitmap(fullImage);
//                }
//
//                titleView.setText(data.title);
//                infoView.setText("Dimensiones: " + data.width + " x " + data.height);
//            }
//        }
//    }
//}

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

        // Obtener extras
        String filename = getIntent().getStringExtra("filename");
        int position = getIntent().getIntExtra("position", -1); // Número de imagen

        if (filename != null) {
            DataSingleton.ImageData data = DataSingleton.getInstance().getImageData(filename);
            if (data != null) {
                Bitmap fullImage = data.fullImage;
                if (fullImage != null) {
                    imageView.setImageBitmap(fullImage);
                }

                // ✅ Mostrar título con número si lo hay
                if (position != -1) {
                    titleView.setText(position + ". " + data.title);
                } else {
                    titleView.setText(data.title);
                }

                infoView.setText("Dimensiones: " + data.width + " x " + data.height);
            }
        }
    }
}
