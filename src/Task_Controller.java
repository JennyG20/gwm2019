import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ResourceBundle;

public class Task_Controller implements Initializable {

    public static Task task;

    public static Assessment assessment;

    @FXML
    private Label username_label;

    @FXML
    private Label userrole_label;

    @FXML
    private Label userid_label;

    @FXML
    private Label title_label;

    @FXML
    private Label description_label;

    @FXML
    private Label supervisor_label;

    @FXML
    private Label deadline_label;

    @FXML
    private Label collaborators_label;

    @FXML
    private TextArea post_txt;

    @FXML
    private VBox list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username_label.setText(Core.getUser().getFirstname() + " " + Core.getUser().getSecondname());

        userrole_label.setText(Core.getUser().getUsertype().toUpperCase());

        userid_label.setText("User ID: " + Core.getUser().getId());

        title_label.setText(assessment.getTitle());

        description_label.setText("Assessment description: " + task.getDescription());

        supervisor_label.setText("Supervised by: " + Core.db.getUserName(assessment.getSupervisor()));

        collaborators_label.setText("Collaborators: " + task.getCollaboratorsAsOneString());

        deadline_label.setText("Deadline: " + task.getDeadline());

        for(Post post : task.getPosts()){
            addPost(post);
        }
    }

    @FXML
    public void back_buttonPressed() {
        Core.loadScene(Core.ASMT, new Object[]{assessment});
    }

    @FXML void postButtonPressed(){
        Core.db.addPost(task.getID(), Core.getUser().getId(), post_txt.getText());
        Post post = new Post(task.getID(), Core.getUser().getId(), task.getID(), post_txt.getText());
        addPost(post);
    }

    private void addPost(Post post){
        TitledPane titledPane = new TitledPane();
        titledPane.setPrefWidth(780);
        titledPane.setMaxWidth(780);
        titledPane.setCollapsible(true);
        titledPane.setExpanded(true);
        titledPane.setAnimated(false);
        titledPane.setText(Core.db.getUserName(post.getUser())  + "(" + post.getUser()+ ")" + " said:");
        titledPane.setPadding(new Insets(10,0,0,10));
        titledPane.setFont(Font.font("System", FontWeight.BOLD, 12));

        VBox vBox = new VBox();
        vBox.setMaxWidth(780);

        Label label1 = new Label();
        label1.setText(post.getText());
        label1.setPadding(new Insets(10,15,10,15));
        label1.setWrapText(true);

        vBox.getChildren().add(label1);

        titledPane.setContent(vBox);

        list.getChildren().add(titledPane);
    }
}
