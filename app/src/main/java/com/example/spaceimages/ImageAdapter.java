//package com.example.spaceimages;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
//
//    public interface OnItemClickListener {
//        void onItemClick(DataSingleton.ImageData data);
//    }
//
//    private List<DataSingleton.ImageData> sortedImages = new ArrayList<>();
//    private final OnItemClickListener listener;
//
//    public ImageAdapter(OnItemClickListener listener) {
//        this.listener = listener;
//    }
//
//    public void setData(Collection<DataSingleton.ImageData> data) {
//        sortedImages = data.stream()
//                .sorted((a, b) -> (b.width * b.height) - (a.width * a.height))
//                .collect(Collectors.toList());
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_image, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
//        DataSingleton.ImageData data = sortedImages.get(position);
//        holder.title.setText((position + 1) + ". " + data.title);
//        holder.url.setText(data.src);
//
//        if (data.thumbnail != null) {
//            holder.icon.setImageBitmap(data.thumbnail);
//        } else {
//            new ThumbnailDownloadTask(holder.icon, data).execute();
//        }
//
//        holder.itemView.setOnClickListener(v -> listener.onItemClick(data));
//    }
//
//    @Override
//    public int getItemCount() {
//        return sortedImages.size();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView title, url;
//        ImageView icon;
//
//        ViewHolder(View itemView) {
//            super(itemView);
//            title = itemView.findViewById(R.id.text_title);
//            url = itemView.findViewById(R.id.text_url);
//            icon = itemView.findViewById(R.id.image_icon);
//        }
//    }
//}

package com.example.spaceimages;

import static com.example.spaceimages.DataSingleton.extractFilename;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.*;
import java.util.stream.Collectors;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final Context context;
    private List<DataSingleton.ImageData> sortedImages = new ArrayList<>();
    private final OnItemClickListener listener;

    // Cambiamos la interfaz para incluir la posición
    public interface OnItemClickListener {
        void onItemClick(DataSingleton.ImageData data, int position);
    }

    public ImageAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataSingleton.ImageData data = sortedImages.get(position);

        // Set position number (1-based)
        holder.number.setText(String.valueOf(position + 1));

        // Set URL
        holder.url.setText(data.src);

        // Load thumbnail
        if (data.thumbnail != null) {
            holder.thumbnail.setImageBitmap(data.thumbnail);
        } else {
            new ThumbnailDownloadTask(holder.thumbnail, data).execute();
        }

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageActivity.class);
            intent.putExtra("position", position + 1); // 1-based index
            intent.putExtra("filename", extractFilename(data.src));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sortedImages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, url;
        ImageView thumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.tv_number);
            url = itemView.findViewById(R.id.tv_url);
            thumbnail = itemView.findViewById(R.id.iv_thumbnail);
        }
    }
}
