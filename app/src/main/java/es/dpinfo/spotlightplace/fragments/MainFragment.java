package es.dpinfo.spotlightplace.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.adapters.PlacesAdapter;
import es.dpinfo.spotlightplace.interfaces.IPlaceListMvp;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.presenters.PlacesListPresenter;
import es.dpinfo.spotlightplace.repository.ApiDAO;

/**
 * Created by dprimenko on 3/01/17.
 */
public class MainFragment extends Fragment implements IPlaceListMvp.View, ApiDAO.AsyncApiRequestListener{

    private Toolbar toolbarMain;
    private CoordinatorLayout clMainFragment;
    private FloatingActionButton fabAddEvent;
    private MainFragmentListener mCallback;
    private ListView lwPlaces;
    private PlacesAdapter adapter;
    private SwipeRefreshLayout swipe;
    private PlacesListPresenter presenter;

    @Override
    public PlacesAdapter getAdapter() {
        return adapter;
    }


    public interface MainFragmentListener {
        void onManageEventFragment();
        void onProfileFragment();
    }

    public static MainFragment newInstance(Bundle bundle) {
        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MainFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View rootView = view;

        presenter = new PlacesListPresenter(this);

        clMainFragment = (CoordinatorLayout) rootView.findViewById(R.id.cl_main_fragment);
        fabAddEvent = (FloatingActionButton) rootView.findViewById(R.id.fabAddEvent);
        toolbarMain = (Toolbar) rootView.findViewById(R.id.toolbarMain);
        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_list_places);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarMain);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(R.mipmap.ic_launcher_spotlight);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Spotlight Place");

        lwPlaces = (ListView) view.findViewById(R.id.lw_places);

        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onManageEventFragment();
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter = new PlacesAdapter(getActivity(), R.layout.item_place);
                lwPlaces.setAdapter(adapter);

                presenter.requestPlaces(MainFragment.this);
            }
        });

        swipe.post(new Runnable() {
            @Override
            public void run() {

                adapter = new PlacesAdapter(getActivity(), R.layout.item_place);
                lwPlaces.setAdapter(adapter);

                presenter.requestPlaces(MainFragment.this);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_profile_main) {
            mCallback.onProfileFragment();
        }

        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPreExecute() {
        swipe.setRefreshing(true);
    }

    @Override
    public void onDoInBackground(SpotPlace spotPlace) {
        addPlace(spotPlace);
    }

    @Override
    public void onPostExecute() {
        swipe.setRefreshing(false);
    }

    public void addPlace(final SpotPlace spotPlace) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        presenter.addPlaceToAdapter(spotPlace);
                    }
                });
            }
        }).start();
    }

    @Override
    public void setMessageError(int error) {
        Snackbar.make(clMainFragment, getString(error), Snackbar.LENGTH_LONG).show();
    }
}
