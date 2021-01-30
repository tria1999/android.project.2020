package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.graphics.Bitmap;
import android.net.Uri;
import twitter4j.MediaEntity;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

public class RecyclerPost {
    private String username;
    private String text;
    private String smn;
    //private MediaEntity[] mediaEntities;
    //private List<String> ImageUrls;
    private String profileImage;

    public RecyclerPost(String username, String text, String smn, String profileImage) {
        this.username = username;
        this.text = text;
        this.smn = smn;
        //this.mediaEntities = mediaEntities;
        this.profileImage = profileImage;

        /*ImageUrls = new ArrayList<>();
        for(int i=0;i< mediaEntities.length;i++)
        {
            if(mediaEntities[i].getType().equals("photo")){
                ImageUrls.add(mediaEntities[i].getMediaURL());
            }
        }*/
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSmn() {
        return smn;
    }

    public void setSmn(String smn) {
        this.smn = smn;
    }


    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
