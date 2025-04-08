package com.example.spaceimages;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button clearButton;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recycler_history);
        clearButton = findViewById(R.id.button_clear);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter();
        recyclerView.setAdapter(adapter);

        loadHistory();

        clearButton.setOnClickListener(v -> showClearConfirmation());
    }

    private void loadHistory() {
        List<DataSingleton.HistoryEntry> history = DataSingleton.getInstance().getDownloadHistory();
        Log.d("History", "Loaded history size: " + history.size());
        adapter.setData(history);
    }

    private void showClearConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("¿Borrar historial?")
                .setMessage("Se eliminarán todas las imágenes del historial y del caché.")
                .setPositiveButton("Sí", (dialog, which) -> {
                    DataSingleton.getInstance().clearAll();
                    adapter.setData(DataSingleton.getInstance().getDownloadHistory());
                    Toast.makeText(this, "Historial borrado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
