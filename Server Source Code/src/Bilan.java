import java.util.Date;

public class Bilan {
    int id;
    int id_client;
    String nom;
    Double montant;
    String analyses;
    Date date;

    public Bilan(int id, int id_client, String nom, Double montant, String analyses, Date date) {
        this.id = id;
        this.id_client = id_client;
        this.nom = nom;
        this.montant = montant;
        this.analyses = analyses;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getId_client() {
        return id_client;
    }

    public String getNom() {
        return nom;
    }

    public Double getMontant() {
        return montant;
    }

    public String getAnalyses() {
        return analyses;
    }

    public Date getDate() { return date; }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public void setAnalyses(String analyses) {
        this.analyses = analyses;
    }

    public void setDate(Date date) { this.date = date; }
}
