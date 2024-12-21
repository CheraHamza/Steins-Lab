import java.util.Date;

public class Patient {
    private int id;
    private String nom;
    private String prenom;
    private Date date_nais;
    private String sexe;
    private String telephone;
    private String email;
    private String ville;

    public Patient() {

    }

    public Patient(int id, String nom, String prenom, Date date_nais, String sexe, String telephone, String email, String ville) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.date_nais = date_nais;
        this.sexe = sexe;
        this.telephone = telephone;
        this.email = email;
        this.ville = ville;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Date getDate_nais() {
        return date_nais;
    }

    public String getSexe() {
        return sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getVille() {
        return ville;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDate_nais(Date date_nais) {
        this.date_nais = date_nais;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
}
