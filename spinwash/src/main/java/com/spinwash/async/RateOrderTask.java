package com.spinwash.async;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.spinwash.OrderStatusActivity;
import com.spinwash.http.RestClient;
import com.spinwash.http.RestClient.RequestMethod;
import com.spinwash.utils.Utils;
import com.spinwash.vo.TResponse;
import com.spinwash.widgets.TCustomProgressDailogue;

import java.util.List;


public class RateOrderTask extends
		AsyncTask<String, Void, TResponse<String>> {

	private Context ctx;
	private  List<Pair<String,String>> params;
	private TCustomProgressDailogue pd;
	public RateOrderTask(Context context, List<Pair<String,String>> params	) {
		 this.ctx = context;
		this.params = params;
		this.pd = new TCustomProgressDailogue(ctx);
		this.pd.setCancelable(false);
	}

	@Override
	protected void onPreExecute() {
		pd.show();
	}

	@Override
	protected TResponse<String> doInBackground(String... params) {
		TResponse<String> response =  new TResponse<String>();

		try {
			String result=null;
			RestClient restClient = new RestClient(Utils.URL_SERVER);
			 
	//			restClient.addParam("id", this.params);
			//	Log.i("id", this.params);
			for (Pair<String,String> np : this.params) {
				restClient.addParam(np.first,  np.second);
			}
	         
			restClient.execute(RequestMethod.POST);
			result = restClient.getResponseString();
			 response.setResponseContent(result);
			response.setHasError(false);

		} catch (Exception e) {
			 
			response.setResponseContent(e.toString());
			response.setHasError(true);
			e.printStackTrace();
		}

		return response;
	}

	@Override
	protected void onPostExecute(TResponse<String> result) {

		 
		if (pd.isShowing()){
			pd.dismiss();
		}

		if (ctx instanceof OrderStatusActivity) {
		  ((OrderStatusActivity) ctx).feedbackStatus(result);
			// Log.v("response", result.getResponseContent());
		}
	

	}

}
