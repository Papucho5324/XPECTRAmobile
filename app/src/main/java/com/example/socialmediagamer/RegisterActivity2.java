package com.example.socialmediagamer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity2 extends AppCompatActivity {
    TextInputEditText mNumberAccount;
    TextInputEditText mEmail;
    TextInputEditText mRegisterPassword;
    TextInputEditText mRegisterPasswordConfirm;
    Button mBtnCancelar;
    Button mbtnRegisterAcept;
    private FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register2);

        mNumberAccount = findViewById(R.id.NumberAccount);
        mEmail = findViewById(R.id.Email);
        mRegisterPassword = findViewById(R.id.RegisterPassword);
        mRegisterPasswordConfirm = findViewById(R.id.RegisterPasswordConfirm);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


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

    public boolean isEmailValid(String email) {
        String expresion = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(expresion, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean register() {
        String numberAccount = mNumberAccount.getText().toString();
        String Email = mEmail.getText().toString();
        String registerPassword = mRegisterPassword.getText().toString();
        String registerPasswordConfirm = mRegisterPasswordConfirm.getText().toString();

        if (!numberAccount.isEmpty() && !Email.isEmpty() && !registerPassword.isEmpty() && !registerPasswordConfirm.isEmpty()) {
            if (isEmailValid(Email)) {
                if (registerPassword.equals(registerPasswordConfirm)) {
                    if (registerPassword.length() >= 6) {
                        createUser(Email, registerPassword);  // Parámetros añadidos aquí
                    } else {
                        Toast.makeText(this, "Contraseña muy corta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Correo invalido", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void createUser(String Email, String registerPassword) {
        mAuth.createUserWithEmailAndPassword(Email, registerPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {  // Error corregido aquí
                            Map<String, Object>
                            String userId = mAuth.getCurrentUser().getUid();
                            mFirestore.collection("Users").document().set();
                            Toast.makeText(RegisterActivity2.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity2.this, "Error al crear el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
