package server.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import utility.Log;
import utility.Record;
import utility.User;

public class DatabaseHandler {

    private String url;
    Connection conn;

    public DatabaseHandler(String url) {
        this.url = "jdbc:sqlite:" + url;
        this.conn = null;
        initializeDB();
        createTables();
    }

    private void initializeDB() {
        try {
            this.conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createTables() {
        // SQL statement for creating a new table
        String sql1 = "CREATE TABLE IF NOT EXISTS users (\n" + " id integer PRIMARY KEY, \n" + " name VARCHAR(255),\n"
                + " role VARCHAR(20) NOT NULL,\n" + " personal_number VARCHAR(20) NOT NULL,\n"
                + " password text NOT NULL,\n" + " salt text NOT NULL,\n" + " division VARCHAR(20),\n" + " otp_secret text NOT NULL\n" + ");";
        String sql2 = "CREATE TABLE IF NOT EXISTS records (\n" + " record_id integer PRIMARY KEY,\n"
                + " patient_personal_number INT NOT NULL,\n" + " doctor_personal_number INT NOT NULL,\n"
                + " nurse_personal_number INT NOT NULL,\n" + " division VARCHAR(20) NOT NULL,\n"
                + " record VARCHAR(255) NOT NULL\n" + ");";
        String sql3 = "CREATE TABLE IF NOT EXISTS logs (\n" + "log_id integer PRIMARY KEY,\n"
                + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,\n" + "user_personal_number INT NOT NULL,\n"
                + "record_id INT,\n" + "action_type VARCHAR(20) NOT NULL,\n" + "action VARCHAR(255) NOT NULL\n" + ");";
        String sql4 = "CREATE TABLE IF NOT EXISTS treating (\n" + "id integer PRIMARY KEY,\n"
                + "doctor_personal_number INT NOT NULL,\n" + "patient_personal_number INT NOT NULL\n" + ");";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
            stmt.execute(sql4);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Update a record.
     *
     * @param change_column column to change
     * @param value         new value
     * @param search_column column to search for record
     * @param search_term   is the term to identify the record
     */
    public void updateRecord(String change_column, String value, String search_column, String search_term) {
        String sql = "UPDATE records SET " + change_column + " = ? WHERE " + search_column + " = ?";

        try {
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, value);
            pstmt.setString(2, search_term);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Record to be added to database.
     *
     * @param r to be added to database
     */
    public void addRecord(Record r) {
        String sql = "INSERT INTO records(patient_personal_number, doctor_personal_number, nurse_personal_number, division, record) VALUES(?,?,?,?,?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, r.getPatientPersonalNumber());
            pstmt.setString(2, r.getDoctorPersonalNumber());
            pstmt.setString(3, r.getNursePersonalNumber());
            pstmt.setString(4, r.getDivision());
            pstmt.setString(5, r.getRecord());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Add a log to the database.
     *
     * @param log to be added to database
     */
    public void addLog(Log log) {

        String sql = "INSERT INTO logs(user_personal_number, record_id, action_type, action) VALUES(?,?,?,?)";

        try {
            System.out.println("Log");
            System.out.println(log.getPersonalNumber());
            System.out.println(log.getRecordId());
            System.out.println(log.getActionType());
            System.out.println(log.getAction());
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, log.getPersonalNumber());
            pstmt.setInt(2, log.getRecordId());
            pstmt.setString(3, log.getActionType().toString());
            pstmt.setString(4, log.getAction());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    /**
     * Add a user to the database.
     *
     * @param user object to be added to database.
     */
    public void addUser(User user) {
        String sql = "INSERT INTO users(name, role, personal_number, password, salt, division, otp_secret) VALUES(?,?,?,?,?,?,?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getRole());
            pstmt.setString(3, user.getPersonalNumber());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getSalt());

            if (user.getDivision() != "") {
                pstmt.setString(6, user.getDivision());
            } else {
                pstmt.setString(6, null);
            }

            pstmt.setString(7, user.getOtpSecret());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addTreatment(String doctor_personal_number, String patient_personal_number) {
        String sql = "INSERT INTO treating(doctor_personal_number, patient_personal_number) VALUES(?,?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, doctor_personal_number);
            pstmt.setString(2, patient_personal_number);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Integer getLatestRecordId() {
        String sql = "SELECT * FROM records ORDER BY record_id DESC LIMIT 1;";
        //String sql = "SELECT * FROM records WHERE record_id = MAX(record_id);";

        Integer id = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            rs.next();
            id = rs.getInt("record_id");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return id;
    }

    /**
     * Delete a user from the database.
     *
     * @param personal_number of user to be deleted
     */
    public void deleteUser(String personal_number) {
        // Specify sql command
        String sql = "DELETE FROM users WHERE personal_number='" + personal_number + "';";

        try {
            // Execute command
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Delete a record.
     *
     * @param id of record to be deleted.
     */
    public void deleteRecord(int id) {
        String sql = "DELETE FROM records where record_id='" + id + "';";

        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Find all users which fulfill the requirements.
     *
     * @param column      to search for
     * @param search_term to search for
     * @return a set of all users which fulfill the requirements.
     */
    public List<User> findUsers(String column, String search_term) {
        List<User> users = new LinkedList<>();

        String sql = "SELECT * FROM users WHERE " + column + "='" + search_term + "';";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String role = rs.getString("role");
                String personal_number = rs.getString("personal_number");
                String password = rs.getString("password");
                String salt = rs.getString("salt");
                String division = rs.getString("division");
                String otpSecret = rs.getString("otp_secret");
                User user = new User(name, role, personal_number, password, salt, division, id, otpSecret);
                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return users;
    }

    /**
     * Find all records which fulfill the requirements.
     *
     * @param column      to search for
     * @param search_term to search for
     * @return a set of all records which fulfill the requirements.
     */
    public List<Record> findRecords(String column, String search_term) {
        List<Record> list = new ArrayList<>();

        String sql = "SELECT * FROM records WHERE " + column + "='" + search_term + "';";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String patient_personal_number = rs.getString("patient_personal_number");
                String doctor_personal_number = rs.getString("doctor_personal_number");
                String nurse_personal_number = rs.getString("nurse_personal_number");
                String division = rs.getString("division");
                String record = rs.getString("record");
                Integer record_id = rs.getInt("record_id");
                Record r = new Record(patient_personal_number, doctor_personal_number, nurse_personal_number, division,
                        record, record_id);
                list.add(r);
                System.out.println(r);
            }

        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }
        return list;
    }

    /**
     * Find all logs which fulfill the requirements.
     *
     * @param column      to search for
     * @param search_term to search for
     * @return a set of logs.
     */
    public HashSet<Log> findLogs(String column, String search_term) {
        HashSet<Log> set = new HashSet<Log>();

        String sql = "SELECT * FROM logs WHERE " + column + "='" + search_term + "';";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String personal_number = rs.getString("personal_number");
                int record_id = rs.getInt("record_id");
                Integer log_id = rs.getInt("log_id");
                String action_type = rs.getString("action_type");
                String action = rs.getString("action");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                Log log = new Log(personal_number, record_id, log_id, action_type, action, timestamp);
                set.add(log);
                System.out.println(log);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return set;
    }

    public HashSet<String> findPatients(String doctor_personal_number) {
        HashSet<String> result = new HashSet<String>();

        String sql = "SELECT * FROM treating WHERE doctor_personal_number='"+doctor_personal_number+"';";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //pstmt.setString(1, doctor_personal_number);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String patient_personal_number = rs.getString("patient_personal_number");
                result.add(patient_personal_number);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return result;
    }

    /**
     * Prints all users registered in database.
     */
    public void printAllUsers() {
        String sql = "SELECT * FROM users";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" + rs.getString("name") + "\t" + rs.getString("role") + "\t"
                        + rs.getString("personal_number") + "\t" + rs.getString("password") + "\t"
                        + rs.getString("salt") + "\t" + rs.getString("division") + "\t" + rs.getString("otp_secret"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Prints all records from database.
     */
    public void printAllRecords() {
        String sql = "SELECT * FROM records";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("record_id") + "\t" + rs.getString("patient_personal_number") + "\t" + rs.getString("doctor_personal_number") + "\t"
                        + rs.getString("nurse_personal_number") + "\t" + rs.getString("division") + "\t"
                        + rs.getString("record"));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Prints all logs from database.
     */
    public void printAllLogs() {
        String sql = "SELECT * FROM logs";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //Loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("log_id") + "\t" + rs.getString("timestamp").toString() + "\t" + rs.getString("user_personal_number") + "\t"
                        + rs.getString("record_id") + "\t" + rs.getString("action_type") + "\t"
                        + rs.getString("action"));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void printAllTreatments() {
        String sql = "SELECT * FROM treating";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                System.out.println("[Treatment] " + rs.getString("doctor_personal_number") + " is treating " + rs.getString("patient_personal_number"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
