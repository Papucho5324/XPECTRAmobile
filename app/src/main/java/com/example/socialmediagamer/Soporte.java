package com.example.socialmediagamer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Soporte extends AppCompatActivity {

    // Declaración de variables
    ImageView mbtnHome, mbtnProfile, mbtnPay;
    Button mbtnCrearTicket, mbtnBorrarTicket;
    TextView problemaTextView;
    TextView estatusTextView;

    // Instancias de Firebase
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soporte);

        // Inicialización de vistas
        mbtnHome = findViewById(R.id.btnHome);
        mbtnCrearTicket = findViewById(R.id.btnCrearTicket);
        problemaTextView = findViewById(R.id.TicketRason);
        estatusTextView = findViewById(R.id.TicketStatus);

        // Ajuste de insets para padding del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mbtnCrearTicket = findViewById(R.id.btnCrearTicket);
        mbtnCrearTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Soporte.this, CrearTicket.class);
                startActivity(intent);
            }
        });


        mbtnPay = findViewById(R.id.btnPay);
        mbtnPay.setOnClickListener(v -> {
            Intent intent = new Intent(Soporte.this, Payment.class);
            startActivity(intent);
        });


        mbtnProfile = findViewById(R.id.btnProfile);
        mbtnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Soporte.this, Profile.class);
            startActivity(intent);
        });

                mbtnBorrarTicket = findViewById(R.id.btnBorrarTicket);
        mbtnBorrarTicket.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();


                // Borrar el ticket
                mFirestore.collection("Tickets").document(userId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(Soporte.this, "Ticket borrado exitosamente", Toast.LENGTH_SHORT).show();
                            // Actualizar la interfaz (opcional)
                            problemaTextView.setText("No tienes ningún ticket abierto.");
                            estatusTextView.setText("");
                            mbtnCrearTicket.setVisibility(View.VISIBLE);
                            mbtnBorrarTicket.setVisibility(View.GONE); // Hide delete button after deletion
                        })
                        .addOnFailureListener(exception -> {
                            Toast.makeText(Soporte.this, "Error al borrar el ticket: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(Soporte.this, "Debes iniciar sesión para borrar tickets.", Toast.LENGTH_SHORT).show();
            }
        });

        // Acciones de los botones
        mbtnHome.setOnClickListener(v -> {
            Intent intent = new Intent(Soporte.this, HomeActivity.class);
            startActivity(intent);
        });

        // Obtener el usuario actual y leer los datos del ticket
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mFirestore.collection("Tickets").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {

                            // Obtener datos del ticket
                            String problema = documentSnapshot.getString("problema");
                            String estatus = documentSnapshot.getString("estatus");

                            // Mostrar datos del ticket existente
                            problemaTextView.setText(problema);  // Mostrar el problema correcto
                            estatusTextView.setText(estatus);  // Mostrar el estatus correcto
                            mbtnCrearTicket.setVisibility(View.GONE);
                            mbtnBorrarTicket.setVisibility(View.VISIBLE);
                        } else {
                            // No hay ticket, mostrar botón para crear uno
                            mbtnCrearTicket.setVisibility(View.VISIBLE);
                            mbtnBorrarTicket.setVisibility(View.GONE);
                            problemaTextView.setText("No tienes ningún ticket abierto.");
                            estatusTextView.setText("");
                        }
                    });
        } else {
            // Manejar el caso de que no haya un usuario autenticado
            Toast.makeText(this, "Debes iniciar sesión para ver tus tickets.", Toast.LENGTH_SHORT).show();
        }
    }
}
