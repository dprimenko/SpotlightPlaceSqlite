package es.dpinfo.spotlightplace.presenters;

import android.text.TextUtils;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.interfaces.IVerifyMvp;

/**
 * Created by dprimenko on 13/01/17.
 */
public class VerifyNumPresenter implements IVerifyMvp.Presenter {

    private IVerifyMvp.View view;

    public VerifyNumPresenter(IVerifyMvp.View view) {
        this.view = view;
    }

    @Override
    public boolean validateNumberPhone(String regionCode, String numberPhone) {
        boolean result = false;

        if (TextUtils.isEmpty(regionCode) || TextUtils.isEmpty(numberPhone)) {
            view.setMessageError(R.string.data_empty);
        }
        else if(numberPhone.length() < 9) {
            view.setMessageError(R.string.invalid_number_phone_length);
        }
        else {
            result = true;
        }

        return result;
    }

    @Override
    public boolean validateSecretCode(String secretCode) {
        boolean result = false;

        if (IVerifyMvp.SECRET_CODE.equals(secretCode)) {
            result = true;
        }
        else {
            view.setMessageError(R.string.wrong_secret_code);
        }

        return result;
    }

    @Override
    public boolean validateUserAndName(String username, String fullName) {
        boolean result = false;

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(fullName)) {
            view.setMessageError(R.string.userDataEmpty);
        } else {
            result = true;
        }

        return result;
    }
}
