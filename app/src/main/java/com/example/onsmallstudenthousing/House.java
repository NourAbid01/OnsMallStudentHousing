package com.example.onsmallstudenthousing;

import java.util.List;

public class House {
    private int id;
    private int userId; // Identifiant de l'utilisateur propriétaire
    private String address;
    private double price;
    private String category; // Nouvelle catégorie (Appartement, Villa, etc.)
    private String location; // Localisation précise
    private String roomDetails;
    private String dateAdded; // Date ajoutée
    private String description;
    private List<String> images; // Liste des chemins ou URLs des images
    private List<String> equipments; // Liste des équipements disponibles


    public House(int id, int userId, String address, double price, String category, String location, String roomDetails, String dateAdded, String description, List<String> images, List<String> equipments) {
        this.id = id;
        this.userId = userId;
        this.address = address;
        this.price = price;
        this.category = category;
        this.location = location;
        this.roomDetails = roomDetails;
        this.dateAdded = dateAdded;
        this.description = description;
        this.images = images;
        this.equipments = equipments;
    }

    public String getRoomDetails() {

        return roomDetails;
    }


    public void setRoomDetails(String roomDetails) {
        this.roomDetails = roomDetails;
    }

    // Constructeur par défaut
    public House() {
    }

    // Constructeur complet
    public House(int id, int userId, String address, double price, String category,
                 String location, String dateAdded, String description,
                 List<String> images, List<String> equipments) {
        this.id = id;
        this.userId = userId;
        this.address = address;
        this.price = price;
        this.category = category;
        this.location = location;
        this.dateAdded = dateAdded;
        this.description = description;
        this.images = images;
        this.equipments = equipments;
    }

    // Getters et Setters pour tous les attributs
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<String> equipments) {
        this.equipments = equipments;
    }

    @Override
    public String toString() {
        return "House{" +
                "id=" + id +
                ", userId=" + userId +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                ", description='" + description + '\'' +
                ", images=" + images +
                ", equipments=" + equipments +
                '}';
    }
}
