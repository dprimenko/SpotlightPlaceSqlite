package es.dpinfo.spotlightplace.repository;

import java.util.ArrayList;
import java.util.List;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.models.Social;

/**
 * Created by dprimenko on 11/11/16.
 */
public class SocialDAO {
    private static SocialDAO ourInstance = new SocialDAO();
    private List<Social> socialList;

    public static SocialDAO getInstance() {
        return ourInstance;
    }

    private SocialDAO() {
        socialList = new ArrayList<>();

        addSocial(new Social(R.drawable.fb, "Like on Facebook"));
        addSocial(new Social(R.drawable.twitter, "Follow us in Twitter"));
        addSocial(new Social(R.drawable.gplay, "Rate us on Play Store"));
        addSocial(new Social(R.drawable.ghub, "Visit us on Github"));
    }

    public void addSocial(Social social) {
        socialList.add(social);
    }

    public List<Social> getSocialList() {
        return socialList;
    }
}
