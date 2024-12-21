public class Analyse {
    private int id;
    private String nom;

    private String nom_cours;
    private Float prix;
    private String result;
    private String code;

    public Analyse(int id, String nom, String nom_cours, Float prix) {
        this.id = id;
        this.nom = nom;
        this.nom_cours = nom_cours;
        this.prix = prix;
    }

    public Analyse(String nom, Float prix, String result) {
        this.nom = nom;
        this.prix = prix;
        this.result = result;
    }

    public Analyse(String code, String nom, String nom_cours, String result) {
        this.code = code;
        this.nom = nom;
        this.nom_cours = nom_cours;
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public String getCode() {
        return code;
    }

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getNom_cours() {
        return nom_cours;
    }

    public Float getPrix() {
        return prix;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNom_cours(String nom_cours) {
        this.nom_cours = nom_cours;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }
}
