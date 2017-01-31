package es.dpinfo.spotlightplace.presenters;

import android.support.v4.app.Fragment;

import es.dpinfo.spotlightplace.interfaces.IManageEventMvp;
import es.dpinfo.spotlightplace.models.SpotPlace;
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
    public void uploadPlace(Fragment fragment, SpotPlace spotPlace) {
        ApiDAO.getInstance().uploadPlace(fragment, spotPlace);
    }
}
