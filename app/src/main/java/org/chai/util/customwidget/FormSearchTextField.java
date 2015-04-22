package org.chai.util.customwidget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.chai.activities.IViewManipulator;
import org.chai.util.Utils;

/**
 * Created by Zed on 4/10/2015.
 */
public class FormSearchTextField extends EditText {
    public boolean isFocused = false;
    public boolean keyboardShowing = false;

    IViewManipulator activity;
    ListView listView;

    public FormSearchTextField(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            InputMethodManager mgr = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
            activity.showAllViews();
            return true;
        }
        return false;
    }

    public void setViewManipulator(IViewManipulator manipulator){
        this.activity = manipulator;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        Utils.log("Focus chaned -> " + focused);
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        isFocused = focused;
    }

    public void setListView(ListView list){
        this.listView = list;
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.log("List Item clicked");
                ArrayAdapter<String> adapter = (ArrayAdapter<String>)parent.getAdapter();
                String selected = adapter.getItem(position);
                FormSearchTextField.this.setText(selected);

                InputMethodManager mgr = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(FormSearchTextField.this.getWindowToken(), 0);
                activity.showAllViews();
            }
        });
    }
}
