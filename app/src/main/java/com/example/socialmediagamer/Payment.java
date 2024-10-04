package com.example.socialmediagamer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Payment extends AppCompatActivity {

    ImageView mbtnHome, mbtnHelp, mbtnProfile;
    Button mbtnPayBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mbtnHome = findViewById(R.id.btnHome);
        mbtnHome.setOnClickListener(v -> {
            Intent intent = new Intent(Payment.this, HomeActivity.class);
            startActivity(intent);
        });

        mbtnHelp = findViewById(R.id.btnHelp);
        mbtnHelp.setOnClickListener(v -> {
            Intent intent = new Intent(Payment.this, Soporte.class);
            startActivity(intent);
        });

        mbtnPayBill = findViewById(R.id.btnPayBill);
        mbtnPayBill.setOnClickListener(v -> {
            Intent intent = new Intent(Payment.this, PayBills.class);
            startActivity(intent);
        });

        mbtnProfile = findViewById(R.id.btnProfile);
        mbtnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Payment.this, Profile.class);
            startActivity(intent);
        });


    }
}