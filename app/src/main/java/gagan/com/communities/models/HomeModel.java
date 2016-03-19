package gagan.com.communities.models;

/**
 * Created by sony on 17-01-2016.
 */
public class HomeModel {


    private String id,userid,title,message,path,image,location,type,create_date,comments_count,like_count,dislike_count,username,profile_pic;

    private boolean anon_user;
    private boolean is_liked;

    public boolean is_disliked() {
        return is_disliked;
    }

    public void setIs_disliked(boolean is_disliked) {
        this.is_disliked = is_disliked;
    }

    private boolean is_disliked;

    public boolean isAnon_user() {
        return anon_user;
    }

    public void setAnon_user(boolean anon_user) {
        this.anon_user = anon_user;
    }

    public boolean is_liked() {
        return is_liked;
    }

    public void setIs_liked(boolean is_liked) {
        this.is_liked = is_liked;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getDislike_count() {
        return dislike_count;
    }

    public void setDislike_count(String dislike_count) {
        this.dislike_count = dislike_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getComments_count() {
        return comments_count;
    }

    public void setComments_count(String comments_count) {
        this.comments_count = comments_count;
    }

//    "id": "1",
//            "userid": "1",
//            "title": "post1",
//            "message": "message",
//            "path": "",
//            "image": "",
//            "location": "chandigarh",
//            "type": "",
//            "create_date": "2016-02-19 00:00:00",
//            "status": "0",
//            "comments_count": "1"
}
