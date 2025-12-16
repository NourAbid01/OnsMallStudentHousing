package com.example.onsmallstudenthousing;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;

public class HomeLocataireActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_locataire);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        String userEmail = sharedPreferences.getString("userEmail", "");
        String phone = sharedPreferences.getString("userPhone", "");
        String userName = sharedPreferences.getString("userName", "");
        String userType = sharedPreferences.getString("userType", "locataire");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem addHouseItem = menu.findItem(R.id.nav_ajout_maison);

        if ("locataire".equals(userType)) {
            addHouseItem.setVisible(false);
        } else {
            addHouseItem.setVisible(true);
        }

        //la partie en haut du drawer extrait les 2 layout menu et heaser
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navEmail = headerView.findViewById(R.id.nav_email);

        navUsername.setText(userName);
        navEmail.setText(userEmail);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
/*
        // Exemple d'insertion d'une maison
        House house = new House();
        house.setUserId(1); // Identifiant de l'utilisateur (peut être obtenu après la connexion)
        house.setAddress("Sfax, cité el ons");
        house.setPrice(500.00);
        house.setCategory("Appartement");
        house.setLocation("34.827716920794956, 10.749091817482999");
        house.setDateAdded("2024-11-27");
        house.setDescription("Appartement lumineux ");
        house.setImages(Arrays.asList("image1.jpg", "image2.jpg"));
        house.setEquipments(Arrays.asList("WiFi", "Télévision", "Machine à laver"));
*/
        DatabaseHelper dbHelper = new DatabaseHelper(this);
      /*  boolean result = dbHelper.insertHouse(house);

        if (result) {
            Log.d("MainActivity", "Maison insérée avec succès.");
        } else {
            Log.d("MainActivity", "Échec de l'insertion de la maison.");
        }*/
       // DatabaseHelper dbHelper = new DatabaseHelper(this);
       //dbHelper.deleteAllHouses();

        List<House> houses = dbHelper.getAllHouses();
        LinearLayout cardsContainer = findViewById(R.id.cardsContainer);
        for (House house1 : houses) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.card_layout, null);

            ImageView imageView = cardView.findViewById(R.id.imageAppU);
            TextView titleTextView = cardView.findViewById(R.id.desAppU);
            TextView descriptionTextView = cardView.findViewById(R.id.descrpAppU);

            if (!house1.getImages().isEmpty()) {
                Uri imageUri = Uri.parse(house1.getImages().get(0)); // Récupérer la première image
                imageView.setImageURI(imageUri);
            } else {
                imageView.setImageResource(R.drawable.appartementu); // Image par défaut si aucune image n'existe
            }

            titleTextView.setText(house1.getCategory());
            descriptionTextView.setText(String.valueOf(house1.getPrice()));

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(HomeLocataireActivity.this, DetailsMaisonActivity.class);
                Log.d("DetailsMaisonActivity", "User Phone number: " + phone);


                intent.putExtra("userId", house1.getUserId());

                // Get the User object from the database
                User user = dbHelper.getUserById(house1.getUserId());
                intent.putExtra("userId", userId);
                intent.putExtra("userId", userId);
                intent.putExtra("houseId", house1.getId());
                intent.putExtra("phone", phone);
                intent.putExtra("address", house1.getAddress());
                intent.putExtra("price", house1.getPrice());
                intent.putExtra("category", house1.getCategory());
                intent.putExtra("location", house1.getLocation());
                intent.putExtra("dateAdded", house1.getDateAdded());
                intent.putExtra("description", house1.getDescription());
                intent.putExtra("roomDetails", house1.getRoomDetails());
                intent.putExtra("images", house1.getImages().toArray(new String[0]));
                intent.putExtra("equipments", house1.getEquipments().toArray(new String[0]));

                // Start the DetailsMaisonActivity
                startActivity(intent);
            });

            // Ajouter la carte dans le LinearLayout
            cardsContainer.addView(cardView);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Traitement des éléments du menu
        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, HomeLocataireActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_profil) {
            Intent intent = new Intent(this, ProfilLocataireActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mot_de_passe) {
            Intent intent = new Intent(this, MotPasseOublie.class);
            startActivity(intent);
            Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_type_user) {
            // Afficher des informations sur l'application
            Intent intent = new Intent(this, TypeUserActivity.class);
            startActivity(intent);
            Toast.makeText(this, "About selected", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_ajout_maison) {
            // Afficher des informations sur l'application
            Intent intent = new Intent(this, UploadHouseActivity.class);
            startActivity(intent);
            Toast.makeText(this, "About selected", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_logout) {
            logout();
            Toast.makeText(this, "About selected", Toast.LENGTH_SHORT).show();
        }
        // Fermer le tiroir après une sélection
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logout() {
        // Supprimer les informations de l'utilisateur depuis SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Effacer toutes les données stockées
        editor.apply();

        // Afficher un message de déconnexion
        Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show();

        // Rediriger vers l'écran de connexion
        Intent intent = new Intent(HomeLocataireActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();  // Fermer l'activité actuelle pour que l'utilisateur ne puisse pas revenir en arrière
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



}