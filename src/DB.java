import java.sql.*;
import java.util.ArrayList;

public class DB {

    static Connection conn = null;

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
            conn = DriverManager.getConnection("jdbc:mysql://localhost/main?" +
                    "user=root&password=h8J2Kws345ON3");

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public Assessment[] getAssessmentsByUser(int id) {
        ArrayList<Assessment> assessments = new ArrayList<>();
        ResultSet rs = executeQuery("SELECT assessment FROM part_assessment WHERE user = " + id + ";");
        try {
            while (rs.next()) {
                ResultSet rs2 = executeQuery("SELECT * FROM assessment WHERE id = " + rs.getInt("assessment"));

                try {
                    while (rs2.next()) {
                        String[] collaborators = getAsNames(getAssessmentCollaborators(rs2.getInt("id")));
                        assessments.add(new Assessment(rs2.getInt("id"), rs2.getInt("supervisor"), rs2.getString("title"), rs2.getString("desc"), rs2.getString("created"), rs2.getString("deadline"), getGroup(rs2.getInt("group")).getTitle(), collaborators));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        try {
            while (rs.next()) {
                assessment = (new Assessment(rs.getInt("id"), rs.getInt("supervisor"), rs.getString("title"), rs.getString("desc"), rs.getString("created"), rs.getString("deadline"), getGroup(rs.getInt("group")).getTitle(), collaborators));
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
                    tasks.add(new Task(rs.getInt("id"), rs.getInt("assessment"), rs.getString("description"), rs.getString("deadline"), collaborators, collaboratorsID));
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
                return new User(rs.getInt("id"), rs.getString("email"), rs.getString("firstname"), rs.getString("secondname"), rs.getString("usertype"));
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

    public Group getGroup(int id) {
        ResultSet rs = executeQuery("SELECT * FROM `group` WHERE id = " + id + ";");
        try {
            while (rs.next()) {
                return new Group(rs.getInt("id"), rs.getString("title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public void executeStetment(String sql) {
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
        ResultSet rs = executeQuery("SELECT id, email, firstname, secondname, usertype FROM user WHERE email = '" + username + "' AND password = '" + password + "';");

        try {
            if (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("email"), rs.getString("firstname"), rs.getString("secondname"), rs.getString("usertype"));
                close(rs);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        close(rs);

        return null;
    }

    public void addCollaborator(int task, int user){
        executeStetment("INSERT INTO `main`.`part_task` (`user`, `task`) VALUES ('" + user + "', '" + task + "`);");
    }

    public void addPost(int task, int user, String text){
        executeStetment("INSERT INTO `main`.`post` (`task`, `user`, `text`) VALUES ('" + task + "', '" + user + "', '" + escapeStringForMySQL(text) + "');");
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
        return s.replaceAll("\b","\\b")
                .replaceAll("\n","\\n")
                .replaceAll("\r", "\\r")
                .replaceAll("\t", "\\t")
                .replaceAll("\\x1A", "\\Z")
                .replaceAll("\\x00", "\\0")
                .replaceAll("'", "\\'")
                .replaceAll("\\'", "\\\\'")
                .replaceAll("\\\"", "\\\\\"")
                .replaceAll("%", "\\%")
                .replaceAll("_","\\_");

    }
}
