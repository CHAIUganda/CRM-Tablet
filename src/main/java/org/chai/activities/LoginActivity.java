package org.chai.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.chai.R;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.User;
import org.chai.model.UserDao;
import org.chai.rest.Place;
import org.chai.rest.RestClient;
import org.chai.util.Utils;

import java.util.List;
import java.util.UUID;

public class LoginActivity extends Activity {

    private static String TAG = "chai-crm-android";
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private UserDao userDao;
    private String role = User.ROLE_DETAILER;

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
        initialiseGreenDao();
        //create initial data incase there is none
      /*  SampleData sampleData = new SampleData(this);
        sampleData.createBaseData();
*/
        Activity activity = this;
        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
//                onLoginSuccessfull("sales1", "pass",User.ROLE_SALES);
               final ProgressDialog dialog = showProgressDialog();
                final String user = ((EditText) findViewById(R.id.userTxt)).getText().toString();
                final String pass = ((EditText) findViewById(R.id.passwordTxt)).getText().toString();

                final Place place = new Place();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean islogin = false;
                        //check ofline login
                        List<User> loggedInUser = userDao.queryBuilder().where(UserDao.Properties.UserName.eq(user), UserDao.Properties.Password.eq(Utils.encrypeString(pass))).list();
                        if (loggedInUser.isEmpty()) {
                            //ensure we dont have more than one user on one tablet
                            if (userDao.loadAll().isEmpty()) {
                                User remoteUser = place.login(user, pass);
                                //add this user to offline db
                                if (remoteUser!=null) {
                                    User newUser = new User(null);
                                    newUser.setUuid(UUID.randomUUID().toString());
                                    newUser.setUserName(remoteUser.getUserName());
                                    newUser.setPassword(Utils.encrypeString(pass));
                                    newUser.setRole(remoteUser.getRole());
                                    userDao.insert(newUser);
                                    role = remoteUser.getRole();
                                }
                                islogin = true;

                            }
                        } else {
                            islogin = true;
                            role = loggedInUser.get(0).getRole();
                        }
                        if (islogin) {
                            dialog.dismiss();
                            onLoginSuccessfull(user, pass,role);
                        } else {
                            dialog.dismiss();
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

    private ProgressDialog showProgressDialog() {
        final ProgressDialog progressDialog  = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging in,Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        return progressDialog;
    }

    private void onLoginSuccessfull(String user, String pass,String role) {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        RestClient.userName = user;
        RestClient.password = pass;
        RestClient.role = role;
        startActivity(i);
    }


    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            userDao = daoSession.getUserDao();
        } catch (Exception ex) {
            Toast.makeText(this, "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


}

