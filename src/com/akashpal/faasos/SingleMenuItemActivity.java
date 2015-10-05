package com.akashpal.faasos;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class SingleMenuItemActivity extends Activity{
	
	
	String name,image,desc;
	int price, spice;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);
     
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        name = in.getStringExtra(Constants.TAG_NAME);
        image = in.getStringExtra(Constants.TAG_IMAGE);
        String category = in.getStringExtra(Constants.TAG_CATEGORY);
        spice = Integer.parseInt(in.getStringExtra(Constants.TAG_SPICE_METER));
        desc = in.getStringExtra(Constants.TAG_DESC);
        String rating = in.getStringExtra(Constants.TAG_RATING);        
        price = Integer.parseInt(in.getStringExtra(Constants.TAG_PRICE));
        String is_veg = in.getStringExtra(Constants.TAG_IS_VEG);
        
        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblPrice = (TextView) findViewById(R.id.price_label);
        TextView lblCategory = (TextView) findViewById(R.id.category_label);
        TextView lblisVeg = (TextView) findViewById(R.id.is_veg);
        TextView lblDesc = (TextView) findViewById(R.id.desc);
        
		ImageView imageView = (ImageView) findViewById(R.id.image);
		Uri imageUri =Uri.parse(image);
		Picasso.with(this).load(imageUri.toString()).into(imageView);
        
        lblName.setText(name);
        
        RatingBar rb1 = (RatingBar) findViewById(R.id.ratingBar_spice);
        rb1.setRating(spice);
        
        RatingBar rb2 = (RatingBar) findViewById(R.id.ratingBar_rating);
        rb2.setRating(Float.parseFloat(rating));
        
        
        lblPrice.setText(price +"/-");
        lblCategory.setText(category);
        lblisVeg.setText(is_veg);
        
        lblDesc.setText(desc);
        
    }

	@Override
	protected void onResume() {
		super.onResume();		
	}

	
	public void likeIt(View v){
	    Toast.makeText(this, "like it", Toast.LENGTH_SHORT).show();	

	    DatabaseHandler db = new DatabaseHandler(this);
	    db.addLike(name, price, spice, image);
	    db.getLikedItems();
	    Intent i = new Intent(SingleMenuItemActivity.this,LikedItemsActivity.class);
	    startActivity(i);
	}
	
    public void back(View v){
	    Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
        onBackPressed();
        
	}
    
    public void share(View v){
	    Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
	    Intent sendIntent = new Intent();
	    sendIntent.setAction(Intent.ACTION_SEND);
	    sendIntent.putExtra(Intent.EXTRA_TEXT,"Name of Item:-" + name +"\n" + "Description:-" + desc);
	    sendIntent.setType("text/plain");
	    startActivity(Intent.createChooser(sendIntent,"Choose to share"));
       
	}
    
    public void sms(View v){
	    Toast.makeText(this, "sms", Toast.LENGTH_SHORT).show();
	    
        String message = "Name of Item:-" + name +"\n" + "Description:-" + desc;
        
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", message); 
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
	}

}
