package org.richit.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeCityController extends AppCompatActivity {

    EditText city_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city_controller);

        city_et=findViewById(R.id.city_et);

        city_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String newCity=city_et.getText().toString();
                Intent intent=new Intent(ChangeCityController.this,MainActivity.class);
                intent.putExtra("City",newCity);
                startActivity(intent);

                return false;
            }
        });
    }
}
