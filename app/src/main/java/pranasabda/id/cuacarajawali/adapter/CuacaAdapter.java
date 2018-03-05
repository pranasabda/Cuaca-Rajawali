package pranasabda.id.cuacarajawali.adapter;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pranasabda.id.cuacarajawali.R;
import pranasabda.id.cuacarajawali.model.Cuaca;

/**
 * Created by prana on 05/03/18.
 */

public class CuacaAdapter extends ArrayAdapter<Cuaca> {
    private final AppCompatActivity context;
    private final List<Cuaca> cuaca;

    public CuacaAdapter(AppCompatActivity context, List<Cuaca> cuaca){
        super(context, R.layout.list_item, cuaca);
        this.context = context;
        this.cuaca = cuaca;
    }

    public View getView(int position, View view, ViewGroup parent) {

        //Mempersiapkan Layout / ambil piring
        LayoutInflater inflater = context.getLayoutInflater();

        //siapin data / di buka
        View rowView = inflater.inflate(R.layout.list_item,null,true);

        //Manggil data / data jadi
        TextView dt = (TextView) rowView.findViewById(R.id.tv_day);
        TextView weather = (TextView) rowView.findViewById(R.id.tv_cuaca);
        TextView temp = (TextView) rowView.findViewById(R.id.tv_temp_detail);
        TextView humadity = (TextView) rowView.findViewById(R.id.tv_humadity_detail);

        //nampilin data / siap disajikan
//        dt.setText(cuaca[position].getDt());
//        weather.setText(cuaca[position].getWeather());
//        temp.setText(cuaca[position].getTemp());

        //nampilin data / siap disajikan
        dt.setText(cuaca.get(position).getDt());
        weather.setText(cuaca.get(position).getWeather());
        temp.setText(cuaca.get(position).getTemp());
        humadity.setText(cuaca.get(position).getHumadity());


        return rowView;
    }
}
