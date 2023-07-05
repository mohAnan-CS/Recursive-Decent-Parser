module com.birzeit.recursivedescentparser {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.birzeit.recursivedescentparser to javafx.fxml;
    exports com.birzeit.recursivedescentparser;
}