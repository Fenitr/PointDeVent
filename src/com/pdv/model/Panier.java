package com.pdv.model;

import java.util.ArrayList;
import java.util.List;

public class Panier {
    private List<Article> articles = new ArrayList<>();

    public void ajouterArticle(Article a) { articles.add(a); }
    public void supprimerArticle(String nom) { articles.removeIf(a -> a.getNom().equals(nom)); }
    public void vider() { articles.clear(); }
    public List<Article> getArticles() { return articles; }

    public double calculerTotal() {
        return articles.stream().mapToDouble(Article::getSousTotal).sum();
    }

    public boolean estVide() {
        return articles.isEmpty();
    }
}
