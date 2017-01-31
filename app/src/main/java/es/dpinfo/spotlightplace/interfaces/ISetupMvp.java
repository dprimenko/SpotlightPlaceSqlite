package es.dpinfo.spotlightplace.interfaces;

import android.content.Context;

import org.json.JSONObject;

import es.dpinfo.spotlightplace.models.User;

/**
 * Created by dprimenko on 26/01/17.
 */
public interface ISetupMvp {

    interface View {
        void startMainActivity();
        void closeApp();
    }
    interface Presenter {
        void saveAccountPreferences(Context context, JSONObject object);
        void checkAccountPreferences(Context context);
        void checkAccountPreferences(Context context, User user);
    }
}
