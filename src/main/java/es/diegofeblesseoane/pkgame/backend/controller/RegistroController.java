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

public class RegistroController extends AbstractController {
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

    public RegistroController() {
        this.usuarioServiceModel = new UsuarioServiceModel();
    }

    public RegistroController(UsuarioServiceModel usuarioServiceModel) {
        this.usuarioServiceModel = usuarioServiceModel;
    }

    public UsuarioServiceModel getUsuarioServiceModel() {
        return this.usuarioServiceModel;
    }

    @FXML
    public void onRegistroCompletadoClick() {
        try {
            // Limpiar mensaje anterior
            textMensaje.setText("");
            
            // Obtener y limpiar los valores de los campos
            String email = textFieldNombreUsuario2 != null ? textFieldNombreUsuario2.getText().trim() : "";
            String nombreUsuario = textFieldNombreUsuario != null ? textFieldNombreUsuario.getText().trim() : "";
            String contrasenia = passwordFieldContrasenia != null ? passwordFieldContrasenia.getText().trim() : "";
            String contrasenia2 = passwordFieldContrasenia1 != null ? passwordFieldContrasenia1.getText().trim() : "";
            
            // Validaciones básicas
            if (email.isEmpty()) {
                textMensaje.setText("❌ El email no puede estar vacío");
                return;
            }
            
            if (nombreUsuario.isEmpty()) {
                textMensaje.setText("❌ El nombre de usuario no puede estar vacío");
                return;
            }
            
            if (contrasenia.isEmpty() || contrasenia2.isEmpty()) {
                textMensaje.setText("❌ Las contraseñas no pueden estar vacías");
                return;
            }

            // Validar que las contraseñas coincidan
            if (!contrasenia.equals(contrasenia2)) {
                textMensaje.setText("❌ Las contraseñas no coinciden");
                return;
            }

            // Crear un nuevo usuario
            UsuarioEntity usuarioNuevo = new UsuarioEntity(email, nombreUsuario, contrasenia);

            // Validar datos con el servicio
            String errorValidacion = getUsuarioServiceModel().validarUsuario(usuarioNuevo);
            if (errorValidacion != null) {
                textMensaje.setText("❌ " + errorValidacion);
                return;
            }

            System.out.println("🔄 Intentando registrar usuario: " + usuarioNuevo.getEmail());

            // Intentar agregar el usuario a la base de datos
            if (getUsuarioServiceModel().agregarUsuario(usuarioNuevo)) {
                textMensaje.setText("✅ ¡Usuario registrado correctamente! Redirigiendo...");
                System.out.println("✅ Usuario registrado exitosamente: " + usuarioNuevo.getEmail());
                
                // Esperar un momento antes de redirigir para mostrar el mensaje
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        javafx.application.Platform.runLater(this::openVolverClick);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                textMensaje.setText("❌ Usuario ya registrado o error en el registro");
            }
            
        } catch (SQLException e) {
            textMensaje.setText("❌ Error de conexión. Inténtalo de nuevo.");
            System.err.println("Error en onRegistroCompletadoClick: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            textMensaje.setText("❌ Error inesperado. Inténtalo de nuevo.");
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    protected void openVolverClick() {
        try {
            Stage stage = (Stage) buttonRegistro.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("inicioSesion.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 2100, 1200);
            stage.setTitle("PK-GAME - Inicio Sesion");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            textMensaje.setText("Error al volver a la pantalla de inicio de sesión.");
            e.printStackTrace();
        }
    }
}
