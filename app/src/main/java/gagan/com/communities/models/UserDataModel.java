package gagan.com.communities.models;

import java.io.Serializable;

/**
 * Created by sony on 25-02-2016.
 */
public class UserDataModel implements Serializable
{

    private String uId,name,email,password,gender,home_society,profession,location,profile_pic;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHome_society() {
        return home_society;
    }

    public void setHome_society(String home_society) {
        this.home_society = home_society;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    //                "uId": "3",
//                    "name": "Gagan",
//                    "email": "gagan@gmail.com",
//                    "password": "e10adc3949ba59abbe56e057f20f883e",
//                    "gender": "male",
//                    "home_society": "gshsbvdvv",
//                    "session_key": "",
//                    "create_date": "2016-02-25 16:55:58",
//                    "role_id": "2",
//                    "profession": "Developers",
//                    "location": "Indian Roller",
//                    "delete_status": "0",
//                    "device_type": "android",
//                    "device_token": "APA91bGCMny0u3-IbqFzpThqIM3S1T5gr_ZPnmUsSEIvcuwBMsQBPwELcHjF1wfSjyniC2BKFmJzsxMkTLv3CarAkG0MaZCZrRMj2AYAPrWaEYrnXVUC2Mi6f2CbOvK2FT1T6AjAGFWv",
//                    "update_date": "2016-02-25 09:55:58",
//                    "profile_pic": "",
//                    "path": "",
//                    "is_fb": "0",
//                    "facebook_id": "",
//                    "login_status": "1"
}
