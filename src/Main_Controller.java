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

public class Main_Controller implements Initializable {

    @FXML
    private Label username_label;

    @FXML
    private Label userrole_label;

    @FXML
    private Label userid_label;

    @FXML
    private Button newAssessment_btn;

    @FXML
    private VBox list;

    @FXML
    private Button groupEditorButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username_label.setText(Core.getUser().getFirstname() + " " + Core.getUser().getSecondname());

        userrole_label.setText(Core.getUser().getUsertype().toUpperCase());

        if(Core.getUser().getUsertype().equals("student")){
            newAssessment_btn.setManaged(false);
        }
        if(Core.getUser().getUsertype().equals("student") || Core.getUser().getUsertype().equals("teacher")){
            groupEditorButton.setManaged(false);
        }

        userid_label.setText("User ID: " + Core.getUser().getId());

        Core.getUser().reloadAssessments();

        for(Assessment asmt : Core.getUser().getAssessments()){
            addAssessment(asmt);
        }
    }

    @FXML
    private void groupEditorButtonPressed(){
        Core.loadScene(Core.GROUPS, null);
    }

    @FXML
    private void newAssessmentButtonpressed(){
        Core.loadScene(Core.ASMT_EDIT, new Object[]{new Assessment()});
    }

    @FXML
    private void onSettingsButtonPressed(){
        Core.loadScene(Core.SETTINGS, null);
    }

    @FXML
    private void onSignOutButtonPressed(){
        Core.loadScene(Core.LOGIN, null);
    }

    private void addAssessment(Assessment asmt){

        String title = asmt.getTitle();
        String supervisor = Core.db.getUser(asmt.getSupervisor()).getFullname();
        String collaborators = asmt.getCollaboratorsAsOneString();
        String deadline = asmt.getDeadline();
        String description = asmt.getDesc();

        TitledPane titledPane = new TitledPane();
        titledPane.setPrefWidth(780);
        titledPane.setExpanded(false);
        titledPane.setMaxWidth(780);
        titledPane.setCollapsible(true);
        titledPane.setExpanded(true);
        titledPane.setAnimated(false);
        titledPane.setText(title + (Core.getUser().getUsertype().equals("teacher") || Core.getUser().getUsertype().equals("admin")?"":(" - " +supervisor)));
        titledPane.setPadding(new Insets(10,0,0,10));
        titledPane.setFont(Font.font("System", FontWeight.BOLD, 12));

        VBox vBox = new VBox();
        vBox.setMaxWidth(780);

        Label label1 = new Label();

        label1.setText("Collaborators: " + collaborators);
        label1.setPadding(new Insets(10,15,0,15));

        Label label2 = new Label();
        label2.setText("Deadline: " + deadline);
        label2.setPadding(new Insets(10,15,0,15));

        Label label3 = new Label();
        label3.setText("Description: " + description);
        label3.setPadding(new Insets(10,15,10,15));
        label3.setWrapText(true);

        Button button1 = new Button();
        button1.setText("Open");
        button1.setOnAction(event -> {
            if(!(Core.getUser().getUsertype().equals("teacher") || Core.getUser().getUsertype().equals("admin"))) Core.loadScene(Core.ASMT, new Object[]{asmt});
            else Core.loadScene(Core.ASMT_EDIT, new Object[]{asmt});
        });

        vBox.getChildren().add(label1);
        vBox.getChildren().add(label2);
        vBox.getChildren().add(label3);
        vBox.getChildren().add(button1);

        titledPane.setContent(vBox);

        list.getChildren().add(titledPane);
    }
}
