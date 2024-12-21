public class Entrant {
    private int id;
    private String name;
    private String prenom;
    private String montant;
    private String date;

    public Entrant(int id, String name, String prenom, String montant, String date) {
        this.id = id;
        this.name = name;
        this.prenom = prenom;
        this.montant = montant;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getMontant() {
        return montant;
    }

    public String getDate() {
        return date;
    }
}