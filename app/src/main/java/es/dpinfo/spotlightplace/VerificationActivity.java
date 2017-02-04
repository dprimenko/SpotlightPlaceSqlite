package es.dpinfo.spotlightplace;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONObject;

import es.dpinfo.spotlightplace.fragments.AskNumberFragment;
import es.dpinfo.spotlightplace.fragments.VerifyNumFragment;
import es.dpinfo.spotlightplace.interfaces.ISetupMvp;
import es.dpinfo.spotlightplace.models.User;
import es.dpinfo.spotlightplace.presenters.SetupPresenter;
import es.dpinfo.spotlightplace.repository.ApiDAO;
import es.dpinfo.spotlightplace.schemas.SpotlightContract;

/**
 * Created by dprimenko on 3/01/17.
 */
public class VerificationActivity extends AppCompatActivity implements AskNumberFragment.AskNumberFragmentListener, VerifyNumFragment.VerifyNumFragmentListener, ApiDAO.SetupUserListener, ISetupMvp.View {

    private AskNumberFragment askNumberFragment;
    private VerifyNumFragment verifyNumFragment;
    private FragmentTransaction ft;
    private SetupPresenter presenter;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadVerificationActivity();
                } else {
                    Toast.makeText(VerificationActivity.this, "INTERNET Denied", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, REQUEST_CODE_ASK_PERMISSIONS);
            } else {
                loadVerificationActivity();
            }
        } else {
            loadVerificationActivity();
        }
    }

    @Override
    public void onVerifyNumFragment(Bundle bundle) {
        verifyNumFragment = VerifyNumFragment.newInstance(bundle);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_verification, verifyNumFragment);
        ft.addToBackStack("ASK_NUM");
        ft.commit();
    }

    @Override
    public void onCodeVerified(Bundle bundle) {

        User user = new User();
        user.setmNumberPhone(bundle.getString(SpotlightContract.UserEntry.KEY_NUMBER_PHONE));
        presenter.checkAccountPreferences(this, user);
    }

    @Override
    public void onSetupUserSuccess(JSONObject response) {
        presenter.saveAccountPreferences(this, response);
    }

    @Override
    public void onSetupUserError(String error) {
        Log.d("Error", error.toString());
        finish();
    }


    @Override
    public void startMainActivity() {
        Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loadAskNumberFragment() {
        setContentView(R.layout.activity_verification);
        askNumberFragment = AskNumberFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_verification, askNumberFragment).commit();
    }

    @Override
    public void closeApp() {
        finish();
    }

    private void loadVerificationActivity() {
        presenter = new SetupPresenter(this);
        presenter.checkAccountPreferences(this);

    }
}
