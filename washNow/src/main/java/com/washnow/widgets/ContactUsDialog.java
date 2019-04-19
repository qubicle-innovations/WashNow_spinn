package com.washnow.widgets;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.washnow.R;


/**
 * Created by aswin on 08/07/16.
 */
public class ContactUsDialog extends Dialog implements View.OnClickListener {

    private Context context;

    public ContactUsDialog(Context context) {
        super(context, R.style.full_screen_dialog);
        this.setContentView(R.layout.contact_layout);
        this.context = getContext();
        initLayout();



    }



    private void initLayout(){

            findViewById(R.id.imDial).setOnClickListener(this);
            findViewById(R.id.imEmail).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imEmail:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "help@washnow.me", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Wash now support");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                context.startActivity(Intent.createChooser(emailIntent, "Contact support team"));
                dismiss();
                break;
            case R.id.imDial:
                try {

                    Uri number = Uri.parse("tel:33007779");
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    context.startActivity(callIntent);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Please call 33007779", Toast.LENGTH_LONG).show();
                }
                dismiss();
                break;
            default:
                dismiss();
                break;

        }
    }

 

}
