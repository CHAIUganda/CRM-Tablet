package org.chai.util.customwidget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import org.chai.R;
import org.chai.model.CustomerContact;

import java.util.UUID;

/**
 * Created by victor on 3/6/15.
 */
public class ContactWidgetView extends LinearLayout {
    private Button displayBtn;
    private Context context;
    private Activity parentActivity;
    private CustomerContact customerContact;

    public ContactWidgetView(Context context,CustomerContact customerContact) {
        super(context);
        this.context = context;
        this.customerContact = customerContact;

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.contact_template, this, true);
        displayBtn = (Button)getChildAt(0);
        displayBtn.setText((customerContact.getNames()!=null?customerContact.getNames():"no name")+":"+customerContact.getContact());
        displayBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit contact
                showEditForm();
            }
        });
    }

    public ContactWidgetView(Activity activity,CustomerContact customerContact) {
        super(activity.getApplicationContext());
        this.context = activity.getApplicationContext();
        this.parentActivity = activity;
        setCustomerContact(customerContact);

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.contact_template, this, true);
        displayBtn = (Button)getChildAt(0);
        displayBtn.setText((customerContact.getNames()!=null?customerContact.getNames():"no name")+":"+customerContact.getContact());
        displayBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit contact
                showEditForm();
            }
        });
    }

    private void showEditForm() {
        LayoutInflater layoutInflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View entryView = layoutInflater.inflate(R.layout.add_contact_layout, null);
        ((EditText) entryView.findViewById(R.id.customer_contact_telephone)).setText(customerContact.getContact());
        ((EditText) entryView.findViewById(R.id.customer_contact_names)).setText(customerContact.getNames());
        setSpinnerSelection(((Spinner) entryView.findViewById(R.id.customer_contact_gender)), customerContact.getGender());
        setSpinnerSelection(((Spinner) entryView.findViewById(R.id.customer_contact_type)),customerContact.getRole());

        AlertDialog.Builder alert = new AlertDialog.Builder(parentActivity);
        alert.setIcon(R.drawable.icon).setTitle("Contact").setView(entryView).setPositiveButton("Update", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int button) {
                customerContact.setContact(((EditText) entryView.findViewById(R.id.customer_contact_telephone)).getText().toString());
                customerContact.setNames(((EditText) entryView.findViewById(R.id.customer_contact_names)).getText().toString());
                customerContact.setGender(((Spinner) entryView.findViewById(R.id.customer_contact_gender)).getSelectedItem().toString());
                customerContact.setRole(((Spinner) entryView.findViewById(R.id.customer_contact_type)).getSelectedItem().toString());
                setButtonLabel();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int button) {

            }
        });
        alert.show();
    }

    public CustomerContact getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(CustomerContact customerContact) {
        this.customerContact = customerContact;
    }
    private void setSpinnerSelection(Spinner spinner, String item) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int position = adapter.getPosition(item);
        spinner.setSelection(position);
    }
    private void setButtonLabel(){
        displayBtn.setText((customerContact.getNames()!=null?customerContact.getNames():"no name")+":"+customerContact.getContact());
    }
}
