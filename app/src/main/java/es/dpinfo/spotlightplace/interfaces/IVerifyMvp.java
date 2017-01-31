package es.dpinfo.spotlightplace.interfaces;

/**
 * Created by dprimenko on 13/01/17.
 */
public interface IVerifyMvp {

    String SECRET_CODE = "R4769";

    interface View {
        void setMessageError(int messageError);
    }

    interface Presenter {
        boolean validateNumberPhone(String regionCode, String numberPhone);
        boolean validateSecretCode(String secretCode);
        boolean validateUserAndName(String username, String fullName);
    }
}
