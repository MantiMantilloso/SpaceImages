package com.example.spaceimages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.*;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<DataSingleton.HistoryEntry> historyList = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public void setData(List<DataSingleton.HistoryEntry> list) {
        historyList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataSingleton.HistoryEntry entry = historyList.get(position);
        holder.textFilename.setText(entry.filename);
        holder.textViews.setText("Visto: " + entry.viewCount + " veces");
        holder.textDate.setText("Fecha: " + dateFormat.format(entry.downloadTime));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textFilename, textViews, textDate;

        ViewHolder(View itemView) {
            super(itemView);
            textFilename = itemView.findViewById(R.id.text_filename);
            textViews = itemView.findViewById(R.id.text_views);
            textDate = itemView.findViewById(R.id.text_date);
        }
    }
}
