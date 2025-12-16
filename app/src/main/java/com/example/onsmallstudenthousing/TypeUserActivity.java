package com.example.onsmallstudenthousing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TypeUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_type_user);

        Button locataireButton = findViewById(R.id.typeuser_locataire_Button);
        locataireButton.setOnClickListener(v -> {
            // Stocker le type d'utilisateur dans SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userType", "locataire");
            editor.apply();

            // Rediriger vers la page principale ou l'écran correspondant
            Intent intent = new Intent(TypeUserActivity.this, HomeLocataireActivity.class);
            startActivity(intent);
            finish(); // Ferme l'écran de sélection
        });
        Button proprietaireButton = findViewById(R.id.typeuser_proprietaire_Button);
        proprietaireButton.setOnClickListener(v -> {
            // Stocker le type d'utilisateur dans SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userType", "proprietaire");
            editor.apply();

            // Rediriger vers la page principale ou l'écran correspondant
            Intent intent = new Intent(TypeUserActivity.this, HomeLocataireActivity.class);
            startActivity(intent);
            finish(); // Ferme l'écran de sélection
        });


    }
}