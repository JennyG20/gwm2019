public class Assessment {
    private int id, supervisor;
    private String title, desc, created, deadline, group;
    private String[] collaborators;

    public Assessment(int id, int supervisor, String title, String desc, String created, String deadline, String group, String[] collaborators) {
        this.id = id;
        this.supervisor = supervisor;
        this.title = title;
        this.desc = desc;
        this.created = created;
        this.deadline = deadline;
        this.group = group;
        this.collaborators = collaborators;
    }

    public String getCollaboratorsAsOneString() {
        String collaboratorString = "";
        for (String s : collaborators) {
            collaboratorString += s + ", ";
        }

        return collaboratorString.substring(0, collaboratorString.length()-3);
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String[] getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(String[] collaborators) {
        this.collaborators = collaborators;
    }
}
