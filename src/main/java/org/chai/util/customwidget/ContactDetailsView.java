package org.chai.util.customwidget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.chai.R;
import org.chai.model.CustomerContact;

/**
 * Created by victor on 3/13/15.
 */
public class ContactDetailsView extends LinearLayout {
    private Context context;
    private Activity parentActivity;
    private TextView contentText;
    private ExpandablePanel panel;

    public ContactDetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout =(LinearLayout)inflater.inflate(R.layout.expandable_view_template, this, true);

        contentText = (TextView) linearLayout.findViewById(R.id.value);
    }

    public ContactDetailsView(Activity activity, CustomerContact customerContact){
        super(activity.getApplicationContext());
        this.context = activity.getApplicationContext();
        this.parentActivity = activity;
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.expandable_view_template, this, true);

        contentText = (TextView)findViewById(R.id.value);
        setContent(customerContact);
        panel = (ExpandablePanel)findViewById(R.id.expandablePanel);

        panel.setOnExpandListener(new ExpandablePanel.OnExpandListener() {
            public void onCollapse(View handle, View content) {
                Button btn = (Button) handle;
                btn.setText("More");

//                panel.setCollapsedHeight(100);
            }

            public void onExpand(View handle, View content) {
                Button btn = (Button) handle;
//                panel.setCollapsedHeight(30);
                btn.setText("Less");
            }
        });
    }

    public void setContent(CustomerContact contact) {
        this.contentText.setText("Number:"+(contact.getContact() == null ? "NA" : contact.getContact())+"\n"
                +"Name:"+(contact.getNames()== null ? "NA" : contact.getNames())+"\n"
                +"Gender:"+(contact.getGender()== null ? "NA" : contact.getGender())+"\n"
                +"Role:"+(contact.getRole()== null ? "NA" : contact.getRole()));
    }
}
