public class Task {

    private int ID, assessment;
    private String description, deadline;
    private String[] collaborators;

    public Task(int ID, int assessment, String description, String deadline, String[] collaborators) {
        this.ID = ID;
        this.assessment = assessment;
        this.description = description;
        this.deadline = deadline;
        this.collaborators = collaborators;
    }

    public String getCollaboratorsAsOneString() {
        String collaboratorString = "";
        for (String s : collaborators) {
            collaboratorString += s + ", ";
        }

        return collaboratorString.substring(0, collaboratorString.length()-3);
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
}
