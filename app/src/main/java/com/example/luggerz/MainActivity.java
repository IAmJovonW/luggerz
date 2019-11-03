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
    }

    private void goToLuggerLanding() {
        Log.d(TAG, "Navigating To Lugger Landing Activity");

        Intent i = new Intent(this, LuggerLanding.class);
        startActivity(i);
        finish();
    }
}
