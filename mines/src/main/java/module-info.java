module com.example.mines {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.mines to javafx.fxml;
    exports com.example.mines;
}