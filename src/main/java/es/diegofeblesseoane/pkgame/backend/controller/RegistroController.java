package es.diegofeblesseoane.pkgame.backend.controller;

import java.sql.SQLException;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import es.diegofeblesseoane.pkgame.backend.controller.abstracts.AbstractController;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioEntity;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioServiceModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegistroController extends AbstractController{
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
    public Text textMensaje;
    
    private UsuarioServiceModel usuarioServiceModel;
    
    /**
     * Devuelve el modelo de usuario.
     * @return El modelo de usuario.
     */
    public UsuarioServiceModel getUsuarioServiceModel() {
        return this.usuarioServiceModel;
    }

    /**
     * * Metodo que se ejecuta al hacer click en el boton de registrar
     * * Registra un nuevo usuario en la base de datos
     * @throws SQLException Excepcion de SQL
     */

    @FXML
    public void onRegistroCompletadoClick() throws SQLException{
        if (passwordFieldContrasenia == null || passwordFieldContrasenia.getText().isEmpty()
                || passwordFieldContrasenia1 == null || passwordFieldContrasenia1.getText().isEmpty()) {
            textMensaje.setText("¡El password no puede ser nulo o vacio!");
            return;
        }

        if (passwordFieldContrasenia.getText().equals(passwordFieldContrasenia1.getText())) {
            textMensaje.setText("¡El password es correcto!");
        }

        UsuarioEntity usuarioNuevo = new UsuarioEntity(textFieldNombreUsuario2.getText(), textFieldNombreUsuario.getText(),
                passwordFieldContrasenia.getText());

        if (!getUsuarioServiceModel().agregarUsuario(usuarioNuevo)) {
            textMensaje.setText("Usuario ya registrado o null");
            return;
        } else {
            textMensaje.setText("Usuario Registrado Correctamente");
            openVolverClick();
            return;
        }
    }

    /**
     * * Metodo que se ejecuta al hacer click en el boton de volver
     * * Vuelve a la pantalla de inicio de sesion
     */
    @FXML
    protected void openVolverClick() {
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
