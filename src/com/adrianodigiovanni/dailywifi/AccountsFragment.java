package com.adrianodigiovanni.dailywifi;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

public class AccountsFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final String[] ACCOUNTS_PROJECTION = new String[] {
			AccountsTable.COLUMN_ID, AccountsTable.COLUMN_SSID,
			AccountsTable.COLUMN_USERNAME, AccountsTable.COLUMN_PASSWORD };

	SimpleCursorAdapter mAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setEmptyText(getResources().getString(R.string.text_view_no_accounts));

		mAdapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_1, null,
				new String[] { AccountsTable.COLUMN_SSID },
				new int[] { android.R.id.text1 }, 0);
		
		setListAdapter(mAdapter);
		setListShown(false);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		
		Intent intent = new Intent(getActivity(), AddEditAccountActivity.class);
		
		Uri uri = Uri.withAppendedPath(AccountsProvider.CONTENT_ID_URI, Long.toString(id));
		intent.putExtra(AccountsProvider.CONTENT_ITEM_TYPE, uri);
		startActivity(intent);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri = AccountsProvider.CONTENT_URI;
		return new CursorLoader(getActivity(), baseUri, ACCOUNTS_PROJECTION,
				null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}
}
