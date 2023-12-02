package d.dictionary;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {
    public static Stage stage;
    public static Scene scene;

    @Override
    public void start(Stage stage) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("Scene.fxml"));

            stage.initStyle(StageStyle.UNDECORATED);

            scene = new Scene(root, 800, 600);
            Image icon = new Image("C:/Users/ACER/IdeaProjects/TranslateJava/src/main/resources/com/example/translatejava/images/Icon.png");
            stage.getIcons().add(icon);
            stage.setTitle("Dictionary");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}