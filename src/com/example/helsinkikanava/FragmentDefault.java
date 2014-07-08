package com.example.helsinkikanava;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//Example class for demostrating Fragment. -Eetu
public class FragmentDefault extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_default, container, false);
        return rootView;
    }

    public FragmentDefault() {
    }
}
