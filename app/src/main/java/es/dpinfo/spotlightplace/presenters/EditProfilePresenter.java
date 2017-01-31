package es.dpinfo.spotlightplace.presenters;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.interfaces.IEditProfileMvp;
import es.dpinfo.spotlightplace.models.User;
import es.dpinfo.spotlightplace.preferences.AccountPreferences;
import es.dpinfo.spotlightplace.repository.ApiDAO;

/**
 * Created by dprimenko on 31/01/17.
 */
public class EditProfilePresenter implements IEditProfileMvp.Presenter {

    private IEditProfileMvp.View view;

    public EditProfilePresenter(IEditProfileMvp.View view) {
        this.view = view;
    }

    @Override
    public void validateUserData(Fragment fragment, AccountPreferences preferences, String fullName, String nick, String email) {
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(nick) || TextUtils.isEmpty(email)) {
            view.setMessageError(R.string.userDataEmpty);
        } else {
            preferences.putFullName(fullName);
            preferences.putNick(nick);
            preferences.putEmail(email);

            User user = new User();

            user.setmId(preferences.getId());
            user.setmNumberPhone(preferences.getNumberPhone());
            user.setmProfileImg(preferences.getProfileImg());
            user.setmNick(preferences.getNick());
            user.setmFullName(preferences.getFullName());
            user.setmEmail(preferences.getEmail());
            user.setmTypeAcc(preferences.getTypeAcc());
            user.setmLastLogin(preferences.getLastLogin());
            user.setmCreated(preferences.getCreatedOn());

            ApiDAO.getInstance().updateUser(fragment, user);
        }
    }
}
