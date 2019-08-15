import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {

    private int ID, assessment;
    private String description, deadline;
    private String[] collaborators;
    private Integer[] collaboratorsID;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Task(int ID, int assessment, String description, String deadline, String[] collaborators, Integer[] collaboratorsID) {
        this.ID = ID;
        this.assessment = assessment;
        this.description = description;
        this.deadline = deadline;
        this.collaborators = collaborators;
        this.collaboratorsID = collaboratorsID;
    }

    public Task(int assessment) {
        this.ID = -1;
        this.assessment = assessment;
        this.description = "";
        this.deadline = formatter.format(LocalDate.now());
        this.collaborators = new String[0];
        this.collaboratorsID = new Integer[0];
    }

    public String getCollaboratorsAsOneString() {
        String collaboratorString = "";
        for (String s : collaborators) {
            collaboratorString += s + ", ";
        }
        if(collaboratorString.length()==0) return "No collaborators";

        return collaboratorString.substring(0, collaboratorString.length()-2);
    }

    public Post[] getPosts() {
        return Core.db.getPosts(ID);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAssessment() {
        return assessment;
    }

    public void setAssessment(int assessment) {
        this.assessment = assessment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String[] getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(String[] collaborators) {
        this.collaborators = collaborators;
    }

    public Integer[] getCollaboratorsID() {
        return collaboratorsID;
    }

    public void setCollaboratorsID(Integer[] collaboratorsID) {
        this.collaboratorsID = collaboratorsID;
    }
}
