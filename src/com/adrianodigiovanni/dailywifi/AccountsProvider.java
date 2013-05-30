package com.adrianodigiovanni.dailywifi;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/* 
 * TODO: Implement a contract class
 * @see http://developer.android.com/guide/topics/providers/content-provider-creating.html#ContractClass
 */
public class AccountsProvider extends ContentProvider {

	private static final String SCHEME = "content://";

	/**
	 * @see http
	 *      ://developer.android.com/guide/topics/providers/content-provider-
	 *      creating.html#ContentURI
	 */
	private static final String AUTHORITY = "com.adrianodigiovanni.dailywifi.provider";

	private static final Uri AUTHORITY_URI = Uri.parse(SCHEME + AUTHORITY);

	private static final String PATH_ACCOUNTS = "accounts";
	private static final String PATH_ACCOUNT_ID = "accounts/";
	private static final String PATH_ACCOUNT_ID_PATTERN = "accounts/#";

	public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
			PATH_ACCOUNTS);
	public static final Uri CONTENT_ID_URI = Uri.withAppendedPath(
			AUTHORITY_URI, PATH_ACCOUNT_ID);

	/**
	 * @see http
	 *      ://developer.android.com/guide/topics/providers/content-provider-
	 *      creating.html#TableMIMETypes
	 */
	private static final String PROVIDER_SPECIFIC_CONTENT_TYPE = "vnd."
			+ AUTHORITY + "." + PATH_ACCOUNTS;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ PROVIDER_SPECIFIC_CONTENT_TYPE;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ PROVIDER_SPECIFIC_CONTENT_TYPE;

	private static final int ACCOUNTS = 1;
	private static final int ACCOUNT_ID = 2;

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, PATH_ACCOUNTS, ACCOUNTS);
		sURIMatcher.addURI(AUTHORITY, PATH_ACCOUNT_ID_PATTERN, ACCOUNT_ID);
	}

	AccountsDatabaseHelper mDatabaseHelper;

	@Override
	public boolean onCreate() {
		mDatabaseHelper = new AccountsDatabaseHelper(getContext());
		return false;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriMatch = sURIMatcher.match(uri);
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		int count = 0;
		switch (uriMatch) {
		case ACCOUNTS:
			count = db.delete(AccountsTable.TABLE_NAME, selection,
					selectionArgs);
			break;
		case ACCOUNT_ID:
			String id = uri.getLastPathSegment();
			String newSelection = AccountsTable.COLUMN_ID + "=" + id;
			String[] newSelectionArgs = selectionArgs;
			if (!TextUtils.isEmpty(selection)) {
				newSelection += " AND " + selection;
				newSelectionArgs = null;
			}
			count = db.delete(AccountsTable.TABLE_NAME, newSelection,
					newSelectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		int uriMatch = sURIMatcher.match(uri);
		switch (uriMatch) {
		case ACCOUNTS:
			return CONTENT_TYPE;
		case ACCOUNT_ID:
			return CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriMatch = sURIMatcher.match(uri);
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		long id = 0;
		switch (uriMatch) {
		case ACCOUNTS:
			id = db.insert(AccountsTable.TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.withAppendedPath(CONTENT_ID_URI, Long.toString(id));
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(AccountsTable.TABLE_NAME);

		int uriMatch = sURIMatcher.match(uri);
		switch (uriMatch) {
		case ACCOUNTS:
			break;
		case ACCOUNT_ID:
			queryBuilder.appendWhere(AccountsTable.COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriMatch = sURIMatcher.match(uri);
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		int count = 0;
		switch (uriMatch) {
		case ACCOUNT_ID:
			String id = uri.getLastPathSegment();
			String newSelection = AccountsTable.COLUMN_ID + "=" + id;
			String[] newSelectionArgs = selectionArgs;
			if (!TextUtils.isEmpty(selection)) {
				newSelection += " AND " + selection;
				newSelectionArgs = null;
			}
			count = db.update(AccountsTable.TABLE_NAME, values, newSelection,
					newSelectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}
