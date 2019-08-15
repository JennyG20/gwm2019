import javafx.collections.FXCollections;
import javafx.event.Event;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AssessmentCreator_Controller implements Initializable {

    public static Assessment assessment;

    public static ArrayList<Task> tasks = new ArrayList<>();

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

    @FXML
    private Button publish_btn;

    @FXML
    private Button newTaskButton;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int taskNum = 0;

    public AssessmentCreator_Controller() {
    }

    public static void init(Assessment assessment) {
        AssessmentCreator_Controller.assessment = assessment;
        Task[] tempTasks = assessment.getTasks();
        tasks = new ArrayList<>();
        for (Task t : tempTasks) tasks.add(t);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (assessment == null) assessment = new Assessment();

        username_label.setText(Core.getUser().getFirstname() + " " + Core.getUser().getSecondname());
        userrole_label.setText(Core.getUser().getUsertype().toUpperCase());
        userid_label.setText("User ID: " + Core.getUser().getId());

        title_txt.setText(assessment.getTitle());
        date_picker.setValue(LocalDate.from(formatter.parse(assessment.getDeadline())));
        group_selector.setItems(FXCollections.observableArrayList());
        for (Group g : Core.db.getGroups()) group_selector.getItems().add(g.getTitle());
        group_selector.getSelectionModel().select(assessment.getGroup());
        group_selector.setOnAction((Event ev) -> assessment.setGroup(group_selector.getSelectionModel().getSelectedIndex() + 1));
        description_txt.setText(assessment.getDesc());

        for (Task t : tasks) addTask(t);
    }

    @FXML
    public void back_buttonPressed() {
        Core.loadScene(Core.MAIN, null);
    }

    @FXML
    public void newTaskButtonPressed() {
        tasks.add(new Task(assessment.getId()));
        addTask(tasks.get(tasks.size() - 1));
        tasks.get(tasks.size() - 1).setAssessment(assessment.getId());
    }

    @FXML
    private void onPublishButtonPressed() {
        if (title_txt.getText().equals("") ||
                formatter.format(date_picker.getValue()).equals("") ||
                description_txt.getText().equals("")) {

            publish_btn.setText("Empty Fields!");
            return;
        }

        try {
            assessment.setGroup(Core.db.getGroupByName(((String) group_selector.getSelectionModel().getSelectedItem())));
        } catch (Exception e) {
            publish_btn.setText("Select Group!");
            return;
        }

        try {
            assessment.setTitle(title_txt.getText());
            assessment.setDeadline(formatter.format(date_picker.getValue()));
            assessment.setDesc(description_txt.getText());
            assessment.setCreated(formatter.format(LocalDate.now()));
            assessment.setGroup(Core.db.getGroupByName(((String) group_selector.getSelectionModel().getSelectedItem())));
            assessment.setCollaboratorsID(Core.db.getGroup(assessment.getGroup()).getMembers());
            assessment.setSupervisor(Core.getUser().getId());
        } catch (Exception e) {
            publish_btn.setText("Error!");
            return;
        }

        if (assessment.getId() == -1) {
            Core.db.addAssessment(assessment);
            for (Task t : tasks) {
                Core.db.addTask(t);
            }
        } else {
            Core.db.updateAssessment(assessment);
            for (Task t : tasks) {
                t.setAssessment(assessment.getId());
                if (t.getID() == -1) Core.db.addTask(t);
                else Core.db.updateTask(t);
            }
        }

        publish_btn.setText("Published");
        publish_btn.setDisable(true);
        newTaskButton.setDisable(true);
    }

    @FXML
    private void onSettingsButtonPressed() {
        Core.loadScene(Core.SETTINGS, null);
    }

    @FXML
    private void onSignOutButtonPressed() {
        Core.loadScene(Core.LOGIN, null);
    }

    private void addTask(Task t) {
        taskNum++;
        String title = "Task " + taskNum;
        String collaborators = t.getCollaboratorsAsOneString();
        String description = t.getDescription();

        TitledPane titledPane = new TitledPane();
        titledPane.setExpanded(false);
        titledPane.setPrefWidth(780);
        titledPane.setMaxWidth(780);
        titledPane.setCollapsible(true);
        titledPane.setExpanded(true);
        titledPane.setAnimated(false);
        titledPane.setText(title);
        titledPane.setPadding(new Insets(10, 0, 0, 10));
        titledPane.setFont(Font.font("System", FontWeight.BOLD, 12));

        VBox vBox = new VBox();
        vBox.setMaxWidth(780);
        vBox.setSpacing(10);

        Label label1 = new Label();

        label1.setText("Collaborators: " + collaborators);
        label1.setPadding(new Insets(10, 15, 0, 0));

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
        button2.setText("Apply");
        button2.setOnAction(event -> {
            try {
                tasks.set(taskNum - 1, new Task(t.getID(), t.getAssessment(), field3.getText(), datePicker.getValue().format(formatter), t.getCollaborators(), t.getCollaboratorsID()));
            } catch (Exception e) {

            } finally {
                button2.setText("Applied");
            }
        });

        vBox.getChildren().add(label1);
        vBox.getChildren().add(datePicker);
        vBox.getChildren().add(field3);
        vBox.getChildren().add(button1);
        vBox.getChildren().add(button2);
        titledPane.setContent(vBox);

        list.getChildren().add(titledPane);
    }

}


