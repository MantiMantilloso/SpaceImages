package com.example.spaceimages;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParserTask extends AsyncTask<Void, Void, List<DataSingleton.ImageData>> {

    private final HtmlParserCallback callback;

    public interface HtmlParserCallback {
        void onParsed(List<DataSingleton.ImageData> dataList);
    }

    public HtmlParserTask(HtmlParserCallback callback) {
        this.callback = callback;
    }

    @Override
    protected List<DataSingleton.ImageData> doInBackground(Void... voids) {
        try {
            URL url = new URL("https://spacetelescope.org/images/archive/category/galaxies/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String html = convertStreamToString(in);

            Pattern pattern = Pattern.compile("var images = (\\[.*?\\]);", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(html);

            if (matcher.find()) {
                String jsonArray = matcher.group(1);
                return parseImageData(jsonArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<DataSingleton.ImageData> parseImageData(String json) {
        List<DataSingleton.ImageData> result = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                DataSingleton.ImageData data = new DataSingleton.ImageData();
                data.title = obj.getString("title");
                data.width = obj.getInt("width");
                data.height = obj.getInt("height");
                data.src = obj.getString("src");
                result.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String convertStreamToString(InputStream inputStream) throws Exception {
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    @Override
    protected void onPostExecute(List<DataSingleton.ImageData> dataList) {
        if (callback != null && dataList != null) {
            callback.onParsed(dataList);
        }
    }
}
