package mapper;

import entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class UserMapper {

    public Integer registerUser( Logger logger, Connection connection, User user ) {
        PreparedStatement preparedStatement = null;
        Integer userID = null;

        String SQLString = "select USER_ID.nextval from dual";
        try {
            preparedStatement = connection.prepareStatement( SQLString );
            ResultSet rs = preparedStatement.executeQuery();
            try {
                if ( rs.next() ) {
                    userID = rs.getInt( 1 );
                }
            } finally {
                try {
                    rs.close();
                } catch ( Exception ignore ) {
                }
            }
        } catch ( SQLException e ) {
            logger.severe( "Method registerUser (Part1) - Execution SQL Exception : [ " + e + " ]" );
            return 0;
        } finally {
            try {
                if ( preparedStatement != null ) {
                    preparedStatement.close();
                }
            } catch ( SQLException e ) {
                logger.severe( "Method registerUser (Part1) - Closing SQL Exception : [ " + e + " ]" );
                return 0;
            }
        }

        if ( userID != null ) {
            String insertTableSQL;

            user.setUserId( userID );

            java.util.Date date = new java.util.Date();
            long t = date.getTime();
            java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp( t );

            try {
                insertTableSQL = "INSERT INTO ML_USER_TBL VALUES"
                        + "(?, ?, ?, ?, ?, ?)";

                preparedStatement = connection.prepareStatement( insertTableSQL );

                preparedStatement.setInt( 1, user.getUserId() );
                preparedStatement.setString( 2, user.getUsername() );
                preparedStatement.setString( 3, user.getPassword() );
                preparedStatement.setTimestamp( 4, sqlTimestamp );
                preparedStatement.setNull( 5, java.sql.Types.TIMESTAMP );
                preparedStatement.setInt( 6, 3 );

                preparedStatement.executeUpdate();
            } catch ( SQLException e ) {
                logger.severe( "Method registerUser (Part2) - Execution SQL Exception : [ "
                        + e + " ], User content : [" + user.toString() + "]" );
                return 0;
            } finally //The statement must be closed, because of : java.sql.SQLException: ORA-01000
            {
                try {
                    if ( preparedStatement != null ) {
                        preparedStatement.close();
                    }
                } catch ( SQLException e ) {
                    logger.severe( "Method registerUser (Part2) - Closing SQL Exception : [ "
                            + e + " ], User content : [" + user.toString() + "]" );
                    return 0;
                }
            }

            logger.info( "Method registerUser success! : [ User ID : " + user.getUserId() + "]" );
            return userID;
        }
        logger.severe( "Method registerUser (Part1) - [ User ID : " + userID
                + ", User : " + user.toString() + " ]" );
        return 0;
    }

    public User getUserByUNandPW( Logger logger, Connection connection, String username, String password ) {
        User user = null;
        PreparedStatement preparedStatement = null;
        String hashedPassword = null;

        try {

            String SQLString = "SELECT * "
                    + "FROM ML_USER_TBL userr "
                    + "WHERE userr.USERNAME = ? ";

            preparedStatement = connection.prepareStatement( SQLString );

            preparedStatement.setString( 1, username );

            ResultSet rs = preparedStatement.executeQuery();

            try {
                if ( rs.next() ) {
                    user = new User();

                    user.setUserId( rs.getInt( 1 ) );
                    user.setUsername( rs.getString( 2 ) );
                    hashedPassword = rs.getString( 3 );
                    user.setUserLevel( rs.getInt( 6 ) );
                    //take care of user_last login here... eventually :D
                }
            } finally {
                try {
                    rs.close();
                } catch ( Exception ignore ) {
                }
            }
        } catch ( Exception e ) {
            logger.severe( "Method getUserByUNandPW - Execution Exception : [ "
                    + e + " ], User content : [ username : " + username
                    + ", password " + password + " ]" );
            return null;
        } finally //The statement must be closed, because of : java.sql.SQLException: ORA-01000
        {
            try {
                if ( preparedStatement != null ) {
                    preparedStatement.close();
                }
            } catch ( SQLException e ) {
                logger.severe( "Method getUserByUNandPW - Closing SQL Exception : [ "
                        + e + " ], User content : [" + user.toString() + "]" );
                return null;
            }
        }
        boolean isCorrectPassword = BCrypt.checkpw( password, hashedPassword );
        System.out.println( "isCorrectPassword ? : " + isCorrectPassword );
        if ( isCorrectPassword ) {
            return user;
        }
        return null;
    }
}
