package es.dpinfo.spotlightplace.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.models.Social;
import es.dpinfo.spotlightplace.repository.SocialDAO;

/**
 * Created by dprimenko on 21/10/16.
 */

public class SocialAdapter extends ArrayAdapter<Social> {

    private Context context;

    public SocialAdapter(Context context) {
        super(context, R.layout.item_social_about, SocialDAO.getInstance().getSocialList());
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imvSocialAbout;
        TextView txvSocialAbout;

        LayoutInflater inflater = LayoutInflater.from(context);

        View item = inflater.inflate(R.layout.item_social_about, null);

        imvSocialAbout = (ImageView) item.findViewById(R.id.imv_social_about);
        txvSocialAbout = (TextView) item.findViewById(R.id.txv_social_about);


        imvSocialAbout.setImageResource(getItem(position).getmImg());
        txvSocialAbout.setText(getItem(position).getmText());
        return item;

    }
}

