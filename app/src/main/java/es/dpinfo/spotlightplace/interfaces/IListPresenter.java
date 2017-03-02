package es.dpinfo.spotlightplace.interfaces;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

/**
 * Created by dprimenko on 2/03/17.
 */
public interface IListPresenter {

    int PLACE = 1;
    int CATEGORY = 3;

    interface View {
        CursorAdapter getCursorAdapter();
        void setCursor(Cursor cursor);
    }

    interface Presenter {
        Context getContext();

        void getAllFields(CursorAdapter adapter);
        void restartLoader(CursorAdapter adapter);
    }
}
