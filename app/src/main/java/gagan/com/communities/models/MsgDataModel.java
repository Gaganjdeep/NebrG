package gagan.com.communities.models;

/**
 * Created by sony on 20-03-2016.
 */
public class MsgDataModel
{


    private boolean lastMsg;

    private String id,sender_userid,recipient_userid,message,created_at,username,profile_pic;

//    "id": "12",
//            "sender_userid": "24",
//            "recipient_userid": "1",
//            "message": "hi",
//            "created_at": "2016-03-20 22:33:21",
//            "updated_date": "2016-03-20 10:03:21",
//            "status": "0",
//            "username": "vimajl",
//            "profile_pic": "http://orasisdata.com/Neiber/images/1454321304431.jpg"


    public String getCreated_at()
    {
        return created_at;
    }

    public boolean isLastMsg()
    {
        return lastMsg;
    }

    public void setLastMsg(boolean lastMsg)
    {
        this.lastMsg = lastMsg;
    }

    public MsgDataModel(boolean lastMsg, String id, String sender_userid, String recipient_userid, String message, String created_at, String username, String profile_pic)
    {
        this.lastMsg = lastMsg;
        this.id = id;
        this.sender_userid = sender_userid;
        this.recipient_userid = recipient_userid;
        this.message = message;
        this.created_at = created_at;
        this.username = username;
        this.profile_pic = profile_pic;
    }

    public String getId()
    {
        return id;
    }

    public String getSender_userid()
    {
        return sender_userid;
    }

    public String getRecipient_userid()
    {
        return recipient_userid;
    }

    public String getMessage()
    {
        return message;
    }

    public String getUsername()
    {
        return username;
    }

    public String getProfile_pic()
    {
        return profile_pic;
    }
}
