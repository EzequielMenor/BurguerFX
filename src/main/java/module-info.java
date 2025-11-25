module ezequiel.burgerfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens ezequiel.burgerfx to javafx.fxml;
    exports ezequiel.burgerfx;
}