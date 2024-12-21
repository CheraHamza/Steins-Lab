package com.example.laboratory;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.time.LocalDate;

public class AddPatientController {
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private ChoiceBox sexe;
    @FXML
    private DatePicker date_nais;
    @FXML
    private TextField tel;
    @FXML
    private TextField email;
    @FXML
    private TextField ville;
    @FXML
    private HBox btnContainer;

    public String getClientInfo() {
        String nom = this.nom.getText().trim();
        String prenom = this.prenom.getText().trim();
        String sexe = this.sexe.getValue().toString().trim();
        String date = this.date_nais.getValue().toString().trim();
        String tel = this.tel.getText().trim();
        String email = this.email.getText().trim();
        String ville = this.ville.getText().trim();

        return nom + ":" + prenom + ":" + sexe + ":" + date + ":" + tel + ":" + email + ":" + ville;
    }

    public void setText(String nom, String prenom, String date, String sexe, String tel, String email, String ville) {
        this.nom.setText(nom);
        this.prenom.setText(prenom);
        this.sexe.setValue((Object) sexe);
        this.date_nais.setValue(LocalDate.parse(date));
        this.tel.setText(tel);
        this.email.setText(email);
        this.ville.setText(ville);
    }

    public TextField getNom() {
        return nom;
    }

    public TextField getPrenom() {
        return prenom;
    }

    public ChoiceBox getSexe() {
        return sexe;
    }

    public DatePicker getDate_nais() {
        return date_nais;
    }

    public TextField getTel() {
        return tel;
    }

    public TextField getEmail() {
        return email;
    }

    public TextField getVille() {
        return ville;
    }
}
