package com.washnow.widgets;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.EditText;

import com.washnow.R;
import com.washnow.async.RateOrderTask;
import com.washnow.vo.OrderVo;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;


/**
 * Created by aswin on 14/01/17.
 */

public class FeedbackDialog extends Dialog implements View.OnClickListener {
    private Context context;
    OrderVo orderVo;
    private static final int CAMERA_REQUEST_DIALOG = 2001;
    private final int CROP_PIC_DIALOG = 2002;
    private static final int PICK_FROM_FILE_DIALOG = 2003;

    public FeedbackDialog(Context context, OrderVo orderVo) {
        super(context, R.style.full_screen_dialog);
        this.setContentView(R.layout.feedback_layout);
        findViewById(R.id.btSubmit).setOnClickListener(this);
        this.context = context;
        setCancelable(false);
        MaterialRatingBar ratingBar = (MaterialRatingBar) findViewById(R.id.rbFeedback);
        ratingBar.setStepSize((float) 1.0f);
        ratingBar.setMax(5);
        ratingBar.setNumStars(5);
        this.orderVo = orderVo;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSubmit:
                submitFeedback();
                break;


        }
    }

    public void submitFeedback() {
        EditText etFeedback = (EditText) findViewById(R.id.etFeedback);
        MaterialRatingBar ratingBar = (MaterialRatingBar) findViewById(R.id.rbFeedback);

        ArrayList<Pair<String, String>> params = new ArrayList<Pair<String, String>>();

        params.add(new Pair<String, String>("process", "add_rating"));
        params.add(new Pair<String, String>("order_id", orderVo.getId()));
        params.add(new Pair<String, String>("rating", String.valueOf(ratingBar.getProgress())));
        if (etFeedback.getText() != null)
            params.add(new Pair<String, String>("feedback", etFeedback.getText().toString()));

        new RateOrderTask(context, params).execute();

    }


}
