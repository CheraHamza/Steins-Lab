public class Payment {
    private int id;
    private String username;
    private String montant;
    private String date;

    public Payment(int id, String username, String montant, String date) {
        this.id = id;
        this.username = username;
        this.montant = montant;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMontant() {
        return montant;
    }

    public String getDate() {
        return date;
    }
}
