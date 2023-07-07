package com.birzeit.recursivedescentparser;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ParserController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}