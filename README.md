# 🛍️ Point de Vente – Application Java Swing (Projet pour apprendre JAVA SWING)

Une application de gestion de panier pour point de vente développée en Java Swing.  
Ce projet propose une interface intuitive avec calcul automatique du total et génération d’un ticket de caisse. Il utilise une base de données locale SQLite pour enregistrer les ventes.  
Parfait pour apprendre la programmation orientée objet, l’architecture MVC, et les interfaces graphiques Java.

---

## ✨ Fonctionnalités

- Interface utilisateur simple et fluide (Java Swing)
- Ajout, suppression, et vidage d’un panier d’achats
- Calcul automatique des sous-totaux et du total
- Bouton **PAYER** générant un ticket de caisse
- Enregistrement des ventes dans une base SQLite (`ventes.db`)
- Compatible avec Windows, macOS, Linux

---

## 🚀 Installation et exécution (avec invite de commande Windows)

### ✅ Prérequis

- Java JDK installé (11 ou supérieur)  
  👉 [Télécharger depuis Adoptium](https://adoptium.net)

- [Télécharger le pilote JDBC SQLite](https://github.com/xerial/sqlite-jdbc/releases)  
  (Ex : `sqlite-jdbc-3.43.2.1.jar`)

---

### 🛠️ Étapes

1. **Cloner le projet ou le télécharger** :
   ```bash
   git clone https://github.com/utilisateur/PointDeVent.git
   cd PointDeVent
   java -cp ".;sqlite-jdbc-3.50.1.0.jar;bin" com.pdv.main.Main
