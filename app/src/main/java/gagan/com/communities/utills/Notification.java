package gagan.com.communities.utills;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GagaN on 30-06-2016.
 */
public enum Notification
{


    PostAdded(11),
    GroupInvitation(1),
    PostLiked(3),
    Userfollow(5),
    CommentAdded(7),
    MessageRecieved(9);


    int value;

    Notification(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }


    private static Map<Integer, Notification> map = new HashMap<Integer, Notification>();

    static
    {
        for (Notification notification : Notification.values())
        {
            map.put(notification.value, notification);
        }
    }


    public static Notification valueOf(int legNo)
    {
        return map.get(legNo);
    }


}
