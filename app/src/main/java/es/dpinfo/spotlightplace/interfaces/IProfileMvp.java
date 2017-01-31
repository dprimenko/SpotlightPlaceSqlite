package es.dpinfo.spotlightplace.interfaces;

import android.os.Bundle;

/**
 * Created by dprimenko on 29/01/17.
 */
public interface IProfileMvp {

    interface View {

    }

    interface Presenter {
        void updateAccountPreferenes(Bundle bundle);
    }
}
