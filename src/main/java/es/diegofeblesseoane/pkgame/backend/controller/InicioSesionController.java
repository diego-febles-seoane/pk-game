package es.diegofeblesseoane.pkgame.backend.controller;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InicioSesionController {

    private String nombreUsuario = "Diego";
    private String contrasenia = "1234a";

    @FXML
    public TextField textFieldNombreUsuario;

    @FXML
    public Button buttonIniciarSesion;

    @FXML
    public PasswordField passwordFieldContrasenia;

    @FXML
    public Button buttonRegistro;

    @FXML
    public Text textFalloInicio;

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

    @FXML
    public void onMenuPrincipalClick(){
        if (textFieldNombreUsuario== null || textFieldNombreUsuario.getText().isEmpty() || 
            passwordFieldContrasenia == null || passwordFieldContrasenia.getText().isEmpty() ) {
                textFalloInicio.setText("Credenciales null o vacias");
                return;
        }

        if (!textFieldNombreUsuario.getText().equals(nombreUsuario) || !passwordFieldContrasenia.getText().equals(contrasenia)) {
            textFalloInicio.setText("Credenciales invalidas");
            return;
        }

        try {
            Stage stage = (Stage) buttonIniciarSesion.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
            stage.setTitle("RPS Pokemon - Inicio Sesion");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
