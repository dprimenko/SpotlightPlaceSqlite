package es.dpinfo.spotlightplace.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import es.dpinfo.spotlightplace.interfaces.IPlaceListMvp;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.repository.ApiDAO;

/**
 * Created by dprimenko on 18/1/17.
 */

public class PlacesListPresenter implements IPlaceListMvp.Presenter {

    private IPlaceListMvp.View view;

    public interface ActionsFragmentListener {
        void onMainFragment(Bundle bundle);
    }

    public PlacesListPresenter(IPlaceListMvp.View view) {
        this.view = view;
    }

    @Override
    public void requestPlaces(Fragment activity) {
        ApiDAO.getInstance().requestAllPlaces(activity);
    }

    @Override
    public void addPlaceToAdapter(SpotPlace spotPlace) {
        try {
            view.getAdapter().add(spotPlace);
        } catch (Exception e) {
            view.setMessageError(1);
        }

    }
}
