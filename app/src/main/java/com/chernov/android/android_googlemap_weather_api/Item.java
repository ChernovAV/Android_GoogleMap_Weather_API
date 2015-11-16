package com.chernov.android.android_googlemap_weather_api;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    // долгота
    String lon;
    // широта
    String lat;
    // основная информация (облачно)
    String main;
    // icon
    String icon;
    // температура
    String temp;
    // атмосферное давление
    String pressure ;
    // влажность
    String humidity;
    // минимальная температура
    String temp_min;
    // максимальная температура
    String temp_max;
    // скорость ветра
    String speed;
    // направление ветра
    String deg;
    // облачность
    String all;
    // sunrise
    String sunrise;
    // sunset
    String sunset;
    // город
    String city;
    // Страна
    String country;
    // код в
    Bitmap image;

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBitmap(Bitmap image) {
        this.image = image;
    }

    String getLon() {
        return lon;
    }

    String getLat() {
        return lat;
    }

    String getMain() {
        return main;
    }

    String getTemp() {
        return temp;
    }

    String getIcon() {
        return icon;
    }

    String getPressure() {
        return pressure;
    }

    String getHumidity() {
        return humidity;
    }

    String getTemp_min() {
        return temp_min;
    }

    String getTemp_max() {
        return temp_max;
    }

    String getSpeed() {
        return speed;
    }

    String getDeg() {
        return deg;
    }

    String getAll() {
        return all;
    }

    String getSunrise() {
        return sunrise;
    }

    String getSunset() {
        return sunset;
    }

    String getCity() {
        return city;
    }

    String getCountry() {
        return country;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lon);
        dest.writeString(lat);
        dest.writeString(main);
        dest.writeString(icon);
        dest.writeString(temp);
        dest.writeString(pressure);

        dest.writeString(humidity);
        dest.writeString(temp_min);
        dest.writeString(temp_max);
        dest.writeString(speed);

        dest.writeString(deg);
        dest.writeString(all);
        dest.writeString(sunrise);
        dest.writeString(sunset);

        dest.writeString(city);
        dest.writeString(country);
    }
}