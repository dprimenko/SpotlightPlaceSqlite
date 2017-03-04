package es.dpinfo.spotlightplace.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.interfaces.IEditProfileMvp;
import es.dpinfo.spotlightplace.preferences.AccountPreferences;
import es.dpinfo.spotlightplace.presenters.EditProfilePresenter;
import es.dpinfo.spotlightplace.presenters.PlacesListPresenter;
import es.dpinfo.spotlightplace.repository.ApiDAO;

/**
 * Created by dprimenko on 30/01/17.
 */
public class EditProfileFragment extends Fragment implements ApiDAO.UpdateUserApiRequestListener, IEditProfileMvp.View {

    private EditProfileFragmentListener editProfileFragmentListener;
    private RelativeLayout rlEditProfile;
    private EditText edtUpdateFullName, edtUpdateNick, edtUpdateEmail;
    private CircleImageView imvEditProfile;
    private AccountPreferences preferences;
    private EditProfileFragmentListener mCallback;
    private PlacesListPresenter.ActionsFragmentListener callbackMain;
    private EditProfilePresenter presenter;


    public interface EditProfileFragmentListener {
        void onProfileFragment();
    }

    public static EditProfileFragment newInstance(Bundle bundle) {

        EditProfileFragment editProfileFragment = new EditProfileFragment();
        editProfileFragment.setArguments(bundle);
        return editProfileFragment;
    }

    public EditProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = new EditProfilePresenter(this);
        Log.d("BackStackEdt", String.valueOf(getActivity().getSupportFragmentManager().getBackStackEntryCount()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (EditProfileFragmentListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement EditProfileFragmentListener");
        }

        try {
            callbackMain = (PlacesListPresenter.ActionsFragmentListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PlacesListPresenter.ActionsFragmentListener");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View rootView = view;

        preferences = AccountPreferences.getInstance(getActivity());

        rlEditProfile = (RelativeLayout) rootView.findViewById(R.id.rl_edit_profile);
        edtUpdateFullName = (EditText) rootView.findViewById(R.id.edt_fullname_profile);
        edtUpdateNick = (EditText) rootView.findViewById(R.id.edt_nick_profile);
        edtUpdateEmail = (EditText) rootView.findViewById(R.id.edt_email_profile);
        imvEditProfile = (CircleImageView) rootView.findViewById(R.id.imv_edit_profile);

        if (getArguments().getBoolean("initial")) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Initial configuration");
        } else {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit profile");
        }

        loadUserInfo();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_manage_event, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_manage_event) {
            presenter.validateUserData(this, preferences, edtUpdateFullName.getText().toString(), edtUpdateNick.getText().toString(), edtUpdateEmail.getText().toString());
        } else if(item.getItemId() == android.R.id.home) {
            mCallback.onProfileFragment();
        }

        return true;
    }

    private void loadUserInfo() {
        Picasso.with(getActivity()).load(preferences.getProfileImg()).into(imvEditProfile);
        edtUpdateFullName.setText(preferences.getFullName());
        edtUpdateNick.setText(preferences.getUsername());
        edtUpdateEmail.setText(preferences.getEmail());
    }


    @Override
    public void onUpdateUserResponseSuccess() {
        if (getArguments().getBoolean("initial")) {
            callbackMain.onMainFragment(null);
        } else {
            mCallback.onProfileFragment();
        }

        this.onDestroy();
    }

    @Override
    public void onUpdateResponseError(String error) {
        Log.d("ErrorUploadUser", error);
    }

    @Override
    public void setMessageError(int messageError) {
        Snackbar.make(rlEditProfile, messageError, Snackbar.LENGTH_LONG).show();
    }
}
