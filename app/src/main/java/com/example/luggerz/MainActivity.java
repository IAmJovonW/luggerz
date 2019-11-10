package com.example.luggerz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity" ;
    private Button btnLugger;
    private Button btnPatron;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLugger = findViewById(R.id.btnLugger);
        btnLugger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLuggerLanding();
            }
        });

        btnPatron = findViewById(R.id.btnPatron);
        btnPatron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPatronLanding();
            }
        });
    }

    private void goToPatronLanding() {
        Log.d(TAG, "Navigating To Patron Landing Activity");

        Intent i = new Intent(this, PatronMainActivity.class);
        startActivity(i);
        finish();
    }

    private void goToLuggerLanding() {
        Log.d(TAG, "Navigating To Lugger Landing Activity");

        Intent i = new Intent(this, LuggerMainActivity.class);
        startActivity(i);
        finish();
    }
}
