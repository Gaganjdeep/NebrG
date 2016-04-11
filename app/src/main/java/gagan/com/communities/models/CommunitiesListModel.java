package gagan.com.communities.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by sony on 08-03-2016.
 */
public class CommunitiesListModel implements Serializable
{


    public boolean isMyCommunity()
    {
        return isMyCommunity;
    }

    public void setMyCommunity(boolean myCommunity)
    {
        isMyCommunity = myCommunity;
    }

    private boolean isMyCommunity;

    private String cid, c_name, c_genre, c_description, owner_id, user_id, created_at;

    private Double lat, Lng;

    public LatLng getLatLng()
    {
        return new LatLng(lat, Lng);
    }

    public void setLatLng(LatLng latLng)
    {
        lat = latLng.latitude;
        Lng = latLng.longitude;
    }

    public String getCid()
    {
        return cid;
    }

    public void setCid(String cid)
    {
        this.cid = cid;
    }

    public String getC_name()
    {
        return c_name;
    }

    public void setC_name(String c_name)
    {
        this.c_name = c_name;
    }

    public String getC_genre()
    {
        return c_genre;
    }

    public void setC_genre(String c_genre)
    {
        this.c_genre = c_genre;
    }

    public String getC_description()
    {
        return c_description;
    }

    public void setC_description(String c_description)
    {
        this.c_description = c_description;
    }

    public String getOwner_id()
    {
        return owner_id;
    }

    public void setOwner_id(String owner_id)
    {
        this.owner_id = owner_id;
    }


    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getCreated_at()
    {
        return created_at;
    }

    public void setCreated_at(String created_at)
    {
        this.created_at = created_at;
    }

    //    "cid": "1",
//            "c_name": "first community",
//            "c_genre": "english",
//            "c_lat": "23",
//            "c_long": "73.65",
//            "c_description": "this is dummy description",
//            "owner_id": "9041638559",
//            "c_type": "1",
//            "user_id": "1",
//            "created_at": "2016-03-06 18:55:44"
}
