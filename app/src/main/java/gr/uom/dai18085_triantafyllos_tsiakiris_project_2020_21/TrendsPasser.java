package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;

import java.util.ArrayList;
import java.util.List;

public class TrendsPasser implements Runnable {

    private Twitter twitter;
    private Trends trends;
    private List<String> trending;
    public boolean done;
    private Object lock;

    public TrendsPasser(Twitter twitter, Trends trends, List<String> trending, Object lock) {
        this.twitter = twitter;
        this.trends=trends;
        this.trending=trending;
        done=false;
        this.lock=lock;
    }

    @Override
    public void run() {
        try  {
            trends = twitter.getPlaceTrends(1);
            if (trends!=null)
            {
                trending.clear();
                for(Trend t: trends.getTrends()){
                    if(t.getName().charAt(0)=='#')
                        trending.add(t.getName());
                    else
                        trending.add('#'+t.getName());
                    //System.out.println(t.getName());
                }
                synchronized (lock) {
                    done = true;
                    lock.notify();
                }
            }
            else{
                trending.add("#NetworkConnectionError");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
