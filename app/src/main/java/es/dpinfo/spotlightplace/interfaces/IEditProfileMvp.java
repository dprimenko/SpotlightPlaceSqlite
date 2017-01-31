package es.dpinfo.spotlightplace.interfaces;


import android.support.v4.app.Fragment;

import es.dpinfo.spotlightplace.preferences.AccountPreferences;

/**
 * Created by dprimenko on 31/01/17.
 */
public interface IEditProfileMvp {

    interface View {
        void setMessageError(int messageError);
    }

    interface Presenter {
        void validateUserData(Fragment fragment, AccountPreferences preferences, String fullName, String nick, String email);
    }
}
