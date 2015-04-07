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
    TextView teamAverageTextView;


    public SummaryReportTable(Context context,String item,String weekly,String monthly,String teamAverage){
        super(context);
        this.setLayoutParams(rowParams);
        this.setWeightSum(1f);
        this.setBackgroundColor(Color.WHITE);

        itemTextView = new TextView(context);
        itemParams.weight = 0.25f;
        itemTextView.setBackgroundResource(R.drawable.bordertextfield);
        itemTextView.setLayoutParams(itemParams);
        itemTextView.setText(item);
        itemTextView.setTextColor(Color.BLACK);

        weeklyTextView  =new TextView(context);
        weeklyParams.weight = 0.25f;
        weeklyTextView.setBackgroundResource(R.drawable.bordertextfield);
        weeklyTextView.setLayoutParams(itemParams);
        weeklyTextView.setText(weekly);
        weeklyTextView.setTextColor(Color.BLACK);

        monthlyTextView = new TextView(context);
        monthlyParams.weight = 0.25f;
        monthlyTextView.setBackgroundResource(R.drawable.bordertextfield);
        monthlyTextView.setLayoutParams(itemParams);
        monthlyTextView.setText(monthly);
        monthlyTextView.setTextColor(Color.BLACK);

        teamAverageTextView = new TextView(context);
        monthlyParams.weight = 0.25f;
        teamAverageTextView.setBackgroundResource(R.drawable.bordertextfield);
        teamAverageTextView.setLayoutParams(itemParams);
        teamAverageTextView.setText(teamAverage);
        teamAverageTextView.setTextColor(Color.BLACK);

        this.addView(itemTextView);
        this.addView(weeklyTextView);
        this.addView(monthlyTextView);
        this.addView(teamAverageTextView);
    }


}
