import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AssessmentCreator_Controller implements Initializable {

    public static Assessment assessment;

    public static Task[] tasks;

    @FXML
    private Label username_label;

    @FXML
    private Label userrole_label;

    @FXML
    private Label userid_label;

    @FXML
    private VBox list;

    @FXML
    private TextField title_txt;

    @FXML
    private DatePicker date_picker;

    @FXML
    private ComboBox group_selector;

    @FXML
    private TextArea description_txt;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public AssessmentCreator_Controller(){
    }

    public static void init(Assessment assessment){
        AssessmentCreator_Controller.assessment = assessment;

        tasks = assessment.getTasks();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username_label.setText(Core.getUser().getFirstname() + " " + Core.getUser().getSecondname());
        userrole_label.setText(Core.getUser().getUsertype().toUpperCase());
        userid_label.setText("User ID: " + Core.getUser().getId());

        title_txt.setText(assessment.getTitle());
        date_picker.setValue(LocalDate.from(formatter.parse(assessment.getDeadline())));
        //TODO group selector
        description_txt.setText(assessment.getDesc());

        int n = 0;
        for(Task t : tasks) addTask(t, n++);
    }

    @FXML
    public void back_buttonPressed() {
        Core.loadScene(Core.MAIN, null);
    }

    @FXML
    private void onPublishButtonPressed(){

    }

    @FXML
    private void onSettingsButtonPressed(){
        Core.loadScene(Core.SETTINGS, null);
    }

    @FXML
    private void onSignOutButtonPressed(){
        Core.loadScene(Core.LOGIN, null);
    }

    private void addTask(Task t, int n){


        String title = "Task " + n;
        String collaborators = t.getCollaboratorsAsOneString();
        String description = t.getDescription();

        TitledPane titledPane = new TitledPane();
        titledPane.setPrefWidth(780);
        titledPane.setMaxWidth(780);
        titledPane.setCollapsible(true);
        titledPane.setExpanded(true);
        titledPane.setAnimated(false);
        titledPane.setText(title);
        titledPane.setPadding(new Insets(10,0,0,10));
        titledPane.setFont(Font.font("System", FontWeight.BOLD, 12));

        VBox vBox = new VBox();
        vBox.setMaxWidth(780);
        vBox.setSpacing(10);

        Label label1 = new Label();

        label1.setText("Collaborators: " + collaborators);
        label1.setPadding(new Insets(10,15,0,0));

        DatePicker datePicker = new DatePicker();
        LocalDate localDate = LocalDate.parse(t.getDeadline(), formatter);
        datePicker.setValue(localDate);

        TextArea field3 = new TextArea();
        field3.setPromptText("Task description");
        field3.setText(description);
        field3.setWrapText(true);

        Button button1 = new Button();
        button1.setText("Open");
        button1.setOnAction(event -> {
            Core.loadScene(Core.TASK_EDIT, new Object[]{t, assessment});
        });

        Button button2 = new Button();
        button2.setText("Save");
        button2.setOnAction(event -> {
            tasks[n] = new Task(t.getID(),t.getAssessment(),field3.getText(), datePicker.getValue().format(formatter), t.getCollaborators(), t.getCollaboratorsID());
        });

        vBox.getChildren().add(label1);
        vBox.getChildren().add(datePicker);
        vBox.getChildren().add(field3);
        vBox.getChildren().add(button1);

        titledPane.setContent(vBox);

        list.getChildren().add(titledPane);
    }

}


