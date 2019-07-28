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
        ResultSet rs = executeStatement("SELECT assessment FROM part_assessment WHERE user = " + id + ";");
        try {
            while (rs.next()) {
                ResultSet rs2 = executeStatement("SELECT * FROM assessment WHERE id = " + rs.getInt("assessment"));

                try {
                    while (rs2.next()) {
                        String[] collaborators = getAsNames(getAssessmentCollaborators(rs2.getInt("id")));
                        assessments.add(new Assessment(rs2.getInt("id"), rs2.getInt("supervisor"), rs2.getString("title"), rs2.getString("desc"), rs2.getString("created"), rs2.getString("deadline"), getGroup(rs2.getInt("group")).getTitle(), collaborators));
                    }
                } catch (Exception e) {
                    System.out.println("No More Assessments : " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("No Assessments : " + e.getMessage());
        }
        return assessments.toArray(new Assessment[]{});
    }

    public Integer[] getAssessmentCollaborators(int id) {
        ArrayList<Integer> collaborators = new ArrayList<>();
        ResultSet rs = executeStatement("SELECT user FROM part_assessment WHERE assessment = " + id);
        try {
            while (rs.next()) {
                collaborators.add(rs.getInt("user"));
            }
        } catch (Exception e) {
            System.out.println("No Collaborators : " + e.getMessage());
        }
        return collaborators.toArray(new Integer[]{});
    }

    public Integer[] getTaskCollaborators(int id) {
        ArrayList<Integer> collaborators = new ArrayList<>();
        ResultSet rs = executeStatement("SELECT user FROM part_task WHERE task = " + id);
        try {
            while (rs.next()) {
                collaborators.add(rs.getInt("user"));
            }
        } catch (Exception e) {
            System.out.println("No Collaborators : " + e.getMessage());
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
        ResultSet rs = executeStatement("SELECT * FROM assessment WHERE id = " + id);

        String[] collaborators = new String[]{};
        try {
            while (rs.next()) {
                assessment = (new Assessment(rs.getInt("id"), rs.getInt("supervisor"), rs.getString("title"), rs.getString("desc"), rs.getString("created"), rs.getString("deadline"), getGroup(rs.getInt("group")).getTitle(), collaborators));
            }
        } catch (Exception e) {
            System.out.println("No More Assessments : " + e.getMessage());
        }

        return assessment;
    }

    public Task[] getTasks(int id) {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            ResultSet rs = executeStatement("SELECT * FROM task WHERE assessment = " + id);

            try {
                while (rs.next()) {
                    String[] collaborators = getAsNames(getTaskCollaborators(rs.getInt("id")));
                    tasks.add(new Task(rs.getInt("id"), rs.getInt("assessment"), rs.getString("description"), rs.getString("deadline"), collaborators));
                }
            } catch (Exception e) {
                System.out.println("No More Assessments : " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("No Assessments : " + e.getMessage());
        }
        return tasks.toArray(new Task[]{});
    }

    public User getUser(int id) {
        ResultSet rs = executeStatement("SELECT id, firstname, secondname, email, usertype FROM user WHERE id = " + id + ";");
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
        ResultSet rs = executeStatement("SELECT firstname, secondname FROM user WHERE id = " + id + ";");
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
        ResultSet rs = executeStatement("SELECT * FROM `group` WHERE id = " + id + ";");
        try {
            while (rs.next()) {
                return new Group(rs.getInt("id"), rs.getString("title"));
            }
        } catch (Exception e) {
            System.out.println("No Groups : " + e.getMessage());
        }
        return null;
    }

    public ResultSet executeStatement(String sql) {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (stmt.execute(sql)) {
                rs = stmt.getResultSet();
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            return rs;
        }
    }

    public User login(String username, String password) {
        ResultSet rs = executeStatement("SELECT id, email, firstname, secondname, usertype FROM user WHERE email = '" + username + "' AND password = '" + password + "';");

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

    public static void close(ResultSet rs) {

        try {
            Statement st = rs.getStatement();
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
