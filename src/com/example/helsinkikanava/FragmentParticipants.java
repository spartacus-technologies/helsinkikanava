package com.example.helsinkikanava;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

public class FragmentParticipants extends Fragment
{
    View rootView_ = null;					//Owns all fragment Views
    Context parent_ = null;
    String session_title_ = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_participants, container, false);
        rootView_ = rootView;

        generateParticipantListing();
        
        return rootView;
    }

    public FragmentParticipants(Context parent, String title)
    {
    	parent_ = parent;
        session_title_ = title;
    }

    private void generateParticipantListing()
    {
        //Get Parties from wrapperJSON
        TreeSet<String> parties = WrapperJSON.GetParties( session_title_);
//        TreeSet<String> parties = new TreeSet<String>();

//        parties.add("Puolue 1");
//        parties.add("Puolue 2");
//        parties.add("Puolue 3");
//        parties.add("Puolue 4");
//        parties.add("Puolue 5");
//        parties.add("Puolue 6");
//        parties.add("Puolue 7");
//        parties.add("Puolue 8");
//        parties.add("Puolue 9");

        //There was no data
        if (parties == null )
            return;

        createPartySegments(parties);

        //Get participants for each party from wrapperJSON
        for (int i = 0; i < parties.size(); ++i)
        {
            ArrayList<String> participants = new ArrayList<String>();
            participants.add("Matti Meikäläinen"); participants.add("Marja Meikäläinen");

            addParticipantsToParty(i, participants);
        }

    }

    private void createPartySegments(TreeSet<String> parties )
    {
        int i = 0;
        for (String party: parties)
        {
            //Create title for party
            LinearLayout party_layout = new LinearLayout( getActivity() );
            party_layout.setOrientation(LinearLayout.VERTICAL);
            party_layout.setId(i);

            TextView title = new TextView(getActivity());
//            TextView event_description = new TextView(getActivity());
            title.setText(party);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            View underline = new View(getActivity());
            underline.setBackgroundColor(Color.BLACK);
            underline.setPadding(0, 0, 0, 5);

            View spacer = new View(getActivity());
            spacer.setBackgroundColor(Color.TRANSPARENT);
            spacer.setPadding(0, 0, 0, 5);

            party_layout.addView(title, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
//            party_layout.addView(event_description, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
            party_layout.addView(underline, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 5));
            party_layout.addView(spacer, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 5));

            //year_title.setId(debug*10);

            ((LinearLayout) rootView_.findViewById(R.id.fragment_participant_content)).addView(party_layout, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT));
            ++i;
        }
    }

    private void addParticipantsToParty(int party, ArrayList<String>  participants)
    {
        LinearLayout my_root = (LinearLayout) rootView_.findViewById(party);

        LinearLayout text_layout = new LinearLayout(getActivity());
        text_layout.setOrientation( LinearLayout.VERTICAL );

        for (String participant : participants)
        {
            TextView name = new TextView(getActivity());

            name.setText(participant);
            name.setPadding(40,0,0,10);
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            text_layout.addView(name, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        }
        my_root.addView(text_layout, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
    }
}
