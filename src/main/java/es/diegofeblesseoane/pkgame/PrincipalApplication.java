package es.diegofeblesseoane.pkgame;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PrincipalApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
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
    }

    public static void main(String[] args) {
        launch();
    }
}