import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Login_Controller extends Application {

    static Core main;

    private Stage stage;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField username_txt;

    @FXML
    private TextField password_txt;

    @FXML
    private Button signin_btn;

    @FXML
    public void login_buttonPressed() {
        User user = main.db.login(username_txt.getText(), password_txt.getText());
        if (user != null) {
            Core.root = this.root;
            main.setUser(user);
            main.loadScene(main.MAIN, null);
        } else {
            //TODO: handle wrong credentials
        }
    }

    public Login_Controller() {
    }

    @FXML
    private void initialize() {
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.stage.setTitle("GroupWorkManager");
        this.stage.setResizable(false);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Login_Controller.class.getResource("Login.fxml"));

        try {
            this.stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.stage.show();
    }

    public static void main(String[] args) {
        main = new Core();

        launch(args);
    }
}
