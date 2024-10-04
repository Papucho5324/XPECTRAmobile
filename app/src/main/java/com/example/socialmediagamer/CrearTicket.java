package com.example.socialmediagamer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class CrearTicket extends AppCompatActivity {
    // Declaración de botones y campos de texto
    Button mbtnCancelarTicket;
    Button mbtnSaveTicket;
    TextInputEditText mTicketProblema;
    TextView mTicketEstatus;

    // Instancias de Firebase
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser(); // Inicializar currentUser correctamente

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ticket);

        // Ajustes de visualización de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar botones y campos de texto
        mbtnCancelarTicket = findViewById(R.id.btnCancelarTicket);
        mbtnSaveTicket = findViewById(R.id.btnSaveTicket);
        mTicketProblema = findViewById(R.id.TicketProblema);
        mTicketEstatus = findViewById(R.id.TicketStatus);

        // Configurar el botón para cancelar
        mbtnCancelarTicket.setOnClickListener(v -> {
            Intent intent = new Intent(CrearTicket.this, Soporte.class);
            startActivity(intent);
        });

        // Configurar el botón para guardar el ticket
        mbtnSaveTicket.setOnClickListener(v -> {
            String problema = mTicketProblema.getText().toString().trim();
            String estatus = "Pendiente";

            if (!problema.isEmpty() && !estatus.isEmpty()) {
                crearTicket(problema, estatus);
            } else {
                Toast.makeText(CrearTicket.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para crear el ticket
    private void crearTicket(String problema, String estatus) {
        if (currentUser != null) {
            String userId = currentUser.getUid(); // Obtener el ID del usuario

            // Crear un mapa con los datos del ticket
            Map<String, Object> map = new HashMap<>();
            map.put("problema", problema);
            map.put("estatus", estatus);

            // Guardar el ticket en Firestore en la colección adecuada
            mFirestore.collection("Tickets").document(userId).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(CrearTicket.this, "Ticket enviado con éxito", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CrearTicket.this, Soporte.class);
                                startActivity(intent);
                                finish(); // Finalizar la actividad actual
                            } else {
                                Toast.makeText(CrearTicket.this, "Error al enviar el ticket", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
