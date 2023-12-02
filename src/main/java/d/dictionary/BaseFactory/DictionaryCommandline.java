package d.dictionary.BaseFactory;

import javafx.scene.control.Alert;
import javafx.stage.Stage;


public class DictionaryCommandline {


    public static void nullChoiceBox() {
        System.out.println("Vui lòng chọn ngôn ngữ");
    }

    public static void tbAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void closeScene(Stage stage) {
        stage.close();
    }
}
