package es.dpinfo.spotlightplace.fragments;

import android.accounts.Account;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dpinfo.spotlightplace.MainActivity;
import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.adapters.PlacesAdapter;
import es.dpinfo.spotlightplace.interfaces.IPlaceListMvp;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.preferences.AccountPreferences;
import es.dpinfo.spotlightplace.presenters.PlacesListPresenter;
import es.dpinfo.spotlightplace.repository.ApiDAO;

/**
 * Created by dprimenko on 3/01/17.
 */
public class MainFragment extends Fragment implements IPlaceListMvp.View, ApiDAO.AsyncApiRequestListener{


    private CoordinatorLayout clMainFragment;
    private FloatingActionButton fabAddEvent;
    private MainFragmentListener mCallback;
    private ListView lwPlaces;
    private PlacesAdapter adapter;
    private SwipeRefreshLayout swipe;
    private PlacesListPresenter presenter;
    private View navigationHeader;
    private LinearLayout llNavigation;
    private CircleImageView imvNavigationProfile;
    private TextView txvFullNameNavigation, txvEmailNavigation;
    private Target targetBackground;


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

        final View rootView = view;

        presenter = new PlacesListPresenter(this);

        clMainFragment = (CoordinatorLayout) rootView.findViewById(R.id.cl_main_fragment);
        fabAddEvent = (FloatingActionButton) rootView.findViewById(R.id.fabAddEvent);

        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_list_places);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(R.mipmap.ic_launcher_spotlight);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationHeader = ((MainActivity) getActivity()).getNavigationView().getHeaderView(0);
        imvNavigationProfile = (CircleImageView) navigationHeader.findViewById(R.id.imv_profile_navigation);
        llNavigation = (LinearLayout) navigationHeader.findViewById(R.id.ll_navigation_drawer);
        txvFullNameNavigation = (TextView) navigationHeader.findViewById(R.id.txv_fullname_navigation);
        txvEmailNavigation = (TextView) navigationHeader.findViewById(R.id.txv_email_navigation);

        targetBackground = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                BitmapDrawable drawable = new BitmapDrawable(getActivity().getResources(), bitmap);

                llNavigation.setBackground(drawable);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("Background navigation", "doesnt exists");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                llNavigation.setBackground(getResources().getDrawable(R.drawable.placeholder));
            }
        };
        loadContent(rootView);
        loadInfoNavigation();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_profile_main) {
            mCallback.onProfileFragment();
        } else if (item.getItemId() == android.R.id.home) {
            ((MainActivity)getActivity()).setupDrawerContent();
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

    private void loadInfoNavigation() {

        txvFullNameNavigation.setText(AccountPreferences.getInstance(getActivity()).getFullName());
        txvEmailNavigation.setText(AccountPreferences.getInstance(getActivity()).getEmail());

        Picasso.with(getActivity()).load(AccountPreferences.getInstance(getActivity()).getProfileImg()).into(imvNavigationProfile);
        Picasso.with(getActivity()).load("https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-163604.jpg").into(targetBackground);
    }

    private void loadContent(View view) {
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
}
