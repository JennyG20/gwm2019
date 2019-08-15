import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Assessment {
    private int id, supervisor, group;
    private String title, desc, created, deadline;
    private ArrayList<String> collaborators;
    private ArrayList<Integer> collaboratorsID;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Assessment(int id, int supervisor, String title, String desc, String created, String deadline, int group, ArrayList<String> collaborators, ArrayList<Integer> collaboratorsID) {
        this.id = id;
        this.supervisor = supervisor;
        this.title = title;
        this.desc = desc;
        this.created = created;
        this.deadline = deadline;
        this.group = group;
        this.collaborators = collaborators;
        this.collaboratorsID = collaboratorsID;
    }

    public Assessment(int id, int supervisor, String title, String desc, String created, String deadline, int group, String[] collaborators, Integer[] collaboratorsID) {
        this.id = id;
        this.supervisor = supervisor;
        this.title = title;
        this.desc = desc;
        this.created = created;
        this.deadline = deadline;
        this.group = group;
        this.collaborators = new ArrayList<String>();
        this.collaboratorsID = new ArrayList<Integer>();
        for(String s : collaborators) this.collaborators.add(s);
        for(Integer i : collaboratorsID) this.collaboratorsID.add(i);
    }

    public Assessment() {
        this.id = -1;
        this.supervisor = -1;
        this.title = "";
        this.desc = "";
        this.created = "";
        this.deadline = formatter.format(LocalDate.now());
        this.group = 1;
        this.collaborators = new ArrayList<String>();
        this.collaboratorsID = new ArrayList<Integer>();
    }


    public String getCollaboratorsAsOneString() {
        String collaboratorString = "";
        for (String s : collaborators) {
            collaboratorString += s + ", ";
        }
        if(collaboratorString.length() > 3) return collaboratorString.substring(0, collaboratorString.length()-2);
        else return "";
    }

    public Task[] getTasks() {
        return Core.db.getTasks(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(int supervisor) {
        this.supervisor = supervisor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public ArrayList<String> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(ArrayList<String> collaborators) {
        this.collaborators = collaborators;
    }

    public ArrayList<Integer> getCollaboratorsID() {
        return collaboratorsID;
    }

    public void setCollaboratorsID(ArrayList<Integer> collaboratorsID) {
        this.collaboratorsID = collaboratorsID;
    }
}
