package com.example.spaceimages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.*;
import java.util.stream.Collectors;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(DataSingleton.ImageData data);
    }

    private List<DataSingleton.ImageData> sortedImages = new ArrayList<>();
    private final OnItemClickListener listener;

    public ImageAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(Collection<DataSingleton.ImageData> data) {
        sortedImages = data.stream()
                .sorted((a, b) -> (b.width * b.height) - (a.width * a.height))
                .collect(Collectors.toList());
        notifyDataSetChanged();
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
        DataSingleton.ImageData data = sortedImages.get(position);
        holder.title.setText((position + 1) + ". " + data.title);
        holder.url.setText(data.src);

        if (data.thumbnail != null) {
            holder.icon.setImageBitmap(data.thumbnail);
        } else {
            new ThumbnailDownloadTask(holder.icon, data).execute();
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(data));
    }

    @Override
    public int getItemCount() {
        return sortedImages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, url;
        ImageView icon;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_title);
            url = itemView.findViewById(R.id.text_url);
            icon = itemView.findViewById(R.id.image_icon);
        }
    }
}

