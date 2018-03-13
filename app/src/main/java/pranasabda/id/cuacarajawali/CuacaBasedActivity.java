package pranasabda.id.cuacarajawali;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pranasabda.id.cuacarajawali.adapter.CuacaAdapter;
import pranasabda.id.cuacarajawali.model.Cuaca;

public class CuacaBasedActivity extends AppCompatActivity {

    private AlertDialog.Builder alert;
    private ListView listView;
    private List<Cuaca> cuacas;


    //private String URL = "http://samples.openweathermap.org/data/2.5/forecast?zip=94040&appid=b6907d289e10d714a6e88b30761fae22";
    //String URL = "http://samples.openweathermap.org/data/2.5/forecast?zip="+mtv_zipcode+"&appid=b6907d289e10d714a6e88b30761fae22";

    //"http://samples.openweathermap.org/data/2.5/forecast?zip=94040&appid=b6907d289e10d714a6e88b30761fae22"
    //  private String URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Bandung&APPID=2939b4f9a70e7dd25e181b06ab14bc5d&mode=json&units=metric&cnt=17";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuaca_based);

        alert = new AlertDialog.Builder(this);

        TextView m_tv_fullname = (TextView) findViewById(R.id.tv_fullname);
        TextView m_tv_zipcode = (TextView) findViewById(R.id.tv_zipcode);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        String mtv_fullname = (String) b.get("FirstName");
        String mtv_lastname = (String) b.get("LastName");
        String mtv_zipcode = (String) b.get("ZipCode");

        if (b != null) {
            m_tv_fullname.setText(mtv_fullname + " " + mtv_lastname);
            m_tv_zipcode.setText(mtv_zipcode);
        }


        listView = (ListView) findViewById(R.id.list_item_cuaca);
        cuacas = new ArrayList<Cuaca>();
        String URL = "http://api.openweathermap.org/data/2.5/forecast?zip=" + mtv_zipcode + ",us&APPID=2939b4f9a70e7dd25e181b06ab14bc5d&mode=json&units=metric&cnt=17";

        //Sample data sama semua.
        //"http://samples.openweathermap.org/data/2.5/forecast?zip="+mtv_zipcode+"&appid=b6907d289e10d714a6e88b30761fae22"

        RequestParams params = new RequestParams();
        requestCuaca(params, URL);
        todayForcast(params, URL);
        getTimeFromAndroid();

        //Add Back Button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void requestCuaca(RequestParams params, String URL) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL, params, new AsyncHttpResponseHandler() {

            //Hide progress dialog

            @Override
            public void onSuccess(String response) {

                android.util.Log.d("Respon e", ">" + response);

                try {
                    JSONObject weather = new JSONObject(response);
                    JSONArray listWeather = weather.getJSONArray("list");

                    //looping through all cuaca
                    for (int i = 0; i < listWeather.length(); i++) {
                        JSONObject weatherData = listWeather.getJSONObject(i);
                        String dt = convertDataTime(weatherData.getLong("dt"));

                        JSONArray main = weatherData.getJSONArray("weather");
                        String w = main.getJSONObject(0).getString("main");

                        JSONObject temp = weatherData.getJSONObject("main");
                        String t = String.valueOf(temp.getInt("temp")) + "°";

                        JSONObject humidity = weatherData.getJSONObject("main");
                        String h = String.valueOf(humidity.getInt("humidity")) + "%";

                        cuacas.add(new Cuaca(dt, t, w, h));
//                        TextView m_tv_temp_cuaca_today = (TextView)findViewById(R.id.tv_temp_cuaca_today);
//                        TextView m_tv_detail_cuaca_today = (TextView)findViewById(R.id.tv_cuaca_id);
//
//                        m_tv_temp_cuaca_today.setText(t);
//                        m_tv_detail_cuaca_today.setText(w);
                    }

                    CuacaAdapter adapter = new CuacaAdapter(CuacaBasedActivity.this, cuacas);
                    listView.setAdapter(adapter);


                } catch (Exception e) {
                    //RTO
                    e.printStackTrace();
                    alert.setTitle("Gagal");
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setMessage("Terjadi Kesalahan");
                    alert.show();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {

                //No internate connection
                alert.setTitle("Gagal");
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setMessage("No Internet Connection");
                alert.show();

                //When http response code in '404'
                if (statusCode == 404) {
                    alert.setTitle("Gagal");
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setMessage("Terjadi kesalahan - 404");
                    alert.show();
                } else if (statusCode == 500) {
                    alert.setTitle("Gagal");
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setMessage("Terjadi kesalahan - 500");
                    alert.show();
                } else if (statusCode == 502) {
                    alert.setTitle("Gagal");
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setMessage("Terjadi kesalahan BadGateWay - 502");
                    alert.show();
                } else {
                    alert.setTitle("Gagal No Code ZIP");
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setMessage("Terjadi kesalahan Kode ZIP dari API adalah : 94040 atau Koneksi Putus");
                    alert.show();
                }

            }
        });
    }

    private String convertDataTime(Long dataTime) {
        Date date = new Date(dataTime * 1000);
        //pattern data harus benar contoh "EEE, dd MMM" atau "EEEE, dd MMM yy hh:mm"
        Format dateTimeFormat = new SimpleDateFormat("EEE,dd MMM");
        return dateTimeFormat.format(date);
    }

    private void todayForcast(RequestParams params, String URL) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL, params, new AsyncHttpResponseHandler() {
            //android.util.Log.d("Respon e",">"+response);
            //looping through all cuaca
            //for(int i=0; i<1; i++)
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject weather = new JSONObject(response);
                    JSONArray listWeather = weather.getJSONArray("list");
                    int i = 0;
                    if (i == 0) {
                        JSONObject weatherData = listWeather.getJSONObject(i);
                        String dt = convertDataTime(weatherData.getLong("dt"));

                        JSONArray main = weatherData.getJSONArray("weather");
                        String w = main.getJSONObject(0).getString("main");

                        JSONObject temp = weatherData.getJSONObject("main");
                        String t = String.valueOf(temp.getInt("temp")) + "°";

                        JSONObject humidity = weatherData.getJSONObject("main");
                        String h = String.valueOf(humidity.getInt("humidity")) + "%";

                        cuacas.add(new Cuaca(dt, t, w, h));
                        TextView m_tv_temp_cuaca_today = (TextView) findViewById(R.id.tv_temp_cuaca_today);
                        TextView m_tv_detail_cuaca_today = (TextView) findViewById(R.id.tv_cuaca_id);
                        ImageView detailImage = (ImageView) findViewById(R.id.icon_cuaca_today);
                        m_tv_temp_cuaca_today.setText(t);
                        m_tv_detail_cuaca_today.setText(w);

                        if (w.equals("Clear")) {
                            detailImage.setImageResource(R.drawable.ic_011_sun_white);
                        } else if (w.equals("Clouds")) {
                            detailImage.setImageResource((R.drawable.ic_009_cloudy_white));
                        } else if (w.equals("light_clouds")) {
                            detailImage.setImageResource((R.drawable.ic_008_cloud_white));
                        } else if (w.equals("Rain")) {
                            detailImage.setImageResource((R.drawable.ic_006_raining_white));
                        } else if (w.equals("Wind")) {
                            detailImage.setImageResource((R.drawable.ic_003_wind_white));
                        } else {
                            detailImage.setImageResource(R.drawable.ic_001_wind_1_white);
                        }

                    }


                } catch (Exception e) {
                    //RTO
                    e.printStackTrace();
                    alert.setTitle("Gagal");
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setMessage("Terjadi Kesalahan");
                    alert.show();
                }
            }
        });
    }

    private void getTimeFromAndroid() {
//        Date dt = new Date();
        Calendar c = Calendar.getInstance();
//        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        Log.d("jam", "getTimeFromAndroid: " + hours);

        if (hours >= 0 && hours <= 12) {
            TextView m_tv_greeting = (TextView) findViewById(R.id.tv_greeting);
            m_tv_greeting.setText("Good Morning");
        } else if (hours >= 12 && hours <= 16) {
            TextView m_tv_greeting = (TextView) findViewById(R.id.tv_greeting);
            m_tv_greeting.setText("Good Afternoon");
        } else if (hours >= 16 && hours <= 21) {
            TextView m_tv_greeting = (TextView) findViewById(R.id.tv_greeting);
            m_tv_greeting.setText("Good Evening");
        } else if (hours >= 21 && hours <= 24) {
            TextView m_tv_greeting = (TextView) findViewById(R.id.tv_greeting);
            m_tv_greeting.setText("Good Night");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
