package com.example.socialmediagamer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    TextView mPasswordRecovery;
    TextView mPrivacyPolicy;
    Button mBtnLogin;
    Button mBtnRegister;
    TextInputEditText mTextEmail;
    TextInputEditText mTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mTextEmail = findViewById(R.id.TextEmail);
        mTextPassword = findViewById(R.id.TextPassword);
        // Configura el botón de inicio de sesión
        mBtnLogin = findViewById(R.id.btnLogin);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();  // Llama al método login
            }
        });

        // Configura el botón de registro
        mBtnRegister = findViewById(R.id.btnRegister);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity2.class);
                startActivity(intent);
            }
        });

        // Configura el enlace de recuperación de contraseña
        mPasswordRecovery = findViewById(R.id.PasswordRecovery);
        mPasswordRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecoveryPassword.class);
                startActivity(intent);  // Inicia la actividad de recuperación de contraseña
            }
        });

        // Configura el enlace de la política de privacidad
        mPrivacyPolicy = findViewById(R.id.PrivacyPolicy);
        mPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PrivacyPolice.class);
                startActivity(intent);
            }
        });

        // Configura los insets para la compatibilidad con Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método login fuera de onCreate
    private void login() {
        String email = mTextEmail.getText().toString();
        String password = mTextPassword.getText().toString();
        Log.d("Login", "Email: " + email + ", Password: " + password);
    }
}
