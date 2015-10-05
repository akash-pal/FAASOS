package com.akashpal.faasos;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class LazyAdapter extends BaseAdapter {
	

	private Activity activity;
    private static LayoutInflater inflater=null;
    ListView lv;
	// Hashmap for ListView
    private ArrayList<HashMap<String, String>> data;
    

    
	public LazyAdapter(Activity a,ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 View view=convertView;
	     if(convertView==null)
	      view = inflater.inflate(R.layout.list_item, null);
	      
	     
	  // Displaying all values on the screen
	        TextView lblName = (TextView) view.findViewById(R.id.name_list);
	        TextView lblPrice = (TextView) view.findViewById(R.id.price_list);
	        ImageView imageView = (ImageView) view.findViewById(R.id.image_list);
	        
			
	        
	        
	        HashMap<String, String> map = data.get(position);	
			Log.d("SKY",map+"");
			
			
			lblName.setText(map.get(Constants.TAG_NAME));
	        lblPrice.setText(map.get(Constants.TAG_PRICE)+"/-");
	        Uri imageUri =Uri.parse(map.get(Constants.TAG_IMAGE));
			Picasso.with(activity).load(imageUri.toString()).into(imageView);
	        
			Float rating = Float.parseFloat(map.get(Constants.TAG_SPICE_METER));
			
			RatingBar rb = (RatingBar) view.findViewById(R.id.ratingBar_spice_list);
	        rb.setRating(rating);
	        
	      return view;  
	}

}
