 package com.spinwash.adapter;


import java.util.ArrayList;

import com.spinwash.R;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ImagePagerAdapter extends PagerAdapter {
    
	ArrayList<Integer> eList= new ArrayList<Integer>();
	 
	private Context context ;
    public ImagePagerAdapter(Context context ) {
		this.context =context;
		eList.add(R.drawable.ic_intro_1);
		eList.add(R.drawable.ic_intro_2);
		eList.add(R.drawable.ic_intro_3);
		 
		/* Events e3 = new Events();
			e3.setId("-102");
			e3.setPlace(R.drawable.banner_3 );
			eList.add(e3);*/
	}
    @Override
    public int getCount() {
      return eList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	ViewHolder holder;
    	final int pos = position;
    /*	if(container!=null&&container.getTag()!=null) {
    		holder = (ViewHolder) container.getTag();
    	}else {*/
    		 holder  = new ViewHolder();
    		 container.setTag(holder);
    		 holder.imageView = new ImageView(context);
    	     
    	//}
    	
    	//int padding = context.getResources().getDimensionPixelSize( R.dimen.padding_medium);
    	 holder.imageView.setPadding(0, 0, 0, 0);
    	 holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    	 holder.imageView.setImageResource(eList.get(position));
    	 holder.imageView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 
			
		}
	});
    	 
      //  imageView.setImageResource(mImages[position]);
      ((ViewPager) container).addView(holder.imageView, 0);
      return holder.imageView;
    }
 public class ViewHolder{
	 ImageView imageView;
 }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      ((ViewPager) container).removeView((ImageView) object);
    }
  }