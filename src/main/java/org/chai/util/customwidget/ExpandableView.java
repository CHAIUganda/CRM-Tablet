package org.chai.util.customwidget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.chai.R;
import org.chai.model.CustomerContact;

/**
 * Created by victor on 3/13/15.
 */
public class ExpandableView extends LinearLayout {
    private Context context;
    private Activity parentActivity;
    private TextView headerText;
    private TextView contentText;

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.expandable_view_template, this, true);

        headerText = (TextView) getChildAt(0);
        contentText = (TextView) getChildAt(1);
        contentText.setVisibility(View.GONE);
    }

    public ExpandableView(Activity activity,CustomerContact customerContact){
        super(activity.getApplicationContext());
        this.context = activity.getApplicationContext();
        this.parentActivity = activity;
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.expandable_view_template, this, true);

        headerText = (TextView) getChildAt(0);
        contentText = (TextView) getChildAt(1);
        contentText.setVisibility(View.GONE);
        contentText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle_contents(view);
            }
        });
        setContent(customerContact);
    }

    /**
     * onClick handler
     */
    public void toggle_contents(View v) {
        contentText.setVisibility(contentText.isShown()? View.GONE: View.VISIBLE);
            if(contentText.isShown()){
                slideUp(context, contentText);
                contentText.setVisibility(View.GONE);
            }
            else{
                contentText.setVisibility(View.VISIBLE);
                slideDown(context, contentText);
            }
    }

    public static void slideDown(Context ctx, View v){
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slideUp(Context ctx, View v){

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public void setContent(CustomerContact contact) {
        this.headerText.setText((contact.getContact() == null ? "NA" : contact.getContact())+"\n"
                +"Name:"+(contact.getNames()== null ? "NA" : contact.getNames())+"\n"
                +"Gender:"+(contact.getGender()== null ? "NA" : contact.getGender())+"\n"
                +"Role:"+(contact.getRole()== null ? "NA" : contact.getRole()));
    }
}
