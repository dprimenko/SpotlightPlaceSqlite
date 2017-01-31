package es.dpinfo.spotlightplace.interfaces;

import es.dpinfo.spotlightplace.adapters.PlacesAdapter;
import es.dpinfo.spotlightplace.models.SpotPlace;

/**
 * Created by dprimenko on 18/1/17.
 */

public interface IPlaceListMvp {

    interface View {
        PlacesAdapter getAdapter();
        void setMessageError(int error);
    }

    interface Presenter {
        void addPlaceToAdapter(SpotPlace spotPlace);
    }
}
