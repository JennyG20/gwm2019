import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class Settings_Controller implements Initializable {

    public static Task task;

    public static Assessment assessment;

    @FXML
    private Label username_label;

    @FXML
    private Label userrole_label;

    @FXML
    private Label userid_label;

    @FXML
    private TextField connURL_txt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username_label.setText(Core.getUser().getFirstname() + " " + Core.getUser().getSecondname());

        userrole_label.setText(Core.getUser().getUsertype().toUpperCase());

        userid_label.setText("User ID: " + Core.getUser().getId());

    }

    @FXML
    public void back_buttonPressed() {
        Core.loadScene(Core.MAIN, null);
    }

    @FXML
    private void onSettingsButtonPressed(){
        Core.loadScene(Core.SETTINGS, null);
    }

    @FXML
    private void onSignOutButtonPressed(){
        Core.loadScene(Core.LOGIN, null);
    }

    @FXML
    private void onApplyDBURLButtonPressed(){
        DB.connURL = connURL_txt.getText();
        Core.db = new DB();
    }
}
