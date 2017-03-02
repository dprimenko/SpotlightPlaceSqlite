package es.dpinfo.spotlightplace.presenters;

import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;

import es.dpinfo.spotlightplace.SpotlightApplication;
import es.dpinfo.spotlightplace.interfaces.IListPresenter;
import es.dpinfo.spotlightplace.provider.SpotlightContractProvider;

/**
 * Created by dprimenko on 2/03/17.
 */
public class ListPresenterImpl implements IListPresenter.Presenter,LoaderManager.LoaderCallbacks<Cursor> {

    private IListPresenter.View view;
    private int id;

    public ListPresenterImpl(IListPresenter.View view, int id) {
        this.view = view;
        this.id = id;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Loader loader = null;

        switch (id) {
            case IListPresenter.CATEGORY:
                loader = new CursorLoader(
                        SpotlightApplication.getContext(),
                        SpotlightContractProvider.Categories.CONTENT_URI,
                        SpotlightContractProvider.Categories.PROJECTION,
                        null,
                        null,
                        null);
                break;
            case IListPresenter.PLACE:
                loader = new CursorLoader(
                        SpotlightApplication.getContext(),
                        SpotlightContractProvider.Places.CONTENT_URI,
                        SpotlightContractProvider.Places.PROJECTION,
                        null,
                        null,
                        null);
                break;
        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case IListPresenter.CATEGORY:
                data.setNotificationUri(SpotlightApplication.getContext().getContentResolver(), SpotlightContractProvider.Categories.CONTENT_URI);
                break;
            case IListPresenter.PLACE:
                data.setNotificationUri(SpotlightApplication.getContext().getContentResolver(), SpotlightContractProvider.Places.CONTENT_URI);
                break;
        }

        view.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        view.setCursor(null);
    }

    @Override
    public Context getContext() {
        return SpotlightApplication.getContext();
    }

    @Override
    public void getAllFields(CursorAdapter adapter) {
        ((Fragment)view).getLoaderManager().initLoader(id, null, this);
    }

    @Override
    public void restartLoader(CursorAdapter adapter) {
        ((Fragment)view).getLoaderManager().restartLoader(id, null, this);
    }
}
