package es.dpinfo.spotlightplace.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.dpinfo.spotlightplace.R;

/**
 * Created by dprimenko on 6/02/17.
 */
public class LoadingFragment extends Fragment {

    public static LoadingFragment newInstance() {
        return new LoadingFragment();
    }

    public LoadingFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }
}
