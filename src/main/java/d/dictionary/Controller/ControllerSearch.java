package d.dictionary.Controller;

import d.dictionary.App;
import d.dictionary.BaseFactory.DictionaryCommandline;
import d.dictionary.BaseFactory.DictionaryManagement;
import d.dictionary.BaseFactory.Trie;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerSearch extends DictionaryManagement implements Initializable {
    @FXML
    private ImageView exit;
    @FXML
    private ImageView menu, loudspeakers;
    @FXML
    private AnchorPane pane1;
    @FXML
    private AnchorPane pane2;
    @FXML
    Button btnSearch, btnAdd, btnDashBoard, btnDelete, btnTranslation, quizBtn, hangmanBtn;
    @FXML
    TextField textField;
    @FXML
    ListView<String> listView;
    @FXML
    TextArea textArea;
    @FXML
    ListView<String> listHistoryView;
    @FXML
    TextArea label;
    @FXML
    Button edit;
    @FXML
    Button update;
    @FXML
    Label labelTB;

    boolean isEditable = false;

    public ArrayList<String> searchWordsStartingWith(String prefix) {
        return Trie.search(prefix);
    }


    public void search() {
        String searchTerm = textField.getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            listView.getItems().clear();
            textArea.clear();
        } else {
            listView.getItems().setAll(searchWordsStartingWith(searchTerm));
        }
    }

    private void runPauseTransition(int seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(this::clearLabelTB);
        pause.play();
    }

    private void clearLabelTB(ActionEvent actionEvent) {
        labelTB.setText("");
        if (listView.getItems() == null) {
            textArea.clear();
        }
    }

    @FXML
    void handleButtonClick(ActionEvent event) {
        if (event.getSource() == btnAdd) {
            try {
                showWindow("AddNew.fxml", "Add New Word");
            } catch (IOException e) {
                labelTB.setText("Lỗi không mở được cửa sổ AddNew");
                System.out.println("Lỗi không mở được cửa sổ AddNew");
                runPauseTransition(2);
            }
        }
        if (event.getSource() == btnDelete) {
            String deleteTextWord = listView.getSelectionModel().getSelectedItem();
            Trie.delete(deleteTextWord);
            if(deleteWord(deleteTextWord)) {
                System.out.println("Đã xoá thành công từ " + deleteTextWord + "!");
                labelTB.setText("Đã xoá thành công từ " + deleteTextWord + "!");
                runPauseTransition(2);
            }
            else {
                System.out.println("Không tồn tại từ cần xoá");
                try {
                    labelTB.setText("Không tồn tại từ cần xoá");
                    runPauseTransition(2);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (event.getSource() == btnDashBoard) {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("Scene.fxml")));
                ControllerSearch.switchScene(event, root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (event.getSource() == btnTranslation) {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("SceneTranslate.fxml")));
                ControllerSearch.switchScene(event, root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (event.getSource() == btnSearch) {
            try {
                search();
                label.setVisible(true);
                textArea.setVisible(false);
                isEditable = false;

                System.out.println(textField.getText());
                textArea.setText(findWord(textField.getText()));
                label.setText(findWord(textField.getText()));
            } catch (NullPointerException e) {
                System.out.println("Vui lòng nhập từ để tìm kiếm!!");
            }
        }
        if (event.getSource() == update) {
            try {
                String selectedItem = listView.getSelectionModel().getSelectedItem();
                String updatedExplanation = textArea.getText();
                deleteWord(selectedItem);
                addWord(selectedItem , updatedExplanation);
                updateWord(selectedItem, updatedExplanation);

                if (isEditable && selectedItem != null) {
                    labelTB.setText("Dữ liệu đã được cập nhật thành công");
                    DictionaryCommandline.tbAlert("Update", "Dữ liệu đã được cập nhật thành công");
                    runPauseTransition(2);
                } else {
                    labelTB.setText("Dữ liệu đã được cập nhật không thành công !!!");
                    DictionaryCommandline.tbAlert("Update", "Dữ liệu đã được cập nhật không thành công !!!");
                    runPauseTransition(2);
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Đã xảy ra lỗi trong quá trình cập nhật dữ liệu");
                    alert.showAndWait();
                });
            }
        }

        if (event.getSource() == quizBtn) {
            try {
                showWindow("WordPuzzle.fxml", "Word Puzzle");
            } catch (IOException e) {
                System.out.println("khong load duoc game");
            }
        }

        if (event.getSource() == hangmanBtn) {
            try {
                showWindow("Hangman.fxml", "Hangman");
            } catch (IOException e) {
                System.out.println("khong load duoc game");
            }
        }

    }

    private void handleEditButtonClick(ActionEvent event) {
        if (!isEditable) {
            labelTB.setText("Đang ở chế độ chỉnh sửa");
            isEditable = true;
            textArea.setVisible(true);
            label.setVisible(false);
            edit.setText("Edit");
        } else {
            labelTB.setText("Đang ở chế độ xem");
            isEditable = false;
            textArea.setVisible(false);
            label.setVisible(true);
            edit.setText("Watch");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        exit.setOnMouseClicked(event -> {
            close();
            System.exit(0);
        });


        label.setVisible(true);
        textArea.setVisible(false);
        edit.setOnAction(this::handleEditButtonClick);

        loudspeakers.setOnMouseClicked(event -> {
            try {
                String line = textArea.getText();
                String[] parts = line.split("\n");
                String TTS = parts[0];
                try {
                    ControllerTranslate.playSoundGoogleTranslate(TTS, "en");
                } catch (UnknownHostException e) {
                    System.out.println("Không có kết nối mạng vui lòng thử lại");
                    labelTB.setText("Không có kết nối mạng vui lòng thử lại");
                    runPauseTransition(2);
                }
                System.out.println(textArea.getText());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        pane1.setVisible(false);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.2),pane1);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.2),pane2);
        translateTransition.setByX(-800);
        translateTransition.play();

        menu.setOnMouseClicked(event -> {
            pane1.setVisible(true);

            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.2),pane1);
            fadeTransition1.setFromValue(0);
            fadeTransition1.setToValue(0.15);
            fadeTransition1.play();

            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2),pane2);
            translateTransition1.setByX(+800);
            translateTransition1.play();


        });

        pane1.setOnMouseClicked(event -> {

            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.2),pane1);
            fadeTransition1.setFromValue(0.15);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();

            fadeTransition1.setOnFinished(event1 -> {
                pane1.setVisible(false);
            });

            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2),pane2);
            translateTransition1.setByX(-800);
            translateTransition1.play();
        });

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            search();
        });
        listHistoryView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textArea.setText(findWord(newSelection));
                label.setText(findWord(newSelection));
            }
        });
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textArea.setText(findWord(newSelection));
                label.setText(findWord(newSelection));
                if (!listHistoryView.getItems().contains(newSelection)) {
                    listHistoryView.getItems().add(newSelection);
                }
            }
        });
    }
}
