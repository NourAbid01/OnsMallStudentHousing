package com.example.onsmallstudenthousing;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 22;
    private static final String DATABASE_NAME  ="OMSHousing.db" ;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "email TEXT UNIQUE, " +
                "password TEXT NOT NULL," +
                "phone TEXT);";
        db.execSQL(createUsersTable);
        Log.d("DatabaseHelper", "Table 'utilisateurs' created successfully.");
        // Créez la table pour les maisons
        String createHousesTable = "CREATE TABLE houses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "address TEXT, " +
                "price REAL, " +
                "category TEXT, " +
                "location TEXT, " +
                "dateAdded TEXT, " +
                "description TEXT, " +
                "roomDetails TEXT, " +
                "images TEXT, " + // Pour stocker une liste, vous pouvez utiliser une chaîne de caractères séparée par des virgules, ou envisager une autre solution
                "equipments TEXT, " + // Même chose pour la liste des équipements
                "FOREIGN KEY(userId) REFERENCES users(id));"; // Associe la maison à un utilisateur
        db.execSQL(createHousesTable);
        Log.d("DatabaseHelper", "Table 'houses' created successfully.");

        // Table pour enregistrer l'historique de consultation des annonces
        String createHistoryTable = "CREATE TABLE historique_utilisateur (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "houseId INTEGER, " +
                "dateViewed TEXT, " +
                "FOREIGN KEY(userId) REFERENCES users(id), " +
                "FOREIGN KEY(houseId) REFERENCES houses(id));";
        db.execSQL(createHistoryTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS houses");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS historique_utilisateur");
            onCreate(sqLiteDatabase);
    }

    public boolean insertUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);
        long result = db.insert("users", null, values);
        return result != -1;
    }
    public boolean insertUserphone(String username, String email, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);
        values.put("phone", phone);  // Insertion du numéro de téléphone
        long result = db.insert("users", null, values);
        return result != -1; // Retourne true si l'insertion a réussi, false sinon
    }
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0; // Vérifie si le curseur contient des résultats
        cursor.close();
        return exists;
    }
    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("users", null, null); // Supprime toutes les lignes de la table
    }
    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE email = ? AND password = ?",
                new String[]{email, password}
        );
        boolean isValid = cursor.getCount() > 0; // Vérifie si le curseur contient des résultats
        cursor.close();
        return isValid;
    }
    public boolean checkuserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Requête SQL pour vérifier si l'email existe dans la table "users"
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

        // Si le curseur contient des résultats, l'email existe
        boolean exists = cursor.getCount() > 0;

        cursor.close();
        return exists;
    }

    public boolean insertHouse(House house) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", house.getUserId());
        values.put("address", house.getAddress());
        values.put("price", house.getPrice());
        values.put("category", house.getCategory());
        values.put("location", house.getLocation());
        values.put("dateAdded", house.getDateAdded());
        values.put("description", house.getDescription());
        values.put("roomDetails", house.getRoomDetails());
        // Convertir les listes d'images et d'équipements en chaînes de texte (par exemple, séparées par des virgules)
        values.put("images", String.join(",", house.getImages()));
        values.put("equipments", String.join(",", house.getEquipments()));
        values.put("roomDetails", house.getRoomDetails());
        long result = db.insert("houses", null, values);
        return result != -1;
    }
    @SuppressLint("Range")
    public List<House> getAllHouses() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<House> houses = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM houses", null);
        if (cursor.moveToFirst()) {
            do {
                House house = new House();
                house.setId(cursor.getInt(cursor.getColumnIndex("id")));
                house.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
                house.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                house.setRoomDetails(cursor.getString(cursor.getColumnIndex("roomDetails")));
                house.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                house.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                house.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                house.setDateAdded(cursor.getString(cursor.getColumnIndex("dateAdded")));
                house.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                house.setImages(Arrays.asList(cursor.getString(cursor.getColumnIndex("images")).split(",")));
                house.setEquipments(Arrays.asList(cursor.getString(cursor.getColumnIndex("equipments")).split(",")));
                Log.d("DatabaseHelper", "Maison récupérée: " + house.getCategory() + ", Image: " + house.getImages());
                houses.add(house);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return houses;

    }
    @SuppressLint("Range")
    public User getUserById(Integer userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
        }
        cursor.close();
        return user;
    }
    public Boolean updatePassword(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        long result = db.update("users", contentValues, "email = ?", new String[]{email});
        return result != -1;
    }
    public boolean updateUser(int userId, String username, String email, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Ajoutez les valeurs mises à jour dans le ContentValues
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("phone", phone);  // Ajout du téléphone, si c'est modifié

        // Effectuer la mise à jour
        int result = db.update("users", contentValues, "id = ?", new String[]{String.valueOf(userId)});

        // Retourne true si la mise à jour a été réussie, sinon false
        return result > 0;
    }

    @SuppressLint("Range")
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        // Requête SQL pour récupérer l'utilisateur par son email
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
        }
        cursor.close();
        return user; // Retourne l'utilisateur trouvé ou null si aucun utilisateur trouvé
    }





    // Ajouter un enregistrement dans l'historique des consultations
    public void ajouterHistorique(int userId, int houseId, String dateViewed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("houseId", houseId);
        values.put("dateViewed", dateViewed);
        db.insert("historique_utilisateur", null, values);
    }

    // Rechercher des annonces similaires basées sur la catégorie et le prix
    @SuppressLint("Range")
    public List<House> getAnnoncesSimilaires(int houseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<House> houses = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM houses WHERE category = " +
                        "(SELECT category FROM houses WHERE id = ?) " +
                        "AND id != ? " +
                        "ORDER BY ABS(price - (SELECT price FROM houses WHERE id = ?)) LIMIT 5",
                new String[]{String.valueOf(houseId), String.valueOf(houseId), String.valueOf(houseId)});

        if (cursor.moveToFirst()) {
            do {
                House house = new House();
                house.setId(cursor.getInt(cursor.getColumnIndex("id")));
                house.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
                house.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                house.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                house.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                house.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                house.setDateAdded(cursor.getString(cursor.getColumnIndex("dateAdded")));
                house.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                house.setRoomDetails(cursor.getString(cursor.getColumnIndex("roomDetails")));
                house.setImages(Arrays.asList(cursor.getString(cursor.getColumnIndex("images")).split(",")));
                house.setEquipments(Arrays.asList(cursor.getString(cursor.getColumnIndex("equipments")).split(",")));
                houses.add(house);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return houses;
    }



}
