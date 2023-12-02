module d.dictionary {
    requires javafx.controls;
    requires javafx.fxml;
    requires google.cloud.translate;
    requires jlayer;
    requires java.sql;

    opens d.dictionary to javafx.fxml;
    exports d.dictionary;
    exports d.dictionary.BaseFactory;
    opens d.dictionary.BaseFactory to javafx.fxml;
    exports d.dictionary.Controller;
    opens d.dictionary.Controller to javafx.fxml;
}