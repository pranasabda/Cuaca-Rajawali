package pranasabda.id.cuacarajawali;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText m_et_firstname;
    EditText m_et_lastname;
    EditText m_et_zipcode;
    Button btn_submit;

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
                if (m_et_firstname.getText().toString().length() == 0) {
                    //Jika Form firstname Kosong
                    m_et_firstname.setError("please insert your first name");
                } else if (m_et_lastname.getText().toString().length() == 0) {
                    //Jika Form lastname Kosong
                    m_et_lastname.setError("please insert your last name");
                } else if (m_et_zipcode.getText().toString().length() == 0) {
                    //Jika Form Zip Code kosong
                    m_et_zipcode.setError("please insert ZIP code");
                } else if (m_et_zipcode.getText().toString().length() > 5) {
                    //Jika Form Zip Code lebih dari 5
                    m_et_zipcode.setError("Zip Code Max 5 Digit");
                } else if (m_et_zipcode.getText().toString().length() < 5) {
                    //Jika Form Zip Code Kurang dari 5
                    m_et_zipcode.setError("Zip Code Min 5 Digit");
                } else {
                    String mfirstname = m_et_firstname.getText().toString();
                    String mlastname = m_et_lastname.getText().toString();
                    String mZipcode = m_et_zipcode.getText().toString();

                    Intent i = new Intent(MainActivity.this, CuacaBasedActivity.class);
                    i.putExtra("FirstName", mfirstname);
                    i.putExtra("LastName", mlastname);
                    i.putExtra("ZipCode", mZipcode);
                    startActivity(i);
                }
            }
        });


    }
}
