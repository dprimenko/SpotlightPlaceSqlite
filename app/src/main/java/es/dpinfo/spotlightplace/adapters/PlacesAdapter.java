package es.dpinfo.spotlightplace.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.repository.ApiDAO;
import es.dpinfo.spotlightplace.schemas.SpotlightContract;

/**
 * Created by usuario on 13/01/17.
 */

public class PlacesAdapter extends ArrayAdapter<SpotPlace> implements ApiDAO.GmapsRequestStatus {

    private RequestQueue queue;
    private RequestUserDataListener requestUserDataListener;

    public interface RequestUserDataListener {
        void onResponseSuccess(String nick, String fullName, String profileImg);
    }

    private PlaceHolder holder;

    public PlacesAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void addPlace(SpotPlace spotPlace) {
        add(spotPlace);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SpotPlace item = getItem(position);

        if (view == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_place, null);

            holder = new PlaceHolder();
            holder.fabLocation = (FloatingActionButton) view.findViewById(R.id.fab_location_item_event_list);
            holder.fabViewMore = (FloatingActionButton) view.findViewById(R.id.fab_view_more_item_event_list);
            holder.imvItemEventsList = (ImageView) view.findViewById(R.id.imv_item_places_list);
            holder.imvUserItemEventsList = (CircleImageView) view.findViewById(R.id.imv_user_item_places_list);
            holder.txvTitleItemEventsList = (TextView) view.findViewById(R.id.txv_title_item_places_list);
            holder.txvDescriptionItemEventsList = (TextView) view.findViewById(R.id.txv_description_item_places_list);
            holder.txvAddressItemEventsList = (TextView) view.findViewById(R.id.txv_address_item_places_list);
            holder.txvUserItemEventsList = (TextView) view.findViewById(R.id.txv_user_item_places_list);
            holder.txvPeopleEventsList = (TextView) view.findViewById(R.id.txv_users_in_item_places_list);

            view.setTag(holder);

        } else {
            holder = (PlaceHolder) view.getTag();
        }

        holder.fabLocation.setVisibility(View.VISIBLE);
        holder.fabViewMore.setVisibility(View.VISIBLE);

        /*if (checkDateTime(item.getmDateTimeFrom(), item.getmDateTimeTo())) {

        }*/

        getUserData(position);
        Picasso.with(getContext())
                .load(item.getmImg())
                .into(holder.imvItemEventsList);
        holder.txvTitleItemEventsList.setText(item.getmTitle());
        holder.txvDescriptionItemEventsList.setText(item.getmDescription());
        ApiDAO.getInstance().setAddressFormatted(this, item.getmAddress());
        holder.txvPeopleEventsList.setText(String.valueOf(item.getmUsersIn().size()));
        return view;
    }

    @Override
    public void onGmapsRequestResponseSuccess(JSONObject response) {
        try {
            holder.txvAddressItemEventsList.setText(response.getJSONArray("results").getJSONObject(0).getString("formatted_address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGmapsRequestErrorResponse(CharSequence messageError) {
        holder.txvAddressItemEventsList.setText(getContext().getResources().getText(R.string.error_address));
    }

    class PlaceHolder {
        ImageView imvItemEventsList;
        CircleImageView imvUserItemEventsList;
        TextView txvTitleItemEventsList, txvDescriptionItemEventsList, txvAddressItemEventsList, txvUserItemEventsList, txvPeopleEventsList;
        FloatingActionButton fabLocation, fabViewMore;
    }

    private void getUserData(final int item) {
        new GetUserData().execute(new Integer[]{new Integer(item)});
    }

    public boolean checkDateTime(String from, String to) {
        boolean result = false;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateFrom = null;
        Date dateTo = null;
        Calendar now = Calendar.getInstance();
        try {
            dateFrom = formatter.parse(from);
            dateTo = formatter.parse(to);

            if ((now.getTimeInMillis() >= dateFrom.getTime()) && (now.getTimeInMillis() <= dateTo.getTime())) {
                result = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    class GetUserData extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {

            requestUserDataListener = new RequestUserDataListener() {
                @Override
                public void onResponseSuccess(String nick, String fullName, String profileImg) {
                    holder.txvUserItemEventsList.setText(nick);

                    Picasso.with(getContext())
                            .load(profileImg)
                            .into(holder.imvUserItemEventsList);
                }
            };


            queue = Volley.newRequestQueue(getContext());

            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, ApiDAO.URL_USER_BYID + getItem(params[0]).getmCreatorId(), null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response) {

                            String nick = "";
                            String fullName = "";
                            String profileImg = "";

                            try {
                                nick = response.getJSONObject(0).getString(SpotlightContract.UserEntry.KEY_USERNAME);
                                fullName = response.getJSONObject(0).getString(SpotlightContract.UserEntry.KEY_FULLNAME);
                                profileImg = response.getJSONObject(0).getString(SpotlightContract.UserEntry.KEY_PROFILE_IMG);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            requestUserDataListener.onResponseSuccess(nick, fullName, profileImg);
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

            return null;
        }
    }
}
