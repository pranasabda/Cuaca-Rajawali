package pranasabda.id.cuacarajawali;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import java.util.Date;
import java.util.List;

import pranasabda.id.cuacarajawali.adapter.CuacaAdapter;
import pranasabda.id.cuacarajawali.model.Cuaca;

public class CuacaBasedActivity extends AppCompatActivity {

    private AlertDialog.Builder alert;
    private ListView listView;
    private List<Cuaca> cuacas;


    private String URL = "http://samples.openweathermap.org/data/2.5/forecast?zip=94040&appid=b6907d289e10d714a6e88b30761fae22";
    //String URL = "http://samples.openweathermap.org/data/2.5/forecast?zip="+mtv_zipcode+"&appid=b6907d289e10d714a6e88b30761fae22";

    //"http://samples.openweathermap.org/data/2.5/forecast?zip=94040&appid=b6907d289e10d714a6e88b30761fae22"
    //  private String URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Bandung&APPID=2939b4f9a70e7dd25e181b06ab14bc5d&mode=json&units=metric&cnt=17";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuaca_based);

        alert = new AlertDialog.Builder(this);

        TextView m_tv_fullname = (TextView)findViewById(R.id.tv_fullname);
        TextView m_tv_zipcode = (TextView)findViewById(R.id.tv_zipcode);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        String mtv_fullname =(String) b.get("FirstName");
        String mtv_lastname = (String) b.get("LastName");
        String mtv_zipcode =(String) b.get("ZipCode");

        if(b!=null)
        {
            m_tv_fullname.setText(mtv_fullname+" "+mtv_lastname);
            m_tv_zipcode.setText(mtv_zipcode);
        }


        listView = (ListView) findViewById(R.id.list_item_cuaca);
        cuacas = new ArrayList<Cuaca>();

        String URL = "http://samples.openweathermap.org/data/2.5/forecast?zip="+mtv_zipcode+"&appid=b6907d289e10d714a6e88b30761fae22";
        RequestParams params = new RequestParams();
        requestCuaca(params);


    }

    private void requestCuaca(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL, params, new AsyncHttpResponseHandler(){

            //Hide progress dialog

            @Override
            public void onSuccess(String response) {

                android.util.Log.d("Respon e",">"+response);

                try{
                    JSONObject weather = new JSONObject(response);
                    JSONArray listWeather = weather.getJSONArray("list");

                    //looping through all cuaca
                    for(int i=0; i<listWeather.length(); i++) {
                        JSONObject weatherData = listWeather.getJSONObject(i);
                        String dt = convertDataTime(weatherData.getLong("dt"));

                        JSONArray main = weatherData.getJSONArray("weather");
                        String w = main.getJSONObject(0).getString("main");

                        JSONObject temp = weatherData.getJSONObject("main");
                        String t = String.valueOf(temp.getInt("temp"))+ "°";

                        JSONObject humidity = weatherData.getJSONObject("main");
                        String h = String.valueOf(humidity.getInt("humidity"))+ "%";

                        cuacas.add(new Cuaca(dt,t,w,h));
                        TextView m_tv_temp_cuaca_today = (TextView)findViewById(R.id.tv_temp_cuaca_today);
                        TextView m_tv_detail_cuaca_today = (TextView)findViewById(R.id.tv_cuaca_id);
                        m_tv_temp_cuaca_today.setText(t);
                        m_tv_detail_cuaca_today.setText(w);

                    }

                    CuacaAdapter adapter = new CuacaAdapter(CuacaBasedActivity.this, cuacas);
                    listView.setAdapter(adapter);


                } catch (Exception e){
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
                }
                else if (statusCode == 500) {
                    alert.setTitle("Gagal");
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setMessage("Terjadi kesalahan - 500");
                    alert.show();
                }
                else if (statusCode == 502) {
                    alert.setTitle("Gagal");
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setMessage("Terjadi kesalahan BadGateWay - 502");
                    alert.show();
                }
                else {
                    alert.setTitle("Gagal No Code ZIP");
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setMessage("Terjadi kesalahan Kode ZIP dari API adalah : 94040 atau Koneksi Putus");
                    alert.show();
                }

                // JANGAN LUPA INTERNET PERMISION DI MANIFEST
                //    <uses-permission android:name="android.permission.INTERNET"></uses-permission>

            }
        });
    }

    private String convertDataTime(Long dataTime) {
        Date date = new Date(dataTime*1000);
        //pattern data harus benar "EEE, dd MMM"
        Format dateTimeFormat = new SimpleDateFormat("EEE,dd MMM");
        return dateTimeFormat.format(date);
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
