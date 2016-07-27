package gagan.com.communities.models;

import gagan.com.communities.utills.Notification;

/**
 * Created by sony on 17-01-2016.
 */
public class NotificationModel<T>
{
    private String Name, Time, Msg, Image,id;

    public boolean isRead()
    {
        return read;
    }

    public void setRead(boolean read)
    {
        this.read = read;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    private boolean read;


    public String getName()
    {
        return Name;
    }

    public String getTime()
    {
        return Time;
    }

    public String getMsg()
    {
        return Msg;
    }

    public String getImage()
    {
        return Image;
    }


    private T object;

    public T getObject()
    {
        return object;
    }

    public Notification getNotificationType()
    {
        return notificationType;
    }

    private Notification notificationType;

    public NotificationModel()
    {
    }

    public NotificationModel(String name, String time, String msg, String image, T object, Notification notificationType)
    {
        Name = name;
        Time = time;
        Msg = msg;
        Image = image;
        this.object = object;
        this.notificationType = notificationType;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public void setTime(String time)
    {
        Time = time;
    }

    public void setMsg(String msg)
    {
        Msg = msg;
    }

    public void setImage(String image)
    {
        Image = image;
    }

    public void setObject(T object)
    {
        this.object = object;
    }

    public void setNotificationType(Notification notificationType)
    {
        this.notificationType = notificationType;
    }
}
