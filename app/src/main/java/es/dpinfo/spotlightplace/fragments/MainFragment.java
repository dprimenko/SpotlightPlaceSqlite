package es.dpinfo.spotlightplace.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import es.dpinfo.spotlightplace.R;

/**
 * Created by dprimenko on 3/01/17.
 */
public class MainFragment extends Fragment {

    private Toolbar toolbarMain;
    private CoordinatorLayout clMainFragment;
    private TabLayout tabLayoutMain;
    private ViewPager viewPagerMain;
    private FloatingActionButton fabAddEvent;
    private MainFragmentListener mCallback;


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
        //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

        clMainFragment = (CoordinatorLayout) rootView.findViewById(R.id.cl_main_fragment);
        tabLayoutMain = (TabLayout) rootView.findViewById(R.id.tabsMain);
        viewPagerMain = (ViewPager) rootView.findViewById(R.id.viewpagerMain);
        fabAddEvent = (FloatingActionButton) rootView.findViewById(R.id.fabAddEvent);
        toolbarMain = (Toolbar) rootView.findViewById(R.id.toolbarMain);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarMain);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(R.mipmap.ic_launcher_spotlight);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Spotlight Place");

        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onManageEventFragment();
            }
        });
        setupTabLayout();
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

    private void setupTabLayout() {
        setupViewPager(viewPagerMain);
        tabLayoutMain.setupWithViewPager(viewPagerMain);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getChildFragmentManager());

        viewPagerAdapter.addFragment(new InProgressFragment(), getString(R.string.tab_in_progress));
        viewPagerAdapter.addFragment(new ScheduledFragment(), getString(R.string.tab_scheduled));

        viewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
