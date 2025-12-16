package com.example.onsmallstudenthousing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailsMaisonActivity extends AppCompatActivity {

    private int userId, houseId;
    private String category;
    private double price;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_maison);

        databaseHelper = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getInt("userId");
            houseId = extras.getInt("houseId");
            Log.d("DetailsMaisonActivity", "houseId reçu: " + houseId);

            String address = extras.getString("address");
            price = extras.getDouble("price");
            category = extras.getString("category");
            String phone = extras.getString("phone");
            String location = extras.getString("location");
            String dateAdded = extras.getString("dateAdded");
            String description = extras.getString("description");
            String roomDetails = extras.getString("roomDetails");
            String[] images = extras.getStringArray("images");
            String[] equipments = extras.getStringArray("equipments");

            // Mettre à jour les vues
            TextView titleTextView = findViewById(R.id.property_title);
            TextView priceTextView = findViewById(R.id.property_price);
            TextView locationDateTextView = findViewById(R.id.property_location_date);
            TextView descriptionTextView = findViewById(R.id.property_description);
            TextView equipmentListTextView = findViewById(R.id.equipment_list);
            TextView roomsDetailsListTextView = findViewById(R.id.roomDetails_list);
            ImageView phoneImageView = findViewById(R.id.icon_phone);
            ImageView calendarIcon = findViewById(R.id.icon_schedule);
            ImageView imageViewLocalisation = findViewById(R.id.icon_location);

            titleTextView.setText(category);
            priceTextView.setText(price + " DT");
            locationDateTextView.setText(address);
            descriptionTextView.setText("🏠 À louer\n " + description);

            if (equipments != null) {
                StringBuilder equipmentList = new StringBuilder();
                for (String equipment : equipments) {
                    equipmentList.append("✅ ").append(equipment).append("\n");
                }
                equipmentListTextView.setText(equipmentList.toString());
            }

            if (roomDetails != null && !roomDetails.isEmpty()) {
                String[] roomDetailsArray = roomDetails.split(",");
                StringBuilder formattedRoomDetails = new StringBuilder();
                for (String detail : roomDetailsArray) {
                    formattedRoomDetails.append("✅ ").append(detail.trim()).append("\n");
                }
                roomsDetailsListTextView.setText(formattedRoomDetails.toString());
            } else {
                roomsDetailsListTextView.setText("Aucun détail disponible.");
            }

            phoneImageView.setOnClickListener(v -> {
                if (phone != null && !phone.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);
                }
            });

            calendarIcon.setOnClickListener(v -> {
                Intent intent = new Intent(DetailsMaisonActivity.this, CalendarActivity.class);
                startActivity(intent);
            });

            imageViewLocalisation.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + location));
                startActivity(intent);
            });

            ViewFlipper viewFlipper = findViewById(R.id.viewFlipper);
            if (images != null && images.length > 0) {
                for (String image : images) {
                    ImageView imageView = new ImageView(this);
                    int width = 1200;
                    int height = 1200;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                    imageView.setLayoutParams(params);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    Glide.with(this)
                            .load(Uri.parse(image))
                            .into(imageView);

                    viewFlipper.addView(imageView);
                }
            }

            // 📌 Ajouter à l'historique des consultations
            enregistrerConsultation(userId, houseId);

            // 📌 Charger et afficher les annonces similaires
            afficherAnnoncesSimilaires();
        }
    }

    /**
     * 📌 Enregistre la consultation de l'annonce dans l'historique local SQLite
     */
    private void enregistrerConsultation(int userId, int houseId) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateViewed = sdf.format(new Date());
        databaseHelper.ajouterHistorique(userId, houseId, dateViewed);
        Log.d("Historique", "Consultation enregistrée pour l'annonce ID : " + houseId);
    }

    /**
     * 📌 Charge et affiche les annonces similaires en fonction de la catégorie et du prix
     */

    private void afficherAnnoncesSimilaires() {
        List<House> annoncesSimilaires = databaseHelper.getAnnoncesSimilaires(houseId);
        LinearLayout similarListingsContainer = findViewById(R.id.similarListingsContainer);
        HorizontalScrollView horizontalScrollView = findViewById(R.id.horizontalScrollView);
        TextView titleTextView = findViewById(R.id.similar_title);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        String userEmail = sharedPreferences.getString("userEmail", "");
        String userName = sharedPreferences.getString("userName", "");
        String userType = sharedPreferences.getString("userType", "locataire");
        String phone = sharedPreferences.getString("userPhone", "");
        if (!annoncesSimilaires.isEmpty()) {
            horizontalScrollView.setVisibility(View.VISIBLE);
            titleTextView.setVisibility(View.VISIBLE);  // 🔥 Affiche le titre si des annonces existent

            // 🗑️ Supprimer les anciennes annonces pour éviter les doublons
            similarListingsContainer.removeAllViews();

            for (House house : annoncesSimilaires) {
                CardView cardView = new CardView(this);
                cardView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                cardView.setRadius(20f);
                cardView.setCardElevation(4f);

                LinearLayout cardContent = new LinearLayout(this);
                cardContent.setOrientation(LinearLayout.VERTICAL);
                cardContent.setPadding(16, 16, 16, 16);

                ImageView houseImageView = new ImageView(this);
                houseImageView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 400));
                houseImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Glide.with(this)
                        .load(Uri.parse(house.getImages().get(0)))
                        .into(houseImageView);

                cardContent.addView(houseImageView);

                TextView title = new TextView(this);
                title.setText("🏠 " + house.getCategory());
                title.setTextSize(18);
                title.setTextColor(getResources().getColor(android.R.color.black));

                TextView priceTextView = new TextView(this);
                priceTextView.setText(house.getPrice() + " DT");
                priceTextView.setTextSize(16);
                priceTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));

                TextView addressTextView = new TextView(this);
                addressTextView.setText(house.getAddress());
                addressTextView.setTextSize(14);
                addressTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));

                cardContent.addView(title);
                cardContent.addView(priceTextView);
                cardContent.addView(addressTextView);

                cardView.addView(cardContent);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(16, 16, 16, 16);
                cardView.setLayoutParams(layoutParams);

                cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(DetailsMaisonActivity.this, DetailsMaisonActivity.class);
                    intent.putExtra("houseId", house.getId());
                    intent.putExtra("userId", userId);
                    intent.putExtra("address", house.getAddress());
                    intent.putExtra("price", house.getPrice());
                    intent.putExtra("category", house.getCategory());
                    intent.putExtra("phone", phone);
                    intent.putExtra("location", house.getLocation());
                    intent.putExtra("dateAdded", house.getDateAdded());
                    intent.putExtra("description", house.getDescription());
                    intent.putExtra("roomDetails", house.getRoomDetails());
                    intent.putExtra("images", house.getImages().toArray(new String[0]));
                    intent.putExtra("equipments", house.getEquipments().toArray(new String[0]));

                    startActivity(intent);
                });

                similarListingsContainer.addView(cardView);
            }
        } else {
            horizontalScrollView.setVisibility(View.GONE);
            titleTextView.setVisibility(View.GONE);  // 🔥 Masquer le titre si aucune annonce similaire
        }
    }



}
