package com.example.socialmediagamer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.rpc.Help;

public class Profile extends AppCompatActivity {

    // Declaración de variables
    ImageView mbtnHome, mbtnHelp, mbtnPay;
    Button mbtnEditProfile;
    Button mbtnLogout;

    // Instancias de Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);  // Asegúrate que el archivo XML esté correctamente nombrado

        // Ajuste de insets para padding del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de botones y acciones
        mbtnHome = findViewById(R.id.btnHome);
        mbtnHome.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, HomeActivity.class);
            startActivity(intent);
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mbtnLogout = findViewById(R.id.btnLogout);
        mbtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar sesión de FirebaseAuth
                mAuth.signOut();

                // Redirigir al usuario a la pantalla de inicio de sesión
                Intent intent = new Intent(Profile.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Asegurar que el usuario no pueda regresar a la actividad de perfil
                startActivity(intent);
                finish(); // Finalizar la actividad actual
            }
        });

        mbtnEditProfile = findViewById(R.id.btnEditProfile);
        mbtnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, UploadProfile.class);
            startActivity(intent);
        });


        mbtnHelp = findViewById(R.id.btnHelp);
        mbtnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Soporte.class);
                startActivity(intent);
            }
        });

        mbtnPay = findViewById(R.id.btnPay);
        mbtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Payment.class);
                startActivity(intent);
            }
        });

        // Obtener el usuario actual de Firebase Authentication
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Si el usuario está autenticado
        if (currentUser != null) {
            String userId = currentUser.getUid();  // Obtener el UID del usuario autenticado

            // Leer los datos del usuario desde Firestore
            db.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        // Obtener los datos del perfil del usuario
                        String name = documentSnapshot.getString("name");
                        String phone = documentSnapshot.getString("phone");
                        String address = documentSnapshot.getString("address");
                        String postalCode = documentSnapshot.getString("postalCode");

                        // Referenciar las vistas donde se mostrarán los datos
                        TextView nameTextView = findViewById(R.id.userName);
                        TextView phoneTextView = findViewById(R.id.userPhone);
                        TextView addressTextView = findViewById(R.id.userAddress);
                        TextView postalCodeTextView = findViewById(R.id.userPostalCode);

                        // Mostrar los datos en las vistas
                        nameTextView.setText(name);
                        phoneTextView.setText(phone);
                        addressTextView.setText(address);
                        postalCodeTextView.setText(postalCode);
                    }
                }
            });
        } else {
            // Manejar el caso de que no haya un usuario autenticado, si es necesario
        }
    }
}
