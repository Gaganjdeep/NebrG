package gagan.com.communities.models;

/**
 * Created by sony on 17-01-2016.
 */
public class NotificationModel
{
    private String Name,Time,Msg,Image;


    public String getName() {
        return Name;
    }

    public String getTime() {
        return Time;
    }

    public String getMsg() {
        return Msg;
    }

    public String getImage() {
        return Image;
    }

    public NotificationModel(String name, String time, String msg, String image) {
        Name = name;
        Time = time;
        Msg = msg;
        Image = image;
    }
}
