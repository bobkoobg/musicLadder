package entity;

import java.util.Date;

public class UserIdentifiers {

    private String clientReqIP;
    private int curClientId;
    private int curServerId;
    private Date curDate;
    
    public UserIdentifiers( String userReqIP, int curServerId, Date curDate ) {
        this.clientReqIP = userReqIP;
        this.curServerId = curServerId;
        this.curDate = curDate;
    }

    public String getClientReqIP() {
        return clientReqIP;
    }

    public void setClientReqIP( String clientReqIP ) {
        this.clientReqIP = clientReqIP;
    }

    public int getCurClientId() {
        return curClientId;
    }

    public void setCurClientId( int curClientId ) {
        this.curClientId = curClientId;
    }

    public int getCurServerId() {
        return curServerId;
    }

    public void setCurServerId( int curServerId ) {
        this.curServerId = curServerId;
    }

    public Date getCurDate() {
        return curDate;
    }

    public void setCurDate( Date curDate ) {
        this.curDate = curDate;
    }

    @Override
    public String toString() {
        return "UserIdentifiers{" + "clientReqIP=" + clientReqIP + ", curClientId=" 
                + curClientId + ", curServerId=" + curServerId + ", curDate=" 
                + curDate + '}';
    }
    
    

}
