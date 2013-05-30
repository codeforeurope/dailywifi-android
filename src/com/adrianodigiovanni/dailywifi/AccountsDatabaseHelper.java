package com.adrianodigiovanni.dailywifi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountsDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "dailywifi.db";
	private static final int DATABASE_VERSION = 1;

	public AccountsDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		AccountsTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		AccountsTable.onUpgrade(db, oldVersion, newVersion);
	}
}
