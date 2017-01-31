package es.dpinfo.spotlightplace.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.adapters.PlacesAdapter;
import es.dpinfo.spotlightplace.interfaces.IPlaceListMvp;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.presenters.PlacesListPresenter;
import es.dpinfo.spotlightplace.repository.ApiDAO;

/**
 * Created by dprimenko on 3/01/17.
 */
public class ScheduledFragment extends Fragment implements IPlaceListMvp.View, ApiDAO.AsyncApiRequestListener{


    private LinearLayout llScheduled;
    private ListView lwPlaces;
    private PlacesAdapter adapter;
    private PlacesListPresenter presenter;
    private Snackbar snack;

    public ScheduledFragment() {
    }

    @Override
    public PlacesAdapter getAdapter() {
        return adapter;
    }

    public static ScheduledFragment newInstance() {
        return new ScheduledFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_scheduled, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llScheduled = (LinearLayout) view.findViewById(R.id.ll_scheduled);
        lwPlaces = (ListView) view.findViewById(R.id.lw_scheduled_places);

        adapter = new PlacesAdapter(getActivity(), R.layout.item_place, PlacesAdapter.SCHEDULED_ADAPTER);
        lwPlaces.setAdapter(adapter);
        ApiDAO.getInstance().requestAllPlaces(this);
    }

    @Override
    public void setMessageError(int error) {

    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public void onDoInBackground(SpotPlace spotPlace) {
        addPlace(spotPlace);
    }

    public void addPlace(final SpotPlace spotPlace) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (checkDateTime(spotPlace.getmDateTimeFrom(), spotPlace.getmDateTimeTo()) == PlacesAdapter.SCHEDULED_ADAPTER) {
                            adapter.addPlace(spotPlace);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onPostExecute() {
    }

    public int checkDateTime(String from, String to) {
        int result = PlacesAdapter.SCHEDULED_ADAPTER;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateFrom = null;
        Date dateTo = null;
        Calendar now = Calendar.getInstance();
        try {
            dateFrom = formatter.parse(from);
            dateTo = formatter.parse(to);

            if ((now.getTimeInMillis() >= dateFrom.getTime()) && (now.getTimeInMillis() <= dateTo.getTime())) {
                result = PlacesAdapter.IN_PROGRESS_ADAPTER;
            } else if (now.getTimeInMillis() > dateTo.getTime()) {
                result = PlacesAdapter.PAST_ADAPTER;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }
}
