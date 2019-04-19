package com.washnow;

import android.os.Bundle;
import android.widget.ExpandableListView;

import com.washnow.adapter.FAQListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQActivity  extends BaseBackActivity {
 
    FAQListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, String> listDataChild;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_layout);
 
        setTitle("FAQ");
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new FAQListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }
 
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, String>();
 
        // Adding child data
        listDataHeader.add("WHAT DOES Washnow DO?");
        listDataHeader.add("HOW DO I REQUEST A Pickup?");
        listDataHeader.add("HOW DO I CHANGE MY ORDER TIME?");
        listDataHeader.add("WHAT SHOULD I DO IF IM HAVING TECHNICAL/APP ISSUES?");
        listDataHeader.add("HOW DO I CONTACT SUPPORT?");
        listDataHeader.add("HOW DO I PAY? HOW AM I BILLED?");
        listDataHeader.add("IS THERE AN ORDER MINIMUM?");
        listDataHeader.add("IS THERE A DELIVERY FEE?");
        
        // Adding child data
        List<String> ansList = new ArrayList<String>();
        ansList.add("WASHNOW is the easiest and most convenient dry cleaning & laundry option around. You can Request a Laundry Pickup and get your clean clothes delivered to you in 48 hours");
        ansList.add("download our app for iPhone or Android. You will be asked to enter your email address, input your delivery information, and then tap a single button for clean clothes in 48 hours.");
        ansList.add("Change of plans? Not to worry! After you�ve placed an order, you can click on either the pick up or drop off card to change your times.");
        ansList.add("Our engineers are hard at work improving the app and testing for bugs, but we aren�t perfect. If you are experiencing issues with the app, we would love to hear from you via phone +974 3378 4151 or email help@washnow.me");
        ansList.add("Have questions, compliments, or just have a thing for speaking to humans? Feel free to email hello@washnow.me contact us directly at +974 3300 7779. ");
        ansList.add("You need NOT give us your credit card or debit card details. Its CASH ON DELIVERY.\nYou will be charged at the time of delivery of your order.\nYou will have the amount reflected on your App. Have a question about charges? Feel free to reach out to customer support at any time.");
        ansList.add("There is no Minimum Order with WASHNOW. You can place your order for as low  as QAR 5.00");
        ansList.add("We charge a fee of QAR 5.00 on total order value.");
       
         
         for(int i=0;i< listDataHeader.size();i++){
        	 listDataChild.put(listDataHeader.get(i), ansList.get(i)); // Header, Child data
             	 
         }
 
         
    }
}