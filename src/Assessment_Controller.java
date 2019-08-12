import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ResourceBundle;


public class Assessment_Controller implements Initializable {

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
    private VBox list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username_label.setText(Core.getUser().getFirstname() + " " + Core.getUser().getSecondname());

        userrole_label.setText(Core.getUser().getUsertype().toUpperCase());

        userid_label.setText("User ID: " + Core.getUser().getId());

        title_label.setText(assessment.getTitle());

        description_label.setText("Assessment description: " + assessment.getDesc());

        supervisor_label.setText("Supervised by: " + Core.db.getUserName(assessment.getSupervisor()));

        collaborators_label.setText("Collaborators: " + assessment.getCollaboratorsAsOneString());

        deadline_label.setText("Deadline: " + assessment.getDeadline());

        int k = 0;
        for(Task task : assessment.getTasks()){
            k++;
            addTask(task, k);
        }
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

    private void addTask(Task task, int num){
        TitledPane titledPane = new TitledPane();
        titledPane.setPrefWidth(780);
        titledPane.setMaxWidth(780);
        titledPane.setCollapsible(true);
        titledPane.setExpanded(true);
        titledPane.setAnimated(false);
        titledPane.setText("Task " + num);
        titledPane.setPadding(new Insets(10,0,0,10));
        titledPane.setFont(Font.font("System", FontWeight.BOLD, 12));

        VBox vBox = new VBox();
        vBox.setMaxWidth(780);

        Label label1 = new Label();

        label1.setText("Collaborators: " + task.getCollaboratorsAsOneString());
        label1.setPadding(new Insets(10,15,0,15));

        Label label2 = new Label();
        label2.setText("Deadline: " + task.getDeadline());
        label2.setPadding(new Insets(10,15,0,15));

        Label label3 = new Label();
        label3.setText("Description: " + task.getDescription());
        label3.setPadding(new Insets(10,15,10,15));
        label3.setWrapText(true);

        Button button1 = new Button();
        button1.setText("Open");
        button1.setOnAction(event -> {
            Core.loadScene(Core.TASK, new Object[]{task, assessment});
        });

        boolean alreadyCollaborator = false;

        Button button2 = new Button();

        for(int i : task.getCollaboratorsID()){
            if(Core.getUser().getId() == i){
                alreadyCollaborator = true;
            }
        }

        if(alreadyCollaborator){
            button2.setDisable(true);
            button2.setText("Already Collaborator");
        } else {
            button2.setText("Add As Collaborator");
            button2.setOnAction(event -> {
                Core.db.addCollaborator(task.getID(), Core.getUser().getId());
                button2.setDisable(true);
                button2.setText("Already Collaborator");
            });
        }

        vBox.getChildren().add(button2);
        vBox.getChildren().add(button1);
        vBox.getChildren().add(label1);
        vBox.getChildren().add(label2);
        vBox.getChildren().add(label3);

        titledPane.setContent(vBox);

        list.getChildren().add(titledPane);
    }
}
