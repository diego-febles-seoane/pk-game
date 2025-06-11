package es.diegofeblesseoane.pkgame;

import java.io.IOException;

import es.diegofeblesseoane.pkgame.config.GlobalLanguageManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PrincipalApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Inicializar el sistema global de idiomas antes de cargar cualquier interfaz
        System.out.println("🚀 Iniciando PK-GAME...");
        GlobalLanguageManager.initialize();
        System.out.println("✅ Sistema de idiomas inicializado");
        
        FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 2100, 1200);
        stage.setTitle("PK-GAME");
        stage.setResizable(true);  // Permitir redimensionar para mejor experiencia
        stage.setMinWidth(1920);   // Tamaño mínimo
        stage.setMinHeight(1080);
        stage.setMaxWidth(2560);   // Tamaño máximo
        stage.setMaxHeight(1440);
        stage.setScene(scene);
        stage.show();
        
        System.out.println("🎮 PK-GAME iniciado exitosamente");
    }

    public static void main(String[] args) {
        launch();
    }
}