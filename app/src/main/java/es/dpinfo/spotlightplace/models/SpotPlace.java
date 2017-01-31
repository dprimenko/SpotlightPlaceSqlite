package es.dpinfo.spotlightplace.models;

import java.util.List;

/**
 * Created by dprimenko on 10/11/16.
 */
public class SpotPlace {

    private String mId;
    private String mCreatorId;
    private String mTitle;
    private String mImg;
    private String mAddress;
    private String mDescription;
    private String mCategory;
    private String mDateTimeFrom;
    private String mDateTimeTo;
    private List<String> mNpeople;


    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmImg() {
        return mImg;
    }

    public void setmImg(String mImg) {
        this.mImg = mImg;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getmDateTimeFrom() {
        return mDateTimeFrom;
    }

    public void setmDateTimeFrom(String mDateTimeFrom) {
        this.mDateTimeFrom = mDateTimeFrom;
    }

    public String getmDateTimeTo() {
        return mDateTimeTo;
    }

    public void setmDateTimeTo(String mDateTimeTo) {
        this.mDateTimeTo = mDateTimeTo;
    }

    public String getmCreatorId() {
        return mCreatorId;
    }

    public void setmCreatorId(String mCreatorId) {
        this.mCreatorId = mCreatorId;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public List<String> getmNpeople() {
        return mNpeople;
    }

    public void setmNpeople(List<String> mNpeople) {
        this.mNpeople = mNpeople;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;

        if (o instanceof SpotPlace) {

            SpotPlace spotPlace = (SpotPlace) o;

            if (spotPlace.getmId() == this.getmId()) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public String toString() {

        return "{" +
                "\"creator\" : \"" + mCreatorId + "\"" +
                ", \"title\" : \"" + mTitle + "\"" +
                ", \"img\" : \"" + mImg + "\"" +
                ", \"address\" : \"" + mAddress + "\"" +
                ", \"description\" : \"" + mDescription + "\"" +
                ", \"category\" : \"" + mCategory + "\"" +
                ", \"datetime_from\" : \"" + mDateTimeFrom + "\"" +
                ", \"datetime_to\" : \"" + mDateTimeTo + "\"" +
                ", \"people_in\" : [" + getPeopleToString() + "]" +
                "}";
    }

    private String getPeopleToString() {
        String result = "";

        for (int i = 0; i < mNpeople.size(); i++) {
            if (i != (mNpeople.size() - 1)) {
                result += ("\"" + mNpeople.get(i) + "\"" + ",");
            } else {
                result += ("\"" + mNpeople.get(i) + "\"");
            }
        }

        return result;
    }

    public SpotPlace() {

    }

    public SpotPlace(String id, String creatorId, String title, String img, String address, String description, String category, String datetimeFrom, String datetimeTo, List<String> nPeople) {

        this.mId = id;
        this.mCreatorId = creatorId;
        this.mTitle = title;
        this.mImg = img;
        this.mAddress = address;
        this.mDescription = description;
        this.mCategory = category;
        this.mDateTimeFrom = datetimeFrom;
        this.mDateTimeTo = datetimeTo;
        this.mNpeople = nPeople;
    }
}
