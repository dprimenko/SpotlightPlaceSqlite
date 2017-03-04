package es.dpinfo.spotlightplace.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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

import de.hdodenhof.circleimageview.CircleImageView;
import es.dpinfo.spotlightplace.MainActivity;
import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.adapters.PlacesAdapter;
import es.dpinfo.spotlightplace.adapters.PlacesCursorAdapter;
import es.dpinfo.spotlightplace.interfaces.IListPlacesPresenter;
import es.dpinfo.spotlightplace.interfaces.IListPresenter;
import es.dpinfo.spotlightplace.interfaces.IPlaceListMvp;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.preferences.AccountPreferences;
import es.dpinfo.spotlightplace.presenters.ListPlacesPresenter;
import es.dpinfo.spotlightplace.presenters.PlacesListPresenter;
import es.dpinfo.spotlightplace.repository.ApiDAO;

/**
 * Created by dprimenko on 3/01/17.
 */
public class RelevantFragment extends Fragment implements IListPlacesPresenter.View {


    private CoordinatorLayout clMainFragment;
    private FloatingActionButton fabAddEvent;
    private MainFragmentListener mCallback;
    private ListView lwPlaces;
    private PlacesCursorAdapter adapter;
    private ListPlacesPresenter presenter;
    private View navigationHeader;
    private LinearLayout llNavigation;
    private CircleImageView imvNavigationProfile;
    private TextView txvFullNameNavigation, txvEmailNavigation;
    private Target targetBackground;

    @Override
    public void showMessageError(int error) {
        Snackbar.make(clMainFragment, getString(error), Snackbar.LENGTH_LONG).show();
    }


    public interface MainFragmentListener {
        void onManageEventFragment();
        void onProfileFragment();
    }

    public static RelevantFragment newInstance(Bundle bundle) {
        RelevantFragment fragment = new RelevantFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        adapter = new PlacesCursorAdapter(getActivity(), null, IListPresenter.PLACE);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            presenter.addPlace((SpotPlace) bundle.getParcelable("place"));
        }
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
        return inflater.inflate(R.layout.fragment_relevant, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final View rootView = view;

        presenter = new ListPlacesPresenter(this);

        clMainFragment = (CoordinatorLayout) rootView.findViewById(R.id.cl_main_fragment);
        fabAddEvent = (FloatingActionButton) rootView.findViewById(R.id.fab_add_place);


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
                llNavigation.setBackground(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                llNavigation.setBackground(placeHolderDrawable);
            }
        };
        loadContent(rootView);
        loadInfoNavigation();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        ((MainActivity)getActivity()).toolbarMain.setVisibility(View.VISIBLE);
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


    private void loadInfoNavigation() {

        AccountPreferences accPreferences = AccountPreferences.getInstance(getActivity());

        txvFullNameNavigation.setText(accPreferences.getFullName());
        txvEmailNavigation.setText(accPreferences.getEmail());

        Picasso.with(getActivity()).load(accPreferences.getProfileImg()).into(imvNavigationProfile);
        Picasso.with(getActivity()).load(accPreferences.getBackgroundImg()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(targetBackground);
    }

    private void loadContent(View view) {
        lwPlaces = (ListView) view.findViewById(R.id.lw_places);

        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onManageEventFragment();
            }
        });

        presenter.getAllFields(adapter);

        lwPlaces.setAdapter(adapter);
    }

    @Override
    public CursorAdapter getCursorAdapter() {
        return adapter;
    }

    @Override
    public void setCursor(Cursor cursor) {
        if (cursor != null) {
            adapter.swapCursor(cursor);
        }
    }
}
