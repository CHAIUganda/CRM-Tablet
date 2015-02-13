package org.chai.util.customwidget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;
import org.chai.R;

/**
 * Created by victor on 2/12/15.
 */
public class SummaryReportTable extends TableRow {
    LayoutParams rowParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    LayoutParams itemParams = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, Gravity.CENTER);
    LayoutParams weeklyParams = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, Gravity.LEFT);
    LayoutParams monthlyParams = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, Gravity.LEFT);

    TextView itemTextView;
    TextView weeklyTextView;
    TextView monthlyTextView;


    public SummaryReportTable(Context context,String item,String weekly,String monthly){
        super(context);
        this.setLayoutParams(rowParams);
        this.setWeightSum(0.9f);
        this.setBackgroundColor(Color.WHITE);

        itemTextView = new TextView(context);
        itemParams.weight = 0.3f;
        itemTextView.setBackgroundResource(R.drawable.bordertextfield);
        itemTextView.setLayoutParams(itemParams);
        itemTextView.setText(item);
        itemTextView.setTextColor(Color.BLACK);

        weeklyTextView  =new TextView(context);
        weeklyParams.weight = 0.3f;
        weeklyTextView.setBackgroundResource(R.drawable.bordertextfield);
        weeklyTextView.setLayoutParams(itemParams);
        weeklyTextView.setText(weekly);
        weeklyTextView.setTextColor(Color.BLACK);

        monthlyTextView = new TextView(context);
        monthlyParams.weight = 0.3f;
        monthlyTextView.setBackgroundResource(R.drawable.bordertextfield);
        monthlyTextView.setLayoutParams(itemParams);
        monthlyTextView.setText(monthly);
        monthlyTextView.setTextColor(Color.BLACK);

        this.addView(itemTextView);
        this.addView(weeklyTextView);
        this.addView(monthlyTextView);
    }


}
