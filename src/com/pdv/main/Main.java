package com.pdv.main;

import com.pdv.db.DatabaseManager;
import com.pdv.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Le driver JDBC SQLite n'a pas pu être chargé.");
        }

        DatabaseManager.initDB();
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
