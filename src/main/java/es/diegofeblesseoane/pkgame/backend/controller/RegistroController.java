package es.diegofeblesseoane.pkgame.backend.controller;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistroController {
    @FXML
    public TextField textFieldNombreUsuario;

    @FXML
    public Button buttonRegistro;

    @FXML
    public PasswordField passwordFieldContrasenia;

    @FXML
    public TextField textFieldNombreUsuario1;

    @FXML
    public PasswordField passwordFieldContrasenia1;

    @FXML
    public TextField textFieldNombreUsuario2;

    @FXML
    public void onRegistroCompletadoClick(){
        try {
            Stage stage = (Stage) buttonRegistro.getScene().getWindow();
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
