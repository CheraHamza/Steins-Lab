public class Purchase {
    private int id;
    private String productName;
    private String type;
    private String montant;
    private String date;

    public Purchase(int id, String productName, String type, String montant, String date) {
        this.id = id;
        this.productName = productName;
        this.type = type;
        this.montant = montant;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getType() {
        return type;
    }

    public String getMontant() {
        return montant;
    }

    public String getDate() {
        return date;
    }
}
