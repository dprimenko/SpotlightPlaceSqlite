package es.dpinfo.spotlightplace.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.models.User;
import es.dpinfo.spotlightplace.adapters.PlacesAdapter;

/**
 * Created by dprimenko on 14/01/17.
 */
public class ApiDAO {

    private RequestQueue queue;
    private static final String URL_PLACES = "http://192.168.1.111:3000/api/v1/events";
    private static final String URL_USERS = "http://192.168.1.111:3000/api/v1/users/";
    public static final String URL_USER_BYID = "http://192.168.1.111:3000/api/v1/user/id/";
    public static final String URL_USER_BYNUMBER = "http://192.168.1.111:3000/api/v1/user/numberphone/";
    private static final String URL_SETUP = "http://192.168.1.111:3000/api/v1/setup/";
    private static final String URL_ADDRESS_START = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";
    private static final String URL_ADDRESS_END = "&sensor=true";
    private JSONArray jsonArray;
    private JSONArray jsonArrayUser;
    private JSONObject jsonObject;
    private AsyncApiRequestListener asyncApiRequestListener;
    private GmapsRequestStatus gmapsRequestStatus;
    private UserApiRequestListener userApiRequestListener;
    private SetupUserListener setupUserListener;
    private UploadPlaceListener uploadPlaceListener;
    private UpdateUserApiRequestListener updateUserApiRequestListener;


    public interface UploadPlaceListener {
        void onUploadPlaceSuccess(JSONObject response);
        void onUploadPlaceError(String error);
    }

    public interface AsyncApiRequestListener {
        void onPreExecute();
        void onDoInBackground(SpotPlace spotPlace);
        void onPostExecute();
    }

    public interface PlacesApiRequestStatus {
        void onResponseSuccess(JSONArray response);
        void onErrorResponse(CharSequence messageError);
    }

    public interface UserApiRequestListener {
        void onResponseSuccess(JSONArray response);
    }

    public interface UpdateUserApiRequestListener {
        void onResponseSuccess();
        void onResponseError(String error);
    }

    public interface SetupUserListener {
        void onSuccess(JSONObject jsonObject);
        void onError(String error);
    }

    public interface GmapsRequestStatus {
        void onResponseSuccess(JSONObject response);
        void onErrorResponse(CharSequence messageError);
    }

    public interface DownloadPlaces {
        void onSuccessDownload();
        void onErrorDownload(String messageError);
    }

    private static ApiDAO ourInstance = new ApiDAO();

    public static ApiDAO getInstance() {
        return ourInstance;
    }

    private ApiDAO() {
    }

    public SpotPlace parseJsonPlace(Context context, String stringJson) {

        JSONObject jsonObject = null;
        SpotPlace spotPlace = new SpotPlace();

        try {
            jsonObject = new JSONObject(stringJson);
            spotPlace.setmId(jsonObject.getString("_id"));
            spotPlace.setmCreatorId(jsonObject.getString("creator"));
            spotPlace.setmTitle(jsonObject.getString("title"));
            spotPlace.setmImg(jsonObject.getString("img"));
            spotPlace.setmAddress(jsonObject.getString("address"));
            spotPlace.setmDescription(jsonObject.getString("description"));
            spotPlace.setmCategory(jsonObject.getString("category"));
            spotPlace.setmDateTimeFrom(jsonObject.getString("datetime_from"));
            spotPlace.setmDateTimeTo(jsonObject.getString("datetime_to"));

            JSONArray peopleArray = jsonObject.getJSONArray("people_in");
            List<String> people = new ArrayList<>();

            for (int i = 0; i < peopleArray.length(); i++) {
                people.add(peopleArray.getString(i));
            }
            spotPlace.setmNpeople(people);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return spotPlace;
    }

    private void getAddressData(Context context, String address) {

        queue = Volley.newRequestQueue(context);

        jsonObject = null;

        String requestUrl = (URL_ADDRESS_START + address + URL_ADDRESS_END);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        gmapsRequestStatus.onResponseSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        gmapsRequestStatus.onErrorResponse(error.toString());
                    }
                }
        );
        queue.add(getRequest);
    }

    private void postUser(Context context, final User user) {
        queue = Volley.newRequestQueue(context);

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(user.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(
                Request.Method.POST, URL_SETUP, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setupUserListener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setupUserListener.onError(error.toString());
                    }
                });

        queue.add(postRequest);
    }

    private void putUser(Context context, final User user) {
        queue = Volley.newRequestQueue(context);

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(user.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest putRequest = new JsonObjectRequest(
                Request.Method.PUT,
                (URL_USER_BYID + user.getmId()),
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        updateUserApiRequestListener.onResponseSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        updateUserApiRequestListener.onResponseError(error.toString());
                    }
                }
        );
        queue.add(putRequest);
    }

    private void postPlace(Context context, SpotPlace spotPlace) {
        queue = Volley.newRequestQueue(context);

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(spotPlace.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(
                Request.Method.POST, URL_PLACES, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        uploadPlaceListener.onUploadPlaceSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        uploadPlaceListener.onUploadPlaceError(error.toString());
                    }
                });

        queue.add(postRequest);
    }

    public void setAddressFormatted(PlacesAdapter adapter, String address) {
        try {
            gmapsRequestStatus = (GmapsRequestStatus) adapter;
        } catch (ClassCastException e) {
            throw new ClassCastException(adapter.getClass().getName() + " must implement GmapsRequestStatus");
        }

        getAddressData(adapter.getContext(), address);
    }


    public void requestAllPlaces(Fragment fragment) {

        try {
            asyncApiRequestListener = (AsyncApiRequestListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString() + " must implement AsyncApiRequestListener");
        }
        new GetPlaces().execute(fragment.getActivity());
    }

    public void uploadPlace(Fragment fragment, SpotPlace spotPlace) {
        try {
            uploadPlaceListener = (UploadPlaceListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString() + " must implement UploadPlaceListener");
        }

        postPlace(fragment.getActivity(), spotPlace);
    }

    public void setupUser(Context context, User user) {
        try {
            setupUserListener = (SetupUserListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SetupUserListener");
        }

        postUser(context, user);
    }

    public void updateUser(Fragment fragment, User user) {
        try {
            updateUserApiRequestListener = (UpdateUserApiRequestListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString() + " must implement UpdateUserApiRequestListener");
        }

        putUser(fragment.getActivity(), user);
    }

    private ArrayList<String> jsonArrayToArrayList(JSONArray jsonArray) {
        ArrayList<String> array = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                array.add(jsonArray.getString(i));
            } catch (JSONException e) {

            }
        }
        return array;
    }

    public class GetPlaces extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            asyncApiRequestListener.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Context... contexts) {
            boolean error = false;

            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL_PLACES);

            try {
                HttpResponse execute = client.execute(httpGet);
                InputStream iStream = execute.getEntity().getContent();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                boolean endObj = false;
                int s = 0;
                char c = ' ';
                String jsonObj = "";
                s = br.read();
                while ((s = br.read()) != -1) {

                    if (endObj) {
                        endObj = false;
                        SpotPlace spotPlace = parseJsonPlace(contexts[0], jsonObj);
                        asyncApiRequestListener.onDoInBackground(spotPlace);
                        jsonObj = new String();
                    } else {
                        if (s == 125) {
                            endObj = true;
                        }
                        c = (char) s;
                        jsonObj += String.valueOf(c);
                    }
                }
            } catch (Exception e) {
                error = true;
                e.printStackTrace();
            }

            return error;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            asyncApiRequestListener.onPostExecute();
        }
    }
}
