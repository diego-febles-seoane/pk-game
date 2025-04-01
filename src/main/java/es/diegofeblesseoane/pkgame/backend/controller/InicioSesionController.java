package es.diegofeblesseoane.pkgame.backend.controller;

import javafx.scene.control.TextField;
import es.diegofeblesseoane.pkgame.PrincipalApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class InicioSesionController {
    @FXML
    public TextField textFieldNombreUsuario;

    @FXML
    public Button buttonIniciarSesion;

    @FXML
    public PasswordField passwordFieldContrasenia;

    @FXML
    public Button buttonRegistro;

    @FXML
    public void onButtonRegistrarClick(){
        try {
            Stage stage = (Stage) buttonRegistro.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("registro.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
            stage.setTitle("RPS Pokemon - Registro");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
