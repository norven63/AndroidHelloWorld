package com.myAndroid.helloworld.provider;

import android.util.Log;

import android.app.SearchManager;

import android.content.UriMatcher;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import android.content.ContentProvider;

public class MyContentProvider extends ContentProvider {
  public static final String CONTENT_PROVIDER = "com.myAndroid.helloworld.provider";

  private static final int ALL_ROWS = 1;
  private static final int SINGLE_ROWS = 2;
  private static final int SEARCH = 3;

  private static UriMatcher uriMatcher;

  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    uriMatcher.addURI(CONTENT_PROVIDER, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH);
    uriMatcher.addURI(CONTENT_PROVIDER, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH);
    uriMatcher.addURI(CONTENT_PROVIDER, SearchManager.SUGGEST_URI_PATH_SHORTCUT, SEARCH);
    uriMatcher.addURI(CONTENT_PROVIDER, SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", SEARCH);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
    case 3:
      return SearchManager.SUGGEST_MIME_TYPE;

    default:
      throw new IllegalArgumentException("不规范的URI: " + uri);
    }
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean onCreate() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    Log.i("MyProvider", "query()");
    return null;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    // TODO Auto-generated method stub
    return 0;
  }

}
