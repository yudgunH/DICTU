package d.dictionary.Controller;

import d.dictionary.App;
import d.dictionary.BaseFactory.DictionaryCommandline;
import d.dictionary.BaseFactory.DictionaryManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;


public class Controller extends DictionaryManagement {
    @FXML
    Button loginButton;
    @FXML
    protected AnchorPane scenePane;
    @FXML
    TextField nameTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    Button exitButton;


    public void login(ActionEvent event) {
        String username = nameTextField.getText();
        String password = passwordTextField.getText();

        if (username.isEmpty()) {
            showError("Vui lòng nhập tên đăng nhập.");
        } else if (password.isEmpty()) {
            showError("Vui lòng nhập mật khẩu.");
        } else if (!isValidUsername(username)) {
            showError("Hãy nhập tên đăng nhập cho hợp lệ.");
        } else if (!isValidPassWord(password)) {
            showError("Sai mật khẩu.");
        } else if (username.equals("admin") && password.equals("admin")) {
            try {
                App.stage = (Stage) scenePane.getScene().getWindow();
                DictionaryCommandline.closeScene(App.stage);
                Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("SceneSearch.fxml")));
                Controller.switchScene(event, root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isValidUsername(String username) {
        if (username.equals("admin")){
            return true;
        } else {
            showError("Sai tên đăng nhập");
        }
        return !username.isEmpty();
    }
    private boolean isValidPassWord(String password) {
        if (password.equals("admin")){
            return true;
        } else {
            showError("Sai mật khẩu");
        }
        return !password.isEmpty();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Login failed.");
        alert.setContentText(message);

        alert.showAndWait();
    }
    public void initialize() {
        exitButton.setOnAction(event -> {
            System.exit(0);
        });
        Thread searchInitThread = new Thread(() -> {
            try {
                ControllerSearch.init();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        searchInitThread.start();
    }

}