package com.example.socialmediagamer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UploadProfile extends AppCompatActivity {

    TextInputEditText mnameUpload;
    TextInputEditText mphoneUpload;
    TextInputEditText mdirectionUpload;
    TextInputEditText mcodigoPostalUpload;
    Button mbtnSaveProfile;

    // Instancias de Firebase
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Button mbtnCancelarProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_profile);

        // Enlazar vistas
        mnameUpload = findViewById(R.id.nameUpload);
        mphoneUpload = findViewById(R.id.phoneUpload);
        mdirectionUpload = findViewById(R.id.directionUpload);
        mcodigoPostalUpload = findViewById(R.id.codigoPostalUpload);
        mbtnSaveProfile = findViewById(R.id.btnSaveProfile);
        mbtnCancelarProfile = findViewById(R.id.btnCancelarProfile);

        // Aplicar insets para manejar el padding con los bordes del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Acción para cancelar y volver a la actividad anterior
        mbtnCancelarProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UploadProfile.this, Profile.class);
            startActivity(intent);
            finish(); // Terminar la actividad actual para evitar volver con "back"
        });

        // Acción para guardar el perfil
        mbtnSaveProfile.setOnClickListener(v -> {
            String name = mnameUpload.getText().toString().trim();
            String phone = mphoneUpload.getText().toString().trim();
            String address = mdirectionUpload.getText().toString().trim();
            String postalCode = mcodigoPostalUpload.getText().toString().trim();

            if (!name.isEmpty() && !phone.isEmpty() && !address.isEmpty() && !postalCode.isEmpty()) {
                updateUser(name, phone, address, postalCode);
            } else {
                Toast.makeText(UploadProfile.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para actualizar los datos del usuario en Firestore
    private void updateUser(final String name, final String phone, final String address, final String postalCode) {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid(); // Obtener el ID del usuario

            // Crear un mapa con los nuevos datos del perfil
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("phone", phone);
            map.put("address", address);
            map.put("postalCode", postalCode);

            // Actualizar los datos en Firestore
            mFirestore.collection("Users").document(userId).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UploadProfile.this, "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show();
                                // Opcional: puedes redirigir al usuario a la actividad de perfil después de la actualización
                                Intent intent = new Intent(UploadProfile.this, Profile.class);
                                startActivity(intent);
                                finish(); // Finalizar la actividad actual
                            } else {
                                Toast.makeText(UploadProfile.this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
