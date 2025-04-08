//package com.example.spaceimages;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//
//import java.lang.ref.WeakReference;
//import java.net.URL;
//
//public class FullImageDownloadTask extends AsyncTask<Void, Void, Bitmap> {
//    private final DataSingleton.ImageData data;
//    private final WeakReference<Context> contextRef;
//
//    public FullImageDownloadTask(Context context, DataSingleton.ImageData data) {
//        this.data = data;
//        this.contextRef = new WeakReference<>(context);
//    }
//
//    @Override
//    protected Bitmap doInBackground(Void... voids) {
//        try {
//            String filename = extractFilename(data.src);
//            String urlStr = "https://cdn.esahubble.org/archives/images/large/" + filename;
//            URL url = new URL(urlStr);
//            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Bitmap bitmap) {
//        if (bitmap != null && contextRef.get() != null) {
//            data.fullImage = bitmap;
//            String filename = extractFilename(data.src);
//            DataSingleton.getInstance().logImageAccess(filename);
//
//            Intent intent = new Intent(contextRef.get(), ImageActivity.class);
//            intent.putExtra("filename", filename);
//            contextRef.get().startActivity(intent);
//        }
//    }
//
//    private String extractFilename(String src) {
//        return src.substring(src.lastIndexOf("/") + 1);
//    }
//}

package com.example.spaceimages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.net.URL;

public class FullImageDownloadTask extends AsyncTask<Void, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewRef;
    private final DataSingleton.ImageData data;

    public FullImageDownloadTask(ImageView imageView, DataSingleton.ImageData data) {
        this.imageViewRef = new WeakReference<>(imageView);
        this.data = data;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        try {
            String filename = extractFilename(data.src);
            URL url = new URL("https://cdn.esahubble.org/archives/images/large/" + filename);
            Log.d("FullImageDownload", "Downloaded img from: "+url);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            Log.e("FullImageDownload", "Error loading full image", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView imageView = imageViewRef.get();
        if (bitmap != null && imageView != null) {
            imageView.setImageBitmap(bitmap);
            data.fullImage = bitmap; // Cache full image
        }
    }

    private String extractFilename(String src) {
        return src.substring(src.lastIndexOf("/") + 1);
    }
}
