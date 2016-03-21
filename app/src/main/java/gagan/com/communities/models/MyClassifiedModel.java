package gagan.com.communities.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by sony on 20-03-2016.
 */
public class MyClassifiedModel
{
    private String id,userid,title,description,category,image,create_date;

    private LatLng latLng ;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUserid()
    {
        return userid;
    }

    public void setUserid(String userid)
    {
        this.userid = userid;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getCreate_date()
    {
        return create_date;
    }

    public void setCreate_date(String create_date)
    {
        this.create_date = create_date;
    }

    public LatLng getLatLng()
    {
        return latLng;
    }

    public void setLatLng(LatLng latLng)
    {
        this.latLng = latLng;
    }
//    "id": "7",
//            "userid": "24",
//            "title": "my classifieds",
//            "description": "fghhhhjj",
//            "category": "Work Place",
//            "lat": "30.3377",
//            "lng": "76.8765",
//            "image": "http://orasisdata.com/Neiber/images/1458490221.png",
//            "status": "0",
//            "create_date": "2016-03-20 21:40:21",
//            "update_date": "2016-03-20 09:10:21"


}
