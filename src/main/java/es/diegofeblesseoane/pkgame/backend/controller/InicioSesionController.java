package es.diegofeblesseoane.pkgame.backend.controller;

import es.diegofeblesseoane.pkgame.PrincipalApplication;
import es.diegofeblesseoane.pkgame.backend.controller.abstracts.AbstractController;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioEntity;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioServiceModel;
import es.diegofeblesseoane.pkgame.backend.model.UsuarioSesion;
import es.diegofeblesseoane.pkgame.config.GlobalLanguageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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
    
    // Referencias FXML para textos que necesitan traducción
    @FXML
    private Text titleLogin;
    
    @FXML
    private Text subtitleLogin;
    
    @FXML
    private Text labelUserOrEmail;
    
    @FXML
    private Text labelPassword;
    
    @FXML
    private Text linkForgotPassword;
    
    @FXML
    private Text labelNoAccount;
    
    @FXML
    private Text infoWelcome;
    
    @FXML
    private Text infoFeatures;
    
    @FXML
    private Text infoDemoUsers;
    
    @FXML
    private Text infoDemoCredentials;

    private final UsuarioServiceModel usuarioServiceModel;


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
                textFalloInicio.setText("❌ " + GlobalLanguageManager.getText("error.emptyCredentials", "Credenciales vacías"));
                return;
            }

            // Obtener el usuario de la base de datos
            String usuarioInput = textFieldNombreUsuario.getText().trim();
            System.out.println("🔍 Buscando usuario: " + usuarioInput);
            
            textFalloInicio.setText("🔄 " + GlobalLanguageManager.getText("message.verifyingCredentials", "Verificando credenciales..."));
            UsuarioEntity usuarioEntity = usuarioServiceModel.obtenerCredencialesUsuario(usuarioInput);

            // Verificar si el usuario existe
            if (usuarioEntity == null) {
                System.out.println("❌ Usuario no encontrado en la base de datos.");
                textFalloInicio.setText("❌ " + GlobalLanguageManager.getText("error.userNotExists", "El usuario no existe"));
                return;
            }

            // Validar las credenciales
            if (credencialesValidas(usuarioEntity)) {
                System.out.println("✅ Usuario validado correctamente: " + usuarioEntity.getNombre());
                String welcomeMessage = GlobalLanguageManager.getText("message.welcome", "¡Bienvenido") + " " + usuarioEntity.getNombre() + "! " + usuarioEntity.getTitulo();
                textFalloInicio.setText("✅ " + welcomeMessage);
                
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
                textFalloInicio.setText("❌ " + GlobalLanguageManager.getText("error.invalidCredentials", "Credenciales inválidas"));
            }
        } catch (Exception e) {
            textFalloInicio.setText("❌ " + GlobalLanguageManager.getText("error.loginError", "Error al iniciar sesión. Inténtalo de nuevo."));
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
    public void initialize() {
        // Asegurar que el sistema global de idiomas esté inicializado
        GlobalLanguageManager.initialize();
        
        // Cargar textos en el idioma actual
        actualizarTextosInterfaz();
    }
    
    /**
     * Actualiza todos los textos de la interfaz según el idioma actual
     */
    private void actualizarTextosInterfaz() {
        try {
            // Títulos y subtítulos
            if (titleLogin != null) {
                titleLogin.setText(GlobalLanguageManager.getText("title.login", "🎮 PK-GAME"));
            }
            
            if (subtitleLogin != null) {
                subtitleLogin.setText(GlobalLanguageManager.getText("subtitle.login", "Iniciar Sesión"));
            }
            
            // Etiquetas de campos
            if (labelUserOrEmail != null) {
                labelUserOrEmail.setText(GlobalLanguageManager.getText("label.userOrEmail", "👤 Usuario o Email"));
            }
            
            if (labelPassword != null) {
                labelPassword.setText(GlobalLanguageManager.getText("label.password", "🔒 Contraseña"));
            }
            
            // Botones
            if (buttonIniciarSesion != null) {
                buttonIniciarSesion.setText(GlobalLanguageManager.getText("button.enter", "🚀 ENTRAR"));
            }
            
            if (buttonRegistro != null) {
                buttonRegistro.setText(GlobalLanguageManager.getText("button.register", "📝 REGISTRARSE"));
            }
            
            // Enlaces y textos informativos
            if (linkForgotPassword != null) {
                linkForgotPassword.setText(GlobalLanguageManager.getText("link.forgotPassword", "¿Olvidaste tu contraseña?"));
            }
            
            if (labelNoAccount != null) {
                labelNoAccount.setText(GlobalLanguageManager.getText("label.noAccount", "¿No tienes cuenta?"));
            }
            
            if (infoWelcome != null) {
                infoWelcome.setText(GlobalLanguageManager.getText("info.welcome", "¡Bienvenido a\nPK-Game!"));
            }
            
            if (infoFeatures != null) {
                infoFeatures.setText(GlobalLanguageManager.getText("info.features", "🎯 Combate con Pokémon\n⚔️ Sistema de tipos estratégico\n🏆 Estadísticas personales\n🤖 IA inteligente\n📊 Rankings y títulos"));
            }
            
            if (infoDemoUsers != null) {
                infoDemoUsers.setText(GlobalLanguageManager.getText("info.demoUsers", "Usuarios Demo:"));
            }
            
            if (infoDemoCredentials != null) {
                infoDemoCredentials.setText(GlobalLanguageManager.getText("info.demoCredentials", "👑 admin / admin123\n🌟 demo / demo123\n🥚 test / test123"));
            }
            
            // Actualizar prompts de los campos de texto
            if (textFieldNombreUsuario != null) {
                textFieldNombreUsuario.setPromptText(GlobalLanguageManager.getText("prompt.userOrEmail", "Ingresa tu usuario o email"));
            }
            
            if (passwordFieldContrasenia != null) {
                passwordFieldContrasenia.setPromptText(GlobalLanguageManager.getText("prompt.password", "Ingresa tu contraseña"));
            }
            
            System.out.println("📋 Textos de la interfaz de inicio de sesión actualizados");
            
        } catch (Exception e) {
            System.out.println("❌ Error actualizando textos de la interfaz: " + e.getMessage());
        }
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
            textFalloInicio.setText(GlobalLanguageManager.getText("error.sceneChange", "Error al cambiar de escena. Inténtalo de nuevo."));
            System.err.println("Error al cambiar de escena: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
