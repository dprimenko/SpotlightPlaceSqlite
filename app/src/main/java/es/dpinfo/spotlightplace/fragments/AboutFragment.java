package es.dpinfo.spotlightplace.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.adapters.SocialAdapter;
import es.dpinfo.spotlightplace.presenters.PlacesListPresenter;

/**
 * Created by dprimenko on 3/01/17.
 */
public class AboutFragment extends Fragment {

    private PlacesListPresenter.ActionsFragmentListener mCallback;
    private ListView lwSocial;
    private SocialAdapter adapter;


    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (PlacesListPresenter.ActionsFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PlacesListPresenter.ActionsFragmentListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar supportActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.arrow_left);
        supportActionBar.setTitle(getString(R.string.item_main_manu_about));
        supportActionBar.setIcon(null);

        View rootView = view;
        lwSocial = (ListView) rootView.findViewById(R.id.lw_social);
        adapter = new SocialAdapter(getActivity());
        lwSocial.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            mCallback.onMainFragment(null);
        }

        return true;
    }
}
