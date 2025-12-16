package com.example.onsmallstudenthousing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class UploadHouseActivity extends AppCompatActivity {

    private static final int PICK_IMAGES_REQUEST = 100;
    ArrayList<Uri> imageUris = new ArrayList<>();
    LinearLayout imageContainer;
    Button selectImagesButton, saveButton;
    EditText uploadType, uploadNbPiece, uploadMontant, uploadAdresse, uploadDescription, uploadEquipments, uploadLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_house);

        // Liaison avec l'interface
        imageContainer = findViewById(R.id.imageContainer);
        selectImagesButton = findViewById(R.id.selectImagesButton);
        saveButton = findViewById(R.id.saveButton);
        uploadType = findViewById(R.id.upload_House_TypeProp);
        uploadNbPiece = findViewById(R.id.upload_House_nbPiece);
        uploadMontant = findViewById(R.id.upload_House_PrixMois);
        uploadAdresse = findViewById(R.id.upload_House_Adresse);
        uploadDescription = findViewById(R.id.upload_House_Description);
        uploadEquipments = findViewById(R.id.upload_House_Equipments);
        uploadLocation = findViewById(R.id.upload_House_Location);

        // Sélection d'images
        selectImagesButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, PICK_IMAGES_REQUEST);
        });

        // Sauvegarde
        saveButton.setOnClickListener(v -> saveHouse());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUris.add(imageUri);
                    displayImage(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
                displayImage(imageUri);
            }
        }
    }

    private void displayImage(Uri uri) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageURI(uri);
        imageContainer.addView(imageView);
    }

    private void saveHouse() {
        // Récupérer les valeurs
        String type = uploadType.getText().toString().trim();
        String nbPiece = uploadNbPiece.getText().toString().trim();
        String montant = uploadMontant.getText().toString().trim();
        String adresse = uploadAdresse.getText().toString().trim();
        String description = uploadDescription.getText().toString().trim();
        String equipments = uploadEquipments.getText().toString().trim();
        String location = uploadLocation.getText().toString().trim();

        if (type.isEmpty() || nbPiece.isEmpty() || montant.isEmpty() || adresse.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créer l'objet House
        House house = new House();
        house.setCategory(type);
        house.setRoomDetails(nbPiece);
        house.setPrice(Double.parseDouble(montant));
        house.setAddress(adresse);
        house.setDescription(description);
        house.setLocation(location);

        // Ajouter les équipements
        ArrayList<String> equipmentList = new ArrayList<>();
        for (String equip : equipments.split(",")) {
            equipmentList.add(equip.trim());
        }
        house.setEquipments(equipmentList);

        // Ajouter les images
        ArrayList<String> images = new ArrayList<>();
        for (Uri uri : imageUris) {
            images.add(uri.toString());
        }
        house.setImages(images);

        // Sauvegarde dans la base de données
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        boolean isInserted = dbHelper.insertHouse(house);

        if (isInserted) {
            Toast.makeText(this, "Maison ajoutée avec succès", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erreur lors de l'ajout de la maison", Toast.LENGTH_SHORT).show();
        }
    }
}
