package com.adrianodigiovanni.dailywifi;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddEditAccountActivity extends Activity {

	private EditText mEditTextSSID;
	private EditText mEditTextUsername;
	private EditText mEditTextPassword;

	private Uri mUri = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addeditaccount);

		mEditTextSSID = (EditText) findViewById(R.id.edit_text_ssid);
		mEditTextUsername = (EditText) findViewById(R.id.edit_text_username);
		mEditTextPassword = (EditText) findViewById(R.id.edit_text_password);

		Intent intent = getIntent();
		if (intent.hasExtra(AccountsProvider.CONTENT_ITEM_TYPE)) {
			mUri = intent
					.getParcelableExtra(AccountsProvider.CONTENT_ITEM_TYPE);

			findViewById(R.id.button_remove).setVisibility(View.VISIBLE);
			fill(mUri);
		}
	}

	private void fill(Uri uri) {
		final String[] projection = { AccountsTable.COLUMN_SSID,
				AccountsTable.COLUMN_USERNAME, AccountsTable.COLUMN_PASSWORD };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (null != cursor) {
			cursor.moveToFirst();
			mEditTextSSID.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(AccountsTable.COLUMN_SSID)));
			mEditTextUsername.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(AccountsTable.COLUMN_USERNAME)));
			mEditTextPassword.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(AccountsTable.COLUMN_PASSWORD)));

			cursor.close();
		}
	}

	public void onCancel(View view) {
		finish();
	}

	// TODO: Check if wifi is connected and ssid is in the list upon account insert or update in order to start service
	public void onSave(View view) {

		boolean hasErrors = false;

		String ssid = mEditTextSSID.getText().toString().trim();
		String username = mEditTextUsername.getText().toString().trim();
		String password = mEditTextPassword.getText().toString().trim();
		
		Resources resources = getResources();

		if (ssid.isEmpty()) {
			mEditTextSSID.setError(resources.getString(R.string.error_required_ssid));
			hasErrors = true;
		}

		if (username.isEmpty()) {
			mEditTextUsername.setError(resources.getString(R.string.error_required_username));
			hasErrors = true;
		}

		if (password.isEmpty()) {
			mEditTextPassword.setError(resources.getString(R.string.error_required_password));
			hasErrors = true;
		}

		if (!hasErrors) {
			ContentValues values = new ContentValues();
			values.put(AccountsTable.COLUMN_SSID, ssid);
			values.put(AccountsTable.COLUMN_USERNAME, username);
			values.put(AccountsTable.COLUMN_PASSWORD, password);

			if (null == mUri) {
				getContentResolver().insert(AccountsProvider.CONTENT_URI,
						values);
			} else {
				getContentResolver().update(mUri, values, null, null);
			}

			finish();
		}
	}

	// TODO: Implement undo
	public void onRemove(View view) {
		getContentResolver().delete(mUri, null, null);
		finish();
	}
}
