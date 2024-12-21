public class Product {
    private int id;
    private String productNom;
    private int quantity;
    private String type;

    public Product(int id, String productNom, int quantity,String type) {
        this.id = id;
        this.productNom = productNom;
        this.quantity = quantity;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getProductNom() {
        return productNom;
    }

    public int getQuantity() {
        return quantity;
    }
}
