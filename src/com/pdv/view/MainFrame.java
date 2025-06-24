package com.pdv.view;

import com.pdv.controller.PanierController;
import com.pdv.db.DatabaseManager;
import com.pdv.model.Article;
import com.pdv.model.Panier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    private PanierController controller;
    private DefaultTableModel tableModel;
    private JTable tableArticles;
    private JTextField txtNom, txtPrix, txtQuantite;
    private JLabel lblTotal;

    private JButton btnAjouter, btnSupprimer, btnVider, btnPayer;

    public MainFrame() {
        super("Point de Vente - Panier");

        // Initialisation
        controller = new PanierController(new Panier());
        DatabaseManager.initDB(); // Initialise la base de données

        initComponents();
        layoutComponents();
        registerListeners();

        updateTable();
        updateTotal();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        // Table des articles
        tableModel = new DefaultTableModel(new Object[]{"Nom", "Prix Unitaire", "Quantité", "Sous-total"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableArticles = new JTable(tableModel);
        tableArticles.setFillsViewportHeight(true);
        tableArticles.setBackground(new Color(245, 245, 245));

        // Champs de saisie
        txtNom = new JTextField(10);
        txtPrix = new JTextField(6);
        txtQuantite = new JTextField("1", 3);

        // Label du total
        lblTotal = new JLabel("TOTAL : 0.00 €");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));

        // Boutons avec couleurs comme dans la maquette
        btnAjouter = new JButton("Ajouter");
        btnAjouter.setBackground(new Color(46, 204, 113)); // Vert
        btnAjouter.setForeground(Color.BLACK);

        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBackground(new Color(231, 76, 60)); // Rouge
        btnSupprimer.setForeground(Color.WHITE);

        btnVider = new JButton("Vider panier");
        btnVider.setBackground(new Color(243, 156, 18)); // Orange
        btnVider.setForeground(Color.BLACK);

        btnPayer = new JButton("PAYER");
        btnPayer.setBackground(new Color(41, 128, 185)); // Bleu
        btnPayer.setForeground(Color.WHITE);
        btnPayer.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void layoutComponents() {
        // Section ajout produit (NORTH)
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.add(new JLabel("Nom du produit:"));
        inputPanel.add(txtNom);
        inputPanel.add(new JLabel("Prix (€):"));
        inputPanel.add(txtPrix);
        inputPanel.add(new JLabel("Quantité:"));
        inputPanel.add(txtQuantite);
        inputPanel.add(btnAjouter);

        // Tableau (CENTER)
        JScrollPane scrollPane = new JScrollPane(tableArticles);

        // Actions (WEST)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.add(btnSupprimer);
        actionPanel.add(btnVider);

        // Paiement (SOUTH)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.add(lblTotal);
        totalPanel.add(btnPayer);
        bottomPanel.add(actionPanel, BorderLayout.WEST);
        bottomPanel.add(totalPanel, BorderLayout.EAST);

        // Placement
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(inputPanel, BorderLayout.NORTH);
        cp.add(scrollPane, BorderLayout.CENTER);
        cp.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void registerListeners() {
        btnAjouter.addActionListener(this::onAjouter);
        btnSupprimer.addActionListener(this::onSupprimer);
        btnVider.addActionListener(e -> {
            if (controller.getPanier().estVide()) {
                JOptionPane.showMessageDialog(this, "Le panier est déjà vide.");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment vider le panier ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                controller.viderPanier();
                updateTable();
                updateTotal();
            }
        });
        btnPayer.addActionListener(e -> {
            if (controller.getPanier().estVide()) {
                JOptionPane.showMessageDialog(this, "Le panier est vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Confirmer le paiement ?", "Paiement", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                // Enregistrer les ventes
                for (Article a : controller.getPanier().getArticles()) {
                    DatabaseManager.enregistrerArticle(a);
                }
                afficherTicket();
                controller.viderPanier();
                updateTable();
                updateTotal();
            }
        });
    }

    private void onAjouter(ActionEvent e) {
        try {
            String nom = txtNom.getText().trim();
            double prix = Double.parseDouble(txtPrix.getText().trim());
            int quantite = Integer.parseInt(txtQuantite.getText().trim());

            controller.ajouterArticle(nom, prix, quantite);
            updateTable();
            updateTotal();

            txtNom.setText("");
            txtPrix.setText("");
            txtQuantite.setText("1");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Prix ou quantité invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSupprimer(ActionEvent e) {
        int selectedRow = tableArticles.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un article à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nom = (String) tableModel.getValueAt(selectedRow, 0);
        controller.supprimerArticle(nom);
        updateTable();
        updateTotal();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Article a : controller.getPanier().getArticles()) {
            tableModel.addRow(new Object[]{
                    a.getNom(),
                    String.format("%.2f", a.getPrixUnitaire()),
                    a.getQuantite(),
                    String.format("%.2f", a.getSousTotal())
            });
        }
    }

    private void updateTotal() {
        lblTotal.setText("TOTAL : " + String.format("%.2f", controller.getTotal()) + " €");
    }

    private void afficherTicket() {
        StringBuilder sb = new StringBuilder("=== TICKET DE CAISSE ===\n\n");
        for (Article a : controller.getPanier().getArticles()) {
            sb.append(String.format("%s x %d  →  %.2f €\n",
                    a.getNom(), a.getQuantite(), a.getSousTotal()));
        }
        sb.append("-----------------------------\n");
        sb.append(String.format("TOTAL : %.2f €\n", controller.getTotal()));
        sb.append("Merci pour votre achat !");
        JOptionPane.showMessageDialog(this, sb.toString(), "Ticket", JOptionPane.INFORMATION_MESSAGE);
    }
}
