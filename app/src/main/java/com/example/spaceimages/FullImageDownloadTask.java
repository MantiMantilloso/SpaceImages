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

import java.lang.ref.WeakReference;
import java.net.URL;

public class FullImageDownloadTask extends AsyncTask<Void, Void, Bitmap> {
    private final DataSingleton.ImageData data;
    private final WeakReference<Context> contextRef;
    private final int position;

    // ✅ Ahora recibe la posición
    public FullImageDownloadTask(Context context, DataSingleton.ImageData data, int position) {
        this.data = data;
        this.contextRef = new WeakReference<>(context);
        this.position = position;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        try {
            String filename = extractFilename(data.src);
            String urlStr = "https://cdn.esahubble.org/archives/images/large/" + filename;
            URL url = new URL(urlStr);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Context context = contextRef.get();
        if (bitmap != null && context != null) {
            data.fullImage = bitmap;
            String filename = extractFilename(data.src);
            DataSingleton.getInstance().logImageAccess(filename);

            Intent intent = new Intent(context, ImageActivity.class);
            intent.putExtra("filename", filename);
            intent.putExtra("position", position + 1); // Para mostrar el número
            context.startActivity(intent);
        }
    }

    private String extractFilename(String src) {
        return src.substring(src.lastIndexOf("/") + 1);
    }
}
