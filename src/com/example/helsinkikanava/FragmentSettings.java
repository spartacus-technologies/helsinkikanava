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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.TreeSet;

/**
 * Created by vesa on 10.8.2014.
 */
public class FragmentSettings extends Fragment
{
    private Context parent_ = null;
    View rootView_ = null;

    enum SETTINGS {ABOUT,VIDEO};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        rootView_ = rootView;

        createSettings();

        return rootView;
    }

    public FragmentSettings(Context parent)
    {
        parent_ = parent;
    }

    private void createSettings()
    {
        createNewSetting(SETTINGS.ABOUT, "Tietoa sovelluksesta");
        createNewSetting(SETTINGS.VIDEO, "Video");
    }

    private void createNewSetting (SETTINGS setting, String name)
    {
        LinearLayout setting_layout = new LinearLayout( getActivity() );
        setting_layout.setOrientation(LinearLayout.VERTICAL);
        setting_layout.setId(setting.ordinal());

        TextView title = new TextView(getActivity());

        title.setText(name);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        View underline = new View(getActivity());
        underline.setBackgroundColor(Color.BLACK);
        underline.setPadding(0, 0, 0, 5);

        View spacer = new View(getActivity());
        spacer.setBackgroundColor(Color.TRANSPARENT);
        spacer.setPadding(0, 0, 0, 5);

        setting_layout.addView(title, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        setting_layout.addView(underline, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 5));
        setting_layout.addView(spacer, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 5));


        ((LinearLayout) rootView_.findViewById(R.id.fragment_settings_content)).addView(setting_layout, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT));

        createContent(setting);
    }

    private void createContent (SETTINGS setting)
    {
        LinearLayout my_root = (LinearLayout) rootView_.findViewById(setting.ordinal());


        switch (setting)
        {
            case ABOUT:

                LinearLayout text_layout = new LinearLayout(getActivity());
                text_layout.setOrientation( LinearLayout.VERTICAL );

                TextView name = new TextView(getActivity());

                name.setText("Tämän ohjelman versionumero on 0.1\n" +
                        "\n" +
                        "Tämä sovellus hyödyntää open.helsinkikanava.fi:n tarjoamaa avointa dataa. \n" +
                        "\n" +
                        "Ohjelman kehityksestä on vastannut Spartacus Technologies Oy. www.sprtc.us");

                name.setPadding(40,0,0,10);
                name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                text_layout.addView(name, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));

                my_root.addView(text_layout, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
                break;

            case VIDEO:

                LinearLayout text_layout1 = new LinearLayout(getActivity());
                text_layout1.setOrientation( LinearLayout.VERTICAL );

                TextView text = new TextView(getActivity());

                text.setText("Videoiden toistamista varten laitteessasi tulee olla asennettuna kolmannen osapuolen multimediasoitin MX Player.\n" +
                        "\n" + "MX Player on ladattavissa ilmaiseksi Play Storesta.");

                text.setPadding(40,0,0,10);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                text_layout1.addView(text, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));

                my_root.addView(text_layout1, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));

                break;

            default:

                break;

        }
    }
}