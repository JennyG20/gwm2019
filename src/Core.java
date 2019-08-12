import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Core {

    public static final int LOGIN = 0, MAIN = 1, ASMT = 2, TASK = 3, ASMT_EDIT = 4, TASK_EDIT = 5, SETTINGS = 6;

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
                user = null;
                break;
            case MAIN:
                loader.setLocation(Login_Controller.class.getResource("Main.fxml"));
                break;
            case ASMT:
                Assessment_Controller.assessment = (Assessment) params[0];
                loader.setLocation(Login_Controller.class.getResource("Assessment.fxml"));
                break;
            case TASK:
                Task_Controller.task = (Task) params[0];
                Task_Controller.assessment = (Assessment) params[1];
                loader.setLocation(Login_Controller.class.getResource("Task.fxml"));
                break;
            case ASMT_EDIT:
                AssessmentCreator_Controller.init((Assessment) params[0]);
                loader.setLocation(Login_Controller.class.getResource("AssessmentCreator.fxml"));
                break;
            case TASK_EDIT:
                Task_Controller.task = (Task) params[0];
                Task_Controller.assessment = (Assessment) params[1];
                loader.setLocation(Login_Controller.class.getResource("Task.fxml"));
                break;
            case SETTINGS:
                loader.setLocation(Settings_Controller.class.getResource("Settings.fxml"));
                break;
        }

        try {
            root.getChildren().setAll((Node) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
