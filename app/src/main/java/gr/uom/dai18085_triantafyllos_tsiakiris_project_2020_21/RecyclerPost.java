package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.graphics.Bitmap;
import android.net.Uri;
import twitter4j.MediaEntity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerPost {
    private String username;
    private String text;
    private String smn;
    private MediaEntity[] mediaEntities;
    private List<String> ImageUrls;

    public RecyclerPost(String username, String text, String smn, MediaEntity[] mediaEntities) {
        this.username = username;
        this.text = text;
        this.smn = smn;
        this.mediaEntities = mediaEntities;
        ImageUrls = new ArrayList<>();
        for(MediaEntity m: mediaEntities)
        {
            if(m.getType()=="photo"){
                ImageUrls.add(m.getMediaURL());
            }
        }
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

    public MediaEntity[] getMediaEntities() {
        return mediaEntities;
    }

    public void setMediaEntities(MediaEntity[] mediaEntities) {
        this.mediaEntities = mediaEntities;
    }

    public List<String> getImageUrls() {
        return ImageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        ImageUrls = imageUrls;
    }
}
