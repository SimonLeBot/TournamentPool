package com.Kardium.TournamentPool.Storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Passwords {
    private static final File PASSWORD_FILE = new File("C:/TournamentPool/passwords.txt");
    private static Map<String, Integer> passwords;

    private final Connection connection;

    public Passwords() throws Exception {
        Class.forName("org.firebirdsql.jdbc.FBDriver");
        connection = DriverManager.getConnection(
                "jdbc:firebirdsql:kfishbowl3/3050:C:/Program Files/Fishbowl/database/data/TOURNAMENTPOOLDB.FDB",
                "ksqlread","ksqlread");
    }

    public boolean isExistingUser(String name) {
        try {
            String check = "SELECT PASSWORD " +
                    "FROM PASSWORDS\n" +
                    "WHERE USERNAME='" + name + "';";
            ResultSet resultSet = executeRequest(check);
            resultSet.getClob("PASSWORD");
            resultSet.getArray(1);
            System.out.println("Result set = " + resultSet);
        } catch (SQLException sqle) {
            // deal with it
        }
        return getPasswords().containsKey(name);
    }

    int getPasswordFormDB(String name) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("SELECT * FROM PASSWORDS WHERE ");
        return statement.getResultSet().findColumn(name);
    }

    private ResultSet executeRequest(String request) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(request);
        return statement.getResultSet();
    }

    public void registerNewUser(String name, int password) throws AlreadyRegisteredUser{
        String register = "INSERT INTO PASSWORDS (ID, USERNAME, \"PASSWORD\")\n" +
                " VALUES (\n" +
                "1, \n" +
                "'" + name + "', \n" +
                password + "\n" +
                ")";
        if (isExistingUser(name)) {
            throw new AlreadyRegisteredUser();
        }
        getPasswords().put(name, password);
        savePasswords();
    }

    public int getPassword(String name) throws NotRegisteredUserException{
        Integer password = getPasswords().get(name);
        if (password == null) {
            throw new NotRegisteredUserException();
        }
        return password;
    }

    private static void savePasswords() {
        List<String> lines = passwords.entrySet().stream()
                .map((e) -> e.getKey() + ";" + e.getValue())
                .collect(Collectors.toList());
        try {
            Files.deleteIfExists(PASSWORD_FILE.toPath());
            PASSWORD_FILE.createNewFile();
            Files.write(PASSWORD_FILE.toPath(), lines, StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.err.println(e);
            System.out.println(e);
        }
    }

    private static Map<String, Integer> getPasswords() {
        try {
            if (passwords == null) {
                passwords = new HashMap<>();
                // Load all the passwords
                if (!PASSWORD_FILE.exists()) {
                    PASSWORD_FILE.getParentFile().mkdirs();
                    PASSWORD_FILE.createNewFile();
                }
                List<String> lines = Files.readAllLines(PASSWORD_FILE.toPath());
                for (String line : lines){
                    String[] ids = line.split(";");
                    passwords.put(ids[0], Integer.parseInt(ids[1]));
                }
            }
            return passwords;
        } catch (IOException ioe) {
            return new HashMap<>();
        }
    }

    public static void main(String [] args) throws Exception {
        Passwords p = new Passwords();
        p.isExistingUser("TempUser");
    }
}
