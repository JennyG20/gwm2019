import java.sql.*;
import java.util.ArrayList;

public class DB {

    public static String connURL = "jdbc:mysql://localhost/main?" +
            "user=root&password=h8J2Kws345ON3";
    public static Connection conn = null;

    public DB() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(connURL);

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public Assessment[] getAssessmentsByUser(int id) {
        ArrayList<Assessment> assessments = new ArrayList<>();
        ResultSet rs;

        if (getUserType(id).equals("student")) {
            rs = executeQuery("SELECT assessment FROM part_assessment WHERE user = " + id + ";");
        } else if (getUserType(id).equals("teacher")) {
            rs = executeQuery("SELECT * FROM assessment WHERE supervisor = " + id + ";");
        } else {
            rs = executeQuery("SELECT * FROM assessment");
        }

        try {
            while (rs.next()) {
                ResultSet rs2;
                if (getUserType(id).equals("student")) {
                    rs2 = executeQuery("SELECT * FROM assessment WHERE id = " + rs.getInt("assessment"));
                    try {
                        while (rs2.next()) {
                            String[] collaborators = getAsNames(getAssessmentCollaborators(rs2.getInt("id")));
                            Integer[] collaboratorsID = getAssessmentCollaborators(rs2.getInt("id"));
                            assessments.add(
                                    new Assessment(
                                            rs2.getInt("id"),
                                            rs2.getInt("supervisor"),
                                            rs2.getString("title"),
                                            rs2.getString("desc"),
                                            rs2.getString("created"),
                                            rs2.getString("deadline"),
                                            getGroup(rs2.getInt("usergroup")).getId(),
                                            collaborators,
                                            collaboratorsID));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String[] collaborators = getAsNames(getAssessmentCollaborators(rs.getInt("id")));
                    Integer[] collaboratorsID = getAssessmentCollaborators(rs.getInt("id"));
                    assessments.add(new Assessment(rs.getInt("id"), rs.getInt("supervisor"),
                            rs.getString("title"), rs.getString("desc"), rs.getString("created"),
                            rs.getString("deadline"), getGroup(rs.getInt("usergroup")).getId(), collaborators, collaboratorsID));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return assessments.toArray(new Assessment[]{});
    }

    public Integer[] getAssessmentCollaborators(int id) {
        ArrayList<Integer> collaborators = new ArrayList<>();
        ResultSet rs = executeQuery("SELECT user FROM part_assessment WHERE assessment = " + id);
        try {
            while (rs.next()) {
                collaborators.add(rs.getInt("user"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collaborators.toArray(new Integer[]{});
    }

    public Integer[] getTaskCollaborators(int id) {
        ArrayList<Integer> collaborators = new ArrayList<>();
        ResultSet rs = executeQuery("SELECT user FROM part_task WHERE task = " + id);
        try {
            while (rs.next()) {
                collaborators.add(rs.getInt("user"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collaborators.toArray(new Integer[]{});
    }

    public String[] getAsNames(Integer[] ids) {
        ArrayList<String> names = new ArrayList<>();
        for (int id : ids) {
            names.add(getUserName(id));
        }
        return names.toArray(new String[]{});
    }

    public Assessment getAssessmentById(int id) {
        Assessment assessment = null;

        ResultSet rs = executeQuery("SELECT * FROM assessment WHERE id = " + id);

        String[] collaborators = new String[]{};
        Integer[] collaboratorsID = new Integer[]{};
        try {
            while (rs.next()) {
                assessment = (new Assessment(rs.getInt("id"), rs.getInt("supervisor"),
                        rs.getString("title"), rs.getString("desc"), rs.getString("created"),
                        rs.getString("deadline"), getGroup(rs.getInt("usergroup")).getId(), collaborators, collaboratorsID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assessment;
    }

    public Task[] getTasks(int id) {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            ResultSet rs = executeQuery("SELECT * FROM task WHERE assessment = " + id);

            try {
                while (rs.next()) {
                    String[] collaborators = getAsNames(getTaskCollaborators(rs.getInt("id")));
                    Integer[] collaboratorsID = getTaskCollaborators(rs.getInt("id"));
                    tasks.add(new Task(rs.getInt("id"), rs.getInt("assessment"), rs.getString("description"),
                            rs.getString("deadline"), collaborators, collaboratorsID));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks.toArray(new Task[]{});
    }

    public Post[] getPosts(int id) {
        ArrayList<Post> posts = new ArrayList<>();
        try {
            ResultSet rs = executeQuery("SELECT * FROM post WHERE task = " + id);
            try {
                while (rs.next()) {
                    posts.add(new Post(rs.getInt("id"), rs.getInt("user"), rs.getInt("task"), rs.getString("text")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts.toArray(new Post[]{});
    }

    public User getUser(int id) {
        ResultSet rs = executeQuery("SELECT id, firstname, secondname, email, usertype FROM user WHERE id = " + id + ";");
        try {
            while (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("email"), rs.getString("firstname"),
                        rs.getString("secondname"), rs.getString("usertype"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserName(int id) {
        ResultSet rs = executeQuery("SELECT firstname, secondname FROM user WHERE id = " + id + ";");
        try {
            while (rs.next()) {
                return rs.getString("firstname") + " " + rs.getString("secondname");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserType(int id) {
        ResultSet rs = executeQuery("SELECT usertype FROM user WHERE id = " + id + ";");
        try {
            while (rs.next()) {
                return rs.getString("usertype");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Group[] getGroups() {
        ArrayList<Group> groups = new ArrayList<>();
        try {
            ResultSet rs = executeQuery("SELECT * FROM usergroup;");
            try {
                while (rs.next()) {
                    ArrayList<Integer> users = new ArrayList<>();
                    ResultSet rs2 = executeQuery("SELECT user FROM part_group WHERE usergroup = " + rs.getInt("id"));
                    try {
                        while (rs2.next()) {
                            users.add(rs2.getInt("user"));
                        }
                    } catch (Exception e) {
                    }
                    groups.add(new Group(rs.getInt("id"), rs.getString("title"), users));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups.toArray(new Group[]{});
    }

    public Group getGroup(int id) {
        Group group = null;
        try {
            ResultSet rs = executeQuery("SELECT * FROM usergroup WHERE id = " + id + ";");
            try {
                while (rs.next()) {
                    ArrayList<Integer> users = new ArrayList<>();
                    ResultSet rs2 = executeQuery("SELECT user FROM part_group WHERE usergroup = " + rs.getInt("id"));
                    try {
                        while (rs2.next()) {
                            users.add(rs2.getInt("user"));
                        }
                    } catch (Exception e) {
                    }
                    group = new Group(rs.getInt("id"), rs.getString("title"), users);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return group;
    }

    public ResultSet executeQuery(String sql) {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            if (stmt.execute(sql)) {
                rs = stmt.getResultSet();
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            return rs;
        }
    }

    public void executeStatetment(String sql) {
        Statement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.execute(sql);

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public User login(String username, String password) {
        ResultSet rs = executeQuery("SELECT id, email, firstname, secondname, usertype FROM user WHERE email = '" +
                username + "' AND password = '" + password + "';");

        try {
            if (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("email"), rs.getString("firstname"),
                        rs.getString("secondname"), rs.getString("usertype"));
                close(rs);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        close(rs);

        return null;
    }

    public void addAssessment(Assessment asmt) {
        executeStatetment("INSERT INTO `main`.`assessment` (`supervisor`, `desc`, `created`, `deadline`, `title`, `usergroup`) VALUES ('" +
                asmt.getSupervisor() + "', '" + asmt.getDesc() + "', '" + asmt.getCreated() + "', '" + asmt.getDeadline()
                + "', '" + asmt.getTitle() + "', '" + asmt.getGroup() + "');");

        for (Integer i : asmt.getCollaboratorsID()) addAssessmentCollaborator(getLastAssessmentId(), i);
    }

    public int getLastAssessmentId() {
        ResultSet rs = executeQuery("SELECT MAX(id) as maxId FROM assessment");
        try {
            while (rs.next()) {
                return rs.getInt("maxId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateAssessment(Assessment asmt) {
        executeStatetment("UPDATE `main`.`assessment` SET `supervisor` = '" + asmt.getSupervisor() + "', `desc` = '" + asmt.getDesc()
                + "', `created` = '" + asmt.getCreated() + "', `deadline` = '" + asmt.getDeadline() + "', `title` = '"
                + asmt.getTitle() + "', `usergroup` = '" + asmt.getGroup() + "' WHERE (`id` = '" + asmt.getId() + "');");

        for (Integer i : asmt.getCollaboratorsID()) {
            try {
                ResultSet rs = executeQuery("SELECT MAX(id) FROM assessment;");
                rs.next();
                addAssessmentCollaborator(rs.getInt(1), i);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTask(Task task) {
        if(task.getAssessment() == -1){
            try {
                ResultSet rs = executeQuery("SELECT MAX(id) FROM assessment;");
                rs.next();
                task.setAssessment(rs.getInt(1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        executeStatetment(" INSERT INTO `main`.`task` (`description`, `deadline`, `assessment`) VALUES ('"
                + task.getDescription() + "', '" + task.getDeadline() + "', '" + task.getAssessment() + "');");
    }

    public void updateTask(Task task) {
        executeStatetment("UPDATE `main`.`task` SET `description` = '" + task.getDescription() + "', `deadline` = '"
                + task.getDeadline() + "', `assessment` = '" + task.getAssessment() + "' WHERE (`id` = '" + task.getID() + "');");
    }

    public void addTaskCollaborator(int task, int user) {
        if(!isTaskCollaborator(task, user))executeStatetment("INSERT INTO `main`.`part_task` (`user`, `task`) VALUES ('" + user + "', '" + task + "');");
    }

    public boolean isTaskCollaborator(int task, int user) {
        ResultSet rs = executeQuery("SELECT id FROM part_task WHERE task = '" + task + "' AND user = '" + user + "';");
        try {
            while (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAssessmentCollaborator(int assessment, int user) {
        ResultSet rs = executeQuery("SELECT id FROM part_assessment WHERE assessment = '" + assessment + "' AND user = '" + user + "';");
        try {
            while (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addAssessmentCollaborator(int assessment, int user) {
        if(!isAssessmentCollaborator(assessment, user))executeStatetment("INSERT INTO `main`.`part_assessment` (`user`, `assessment`) VALUES ('" + user + "', '" + assessment + "');");
    }

    public void addPost(int task, int user, String text) {
        executeStatetment("INSERT INTO `main`.`post` (`task`, `user`, `text`) VALUES ('" + task + "', '" + user + "', '" + escapeStringForMySQL(text) + "');");
    }

    public static void close(ResultSet rs) {

        try {
            Statement st = rs.getStatement();
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String escapeStringForMySQL(String s) {
        return s.replaceAll("\b", "\\b")
                .replaceAll("\n", "\\n")
                .replaceAll("\r", "\\r")
                .replaceAll("\t", "\\t")
                .replaceAll("\\x1A", "\\Z")
                .replaceAll("\\x00", "\\0")
                .replaceAll("'", "\\'")
                .replaceAll("\\'", "\\\\'")
                .replaceAll("\\\"", "\\\\\"")
                .replaceAll("%", "\\%")
                .replaceAll("_", "\\_");

    }

    public void addGroup(Group group) {
        executeStatetment("INSERT INTO `main`. `usergroup` (`title`) VALUES('" + group.getTitle() + "');");
    }

    public void addUserToGroup(int user, int group) {
        executeStatetment("INSERT INTO `main`. `part_group` (`usergroup`, `user`)VALUES('" + group + " ', ' " + user + "');");
    }

    public void removeUserFromGroup(int user, int group) {
        executeStatetment("DELETE FROM `main`. `part_group` WHERE(`user` = '" + user + "' AND `usergroup` = '" + group + "');");
    }

    public void removeGroup(int id) {
        executeStatetment("DELETE FROM `main`. `usergroup` WHERE(`id` = '" + id + "');");
    }

    public void setGroupName(int id, String title) {
        executeStatetment("UPDATE `main`.`usergroup` SET `title` = '" + title + "' WHERE (`id` = '" + id + "');");
    }

    public int getLastGroupID() {
        ResultSet rs = executeQuery("SELECT MAX(id) as maxId FROM usergroup");
        try {
            while (rs.next()) {
                return rs.getInt("maxId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getGroupByName(String name) {
        ResultSet rs = executeQuery("SELECT `id` FROM `main`.`usergroup` WHERE `title` = '" + name + "';");
        try {
            while (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
