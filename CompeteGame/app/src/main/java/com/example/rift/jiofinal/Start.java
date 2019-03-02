package com.example.rift.jiofinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(),ActivitesActivity.class);
            startActivity(intent);
        }

        buttonClickLogin();
        buttonClickSignUp();
    }

    private void buttonClickLogin() {
        Button button = (Button)findViewById(R.id.btn_login);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Start.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void buttonClickSignUp() {
        Button button = (Button)findViewById(R.id.btn_register);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Start.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
