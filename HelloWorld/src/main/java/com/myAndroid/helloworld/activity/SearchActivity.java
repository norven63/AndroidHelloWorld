package com.myAndroid.helloworld.activity;

import com.myAndroid.helloworld.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;

public class SearchActivity extends ListActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_search);

    // // 使用Search Manager获得与之个Activity关联的SearchableInfo
    // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    // SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());

    SearchView searchView = (SearchView) findViewById(R.id.searchView);
    searchView.setSubmitButtonEnabled(true);
    // searchView.setSearchableInfo(searchableInfo);// 将Activity的SearchableInfo绑定到搜索视图上

    searchView.setOnSuggestionListener(new OnSuggestionListener() {

      @Override
      public boolean onSuggestionClick(int position) {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public boolean onSuggestionSelect(int position) {
        // TODO Auto-generated method stub
        return false;
      }
    });

    searchView.setOnQueryTextListener(new OnQueryTextListener() {
      @Override
      public boolean onQueryTextChange(String newText) {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public boolean onQueryTextSubmit(String query) {
        // TODO Auto-generated method stub
        return false;
      }
    });
  }

}
