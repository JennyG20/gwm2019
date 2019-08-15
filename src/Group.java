import java.util.ArrayList;

public class Group {
    private int id;
    private String title;
    private ArrayList<Integer> members;

    public Group(int id, String title, ArrayList<Integer> members) {
        this.id = id;
        this.title = title;
        this.members = members;
    }

    public Group() {
        this.id = -1;
        this.title = "";
        this.members = new ArrayList<Integer>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Integer> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Integer> members) {
        this.members = members;
    }

    public void addMember(int id){
        this.members.add(id);
    }

    public String getStudentMembersAsOneString() {
        String membersString = "";
        for (Integer i : members) {
            if(Core.db.getUserType(i).equals("student")) {
                membersString += Core.db.getUserName(i) + "(" + i + "), ";
            }
        }
        if(membersString.length() > 3) return membersString.substring(0, membersString.length()-2);
        else return "";
    }

    public String getTeacherMembersAsOneString() {
        String membersString = "";
        for (Integer i : members) {
            if(!Core.db.getUserType(i).equals("student")) {
                membersString += Core.db.getUserName(i) + "(" + i + "), ";
            }
        }
        if(membersString.length() > 3) return membersString.substring(0, membersString.length()-2);
        else return "";
    }

    public void removeMember(int member) {
        for(int i = 0; i < members.size(); i++){
            if(members.get(i) == member) members.remove(i);
        }
    }
}
