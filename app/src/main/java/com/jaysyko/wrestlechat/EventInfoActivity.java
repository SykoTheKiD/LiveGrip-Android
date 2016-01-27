package com.jaysyko.wrestlechat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class EventInfoActivity extends AppCompatActivity {

    private TextView stickyView;
    private ListView listView;
    private View heroImageView;
    private View stickyViewSpacer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        /* Initialise list view, hero image, and sticky view */
        listView = (ListView) findViewById(R.id.listView);
        heroImageView = findViewById(R.id.heroImageView);
        stickyView = (TextView) findViewById(R.id.stickyView);

        /* Inflate list header layout */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.event_info_header, null);
        stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);

        /* Add list view header */
        listView.addHeaderView(listHeader);

        /* Handle list View scroll events */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                /* Check if the first item is already reached to top.*/
                if (listView.getFirstVisiblePosition() == 0) {
                    View firstChild = listView.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null) {
                        topY = firstChild.getTop();
                    }

                    int heroTopY = stickyViewSpacer.getTop();
                    stickyView.setY(Math.max(0, heroTopY + topY));

                    /* Set the image to scroll half of the amount that of ListView */
                    heroImageView.setY(topY * 0.5f);
                }
            }
        });

        /* Populate the ListView with sample data */
        List<String> modelList = new ArrayList<>();
        modelList.add("Royal Rumble (2016) was a professional wrestling pay-per-view (PPV) event produced by WWE. It took place on January 24, 2016, at Amway Center in Orlando, Florida.[1] It was the 29th event in the Royal Rumble chronology. This was the fifth Royal Rumble to be held in the state of Florida (1990, 1991, 1995, and 2006) and the first pay-per-view event at Amway Center since it opened in 2010.");
        modelList.add("At the event, Roman Reigns defended his WWE World Heavyweight Championship in the Royal Rumble match. The event also marked the first time that a reigning champion has defended his title during the match and the second time (after the 1992 event) that the title is the prize for winning the match.");
        modelList.add("Six matches were contested at the event, with one match contested on the pre-show. In the main event, the returning Triple H won the Royal Rumble match by eliminating Dean Ambrose to win the WWE World Heavyweight Championship, becoming the third person to win from entrant number 30 and the first since John Cena in 2008. The defending champion Roman Reigns was also eliminated by Triple H.");
        modelList.add("Royal Rumble consisted of professional wrestling matches that involved wrestlers from pre-existing scripted feuds or storylines that played out on WWE's primary television programs, Raw and SmackDown. Wrestlers portrayed heroes, villains, or more ambiguous characters as they followed a series of events that built tension and culminated in a wrestling match or series of matches.[3][4]");
        modelList.add("Traditionally, the winner of the 30-man Royal Rumble match is awarded a match for the WWE World Heavyweight Championship at WrestleMania. However, as a culmination of his attempts to deprive WWE World Heavyweight Champion Roman Reigns of the championship (for attacking his son-in-law Triple H and overall disrespecting the McMahon family), WWE owner/chairman/CEO Vince McMahon decided on the January 4, 2016 episode of Raw that Reigns would defend his title in the match.[5]");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.event_info_list_item, modelList);
        listView.setAdapter(adapter);
    }
}