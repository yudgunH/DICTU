package d.dictionary.Controller;

import d.dictionary.BaseFactory.DictionaryManagement;
import d.dictionary.BaseFactory.Trie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerAddWord extends DictionaryManagement implements Initializable {
    @FXML
    private TextField addTextDefinition;
    @FXML
    private TextField addTextWord;
    @FXML
    private Button btnAddNewWord;
    @FXML
    private TextField addTextSpelling, addTextOther;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private Label label;
    private final String[] wordType = {"tính từ", "danh từ", "động từ", "trạng từ"};
    @FXML
    void handleButtonClick(ActionEvent event) {
        if (event.getSource() == btnAddNewWord) {
            if (addTextWord.getText() == null) {
                System.out.println("Vui lòng nhập từ!!");
                label.setStyle("-fx-text-fill: red;");
                label.setText("Vui lòng nhập từ!!");
            } else if (Trie.getSearchedWords().contains(addTextWord.getText())) {
                System.out.println("Đã tồn tại từ này!");
                label.setStyle("-fx-text-fill: red;");
                label.setText("Đã tồn tại từ " + addTextWord.getText());
            } else {
                String definition = addTextWord.getText()
                        + " " + addTextSpelling.getText()
                        + "\n" + choiceBox.getValue()
                        + "\n-" + addTextDefinition.getText()
                        + "\n!" + addTextOther.getText();
                if (addWord(addTextWord.getText(), definition) && addTextWord.getText() != null) {
                    System.out.println("Đã thêm từ " + addTextWord.getText() + " thành công!");
                    label.setStyle("-fx-text-fill: green;");
                    label.setText("Thêm từ " + addTextWord.getText() + " thành công!");
                } else {
                    System.out.println("Thêm từ thất bại!");
                    label.setStyle("-fx-text-fill: red;");
                    label.setText("Thêm từ " + addTextWord.getText() + " thất bại!");
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        choiceBox.getItems().addAll(wordType);
    }
}
