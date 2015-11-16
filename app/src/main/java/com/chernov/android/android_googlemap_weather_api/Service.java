package com.chernov.android.android_googlemap_weather_api;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *  http://api.openweathermap.org/data/2.5/forecast/city?id=524901&APPID={APIKEY}
 *  http://api.openweathermap.org/data/2.5/weather?q=London&appid=a690d3ef090a386e100eed2f74d7152c
 */

public class Service extends IntentService {

    private static String IMG_URL = "http://openweathermap.org/img/w/";
    private static final String ENDPOINT = "http://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "a690d3ef090a386e100eed2f74d7152c";
    public static final String RECEIVER = "receiver";
    public static final String ITEMS = "items";
    static volatile boolean isStopped = false;
    private static final String TAG = "Something";

    // создаем объект для хранения данных

    public Service() {
        super("Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "onHandleIntent");

        ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);

        Item itema = null;
        try {
            itema = getPage(intent.getStringExtra("city"));
            Log.i(TAG, "itema = " + itema.getCity());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(itema!=null) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(ITEMS, itema);
            receiver.send(1, bundle);
        } else {
            receiver.send(0, null);
        }

        isStopped = true;
    }

    public Item getPage(String city) throws IOException, JSONException {
        // Формирование строки запроса
        String url = Uri.parse(ENDPOINT).buildUpon()
           .appendQueryParameter("q", city)
           .appendQueryParameter("appid", API_KEY)
           .build().toString();
        return parseItems(getUrl(url));
    }

    // Парсинг полученной JSON-строки
    public Item parseItems(String page) throws IOException, JSONException {
        Item item = new Item();
        JSONObject jObj = new JSONObject(page);

        JSONObject coordObj = jObj.getJSONObject("coord");

        item.setLon(coordObj.getString("lon"));
        item.setLat(coordObj.getString("lat"));
        item.setCity(jObj.getString("name"));

        JSONObject sysObj = jObj.getJSONObject("sys");

        item.setSunrise(sysObj.getString("sunrise"));
        item.setSunset(sysObj.getString("sunset"));
        item.setCountry(sysObj.getString("country"));

        JSONArray jArr = jObj.getJSONArray("weather");
        JSONObject JSONWeather = jArr.getJSONObject(0);

        item.setMain(JSONWeather.getString("main"));
        item.setIcon(JSONWeather.getString("icon"));

        JSONObject mainObj = jObj.getJSONObject("main");

        item.setHumidity(mainObj.getString("humidity"));
        item.setPressure(mainObj.getString("pressure"));
        item.setTemp_max(mainObj.getString("temp_max"));
        item.setTemp_min(mainObj.getString("temp_min"));
        item.setTemp(mainObj.getString("temp"));

        // Wind
        JSONObject wObj = jObj.getJSONObject("wind");

        item.setSpeed(wObj.getString("speed"));
        item.setDeg(wObj.getString("deg"));

        // Clouds
        JSONObject cObj = jObj.getJSONObject("clouds");

        item.setAll(cObj.getString("all"));

        // получаем бинарный код картинки
        byte[] bitmapBytes = getUrlBytes(IMG_URL + item.getIcon() + ".png");
        // из этого кода делаем Bitmap приминяем готовый битмап к item
        item.setBitmap(BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length));

        return item;
    }

    // получает данные по URL и возвращает их в виде массива байтов
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        // создаем объект URL на базе строки urlSpec
        URL url = new URL(urlSpec);
        /**
         *  создаем объект подключения к заданному URL адресу
         *  url.openConnection() - возвращает URLConnection (подключение по протоколу HTTP)
         *  это откроет доступ для работы с методами запросов.
         *  HttpURLConnection - предоставляет подключение
         */
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            // создаем пустой массив байтов
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // связь с конечной точкой
            InputStream in = connection.getInputStream();
            // если подключение с интернетом отсутствует (нет кода страницы)
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            // разбираем код по 1024 байта, пока не закончится информация
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            // чтение закончено, выдаем массив байтов
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    // преобразует результат (массив байтов) из getUrlBytes(String) в String
    String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
}
