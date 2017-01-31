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

import es.dpinfo.spotlightplace.interfaces.ISetupMvp;
import es.dpinfo.spotlightplace.models.User;
import es.dpinfo.spotlightplace.preferences.AccountPreferences;
import es.dpinfo.spotlightplace.repository.ApiDAO;

/**
 * Created by dprimenko on 26/01/17.
 */
public class SetupPresenter implements ISetupMvp.Presenter {

    private ISetupMvp.View view;
    private SetupPresenter.RequestUserDataListener requestUserDataListener;
    private RequestQueue queue;

    public interface RequestUserDataListener {
        void onResponseSuccess(JSONObject jsonObj);
        void onErrorResponse(String error);
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
                public void onResponseSuccess(JSONObject jsonObj) {

                    if (jsonObj.length() > 0) {
                        String number = "";

                        try {
                            number = jsonObj.getString("number_phone");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (AccountPreferences.getInstance(context).getNumberPhone().equals(number)) {
                            if(updateSharedPreferences(context, jsonObj)) {
                                view.startMainActivity();
                            }

                        }
                    }
                }

                @Override
                public void onErrorResponse(String error) {

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
                                    requestUserDataListener.onResponseSuccess(jsonObj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
            );

            queue.add(getRequest);

        }
    }

    @Override
    public void checkAccountPreferences(final Context context, final User user) {
        requestUserDataListener = new SetupPresenter.RequestUserDataListener() {
            @Override
            public void onResponseSuccess(JSONObject jsonObj) {
                if(updateSharedPreferences(context, jsonObj)) {
                    view.startMainActivity();
                }

            }

            @Override
            public void onErrorResponse(String error) {
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
                                requestUserDataListener.onResponseSuccess(jsonObj);
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

                        requestUserDataListener.onErrorResponse(error.toString());
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
        String nick = "";
        String fullName = "";
        String email = "";
        String typeAcc = "";
        String apiKey = "";

        try {
            id = jsonObj.get("_id").toString();
            numberPhone = jsonObj.getString("number_phone");
            profileImg = jsonObj.getString("profile_img");
            nick = jsonObj.getString("nick");
            fullName = jsonObj.getString("full_name");
            email = jsonObj.getString("email");
            typeAcc = jsonObj.getString("type_acc");
            apiKey = jsonObj.getString("sp_api_key_id");

            result = true;
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        AccountPreferences.getInstance(context).putId(id);
        AccountPreferences.getInstance(context).putNumberPhone(numberPhone);
        AccountPreferences.getInstance(context).putProfileImg(profileImg);
        AccountPreferences.getInstance(context).putNick(nick);
        AccountPreferences.getInstance(context).putFullName(fullName);
        AccountPreferences.getInstance(context).putEmail(email);
        AccountPreferences.getInstance(context).putTypeAcc(typeAcc);
        AccountPreferences.getInstance(context).putApiKey(apiKey);

        return result;
    }
}
