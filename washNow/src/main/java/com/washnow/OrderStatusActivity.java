package com.washnow;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.washnow.async.GetOrderTask;
import com.washnow.async.RateOrderTask;
import com.washnow.parser.Parser;
import com.washnow.utils.DateUtil;
import com.washnow.utils.Utils;
import com.washnow.utils.ZTypeface;
import com.washnow.vo.OrderVo;
import com.washnow.vo.PickupSlot;
import com.washnow.vo.TResponse;
import com.washnow.widgets.FeedbackDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class OrderStatusActivity extends BaseActivity implements OnClickListener {
    private Calendar startDate, endDate;
    private OrderVo order;
    private FeedbackDialog feedbackDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderstatus_layout);
        actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_backnav);
        context = this;

        Intent intent = getIntent();
        if (intent.hasExtra("order")) {
            order = (OrderVo) intent.getSerializableExtra("order");
        } else {
            order = Utils.getOrder(context);

        }
//		Log.v("time", order)

        initLayout();
        setMenu();
        Toolbar parent = (Toolbar) actionBar.getCustomView().getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (order != null) {
            new GetOrderTask(context).execute(order.getId());
        }
    }

    public void initLayout() {
        setTitle("ORDER STATUS");
        RelativeLayout rlStatus = (RelativeLayout) findViewById(R.id.rlStatus);
        ImageView imStatus = (ImageView) findViewById(R.id.imStatus);
        TextView tvPickUp = (TextView) findViewById(R.id.tvPickUp);
        TextView tvPickUpDate = (TextView) findViewById(R.id.tvPickUpDate);
        TextView tvPickUpTime = (TextView) findViewById(R.id.tvPickUpTime);
        TextView tvDelivery = (TextView) findViewById(R.id.tvDelivery);
        TextView tvDeliveryDate = (TextView) findViewById(R.id.tvDeliveryDate);
        TextView tvDeliveryTime = (TextView) findViewById(R.id.tvDeliveryTime);
        TextView tvTitle1 = (TextView) findViewById(R.id.tvTitle1);
        TextView tvTitle2 = (TextView) findViewById(R.id.tvTitle2);
        TextView tvTotal = (TextView) findViewById(R.id.tvTotal);
        TextView tvOrderMsg = (TextView) findViewById(R.id.tvOrderMsg);
        TextView tvAmountTitle = (TextView) findViewById(R.id.tvAmountTitle);
        TextView tvTitle = (TextView) findViewById(R.id.tv_actionbar_title);
        tvTitle1.setTypeface(ZTypeface.UbuntuR(context));
        tvTitle2.setTypeface(ZTypeface.UbuntuR(context));
        tvPickUp.setTypeface(ZTypeface.UbuntuR(context));
        tvPickUpDate.setTypeface(ZTypeface.UbuntuR(context));
        tvPickUpTime.setTypeface(ZTypeface.UbuntuR(context));
        tvDelivery.setTypeface(ZTypeface.UbuntuR(context));
        tvDeliveryDate.setTypeface(ZTypeface.UbuntuR(context));
        tvDeliveryTime.setTypeface(ZTypeface.UbuntuR(context));
        tvTotal.setTypeface(ZTypeface.UbuntuR(context));
        tvAmountTitle.setTypeface(ZTypeface.UbuntuR(context));
        tvAmountTitle.setVisibility(View.INVISIBLE);
        String name = Utils.getSharedPreference(context, "name");

        if (order.getStatus().equalsIgnoreCase("pending")) {
            imStatus.setImageResource(R.drawable.ic_status_1);
            tvTitle1.setText("PICKUP");
            tvTitle2.setText("Scheduled");
            tvOrderMsg.setText("Hey " + name + ", Your Order has been Scheduled for Pickup.  Keep the clothes ready in a bag for our Wash crew to collect\"");
            rlStatus.setOnClickListener(this);
        } else if (order.getStatus().equalsIgnoreCase("accepeted")) {
            imStatus.setImageResource(R.drawable.ic_status_1);
            tvOrderMsg.setText("Hey " + name + ", Your Order has been Scheduled for Pickup.  Keep the clothes ready in a bag for our Wash crew to collect\"");
            tvTitle1.setText("PICKUP");
            tvTitle2.setText("Scheduled");
            rlStatus.setOnClickListener(this);

        } else if (order.getStatus().equalsIgnoreCase("washing")) {
            imStatus.setImageResource(R.drawable.ic_status_2);
            tvTitle1.setText("CLEANING");
            tvTitle2.setText("In Progress");
            tvOrderMsg.setText("Your clothes are being clean and ready");
            rlStatus.setOnClickListener(null);
        } else if (order.getStatus().equalsIgnoreCase("completed")) {
            imStatus.setImageResource(R.drawable.ic_status_3);
            tvAmountTitle.setVisibility(View.VISIBLE);
            tvTitle1.setText("READY");
            tvTitle2.setText("To Deliver");
            tvTotal.setText("QAR " + order.getTotal());
            tvOrderMsg.setText("Your Clothes are ready to deliver. Our Wash crew will contact you upto one hour just before Delivery\n\nPayment:Cash on Delivery");
            rlStatus.setOnClickListener(null);
        } else if (order.getStatus().equalsIgnoreCase("paid")) {
            imStatus.setImageResource(R.drawable.ic_status_4);
            tvTitle1.setText("ORDER");
            tvTitle2.setText("Completed");
            setTitle("REQUEST NEW PICKUP");
            tvAmountTitle.setVisibility(View.VISIBLE);
            tvTotal.setText("QAR " + order.getTotal());
            tvTitle.setOnClickListener(this);
            tvOrderMsg.setText("Please click here to share your feedback");
            tvOrderMsg.setOnClickListener(this);
            rlStatus.setOnClickListener(null);
            if(feedbackDialog==null||!feedbackDialog.isShowing()) {
                feedbackDialog = new FeedbackDialog(context,order);
                feedbackDialog.show();
            }


        } else if (order.getStatus().equalsIgnoreCase("cancelled")) {
            imStatus.setImageResource(R.drawable.ic_status_4);
            tvTitle1.setText("ORDER");
            tvTitle2.setText("Cancelled");
            setTitle("REQUEST NEW PICKUP");
            tvTotal.setTextSize(16);
            tvTotal.setText(order.getNotes());
            tvTitle.setOnClickListener(this);
            tvOrderMsg.setText("Your order has been cancelled. Please click here to contact customer support");
            tvOrderMsg.setOnClickListener(this);

            rlStatus.setOnClickListener(null);
        }
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        startDate.setTime(DateUtil.stringToDate(order.getPick_up_date(), DateUtil.DATETIME_SQL));
        endDate.setTime(DateUtil.stringToDate(order.getDelivery_date(), DateUtil.DATETIME_SQL));

        Date date = startDate.getTime();
        int diff = DateUtil.daysDiff(new Date(), date);
        if (diff == 0) {
            tvPickUpDate.setText("Today");
        } else if (diff == 1) {
            tvPickUpDate.setText("Tomorrow");
        } else {
            tvPickUpDate.setText(DateUtil.dateToString(date, DateUtil.DATE_NAME_PATTERN));
        }
        date = endDate.getTime();
        diff = DateUtil.daysDiff(new Date(), date);
        if (diff == 0) {
            tvDeliveryDate.setText("Today");
        } else if (diff == 1) {
            tvDeliveryDate.setText("Tomorrow");
        } else {
            tvDeliveryDate.setText(DateUtil.dateToString(date, DateUtil.DATE_NAME_PATTERN));
        }
        final ArrayList<PickupSlot> sList = Utils.getPickupSlot();
        for (PickupSlot vo : sList) {
            if (vo.id == startDate.getTime().getHours()) {
                tvPickUpTime.setText(vo.time.replace(":00", ""));


            }
        }

        // tvPickUpTime.setText(DateUtil.dateToString(startDate.getTime(), DateUtil.TIME_12HRS_SHORT));
        // tvDeliveryTime.setText(DateUtil.dateToString(endDate.getTime(), DateUtil.TIME_12HRS_SHORT));
        if (date.getHours() == 18) {
            tvDeliveryTime.setText("6PM - 7PM");
        } else if (date.getHours() == 19) {
            tvDeliveryTime.setText("7PM - 8PM");

        } else if (date.getHours() == 20) {
            tvDeliveryTime.setText("8PM - 9PM");

        } else if (date.getHours() == 21) {
            tvDeliveryTime.setText("9PM - 10PM");

        } else if (date.getHours() == 17) {
            tvDeliveryTime.setText("5PM - 6PM");

        } else if (date.getHours() == 16) {
            tvDeliveryTime.setText("4PM - 5PM");

        } else {
            tvDeliveryTime.setText("ELSE");
        }


        tvPickUp.setText("PICKUP TIME");
        tvDelivery.setText("RETURN TIME");
    }


    public void updateOrder(TResponse<String> result) {


        if (result == null) {
            Toast.makeText(context, "unable to update order status \n please check network connection", Toast.LENGTH_SHORT).show();
        } else if (result.isHasError()) {
            Toast.makeText(context, "unable to update order status \n please try later", Toast.LENGTH_SHORT).show();

        } else if (result.getResponseContent() != null) {

            try {
                order = Parser.getOrders(result.getResponseContent()).get(0);
                Utils.setOrder(context, order);

                Toast.makeText(context, "Order Updated", Toast.LENGTH_SHORT).show();

                initLayout();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.tv_actionbar_title:
                Utils.deleteSharedPreference(context, "orderObject");
                Intent intent = new Intent(context, RequestPickupActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.rlStatus:
                //showCancelDialog();
                break;
            case R.id.tvOrderMsg:
                if (order.getStatus().equalsIgnoreCase("paid")) {

                    showFeedback();
                } else if (order.getStatus().equalsIgnoreCase("cancelled")) {
                    try {

                        Uri number = Uri.parse("tel:33784151");
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                        startActivity(callIntent);

                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,"Please call 33784151",Toast.LENGTH_LONG).show();
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        if (order.getStatus().equalsIgnoreCase("paid") || order.getStatus().equalsIgnoreCase("cancelled")) {
            Intent intent = new Intent(context, RequestPickupActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }

    public void CancelOrder() {

        order.setStatus("Cancelled");

        ArrayList<Pair<String, String>> params = new ArrayList<Pair<String, String>>();

        params.add(new Pair<String, String>("name", order.getName()));
        params.add(new Pair<String, String>("email", order.getEmail()));
        params.add(new Pair<String, String>("address", order.getAddress()));
        params.add(new Pair<String, String>("phone_number", order.getPhone_number()));
        params.add(new Pair<String, String>("longitude", order.getLongitude()));
        params.add(new Pair<String, String>("latitude", order.getLatitude()));
        params.add(new Pair<String, String>("notes", order.getNotes()));

        params.add(new Pair<String, String>("pick_up_date", DateUtil.dateToString(startDate.getTime(), DateUtil.DATETIME_SQL)));
        params.add(new Pair<String, String>("delivery_date", DateUtil.dateToString(endDate.getTime(), DateUtil.DATETIME_SQL)));
        params.add(new Pair<String, String>("status", order.getStatus()));
        params.add(new Pair<String, String>("extras", order.getExtras()));
        params.add(new Pair<String, String>("id", order.getId()));
        params.add(new Pair<String, String>("total", order.getTotal()));

        params.add(new Pair<String, String>("process", "updateorder"));
        new com.washnow.async.OrderUpdateTask(context, params).execute("");


    }

    public boolean showCancelDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Wash Now");
        builder.setMessage("Do you want to cancel the order");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                CancelOrder();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                //dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        return false;


    }

    public void showNoteDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Wash Now");
        alert.setMessage("Please share the reason for cancellation");
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        //input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                order.setNotes(value);
                initLayout();
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                initLayout();
            }
        });

        alert.show();
    }

    public void showFeedback() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Wash Now");
        alert.setMessage("Please rate your last order");
        // Set an EditText view to get user input
         //input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LayoutInflater factory = LayoutInflater.from(this);
        final View feedDialogView = factory.inflate(R.layout.feedback_layout, null);

        MaterialRatingBar ratingBar = (MaterialRatingBar) feedDialogView.findViewById(R.id.rbFeedback);
        ratingBar.setStepSize((float) 1.0f);
        ratingBar.setMax(5);
        ratingBar.setNumStars(5);

         alert.setView(feedDialogView);
        //selectid&id=3866
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                MaterialRatingBar ratingBar = (MaterialRatingBar) feedDialogView.findViewById(R.id.rbFeedback);

                EditText etFeedback =(EditText)feedDialogView.findViewById(R.id.etFeedback);
                ArrayList<Pair<String, String>> params = new ArrayList<Pair<String, String>>();

                params.add(new Pair<String, String>("process","add_rating"));
                params.add(new Pair<String, String>("order_id", order.getId()));
                params.add(new Pair<String, String>("rating", String.valueOf(ratingBar.getProgress())));
                if(etFeedback.getText()!=null)
                params.add(new Pair<String, String>("feedback", etFeedback.getText().toString()));

                new RateOrderTask(context,params).execute();
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
             }
        });

        alert.show();
    }

    public void feedbackStatus(TResponse<String> result) {

        if(feedbackDialog!=null&&feedbackDialog.isShowing()) {
            feedbackDialog.dismiss();
        }
        Utils.deleteSharedPreference(context,"orderObject");
        Toast.makeText(context, "Thank you for your feedback", Toast.LENGTH_SHORT).show();
        finish();
    }
}
