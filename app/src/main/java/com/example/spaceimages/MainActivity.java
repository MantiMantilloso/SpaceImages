//package com.example.spaceimages;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private ImageAdapter adapter;
//    private Button buttonHistory;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        recyclerView = findViewById(R.id.recycler_view);
//        buttonHistory = findViewById(R.id.button_history);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        adapter = new ImageAdapter(data -> {
//            if (data.fullImage == null) {
//                new FullImageDownloadTask(MainActivity.this, data).execute();
//            } else {
//                openImageActivity(data);
//            }
//        });
//
//        recyclerView.setAdapter(adapter);
//
//        buttonHistory.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
//            startActivity(intent);
//        });
//
//        new HtmlParserTask(dataList -> {
//            for (DataSingleton.ImageData data : dataList) {
//                String filename = extractFilename(data.src);
//                DataSingleton.getInstance().addImageData(filename, data);
//            }
//            adapter.setData(DataSingleton.getInstance().getAllImages());
//        }).execute();
//    }
//
//    private void openImageActivity(DataSingleton.ImageData data) {
//        String filename = extractFilename(data.src);
//        DataSingleton.getInstance().logImageAccess(filename);
//
//        Intent intent = new Intent(this, ImageActivity.class);
//        intent.putExtra("filename", filename);
//        startActivity(intent);
//    }
//
//    private String extractFilename(String src) {
//        return src.substring(src.lastIndexOf("/") + 1);
//    }
//}
package com.example.spaceimages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private Button buttonHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        buttonHistory = findViewById(R.id.button_history);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        // ✅ Usa nuevo listener con data y position
//        adapter = new ImageAdapter((data, position) -> {
//            if (data.fullImage == null) {
//                new FullImageDownloadTask(MainActivity.this, data, position).execute();
//            } else {
//                openImageActivity(data, position);
//            }
//        });
          adapter = new ImageAdapter((data, position) -> {
            // ✅ Este Toast aparece cuando haces clic en una imagen
            Toast.makeText(this, "Clic detectado en: " + data.title, Toast.LENGTH_SHORT).show();

            if (data.fullImage == null) {
                new FullImageDownloadTask(MainActivity.this, data, position).execute();
            } else {
                openImageActivity(data, position);
            }
        });

        recyclerView.setAdapter(adapter);

        buttonHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // ✅ Cargar datos con HtmlParserTask
        new HtmlParserTask(dataList -> {
            for (DataSingleton.ImageData data : dataList) {
                String filename = extractFilename(data.src);
                DataSingleton.getInstance().addImageData(filename, data);
            }
            adapter.setData(DataSingleton.getInstance().getAllImages());
        }).execute();
    }

    // ✅ openImageActivity ahora recibe posición
    private void openImageActivity(DataSingleton.ImageData data, int position) {
        String filename = extractFilename(data.src);
        DataSingleton.getInstance().logImageAccess(filename);

        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("filename", filename);
        intent.putExtra("position", position + 1); // para mostrar el número (desde 1)
        startActivity(intent);
    }

    private String extractFilename(String src) {
        return src.substring(src.lastIndexOf("/") + 1);
    }
}
