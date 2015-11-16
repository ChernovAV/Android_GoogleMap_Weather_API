package com.chernov.android.android_googlemap_weather_api;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

public class MainFragment extends Fragment {

    private SearchView mSearchView;
    ImageView image;
    Item weather = new Item();
    private GoogleMap map;
    TextView lon, lat, main, temp, pressure, humidity, temp_min,
                    temp_max, speed, deg, sunrise, sunset, country;
    String onCity = "Киев";
    //String, that is filled after you restore previous
    String queryToSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // при смене ориентации экрана фрагмент сохраняет свое состояние. onDestroy не вызывается
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instance) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        TextLinksSet(v);

        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL); //MAP_TYPE_SATELLITE //MAP_TYPE_TERRAIN

        ((SingleFragmentActivity) getActivity()).getSupportActionBar().setTitle(onCity);

        return v;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

          if(bundle == null) {
              startService(onCity);
          } else {
              setInfo();

              map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                      new LatLng(
                              bundle.getDouble("lat"),
                              bundle.getDouble("lon")),
                      bundle.getInt("zoom")));
          }
    }

    // общение между потоками через ResultReceiver
    private final ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
        @SuppressWarnings("unchecked")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            /**
             *  если resultCode==1, принимаем List
             *  после передергиваем адаптер
             */
           if(resultCode==1) {
                weather = resultData.getParcelable(Service.ITEMS);
                onCity = weather.getCity();
               ((SingleFragmentActivity) getActivity()).getSupportActionBar().setTitle(onCity);
                setInfo();
           } else {
                Toast.makeText(getActivity(), getString(R.string.error),
                        Toast.LENGTH_LONG).show();
           }
        };
    };

    public void TextLinksSet(View v) {
        lon = (TextView) v.findViewById(R.id.lon);
        lat = (TextView) v.findViewById(R.id.lat);
        main = (TextView) v.findViewById(R.id.main);
        temp = (TextView) v.findViewById(R.id.temp);
        pressure = (TextView) v.findViewById(R.id.pressure);
        humidity = (TextView) v.findViewById(R.id.humidity);
        temp_min = (TextView) v.findViewById(R.id.temp_min);
        temp_max = (TextView) v.findViewById(R.id.temp_max);
        speed = (TextView) v.findViewById(R.id.speed);
        deg = (TextView) v.findViewById(R.id.deg);
        sunrise = (TextView) v.findViewById(R.id.sunrise);
        sunset = (TextView) v.findViewById(R.id.sunset);
        country = (TextView) v.findViewById(R.id.country);
        image = (ImageView) v.findViewById(R.id.image);
    }

    private void setInfo() {
        lon.setText(getString(R.string.lon) + weather.getLon());
        lat.setText(getString(R.string.lat) + weather.getLat());
        main.setText(getString(R.string.main) + weather.getMain());
        temp.setText(getString(R.string.temp) + weather.getTemp());
        pressure.setText(getString(R.string.pressure) + weather.getPressure());
        humidity.setText(getString(R.string.humidity) + weather.getHumidity());
        temp_min.setText(getString(R.string.temp_min) + weather.getTemp_min());
        temp_max.setText(getString(R.string.temp_max) + weather.getTemp_max());
        speed.setText(getString(R.string.speed) + weather.getSpeed());
        deg.setText(getString(R.string.deg) + weather.getDeg());
        sunrise.setText(getString(R.string.sunrise) + getTime(weather.getSunrise()));
        sunset.setText(getString(R.string.sunset) + getTime((weather.getSunset())));
        country.setText(getString(R.string.country) + weather.getCountry());
        // применим icon
        // setImage();
        // настройка googlemap
        setMap(weather.getLon(), weather.getLat());
    }

    private void setImage() {
        // применяем bitmap к image
       // image.setImageBitmap(weather.getBitmap());
    }

    // получаем time
    private void setMap(String lon, String lat) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(Float.parseFloat(lat), Float.parseFloat(lon)), 7));

    }

    // получаем time
    private String getTime(String date) {
        // создаем instance календаря
        Calendar mydate = Calendar.getInstance();
        // получаем format: "2008-03-01T13:00:00+01:00" из Unix
        mydate.setTimeInMillis(Integer.parseInt(date) * 1000);
        // оставляем только время, обрезаем лишнее
        String time = mydate.getTime().toString().substring(11, 19);
        return time;
    }

    // запускаем сервис, парсим страницу
    private void startService(String city) {
        Intent intent = new Intent(getActivity(), Service.class);
        intent.putExtra(Service.RECEIVER, resultReceiver);
        intent.putExtra("city", city);
        getActivity().startService(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                // Not implemented here
                return false;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

        mSearchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        mSearchView.setQueryHint("Enter city...");
        mSearchView.requestFocus();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startService(query);
                queryToSave = null;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryToSave = newText;
                if(queryToSave.length()==0) queryToSave = null;
                return false;
            }
        });
        mSearchView.post(new Runnable() {
            @Override
            public void run() {
                if(queryToSave != null) {
                    mSearchView.setQuery(queryToSave, false);
                    mSearchView.setFocusable(true);
                    mSearchView.setIconified(false);
                    mSearchView.requestFocusFromTouch();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("lat", map.getCameraPosition().target.latitude);
        outState.putDouble("lon", map.getCameraPosition().target.longitude);
        outState.putInt("zoom", (int) map.getCameraPosition().zoom);
    }

    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}