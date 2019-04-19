package com.washnow;

import android.app.Activity;

public class MapActivity  extends Activity{
		/*extends BaseActivity implements OnClickListener, OnMapReadyCallback, OnMapLongClickListener, OnMarkerDragListener, ConnectionCallbacks, OnConnectionFailedListener {

	  private GoogleMap map;
	  Marker eventMarker=null;
	  GoogleApiClient    mGoogleApiClient;
	  Location currentGeoLocation ;
	  boolean finish=false;
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.mapview_layout);
	    actionBar = getSupportActionBar();
		actionBar.setCustomView(R.layout.actionbar_backnav);
		context=this;
		
	
	    Intent intent = getIntent();
	 
		  ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

		  setTitle("Pick Location");
		  Toolbar parent = (Toolbar) actionBar.getCustomView().getParent();
		  parent.setContentInsetsAbsolute(0, 0);
	  }
	  
	  public void initLayout(){
		  
		  final LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        currentGeoLocation = mlocManager
	            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

	      Button btSubmit = (Button) findViewById(R.id.btSubmit);
	      
		  if(currentGeoLocation!=null){
			  LatLng  point = new LatLng(currentGeoLocation.getLatitude(), currentGeoLocation.getLongitude());
			  eventMarker = map.addMarker(new MarkerOptions()
		        .position(point)
		        .title(Utils.getAddress(point, context))
		        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				eventMarker.setDraggable(true);
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));

		  }
				    map.setOnMarkerDragListener(this);
		    // Zoom in, animating the camera.
		    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

		  map.setOnMapLongClickListener(this);
		  btSubmit.setOnClickListener(this);
	  }

	 

	
	@Override    
	public void onMapLongClick(LatLng point) {
		if(eventMarker==null){
			eventMarker = map.addMarker(new MarkerOptions()
	        .position(point)
	        .title(Utils.getAddress(point, context))           
	        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mappin)));  
			eventMarker.setDraggable(true);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 18));
		    map.setOnMarkerDragListener(this);
 
			map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
			
		}else {
			animateMarker(point, false);
		}
			
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Intent returnIntent = new Intent();
		if(eventMarker!=null){
			String Address = eventMarker.getTitle();
					
					
			if(Address!=null&&Address.length()>1){
				returnIntent.putExtra("address",Address);
			} 
				
			 
			returnIntent.putExtra("latitude", String.valueOf(eventMarker.getPosition().latitude));
			returnIntent.putExtra("longitude", String.valueOf(eventMarker.getPosition().longitude));
			
			
			setResult(RESULT_OK,returnIntent);
				finish=true; 
		}else if(!finish) {
			finish=false;
			exitDialog();
		}
		if(finish)
		super.finish();
	}
	 public void animateMarker( final LatLng toPosition, final boolean hideMarker) {
	        final Handler handler = new Handler();
	        final long start = SystemClock.uptimeMillis();
	        Projection proj = map.getProjection();
	        Point startPoint = proj.toScreenLocation(eventMarker.getPosition());
	        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
	        final long duration = 500;

	        final Interpolator interpolator = new LinearInterpolator();

	        handler.post(new Runnable() {
	            @Override
	            public void run() {
	                long elapsed = SystemClock.uptimeMillis() - start;
	                float t = interpolator.getInterpolation((float) elapsed
	                        / duration);
	                double lng = t * toPosition.longitude + (1 - t)
	                        * startLatLng.longitude;
	                double lat = t * toPosition.latitude + (1 - t)
	                        * startLatLng.latitude;
	                eventMarker.setPosition(new LatLng(lat, lng));
	    				  eventMarker.setTitle(Utils.getAddress(toPosition, context));
	    			 
	              
	                if (t < 1.0) {
	                    // Post again 16ms later.
	                    handler.postDelayed(this, 16);
	                } else {
	                    if (hideMarker) {
	                    	eventMarker.setPosition(toPosition);
	                        eventMarker.setVisible(false);
	                    } else {
	                        eventMarker.setVisible(true);
	                    }
	                }
	            }
	        });
	    }
	 protected synchronized void buildGoogleApiClient() {
		      mGoogleApiClient = new GoogleApiClient.Builder(this)
		        .addConnectionCallbacks(this)
		        .addOnConnectionFailedListener(this)
		        .addApi(LocationServices.API)
		        .build();
		}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		  Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
	                mGoogleApiClient);
	        if (mLastLocation != null) {
	        	map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 18));
			    map.setOnMarkerDragListener(this);
			    // Zoom in, animating the camera.
			    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
			 
	        }
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btSubmit:
			finish();
			break;
		default:
			break;
		}
	}
	
	 
	
	public boolean exitDialog(){
		  
		 
		  
			
			final AlertDialog.Builder builder		= new AlertDialog.Builder(this);
			
			builder.setTitle("Washnow");
			 builder.setMessage("No location selected, Please drop pin or search for location");
			 builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					finish=true;
					finish();
					
				}
			});
			 
			 builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						//dialog.dismiss();
						finish=false;
					}
				});
			 final AlertDialog dialog = builder.create();
				dialog.show();
			return false;
		  
		
	  
	  }

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map =googleMap;
		initLayout();

	}
}
*/

}