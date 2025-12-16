package com.example.onsmallstudenthousing;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfilLocataireActivity extends AppCompatActivity {
    private  TextView profileName, profileEmail, profileUsername, profilePhone, profilePassword;
    private Button editButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil_locataire);


        profileName = findViewById(R.id.profileUsername);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        profilePhone = findViewById(R.id.profilePhone);
        profilePassword = findViewById(R.id.profilePassword);
        editButton = findViewById(R.id.editProfileButton);

        loadUserProfile();

        editButton.setOnClickListener(v -> {
            // Récupérez les valeurs des champs de texte
            String updatedUsername = profileUsername.getText().toString();
            String updatedEmail = profileEmail.getText().toString();
            String updatedPhone = profilePhone.getText().toString();
            String updatedPassword = profilePassword.getText().toString();

            // Récupérez l'ID de l'utilisateur (vous pouvez le stocker dans SharedPreferences lors de la connexion)
            int userId = 1;   // Récupérez l'ID de l'utilisateur, par exemple à partir de SharedPreferences

                    // Appelez la méthode de mise à jour dans la base de données
                    DatabaseHelper dbHelper = new DatabaseHelper(ProfilLocataireActivity.this);
            boolean updateSuccess = dbHelper.updateUser(userId, updatedUsername, updatedEmail, updatedPassword, updatedPhone);

            // Afficher un message selon si la mise à jour a réussi ou non
            if (updateSuccess) {
                Toast.makeText(ProfilLocataireActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfilLocataireActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        }
        );


    }

    private void loadUserProfile() {
        // Récupérer les SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);

        // Récupérer les données utilisateur stockées
        String userName = sharedPreferences.getString("userName", "N/A");
        String userEmail = sharedPreferences.getString("userEmail", "N/A");
        String userPhone = sharedPreferences.getString("userPhone", "N/A"); // Si vous avez stocké le téléphone
        String userPassword = sharedPreferences.getString("userPassword", "N/A"); // Si vous avez stocké le mot de passe

        // Mettre les valeurs dans les TextViews
        profileEmail.setText(userEmail);
        profileUsername.setText(userName); // ou autre champ à afficher
        profilePhone.setText(userPhone);
        profilePassword.setText(userPassword); // Vous pouvez choisir de ne pas afficher le mot de passe
    }
}
