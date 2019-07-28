public class User {

    private int id;
    private String email, firstname, secondname, usertype;

    private Assessment[] assessments;

    public String getFullname(){
        return firstname + " " + secondname;
    }

    public User(int id, String email, String firstname, String secondname, String usertype) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.secondname = secondname;
        this.usertype = usertype;
        this.assessments = Core.db.getAssessmentsByUser(id);
    }

    public Assessment[] getAssessments() {
        return assessments;
    }

    public void setAssessments(Assessment[] assessments) {
        this.assessments = assessments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
