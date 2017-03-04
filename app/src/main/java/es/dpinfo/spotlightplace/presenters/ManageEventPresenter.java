package es.dpinfo.spotlightplace.presenters;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.google.android.gms.location.places.Place;

import org.w3c.dom.Text;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.interfaces.IManageEventMvp;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.models.User;
import es.dpinfo.spotlightplace.repository.ApiDAO;

/**
 * Created by dprimenko on 27/01/17.
 */
public class ManageEventPresenter implements IManageEventMvp.Presenter {

    private IManageEventMvp.View view;

    public ManageEventPresenter(IManageEventMvp.View view) {
        this.view = view;
    }

    @Override
    public boolean validateFields(SpotPlace place) {

        boolean result = false;

        if (TextUtils.isEmpty(place.getmTitle())) {
            view.setMessageError(R.string.data_empty);
        } else {
            result = true;
        }

        return result;
    }

    @Override
    public void uploadPlace(Fragment fragment, SpotPlace spotPlace) {
        ApiDAO.getInstance().uploadPlace(fragment, spotPlace);
    }
}
