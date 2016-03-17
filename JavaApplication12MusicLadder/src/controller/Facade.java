/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Song;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import mapper.SongMapper;
import utils.DatabaseConnector;

/**
 *
 * @author root
 */
public class Facade
{   
    private static Facade instance = null;
    private Connection connection;
    private DatabaseConnector databaseConnector;
    private static String databaseHost = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
    private static String databaseUsername = "bobkoo";
    private static String databasePassword = "qwerty12345";
//    private static String databaseHost = "jdbc:oracle:thin:@datdb.cphbusiness.dk:1521:dat";
//    private static String databaseUsername = "cphbs96";
//    private static String databasePassword = "cphbs96";
    private SongMapper songMapper = null;
    
    private Facade()
    {
        // Exists only to defeat instantiation.
        databaseConnector = new DatabaseConnector(databaseHost, databaseUsername, databasePassword);
        songMapper = new SongMapper();
    }

    public static Facade getInstance()
    {
        if (instance == null)
        {
            instance = new Facade();
        }
        return instance;
    }
    
    public boolean initializeConnection() {
        if ( connection != null ) {
            System.out.println("Connection already existing");
            return true;
        } else {
            connection = databaseConnector.getConnection();
            
            try
            {
                connection.setAutoCommit(true);
                // termination by the garbage collector
            }
            catch (SQLException ex)
            {
                System.out.println("SQL Exception while trying to connect to db");
                System.out.println("ex : " + ex);
                return false;
            }
            System.out.println("Connection initialized!");
        }
        return true;
    }

    public boolean resetConnection(Connection connection, String dbHost, String dbUsername, String dbPassword)
    {
        connection = new DatabaseConnector(dbHost, dbUsername, dbPassword).getConnection();
        try
        {
            connection.setAutoCommit(true);
            // termination by the garbage collector
        }
        catch (SQLException ex)
        {
            System.out.println("SQL Exception while reseting connection to db");
            System.out.println("ex : " + ex);
            return false;
        }
        System.out.println("Connection reset!");
        return true;
    }

    public boolean closeConnection()
    {
        try
        {
            connection.close();
        }
        catch (SQLException ex)
        {
            System.out.println("SQL Exception while close connection to db");
            System.out.println("ex : " + ex);
            return false;
        }
        System.out.println("Connection closed");
        return true;
    }
    
    public boolean insertSong(Logger logger, String name) {
        return songMapper.insert(logger, connection, name);
    }
}
