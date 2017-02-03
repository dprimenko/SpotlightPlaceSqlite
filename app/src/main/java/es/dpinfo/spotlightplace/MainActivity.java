package es.dpinfo.spotlightplace;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import es.dpinfo.spotlightplace.fragments.EditProfileFragment;
import es.dpinfo.spotlightplace.fragments.MainFragment;
import es.dpinfo.spotlightplace.fragments.ManageEventFragment;
import es.dpinfo.spotlightplace.fragments.ProfileFragment;
import es.dpinfo.spotlightplace.preferences.AccountPreferences;
import es.dpinfo.spotlightplace.presenters.PlacesListPresenter;

/**
 * Created by dprimenko on 3/01/17.
 */
public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentListener, PlacesListPresenter.ActionsFragmentListener, EditProfileFragment.EditProfileFragmentListener, ProfileFragment.ProfileFragmentListener {

    private FrameLayout flMain;
    private MainFragment mainFragment;
    private ManageEventFragment manageEventFragment;
    private ProfileFragment profileFragment;
    private EditProfileFragment editProfileFragment;
    private FragmentTransaction ft;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbarMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_navigation);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbarMain = (Toolbar) findViewById(R.id.toolbar_main);
        flMain = (FrameLayout) findViewById(R.id.fl_main);

        setSupportActionBar(toolbarMain);

        if (AccountPreferences.getInstance(this).getNick().length() < 1) {

            Bundle bundle = new Bundle();
            bundle.putBoolean("initial", true);

            editProfileFragment = EditProfileFragment.newInstance(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, editProfileFragment).commit();
        } else {

            mainFragment = MainFragment.newInstance(null);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, mainFragment).commit();
        }

    }

    @Override
    public void onManageEventFragment() {

        manageEventFragment = ManageEventFragment.newInstance(null);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_main, manageEventFragment);
        ft.addToBackStack("HOME");
        ft.commit();
    }

    @Override
    public void onProfileFragment() {
        profileFragment = ProfileFragment.newInstance();

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_main, profileFragment);
        ft.addToBackStack("HOME");
        ft.commit();
    }

    @Override
    public void onMainFragment() {

        mainFragment = MainFragment.newInstance(null);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_main, mainFragment);
        ft.commit();
    }

    @Override
    public void onEditProfileFragment() {

        Bundle bundle = new Bundle();
        bundle.putBoolean("initial", false);

        editProfileFragment = EditProfileFragment.newInstance(bundle);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_main, editProfileFragment);
        ft.addToBackStack("PROFILE");
        ft.commit();
    }

    public void setupDrawerContent() {
        drawerLayout.openDrawer(GravityCompat.START);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);

                switch (item.getItemId()) {
                    case R.id.action_profile_main:
                        onProfileFragment();
                        break;
                    case R.id.action_relevant:
                        break;
                    case R.id.action_inprogress:
                        break;
                    case R.id.action_scheduled:
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    /*@Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 2) {
            onMainFragment();
        } else {
            super.onBackPressed();
        }
    }*/
}
