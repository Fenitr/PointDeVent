package com.pdv.controller;

import com.pdv.model.Article;
import com.pdv.model.Panier;

public class PanierController {
    private Panier panier;

    public PanierController(Panier panier) {
        this.panier = panier;
    }

    public void ajouterArticle(String nom, double prix, int quantite) {
        if (nom == null || nom.isBlank())
            throw new IllegalArgumentException("Le nom est requis.");
        panier.ajouterArticle(new Article(nom, prix, quantite));
    }

    public void supprimerArticle(String nom) {
        panier.supprimerArticle(nom);
    }

    public void viderPanier() {
        panier.vider();
    }

    public Panier getPanier() { return panier; }
    public double getTotal() { return panier.calculerTotal(); }
}
