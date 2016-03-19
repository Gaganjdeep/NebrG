package gagan.com.communities.models;

/**
 * Created by sony on 29-01-2016.
 */
public class GridModel
{


    private int icon,back_img;
    private String Title,SubTitle;

    public int getBack_img() {
        return back_img;
    }

    public GridModel(int icon, int back_img, String title, String subTitle) {
        this.icon = icon;
        this.back_img = back_img;
        Title = title;
        SubTitle = subTitle;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return Title;
    }

    public String getSubTitle() {
        return SubTitle;
    }
}
