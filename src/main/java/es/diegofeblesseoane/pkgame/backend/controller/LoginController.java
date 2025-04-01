package es.diegofeblesseoane.pkgame.backend.controller;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    public Button buttonRegistrar;

    @FXML
    public Button buttonIniciarSesion;


    @FXML
    public void onIniciarSesionButton(){
        try {
            Stage stage = (Stage) buttonIniciarSesion.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("inicioSesion.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
            stage.setTitle("RPS Pokemon - Inicio Sesion");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
