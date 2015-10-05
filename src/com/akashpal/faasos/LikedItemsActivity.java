package com.akashpal.faasos;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class LikedItemsActivity extends ActionBarActivity {

	DatabaseHandler myDb;

	ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>(); 
	LazyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liked_items_list);
		myDb = new DatabaseHandler(this);
		populateListView();
		
	}
	
	private void populateListView()
	{
		
		al = myDb.getLikedItems();
    	Log.d("DATA", al+" ");
        adapter=new LazyAdapter(LikedItemsActivity.this, al);
		ListView myList = (ListView) findViewById(R.id.list_history);
		myList.setAdapter(adapter);
		 
	}
	
	public void back(View v)
	{
		onBackPressed();
	}


}
