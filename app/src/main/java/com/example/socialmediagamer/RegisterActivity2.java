package com.example.socialmediagamer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity2 extends AppCompatActivity {
    TextInputEditText mNumberAccount;
    TextInputEditText mRegisterEmail;
    TextInputEditText mRegisterPassword;
    TextInputEditText mRegisterPasswordConfirm;
    Button mBtnCancelar;
    Button mbtnRegisterAcept;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register2);

        mNumberAccount = findViewById(R.id.NumberAccount);
        mRegisterEmail = findViewById(R.id.RegisterEmail);
        mRegisterPassword = findViewById(R.id.RegisterPassword);
        mRegisterPasswordConfirm = findViewById(R.id.RegisterPasswordConfirm);

        mbtnRegisterAcept = findViewById(R.id.btnRegisterAcept);
        mbtnRegisterAcept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        mBtnCancelar = findViewById(R.id.btnCancelar);
        mBtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }
    private void register() {
        String numberAccount = mNumberAccount.getText().toString();
        String registerEmail = mRegisterEmail.getText().toString();
        String registerPassword = mRegisterPassword.getText().toString();
        String registerPasswordConfirm = mRegisterPasswordConfirm.getText().toString();

        if (numberAccount.isEmpty() || registerEmail.isEmpty() || registerPassword.isEmpty() || registerPasswordConfirm.isEmpty()) {
            Log.d("Register", "Error: Todos los campos deben ser completados.");
            return;
        }

        if (!isEmailValid(registerEmail)) {
            Log.d("Register", "Error: El correo electrónico no es válido.");
            return;
        }

        if (!registerPassword.equals(registerPasswordConfirm)) {
            Log.d("Register", "Error: Las contraseñas no coinciden.");
            return;
        }
        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        Log.d("Register", "Number Account: " + numberAccount
                + ", Email: " + registerEmail + ", Password: " + registerPassword
                + ", Password Confirm: " + registerPasswordConfirm);
    }
    public static boolean isEmailValid(String email){
        String expresion = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(expresion, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
}
