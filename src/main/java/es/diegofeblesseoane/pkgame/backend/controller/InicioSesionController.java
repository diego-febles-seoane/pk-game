package es.diegofeblesseoane.pkgame.backend.controller;

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

public class InicioSesionController extends AbstractController {

    @FXML
    private TextField textFieldNombreUsuario;

    @FXML
    private Button buttonIniciarSesion;

    @FXML
    private PasswordField passwordFieldContrasenia;

    @FXML
    private Button buttonRegistro;

    @FXML
    private Text textFalloInicio;

    private final UsuarioServiceModel usuarioServiceModel = new UsuarioServiceModel();

    /**
     * Devuelve el modelo de usuario.
     * @return El modelo de usuario.
     */
    public UsuarioServiceModel getUsuarioServiceModel() {
        return usuarioServiceModel;
    }

    @FXML
    private void onButtonRegistrarClick() {
        cambiarEscena("registro.fxml", "RPS Pokemon - Registro");
    }

    @FXML
    private void onMenuPrincipalClick() {
        if (credencialesVacias()) {
            textFalloInicio.setText("Credenciales vacías");
            return;
        }

        UsuarioEntity usuarioEntity = usuarioServiceModel.obtenerCredencialesUsuario(textFieldNombreUsuario.getText());

        if (usuarioEntity == null) {
            textFalloInicio.setText("El usuario no existe");
            return;
        }

        if (credencialesValidas(usuarioEntity)) {
            textFalloInicio.setText("Usuario validado correctamente");
            abrirMenuPrincipal();
        } else {
            textFalloInicio.setText("Credenciales inválidas");
        }
    }

    private boolean credencialesVacias() {
        return textFieldNombreUsuario == null || textFieldNombreUsuario.getText().isEmpty()
                || passwordFieldContrasenia == null || passwordFieldContrasenia.getText().isEmpty();
    }

    private boolean credencialesValidas(UsuarioEntity usuarioEntity) {
        String usuarioInput = textFieldNombreUsuario.getText();
        String contraseniaInput = passwordFieldContrasenia.getText();
        return (usuarioInput.equals(usuarioEntity.getEmail()) || usuarioInput.equals(usuarioEntity.getNombre()))
                && contraseniaInput.equals(usuarioEntity.getContrasenia());
    }

    @FXML
    private void abrirMenuPrincipal() {
        cambiarEscena("menu.fxml", "Pantalla Perfil");
    }

    private void cambiarEscena(String fxml, String titulo) {
        try {
            Stage stage = (Stage) buttonIniciarSesion.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource(fxml));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
