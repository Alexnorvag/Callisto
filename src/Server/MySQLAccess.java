package Server;

import java.sql.*;

public class MySQLAccess {
    private String username;
    private String country;
    private String phone;

    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;


    public MySQLAccess() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //setup the connection with the DB
            connection = DriverManager.getConnection("jdbc:mysql://localhost/callisto_users?user=root&password=root&useSSL=false&serverTimezone=UTC");
            //Statements allow to issue SQL queries to the database
            statement = connection.createStatement();
        } catch (Exception e) {
            System.out.println("MYSQLACCESS ERROR");
        }
    }

    public void selectFromDB(String country, String phone) {
        try {
            preparedStatement = connection.prepareStatement("SELECT username, country, phone FROM callisto_users.all_users WHERE country= ? AND phone= ?; ");
            preparedStatement.setString(1, country);
            preparedStatement.setString(2, phone);
            resultSet = preparedStatement.executeQuery();
            writeResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void insertToDB(String username, String country, String phone) {
        try {
            //PreparedStatements can use variables and are more efficient
            preparedStatement = connection.prepareStatement("INSERT INTO callisto_users.all_users VALUES (DEFAULT, ?, ?, ?)");

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, country);
            preparedStatement.setString(3, phone);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void writeMetaData(ResultSet resultSet) throws SQLException {
        //  Now get some metadata from the database
        // Result set get the result of the SQL query

        System.out.println("The columns in the table are: ");

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
            System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
        }
    }


    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            username = resultSet.getString("username");
            country = resultSet.getString("country");
            phone = resultSet.getString("phone");
            System.out.println("username: " + username);
            System.out.println("country: " + country);
            System.out.println("phone: " + phone);
        }
    }

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {

        }
    }

    public String getUsername() {
        return username;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }
}
