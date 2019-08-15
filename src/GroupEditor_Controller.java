import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class GroupEditor_Controller implements Initializable {

    public static Group[] groups;

    public ArrayList<Group> newGroups = new ArrayList<>();

    @FXML
    private Label username_label;

    @FXML
    private Label userrole_label;

    @FXML
    private Label userid_label;
    @FXML
    private VBox list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username_label.setText(Core.getUser().getFirstname() + " " + Core.getUser().getSecondname());

        userrole_label.setText(Core.getUser().getUsertype().toUpperCase());

        userid_label.setText("User ID: " + Core.getUser().getId());

        groups = Core.db.getGroups();

        for(Group group : groups){
            addGroup(group);
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

    @FXML
    private void newGroupButtonPressed() {
        newGroups.add(new Group());
        newGroups.get(newGroups.size()-1).setId(Core.db.getLastGroupID());
        addGroup(newGroups.get(newGroups.size()-1));
        Core.db.addGroup(newGroups.get(newGroups.size()-1));
    }

    private void addGroup(Group group){
        TitledPane titledPane = new TitledPane();
        titledPane.setExpanded(false);
        titledPane.setPrefWidth(780);
        titledPane.setMaxWidth(780);
        titledPane.setCollapsible(true);
        titledPane.setExpanded(true);
        titledPane.setAnimated(false);
        titledPane.setText( group.getTitle());
        titledPane.setPadding(new Insets(10,0,0,10));
        titledPane.setFont(Font.font("System", FontWeight.BOLD, 12));

        VBox vBox = new VBox();
        vBox.setMaxWidth(780);
        vBox.setSpacing(10);

        Label label1 = new Label();
        Label label2 = new Label();
        label1.setText("Member Students: " + group.getStudentMembersAsOneString());
        label2.setText("Member Teachers: " + group.getTeacherMembersAsOneString());
        label2.setPadding(new Insets(10,15,0,15));
        label1.setPadding(new Insets(10,15,0,15));

        TextField textField0 = new TextField();
        textField0.setPromptText("Group Name");
        textField0.setMaxWidth(120);
        textField0.setText(group.getTitle());
        Button button0 = new Button();
        button0.setText("Set New Name");
        button0.setOnAction(event -> {
            try{
                group.setTitle(textField0.getText());
                Core.db.setGroupName(group.getId(), group.getTitle());
            }catch (Exception e){
                button0.setText("Error");
            }
        });

        TextField textField = new TextField();
        textField.setPromptText("User ID");
        textField.setMaxWidth(120);
        Button button1 = new Button();
        button1.setText("Add User");
        button1.setOnAction(event -> {
            try{
                Core.db.addUserToGroup(Integer.parseInt(textField.getText()), group.getId());
                group.addMember(Integer.parseInt(textField.getText()));
                label1.setText("Member Students: " + group.getStudentMembersAsOneString());
                label2.setText("Member Teachers: " + group.getTeacherMembersAsOneString());
            }catch (Exception e){
                button1.setText("Error");
            }
        });

        Button button2 = new Button();
        button2.setText("Remove User");
        button2.setOnAction(event -> {
            try{
                Core.db.removeUserFromGroup(Integer.parseInt(textField.getText()), group.getId());
                group.removeMember(Integer.parseInt(textField.getText()));
                label1.setText("Member Students: " + group.getStudentMembersAsOneString());
                label2.setText("Member Teachers: " + group.getTeacherMembersAsOneString());
            }catch (Exception e){
                button1.setText("Error");
            }
        });

        Button button3 = new Button();
        button3.setText("Remove Group");
        button3.setOnAction(event -> {
            try{
                Core.db.removeGroup(group.getId());
                for(int i : group.getMembers()){
                    Core.db.removeUserFromGroup(i, group.getId());
                }
                titledPane.setManaged(false);
            }catch (Exception e){
                button1.setText("Error");
            }
        });

        vBox.getChildren().add(textField0);
        vBox.getChildren().add(button0);
        vBox.getChildren().add(textField);
        vBox.getChildren().add(button1);
        vBox.getChildren().add(button2);
        vBox.getChildren().add(label1);
        vBox.getChildren().add(label2);
        vBox.getChildren().add(button3);

        titledPane.setContent(vBox);

        list.getChildren().add(titledPane);
    }
}
