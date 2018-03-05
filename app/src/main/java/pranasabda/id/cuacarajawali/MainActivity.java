package pranasabda.id.cuacarajawali;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText m_et_firstname ;
    EditText m_et_lastname ;
    EditText m_et_zipcode ;
    Button btn_submit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        m_et_firstname = (EditText) findViewById(R.id.et_firstname);
        m_et_lastname = (EditText) findViewById(R.id.et_lastname);
        m_et_zipcode = (EditText) findViewById(R.id.et_zip_code);
        btn_submit = (Button) findViewById(R.id.btn_sumbit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String mfirstname = m_et_firstname.getText().toString();
                String mlastname = m_et_lastname.getText().toString();
                String mZipcode = m_et_zipcode.getText().toString();

                Intent i = new Intent(MainActivity.this, CuacaBasedActivity.class);
                i.putExtra("FirstName", mfirstname);
                i.putExtra("LastName",mlastname);
                i.putExtra("ZipCode",mZipcode);
                startActivity(i);
            }
        });


    }
}
