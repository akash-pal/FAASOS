package com.akashpal.faasos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity  {
	
	// Hashmap for ListView
    ArrayList<HashMap<String, String>> menuslist = new ArrayList<HashMap<String,String>>();		
    ArrayList<HashMap<String, String>> list= new ArrayList<HashMap<String,String>>();	
    HashMap<String, String> map;
    ListView lv;
    LazyAdapter adapter;
    Button sort_price;
    Button sort_category;
    EditText mSearch;
    TextView mCount,mHits;
    String api_hits, count;
    Switch mSwitch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		sort_price = (Button) findViewById(R.id.sort_price); 
		sort_category = (Button) findViewById(R.id.sort_rating); 
		mSearch = (EditText) findViewById(R.id.inputSearch);
		mCount =  (TextView) findViewById(R.id.count);
		mHits = (TextView) findViewById(R.id.apihit);
		mSwitch = (Switch) findViewById(R.id.switch1); 
		
		mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
		            // The toggle is enabled
					Toast.makeText(getBaseContext(), "ON", Toast.LENGTH_LONG).show();
					final ArrayList<HashMap<String, String>> filterlist = new ArrayList<HashMap<String, String>>();
					
					for(int i=0;i<menuslist.size();i++)
					{
						String veg_filter = menuslist.get(i).get(Constants.TAG_IS_VEG);
						if(veg_filter.equals("yes")){
							filterlist.add(menuslist.get(i));
						}
					}
					
					list = filterlist;
                    listupdate(list);
	
					
		        } else {
		            // The toggle is disabled
		        	Toast.makeText(getBaseContext(), "OFF", Toast.LENGTH_LONG).show();
					list=menuslist;
					listupdate(list); 
		        }
			}
		});
		
		
		
		mSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
											
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				final ArrayList<HashMap<String, String>> filterlist = new ArrayList<HashMap<String, String>>();
				String search = mSearch.getText().toString();
				
				if(!search.isEmpty())
				{
				        if(search.matches("[0-9]+") && search.length() > 0)
						{
				        	//search by price
							// contains a number
							Log.d("SKY",search);
							int p = Integer.parseInt(search);
							for(int i=0;i<menuslist.size();i++)
							{
							    int searchPrice = Integer.parseInt(menuslist.get(i).get(Constants.TAG_PRICE));    
							    if(p == searchPrice){
									filterlist.add(menuslist.get(i));
								}
							}
							
						} 
						else
						{
							 // does not contain a number
							//search by name 
							for(int i=0;i<menuslist.size();i++)
							{
								String searchName = menuslist.get(i).get(Constants.TAG_NAME);
								
								if(searchName.contains(search)){
									filterlist.add(menuslist.get(i));
								}
							}
						
						}
											
						list = filterlist;
		                listupdate(list);
			
				}else{
					list=menuslist;
					listupdate(list); 
				}
	
			}
		});
		
		
		// call AsynTask to perform network operation on separate thread
		
		new HttpAsyncTask().execute("http://faasos.0x10.info/api/faasos?type=json&query=list_menu");

		new HttpAsyncTaskHIT().execute("http://faasos.0x10.info/api/faasos?type=json&query=api_hits");
	}
	
    

	public static String GET(String url){
		InputStream inputStream = null;
		String result = "";
		try {
			
			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			
			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			
			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();
			
			// convert inputstream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";
		
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}
		
		return result;
	}
	
	
	
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        
        inputStream.close();
        return result;
        
    }
	
    

    private class HttpAsyncTaskHIT extends AsyncTask<String, Void, String> 
    {

		@Override
		protected String doInBackground(String... urls) {
			 return GET(urls[0]);
		}
		@Override
		protected void onPostExecute(String result) {
			Log.d("SKY-API HITS",result); 
			
			try {

				JSONObject  jsonRootObject = new JSONObject(result); 
				api_hits = jsonRootObject.getString("api_hits");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.d("SKY-API HITS",api_hits+" ");
			mHits.setText(api_hits);
						
		}
    
    }
    
    private class HttpAsyncTask extends AsyncTask<String, Void, String> 
    {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        	try {
				  // Create the root JSONObject from the JSON string.  
                JSONObject  jsonRootObject = new JSONObject(result);  

                //Get the instance of JSONArray that contains JSONObjects  
                 JSONArray jsonArray = jsonRootObject.optJSONArray(Constants.TAG_MENU); 
                 
                 //Log.d("SKY",jsonArray.length()+" ");
                 count = String.valueOf(jsonArray.length());
                 mCount.setText(count);
                 
                 //Iterate the jsonArray and print the info of JSONObjects  
                 for(int i=0; i < jsonArray.length(); i++)
                 {  
                     JSONObject jsonObject = jsonArray.getJSONObject(i);  
                        
                     String name = jsonObject.getString(Constants.TAG_NAME).toString();
                     String image = jsonObject.getString(Constants.TAG_IMAGE).toString();                    
                     String category = jsonObject.getString(Constants.TAG_CATEGORY).toString();
                     String spice_meter = jsonObject.getString(Constants.TAG_SPICE_METER).toString();
                     String desc = jsonObject.getString(Constants.TAG_DESC).toString();
                     String rating = jsonObject.getString(Constants.TAG_RATING).toString();
                     String price = jsonObject.getString(Constants.TAG_PRICE).toString();
                     String is_veg = jsonObject.getString(Constants.TAG_IS_VEG).toString();
                     
                     
                     // creating new HashMap
     				 map = new HashMap<String, String>();
     				
     			     // adding each child node to HashMap key => value
     				 map.put(Constants.TAG_NAME, name);
     				 map.put(Constants.TAG_IMAGE,image);
     				 map.put(Constants.TAG_CATEGORY,category);
     				 map.put(Constants.TAG_SPICE_METER,spice_meter);
     				 map.put(Constants.TAG_DESC, desc);
     				 map.put(Constants.TAG_RATING,rating);
     				 map.put(Constants.TAG_PRICE,price);
     				 map.put(Constants.TAG_IS_VEG,is_veg);
     			     
     				//Log.d("SKY",map+"");
    				
                     menuslist.add(map); 				
                 }               

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	/**
    		 * Updating parsed JSON data into ListView
    		 * */
        	list = menuslist;
        	listupdate(list);
        	        	
       }//end of onPost execute.
            
    }//end of http async task
    
    
    
    public void listupdate(final ArrayList<HashMap<String, String>> data){
    	lv=(ListView)findViewById(R.id.list);
    	
        adapter=new LazyAdapter(MainActivity.this, data);
        lv.setAdapter(adapter);
    	
    	
    	lv.setOnItemClickListener(new OnItemClickListener() {

    		
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Log.d("SKY","data");
				
			    map = data.get(position);	
				Log.d("SKY",map+"");
				
				
				// Starting new intent
				Intent in = new Intent(MainActivity.this, SingleMenuItemActivity.class);
				in.putExtra(Constants.TAG_NAME,map.get(Constants.TAG_NAME) );
				in.putExtra(Constants.TAG_IMAGE, map.get(Constants.TAG_IMAGE));
				in.putExtra(Constants.TAG_CATEGORY,map.get(Constants.TAG_CATEGORY));
				in.putExtra(Constants.TAG_SPICE_METER,map.get(Constants.TAG_SPICE_METER));
				in.putExtra(Constants.TAG_DESC,map.get(Constants.TAG_DESC));
				in.putExtra(Constants.TAG_RATING,map.get(Constants.TAG_RATING));
				in.putExtra(Constants.TAG_PRICE,map.get(Constants.TAG_PRICE));
				in.putExtra(Constants.TAG_IS_VEG,map.get(Constants.TAG_IS_VEG));
				startActivity(in);
			}
    		
		});

    }
    
    
    public void sortPrice(View view){
    	
    	Toast.makeText(this, "Sorted Price", Toast.LENGTH_SHORT).show();
    	Collections.sort(list, new Comparator<HashMap<String,String>>(){

			@Override
			public int compare(HashMap<String, String> lhs,
					HashMap<String, String> rhs) {
				return lhs.get(Constants.TAG_PRICE).compareTo(rhs.get(Constants.TAG_PRICE));
			}
    		
    	});
    	
    	listupdate(list);
    }
    
    public void sortRating(View view){
    	
	    	Toast.makeText(this, "Sorted Category", Toast.LENGTH_SHORT).show();
	    	Collections.sort(list, new Comparator<HashMap<String,String>>(){
	
				@Override
				public int compare(HashMap<String, String> lhs,
						HashMap<String, String> rhs) {
					return lhs.get(Constants.TAG_RATING).compareTo(rhs.get(Constants.TAG_RATING));
				}
	    		
	    	});
	    	
	    	listupdate(list);
    }
    
    
}
