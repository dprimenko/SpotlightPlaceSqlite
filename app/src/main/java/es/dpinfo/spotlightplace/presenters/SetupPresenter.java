package es.dpinfo.spotlightplace.presenters;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.interfaces.ISetupMvp;
import es.dpinfo.spotlightplace.models.User;
import es.dpinfo.spotlightplace.preferences.AccountPreferences;
import es.dpinfo.spotlightplace.repository.ApiDAO;
import es.dpinfo.spotlightplace.schemas.SpotlightContract;

/**
 * Created by dprimenko on 26/01/17.
 */
public class SetupPresenter implements ISetupMvp.Presenter {

    private ISetupMvp.View view;
    private SetupPresenter.RequestUserDataListener requestUserDataListener;
    private RequestQueue queue;

    public interface RequestUserDataListener {
        void onRequestUserResponseSuccess(JSONObject jsonObj);
        void onRequestUserErrorResponse(String error);
    }

    public SetupPresenter (ISetupMvp.View view) {
        this.view = view;
    }

    @Override
    public void saveAccountPreferences(Context context, JSONObject object) {

        if(updateSharedPreferences(context, object)) {
            view.startMainActivity();
        } else {
            view.closeApp();
        }

    }

    @Override
    public void checkAccountPreferences(final Context context) {

        if (AccountPreferences.getInstance(context).accountPrefsExists()) {

            requestUserDataListener = new SetupPresenter.RequestUserDataListener() {
                @Override
                public void onRequestUserResponseSuccess(JSONObject jsonObj) {

                    if (jsonObj.length() > 0) {
                        String number = "";

                        try {
                            number = jsonObj.getString(SpotlightContract.UserEntry.KEY_NUMBER_PHONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (AccountPreferences.getInstance(context).getNumberPhone().equals(number)) {
                            if(updateSharedPreferences(context, jsonObj)) {
                                view.startMainActivity();
                            }  else {
                                view.loadAskNumberFragment();
                            }
                        }
                    }
                }

                @Override
                public void onRequestUserErrorResponse(String error) {
                    Log.d("Error", error);
                    view.setMessageError(R.string.connection_error);
                }
            };

            queue = Volley.newRequestQueue(context);

            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, ApiDAO.URL_USER_BYNUMBER + AccountPreferences.getInstance(context).getNumberPhone(), null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response) {

                            if (response.length() > 0) {

                                JSONObject jsonObj = null;

                                try {
                                    jsonObj = response.getJSONObject(0);
                                    requestUserDataListener.onRequestUserResponseSuccess(jsonObj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                view.loadAskNumberFragment();
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            requestUserDataListener.onRequestUserErrorResponse(error.toString());
                        }
                    }
            );

            queue.add(getRequest);

        } else {
            view.loadAskNumberFragment();
        }
    }

    @Override
    public void checkAccountPreferences(final Context context, final User user) {
        requestUserDataListener = new SetupPresenter.RequestUserDataListener() {
            @Override
            public void onRequestUserResponseSuccess(JSONObject jsonObj) {
                if(updateSharedPreferences(context, jsonObj)) {
                    view.startMainActivity();
                } else {
                    view.loadAskNumberFragment();
                }

            }

            @Override
            public void onRequestUserErrorResponse(String error) {
                Log.e("Error", error);
            }
        };

        queue = Volley.newRequestQueue(context);

        String numberPhone = user.getmNumberPhone();
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, (ApiDAO.URL_USER_BYNUMBER + numberPhone), null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response.length() > 0) {
                            JSONObject jsonObj = null;

                            try {
                                jsonObj = response.getJSONObject(0);
                                requestUserDataListener.onRequestUserResponseSuccess(jsonObj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ApiDAO.getInstance().setupUser(context, user);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        requestUserDataListener.onRequestUserErrorResponse(error.toString());
                    }
                }
        );

        queue.add(getRequest);
    }

    private boolean updateSharedPreferences(Context context, JSONObject jsonObj) {

        boolean result = false;

        String id = "";
        String numberPhone = "";
        String profileImg = "";
        String backgroundImg = "";
        String username = "";
        String fullName = "";
        String email = "";
        String typeAcc = "";
        String apiKey = "";
        try {
            id = jsonObj.get(SpotlightContract.UserEntry.KEY_ID).toString();
            numberPhone = jsonObj.getString(SpotlightContract.UserEntry.KEY_NUMBER_PHONE);
            profileImg = jsonObj.getString(SpotlightContract.UserEntry.KEY_PROFILE_IMG);
            backgroundImg = jsonObj.getString(SpotlightContract.UserEntry.KEY_BACKGROUND_IMG);
            username = jsonObj.getString(SpotlightContract.UserEntry.KEY_USERNAME);
            fullName = jsonObj.getString(SpotlightContract.UserEntry.KEY_FULLNAME);
            email = jsonObj.getString(SpotlightContract.UserEntry.KEY_EMAIL);
            typeAcc = jsonObj.getString(SpotlightContract.UserEntry.KEY_TYPEACC);
            apiKey = jsonObj.getString(SpotlightContract.UserEntry.KEY_PUBLIC_API_KEY);

            AccountPreferences.getInstance(context).putId(id);
            AccountPreferences.getInstance(context).putNumberPhone(numberPhone);
            AccountPreferences.getInstance(context).putProfileImg(profileImg);
            AccountPreferences.getInstance(context).putBackgroundImg(backgroundImg);
            AccountPreferences.getInstance(context).putUsername(username);
            AccountPreferences.getInstance(context).putFullName(fullName);
            AccountPreferences.getInstance(context).putEmail(email);
            AccountPreferences.getInstance(context).putTypeAcc(typeAcc);
            AccountPreferences.getInstance(context).putPublicApiKey(apiKey);

            result = true;
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }



        return result;
    }
}
