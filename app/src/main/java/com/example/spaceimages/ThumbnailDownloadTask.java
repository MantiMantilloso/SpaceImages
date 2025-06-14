//package com.example.spaceimages;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.widget.ImageView;
//
//import java.lang.ref.WeakReference;
//import java.net.URL;
//
//public class ThumbnailDownloadTask extends AsyncTask<Void, Void, Bitmap> {
//    private final WeakReference<ImageView> imageViewRef;
//    private final DataSingleton.ImageData data;
//
//    public ThumbnailDownloadTask(ImageView imageView, DataSingleton.ImageData data) {
//        this.imageViewRef = new WeakReference<>(imageView);
//        this.data = data;
//    }
//
//    @Override
//    protected Bitmap doInBackground(Void... voids) {
//        try {
//            String filename = extractFilename(data.src);
//            String urlStr = "https://cdn.esahubble.org/archives/images/thumb300y/" + filename;
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
//        if (bitmap != null && imageViewRef.get() != null) {
//            imageViewRef.get().setImageBitmap(bitmap);
//            data.thumbnail = bitmap;
//        }
//    }
//
//    private String extractFilename(String src) {
//        return src.substring(src.lastIndexOf("/") + 1);
//    }
//}
package com.example.spaceimages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.net.URL;

public class ThumbnailDownloadTask extends AsyncTask<Void, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewRef;
    private final DataSingleton.ImageData data;

    public ThumbnailDownloadTask(ImageView imageView, DataSingleton.ImageData data) {
        this.imageViewRef = new WeakReference<>(imageView);
        this.data = data;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        try {
            String filename = data.src.substring(data.src.lastIndexOf("/") + 1);
            String thumbnailUrl = "https://cdn.esahubble.org/archives/images/thumb300y/" + filename;
            URL url = new URL(thumbnailUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView imageView = imageViewRef.get();
        if (bitmap != null && imageView != null) {
            imageView.setImageBitmap(bitmap);
            data.thumbnail = bitmap;
        }
    }
}
