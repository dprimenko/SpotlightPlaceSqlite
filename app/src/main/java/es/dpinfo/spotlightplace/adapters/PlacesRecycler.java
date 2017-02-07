package es.dpinfo.spotlightplace.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.repository.ApiDAO;
import es.dpinfo.spotlightplace.schemas.SpotlightContract;


/**
 * Created by dprimenko on 7/02/17.
 */

public class PlacesRecycler extends RecyclerView.Adapter<PlacesRecycler.PlaceHolder> implements ApiDAO.GmapsRequestStatus{

    private RequestQueue queue;
    private PlacesAdapter.RequestUserDataListener requestUserDataListener;
    private GmapsRequestListener gmapsRequestListener;
    private List<SpotPlace> placesList;
    private Context context;

    public interface GmapsRequestListener {
        void onGmapsRequestSuccess(String addressFormatted);
        void onGmapsRequestError();
    }

    public PlacesRecycler(Context context) {
        this.placesList = new ArrayList<>();
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    @Override
    public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_place, parent, false);
        PlaceHolder holder = new PlaceHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PlaceHolder holder, int position) {
        SpotPlace item = placesList.get(position);

        requestUserDataListener = new PlacesAdapter.RequestUserDataListener() {
            @Override
            public void onResponseSuccess(String nick, String fullName, String profileImg) {
                holder.txvUserItemEventsList.setText(nick);

                Picasso.with(getContext())
                        .load(profileImg)
                        .into(holder.imvUserItemEventsList);
            }
        };

        getUserData(position);
        Picasso.with(getContext())
                .load(item.getmImg())
                .into(holder.imvItemEventsList);
        holder.txvTitleItemEventsList.setText(item.getmTitle());
        holder.txvDescriptionItemEventsList.setText(item.getmDescription());

        gmapsRequestListener = new GmapsRequestListener() {
            @Override
            public void onGmapsRequestSuccess(String addressFormatted) {
                holder.txvAddressItemEventsList.setText(addressFormatted);
            }

            @Override
            public void onGmapsRequestError() {
                holder.txvAddressItemEventsList.setText(getContext().getResources().getText(R.string.error_address));
            }
        };

        ApiDAO.getInstance().setAddressFormatted(this, item.getmAddress());
        holder.txvPeopleEventsList.setText(String.valueOf(item.getmUsersIn().size()));
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    @Override
    public void onGmapsRequestResponseSuccess(JSONObject response) {
        try {
            gmapsRequestListener.onGmapsRequestSuccess(response.getJSONArray("results").getJSONObject(0).getString("formatted_address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGmapsRequestErrorResponse(CharSequence messageError) {
        gmapsRequestListener.onGmapsRequestError();
    }

    public void addSpotPlace(SpotPlace place) {
        placesList.add(place);
        notifyDataSetChanged();
    }

    private void getUserData(final int item) {

        new GetUserData().execute(new Integer[]{new Integer(item)});
    }

    class PlaceHolder extends RecyclerView.ViewHolder {

        ImageView imvItemEventsList;
        CircleImageView imvUserItemEventsList;
        TextView txvTitleItemEventsList, txvDescriptionItemEventsList, txvAddressItemEventsList, txvUserItemEventsList, txvPeopleEventsList;
        FloatingActionButton fabViewMore;

        public PlaceHolder(View itemView) {
            super(itemView);

            imvItemEventsList = (ImageView) itemView.findViewById(R.id.imv_item_places_list);
            imvUserItemEventsList = (CircleImageView) itemView.findViewById(R.id.imv_user_item_places_list);
            txvTitleItemEventsList = (TextView) itemView.findViewById(R.id.txv_title_item_places_list);
            txvDescriptionItemEventsList = (TextView) itemView.findViewById(R.id.txv_description_item_places_list);
            txvAddressItemEventsList = (TextView) itemView.findViewById(R.id.txv_address_item_places_list);
            txvUserItemEventsList = (TextView) itemView.findViewById(R.id.txv_user_item_places_list);
            txvPeopleEventsList = (TextView) itemView.findViewById(R.id.txv_users_in_item_places_list);
            fabViewMore = (FloatingActionButton) itemView.findViewById(R.id.fab_view_more_item_event_list);
        }
    }

    class GetUserData extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {

            queue = Volley.newRequestQueue(getContext());

            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, ApiDAO.URL_USER_BYID + placesList.get(params[0]).getmCreatorId(), null,
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
