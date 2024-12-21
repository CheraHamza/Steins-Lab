module com.example.laboratory {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires java.desktop;


    opens com.example.laboratory to javafx.fxml;
    exports com.example.laboratory;
}