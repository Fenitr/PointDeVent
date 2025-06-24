package com.pdv.model;

public class Article {
    private String nom;
    private double prixUnitaire;
    private int quantite;

    public Article(String nom, double prixUnitaire, int quantite) {
        if (prixUnitaire <= 0 || quantite <= 0) {
            throw new IllegalArgumentException("Prix et quantité doivent être positifs.");
        }
        this.nom = nom;
        this.prixUnitaire = prixUnitaire;
        this.quantite = quantite;
    }

    public String getNom() { return nom; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public int getQuantite() { return quantite; }
    public double getSousTotal() { return prixUnitaire * quantite; }
}
