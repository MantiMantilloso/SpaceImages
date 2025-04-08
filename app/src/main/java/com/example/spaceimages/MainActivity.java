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


        adapter = new ImageAdapter(MainActivity.this,(data, position) -> {
            openImageActivity(data, position); // Always open immediately
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
        intent.putExtra("position", position + 1);
        intent.putExtra("thumbnail", data.thumbnail); // Pass thumbnail bitmap
        startActivity(intent);
    }

    private String extractFilename(String src) {
        return src.substring(src.lastIndexOf("/") + 1);
    }
}
