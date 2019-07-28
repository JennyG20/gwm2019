import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Core {

    public static final int LOGIN = 0, MAIN = 1, TASK = 2;

    public static AnchorPane root;
    static DB db;
    private static User user;

    public Core() {
        this.db = new DB();
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User User) {
        user = User;
    }

    public static void loadScene(int scene, Object[] params){
        FXMLLoader loader = new FXMLLoader();

        switch (scene){
            case LOGIN:
                loader.setLocation(Login_Controller.class.getResource("Login.fxml"));
                new Login_Controller();
                break;
            case MAIN:
                loader.setLocation(Login_Controller.class.getResource("Main.fxml"));
                break;
            case TASK:
                Assessment_Controller.assessment = (Assessment) params[0];
                loader.setLocation(Login_Controller.class.getResource("Assessment.fxml"));
                break;
        }

        try {
            root.getChildren().setAll((Node) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
