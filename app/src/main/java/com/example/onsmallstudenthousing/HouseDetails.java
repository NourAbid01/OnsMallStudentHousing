package com.example.onsmallstudenthousing;

public class HouseDetails {

    private  String type;
    private  int montant;
    private  int nbPiece;
    private String imageData;

    public String getType() {
        return type;
    }

    public String getImageData() {
        return imageData;
    }

    public int getNbPiece() {
        return nbPiece;
    }

    public int getMontant() {
        return montant;
    }


    public HouseDetails(String type, int montant, int nbPiece,String imageData) {
        this.type = type;
        this.montant = montant;
        this.nbPiece = nbPiece;
        this.imageData=imageData;
    }



}
