package es.dpinfo.spotlightplace.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.preferences.AccountPreferences;
import es.dpinfo.spotlightplace.presenters.PlacesListPresenter;

/**
 * Created by dprimenko on 29/01/17.
 */
public class ProfileFragment extends Fragment {

    private CircleImageView imvProfileImg;
    private TextView txvUserFullName;
    private PlacesListPresenter.ActionsFragmentListener mCallback;
    private ProfileFragmentListener profileFragmentListener;


    public interface ProfileFragmentListener {
        void onEditProfileFragment();
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("BackStackPro", String.valueOf(getActivity().getSupportFragmentManager().getBackStackEntryCount()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (PlacesListPresenter.ActionsFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PlacesListPresenter.ActionsFragmentListener");
        }

        try {
            profileFragmentListener = (ProfileFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ProfileFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imvProfileImg = (CircleImageView) view.findViewById(R.id.imv_profile_img);
        txvUserFullName = (TextView) view.findViewById(R.id.txv_user_fullname);
        setProfileInfo();
    }



    private void setProfileInfo() {

        AccountPreferences preferences = AccountPreferences.getInstance(getActivity());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(null);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(preferences.getUsername());
        Picasso.with(getActivity()).load(preferences.getProfileImg()).into(imvProfileImg);
        txvUserFullName.setText(preferences.getFullName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_profile, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_edit_profile) {
            profileFragmentListener.onEditProfileFragment();
        } else if(item.getItemId() == android.R.id.home) {
            mCallback.onMainFragment();
        }

        return true;
    }
}
