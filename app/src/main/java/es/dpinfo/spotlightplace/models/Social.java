package es.dpinfo.spotlightplace.models;

/**
 * Created by dprimenko on 11/11/16.
 */
public class Social {

    private int mImg;
    private String mText;

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public int getmImg() {
        return mImg;
    }

    public void setmImg(int mImg) {
        this.mImg = mImg;
    }

    @Override
    public String toString() {
        return ("{" +
                "img:'" + mImg + '\'' +
                ", text:'" + mText + '\'' +
                "}");
    }

    public Social(int img, String text) {
        this.mImg = img;
        this.mText = text;
    }
}
