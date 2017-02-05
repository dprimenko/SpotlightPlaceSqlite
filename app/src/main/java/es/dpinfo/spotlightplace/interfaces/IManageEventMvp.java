package es.dpinfo.spotlightplace.interfaces;

import android.support.v4.app.Fragment;

import com.google.android.gms.location.places.Place;

import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.models.User;

/**
 * Created by dprimenko on 27/01/17.
 */
public interface IManageEventMvp {

    interface View {
        void setMessageError(int messageError);
    }

    interface Presenter {
        boolean validateFields(SpotPlace place);
        void uploadPlace(Fragment fragment, SpotPlace spotPlace);
    }
}
