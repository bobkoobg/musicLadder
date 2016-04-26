package utilities;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class SessionIDsGenerator {

    private SecureRandom random;
    private Map<String, SessionObject> sessionsList;
    private long MAX_DURATION = MILLISECONDS.convert( 30, MINUTES );

    private static String errorMessage = "Empty essential parameters";

    public SessionIDsGenerator() {
        random = new SecureRandom();
        sessionsList = new HashMap();
    }

    public void setMAX_DURATION( long MAX_DURATION ) {
        this.MAX_DURATION = MAX_DURATION;
    }

    private String nextSessionId() {
        return new BigInteger( 130, random ).toString( 32 );
    }

    public String registerSession( String ipAddress, String username ) {
        if ( ipAddress == null || ipAddress.isEmpty() || username == null
                || username.isEmpty() ) {
            return errorMessage;
        }

        Date now = new Date();

        for ( Map.Entry<String, SessionObject> entry : sessionsList.entrySet() ) {

            String key = entry.getKey();
            SessionObject value = entry.getValue();

            if ( now.getTime() - value.getSessionTime().getTime() >= MAX_DURATION ) {
                sessionsList.remove( key );
            } else if ( ipAddress.equals( value.getIpAddress() ) && username.equals( value.getUsername() ) ) {
                return key;
            }
        }

        String newKey = nextSessionId();
        sessionsList.put( newKey, new SessionObject( ipAddress, username ) );
        return newKey;
    }

    public boolean checkSession( String sessionId, String ipAddress ) {
        if ( ipAddress == null || ipAddress.isEmpty() || sessionId == null
                || sessionId.isEmpty() ) {
            return false;
        }

        Date now = new Date();

        for ( Map.Entry<String, SessionObject> entry : sessionsList.entrySet() ) {

            String key = entry.getKey();
            SessionObject value = entry.getValue();

            if ( now.getTime() - value.getSessionTime().getTime() >= MAX_DURATION ) {
                sessionsList.remove( key );
                return false;
            } else if ( sessionId.equals( key ) && ipAddress.equals( value.getIpAddress() ) ) {
                return true;
            }
        }
        return false;
    }

    private class SessionObject {

        private String ipAddress;
        private String username;
        private Date sessionTime;

        public SessionObject( String ipAddress, String username ) {
            this.ipAddress = ipAddress;
            this.username = username;
            sessionTime = new Date();
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public Date getSessionTime() {
            return sessionTime;
        }

        public String getUsername() {
            return username;
        }

    }
}
