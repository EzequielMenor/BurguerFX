package ezequiel.burgerfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));

        Scene scene = new Scene(loader.load());
        stage.setTitle("BurgerFX - Burger del Mes");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
