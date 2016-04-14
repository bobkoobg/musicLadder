package entity;

import java.util.Date;

public class UserIdentifiers {

    private String clientReqIP;
    private int curClientId;
    private int curServerId;
    private Date curDate;
    private String type;

    public UserIdentifiers( String clientReqIP, int curServerId, Date curDate, String type ) {
        this.clientReqIP = clientReqIP;
        this.curServerId = curServerId;
        this.curDate = curDate;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserIdentifiers{" + "clientReqIP=" + clientReqIP + ", curClientId="
                + curClientId + ", curServerId=" + curServerId + ", curDate=" + curDate
                + ", type=" + type + '}';
    }

}
