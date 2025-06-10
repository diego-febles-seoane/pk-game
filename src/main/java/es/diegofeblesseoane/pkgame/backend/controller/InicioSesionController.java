package es.diegofeblesseoane.pkgame.backend.controller;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import es.diegofeblesseoane.pkgame.backend.controller.abstracts.AbstractController;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioEntity;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioServiceModel;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioSesion;
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

    private final UsuarioServiceModel usuarioServiceModel;

    private static final String CREDENCIALES_VACIAS = "Credenciales vacías";
    private static final String USUARIO_NO_EXISTE = "El usuario no existe";
    private static final String CREDENCIALES_INVALIDAS = "Credenciales inválidas";
    private static final String USUARIO_VALIDADO = "Usuario validado correctamente";

    public InicioSesionController() {
        this.usuarioServiceModel = new UsuarioServiceModel();
    }

    public InicioSesionController(UsuarioServiceModel usuarioServiceModel) {
        this.usuarioServiceModel = usuarioServiceModel;
    }

    public UsuarioServiceModel getUsuarioServiceModel() {
        return usuarioServiceModel;
    }

    @FXML
    private void onButtonRegistrarClick() {
        cambiarEscena("registro.fxml", "RPS Pokemon - Registro");
    }

    @FXML
    private void onMenuPrincipalClick() {
        try {
            // Limpiar mensaje anterior
            textFalloInicio.setText("");
            
            // Validar si las credenciales están vacías
            if (credencialesVacias()) {
                textFalloInicio.setText("❌ " + CREDENCIALES_VACIAS);
                return;
            }

            // Obtener el usuario de la base de datos
            String usuarioInput = textFieldNombreUsuario.getText().trim();
            System.out.println("🔍 Buscando usuario: " + usuarioInput);
            
            textFalloInicio.setText("🔄 Verificando credenciales...");
            UsuarioEntity usuarioEntity = usuarioServiceModel.obtenerCredencialesUsuario(usuarioInput);

            // Verificar si el usuario existe
            if (usuarioEntity == null) {
                System.out.println("❌ Usuario no encontrado en la base de datos.");
                textFalloInicio.setText("❌ " + USUARIO_NO_EXISTE);
                return;
            }

            // Validar las credenciales
            if (credencialesValidas(usuarioEntity)) {
                System.out.println("✅ Usuario validado correctamente: " + usuarioEntity.getNombre());
                textFalloInicio.setText("✅ ¡Bienvenido " + usuarioEntity.getNombre() + "! " + usuarioEntity.getTitulo());
                
                // Guardar usuario en sesión
                UsuarioSesion.getInstancia().setUsuario(usuarioEntity);
                
                // Actualizar último acceso
                usuarioServiceModel.actualizarUltimoAcceso(usuarioEntity.getEmail());
                
                // Esperar un momento antes de redirigir
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        javafx.application.Platform.runLater(this::abrirMenuPrincipal);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                System.out.println("❌ Credenciales inválidas.");
                textFalloInicio.setText("❌ " + CREDENCIALES_INVALIDAS);
            }
        } catch (Exception e) {
            textFalloInicio.setText("❌ Error al iniciar sesión. Inténtalo de nuevo.");
            System.err.println("Error en onMenuPrincipalClick: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean credencialesVacias() {
        return textFieldNombreUsuario == null || textFieldNombreUsuario.getText().trim().isEmpty()
                || passwordFieldContrasenia == null || passwordFieldContrasenia.getText().trim().isEmpty();
    }

    private boolean credencialesValidas(UsuarioEntity usuarioEntity) {
        String usuarioInput = textFieldNombreUsuario.getText().trim();
        String contraseniaInput = passwordFieldContrasenia.getText().trim();
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
            Scene scene = new Scene(fxmlLoader.load(), 2100, 1200);
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            textFalloInicio.setText("Error al cambiar de escena. Inténtalo de nuevo.");
            System.err.println("Error al cambiar de escena: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
