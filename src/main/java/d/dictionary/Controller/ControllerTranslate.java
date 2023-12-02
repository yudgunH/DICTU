package d.dictionary.Controller;

import d.dictionary.App;
import d.dictionary.BaseFactory.DictionaryCommandline;
import d.dictionary.BaseFactory.DictionaryManagement;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javazoom.jl.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerTranslate extends DictionaryManagement implements Initializable {
    @FXML
    private ImageView exit;
    @FXML
    private ImageView menu;
    @FXML
    private ImageView loudspeakers;
    @FXML
    private ImageView loudspeakers1;
    @FXML
    private AnchorPane pane1,pane2;
    @FXML
    private TextField textField, textFieldT;
    @FXML
    Button btnSearch, btnDashBoard, hangmanBtn, quizBtn, btnTranslate;
    @FXML
    private ChoiceBox<String> choiceBox1, choiceBox2;
    @FXML
    private Label labelT;
    private final String[] languages = {"English", "Vietnamese", "Japan", "France", "Singapore", "Korea", "Russia", "Thailand"};


    public static void playSoundGoogleTranslate(String text, String language) throws UnknownHostException {
        try {
            String urlStr = "https://translate.google.com/translate_tts?ie=UTF-8&tl="
                    + language
                    + "&client=tw-ob&q="
                    + URLEncoder.encode(text, StandardCharsets.UTF_8);
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream audio = connection.getInputStream();
            new Player(audio).play();
            connection.disconnect();
        } catch (UnknownHostException e) {
            throw new UnknownHostException();
        } catch (IOException e) {
            System.out.println("Không có từ để phát âm");
        } catch (Exception e) {
            System.out.println("Có lỗi gì đó ở đây!!");
        }
    }

    private static String translateFuntion(String languageFrom, String languageTo, String text) throws UnknownHostException {
        try {
            String urlStr = "https://script.google.com/macros/s/AKfycbxzCfT78zpe2hPNd75uEzo7Joq2m-ach6UxoEuKpnf0JEgUcBc4C7SKL6QFeZ5ghDJM/exec"
                    +
                    "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                    "&target=" + languageTo +
                    "&source=" + languageFrom;
            URL url = new URL(urlStr);
            StringBuilder response = new StringBuilder();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (NullPointerException e) {
            System.out.println("Vui lòng nhập từ cần dịch");
        } catch (UnknownHostException e) {
            throw new UnknownHostException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @FXML
    void handleButtonClick(ActionEvent event) {
        if (event.getSource() == btnDashBoard) {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("Scene.fxml")));
                ControllerTranslate.switchScene(event, root);
            } catch (IOException e) {
                throw new RuntimeException(e);
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
        if (event.getSource() == btnSearch) {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("SceneSearch.fxml")));
                ControllerTranslate.switchScene(event, root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (event.getSource() == btnTranslate) {
            try {
                String langFrom = choiceBox1.getValue().substring(0, 2).toLowerCase();
                String langTo = choiceBox2.getValue().substring(0, 2).toLowerCase();

                textFieldT.setText(translateFuntion(langFrom, langTo, textField.getText()));
            } catch (UnknownHostException e) {
                System.out.println("Không có kết nối mạng vui lòng thử lại");
                labelT.setText("Không có kết nối mạng vui lòng thử lại");
            } catch (NullPointerException e) {
                DictionaryCommandline.nullChoiceBox();
            } catch (Exception e) {
                System.out.println("Có lỗi gì đó!");
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resource) {
        exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        choiceBox1.setValue("English");
        choiceBox2.setValue("Vietnamese");
        choiceBox1.getItems().addAll(languages);
        choiceBox2.getItems().addAll(languages);

        labelT.setText("");

        loudspeakers.setOnMouseClicked(event -> {
            try {
                String langFrom = choiceBox1.getValue().substring(0, 2).toLowerCase();
                playSoundGoogleTranslate(textField.getText(), langFrom);
                System.out.println(textField.getText());
            } catch (NullPointerException e) {
                System.out.println("Vui lòng chọn ngôn ngữ!");
                labelT.setText("Vui lòng chọn ngôn ngữ!");
            } catch (UnknownHostException e) {
                labelT.setText("Không có kết nối mạng, xin vui lòng thử lại!");
            }
        });
        loudspeakers1.setOnMouseClicked(event -> {
            try {
                String langTo = choiceBox2.getValue().substring(0, 2).toLowerCase();
                playSoundGoogleTranslate(textFieldT.getText(), langTo);
            } catch (NullPointerException e) {
                System.out.println("Vui lòng nhập từ để đọc!!");
            } catch (UnknownHostException e) {
                System.out.println("Không có kết nối mạng, xin vui lòng thử lại!");
                labelT.setText("Không có kết nối mạng, xin vui lòng thử lại!");
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
    }
}
