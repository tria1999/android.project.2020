package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.util.Log;
import twitter4j.*;

import java.util.List;

public class TwitterSearchForPosts implements Runnable{

    private Twitter twitter;
    private String selectedHashtag;
    public boolean done;
    private Object lock;
    private QueryResult result;

    public TwitterSearchForPosts(Twitter twitter, String selectedHashtag, Object lock) {
        this.twitter = twitter;
        this.selectedHashtag = selectedHashtag;
        this.lock = lock;
        done=false;
    }

    @Override
    public void run() {

        Query query = new Query(selectedHashtag);
        query.setCount(20);
        try{
            // The factory instance is re-useable and thread safe.
            result = twitter.search(query);
            if (result!=null)
            {
                synchronized (lock) {
                    done = true;
                    lock.notify();
                }
            }
            else{
                Log.e("netError","networkError");
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public QueryResult getResult() {
        return result;
    }
}
