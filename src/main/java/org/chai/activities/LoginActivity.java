package org.chai.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.chai.R;
import org.chai.rest.Place;
import org.chai.rest.RestClient;

public class LoginActivity extends Activity {

    private static String TAG = "chai-crm-android";

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.login_activity);

        //create initial data incase there is none
      /*  SampleData sampleData = new SampleData(this);
        sampleData.createBaseData();
*/
        Activity activity = this;
        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                final String user = ((EditText) findViewById(R.id.userTxt)).getText().toString();
                final String pass = ((EditText) findViewById(R.id.passwordTxt)).getText().toString();
                final Place place = new Place();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean islogin = place.login(user, pass);

                        if (islogin) {
                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                            RestClient.userName = user;
                            RestClient.password = pass;
                            startActivity(i);
                        } else {
                            LoginActivity.this.runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Couldnt Login,Please check your Username or Password", Toast.LENGTH_LONG).show();
                                        }
                                    }
                            );

                        }
                    }
                }).start();


            }
        });
    }

}

