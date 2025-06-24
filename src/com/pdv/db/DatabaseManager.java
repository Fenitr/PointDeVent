package com.pdv.db;

import com.pdv.model.Article;
import java.sql.*;
import java.time.LocalDateTime;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:ventes.db";

    
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

   
    public static void initDB() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS ventes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "produit TEXT NOT NULL," +
                    "quantite INTEGER NOT NULL," +
                    "prix REAL NOT NULL," +
                    "sous_total REAL NOT NULL," +
                    "date_vente TEXT NOT NULL)");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base : " + e.getMessage());
        }
    }


    public static void enregistrerArticle(Article a) {
        String sql = "INSERT INTO ventes(produit, quantite, prix, sous_total, date_vente) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getNom());
            ps.setInt(2, a.getQuantite());
            ps.setDouble(3, a.getPrixUnitaire());
            ps.setDouble(4, a.getSousTotal());
            ps.setString(5, LocalDateTime.now().toString());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }
}
