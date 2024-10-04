package com.example.socialmediagamer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import java.util.HashMap;
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

    private void register() {
        String numberAccount = mNumberAccount.getText().toString();
        String email = mEmail.getText().toString();
        String registerPassword = mRegisterPassword.getText().toString();
        String registerPasswordConfirm = mRegisterPasswordConfirm.getText().toString();

        if (!numberAccount.isEmpty() && !email.isEmpty() && !registerPassword.isEmpty() && !registerPasswordConfirm.isEmpty()) {
            if (isEmailValid(email)) {
                if (registerPassword.equals(registerPasswordConfirm)) {
                    if (registerPassword.length() >= 6) {
                        createUser(numberAccount, email, registerPassword);
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
    }

    private void createUser(final String NumberAccount ,final String email, final String registerPassword) {
        mAuth.createUserWithEmailAndPassword(email, registerPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("email", email);
                            map.put("numberAccount", NumberAccount);

                            String userId = mAuth.getCurrentUser().getUid();
                            mFirestore.collection("Users").document(userId).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                     Toast.makeText(RegisterActivity2.this, "El usuario se almaceno correctamente", Toast.LENGTH_SHORT).show();
                                     finish();
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity2.this, "Error al almacenar el usuario", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity2.this, "Error al crear el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}