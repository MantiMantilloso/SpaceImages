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

        // Cargar datos con HtmlParserTask
        reloadImageData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (DataSingleton.getInstance().getAllImages().isEmpty()) {
            reloadImageData();
        }
    }
    private void reloadImageData() {
        new HtmlParserTask(dataList -> {
            for (DataSingleton.ImageData data : dataList) {
                String filename = extractFilename(data.src);
                DataSingleton.getInstance().addImageData(filename, data);
            }
            adapter.setData(DataSingleton.getInstance().getAllImages());
        }).execute();
    }

    private void openImageActivity(DataSingleton.ImageData data, int position) {
        String filename = extractFilename(data.src);


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
