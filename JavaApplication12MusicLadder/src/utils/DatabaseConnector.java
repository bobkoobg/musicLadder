package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * TODO before running this code :
 * Install Oracle SQL Database - http://blog.whitehorses.nl/2014/03/18/installing-java-oracle-11g-r2-express-edition-and-sql-developer-on-ubuntu-64-bit/
 * JDBC Drivers for this database - http://www.oracle.com/technetwork/database/features/jdbc/index-091264.html
 * Example code - http://www.mkyong.com/jdbc/connect-to-oracle-db-via-jdbc-driver-java/
 * Local host solution - http://stackoverflow.com/questions/18192521/ora-12505-tnslistener-does-not-currently-know-of-sid-given-in-connect-descript
 * Add ojdbc.jar to Libraries of Project : Right click -> Properties -> Libraries -> Add JAR
*/
public class DatabaseConnector
{
    private String databaseHost;
    private String databaseUsername;
    private String databasePassword;
//    private static String databaseHost = "jdbc:oracle:thin:@datdb.cphbusiness.dk:1521:dat";
//    private static String databaseUsername = "cphbs96";
//    private static String databasePassword = "cphbs96";

    public DatabaseConnector(String databaseHost, String databaseUsername, String databasePassword)
    {
        this.databaseHost = databaseHost;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
    }

    
    public Boolean testJDBCdriver() {
        System.out.println("-------- Oracle JDBC Driver Testing --------");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
            return false;

        }

        System.out.println("Oracle JDBC Driver Registered!");
        return true;
    }
    
    public Connection getConnection() {
        System.out.println("-------- Oracle JDBC Connection Testing --------");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection( databaseHost , databaseUsername , databasePassword );

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }

        if (connection != null) {
                System.out.println("You made it, take control of your database now!");
        } else {
                System.out.println("Failed to make connection!");
        }
        return connection;
    }
}
