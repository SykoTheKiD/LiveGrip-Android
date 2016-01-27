package com.jaysyko.wrestlechat;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EventInfoActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    private static final int MAX_ROWS = 50;
    private int lastTopValue = 0;

    private List<String> modelList = new ArrayList<>();
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        ListView listView = (ListView) findViewById(R.id.list);
        setTitle("WWE Royal Rumble 2016");
        modelList.add("Royal Rumble (2016) was a professional wrestling pay-per-view (PPV) event produced by WWE. It took place on January 24, 2016, at Amway Center in Orlando, Florida. It was the 29th event in the Royal Rumble chronology. This was the fifth Royal Rumble to be held in the state of Florida (1990, 1991, 1995, and 2006) and the first pay-per-view event at Amway Center since it opened in 2010.\n" +
                "\n" +
                "At the event, Roman Reigns defended his WWE World Heavyweight Championship in the Royal Rumble match. The event also marked the first time that a reigning champion has defended his title during the match and the second time (after the 1992 event) that the title is the prize for winning the match.\n" +
                "\n" +
                "Six matches were contested at the event, with one match contested on the pre-show. In the main event, the returning Triple H won the Royal Rumble match by eliminating Dean Ambrose to win the WWE World Heavyweight Championship, becoming the third person to win from entrant number 30 and the first since John Cena in 2008. The defending champion Roman Reigns was also eliminated by Triple H.\n" +
                "\n" +
                "The Royal Rumble featured the WWE debut of longtime Total Nonstop Action Wrestling (TNA) mainstay A.J. Styles.");
        for (int i = 0; i < MAX_ROWS; i++) {
            modelList.add("List item " + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.event_info_list_item, modelList);
        listView.setAdapter(adapter);

        // inflate custom header and attach it to the list
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.event_info_header, listView, false);
        listView.addHeaderView(header, null, false);

        // we take the background image and button reference from the header
        backgroundImage = (ImageView) header.findViewById(R.id.listHeaderImage);
        listView.setOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Rect rect = new Rect();
        backgroundImage.getLocalVisibleRect(rect);
        if (lastTopValue != rect.top) {
            lastTopValue = rect.top;
            backgroundImage.setY((float) (rect.top / 2.0));
        }
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_event_info);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//    }

}
