package org.richit.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeCityController extends AppCompatActivity {

    EditText editTextCity;
    Button buttonGO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city_controller);

        editTextCity=findViewById(R.id.city_et);
        buttonGO=findViewById(R.id.buttonGO);

        buttonGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city=editTextCity.getText().toString();
                Intent intent=new Intent(ChangeCityController.this,MainActivity.class);
                intent.putExtra("City",city);
                startActivity(intent);
                finish();
            }
        });

    }
}
