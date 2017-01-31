package es.dpinfo.spotlightplace.presenters;

import es.dpinfo.spotlightplace.interfaces.IPlaceListMvp;
import es.dpinfo.spotlightplace.models.SpotPlace;

/**
 * Created by dprimenko on 18/1/17.
 */

public class PlacesListPresenter implements IPlaceListMvp.Presenter {

    private IPlaceListMvp.View view;

    public interface ActionsFragmentListener {
        void onMainFragment();
    }


    public PlacesListPresenter(IPlaceListMvp.View view) {
        this.view = view;
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
