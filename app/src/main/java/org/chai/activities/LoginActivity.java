package org.chai.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.splunk.mint.Mint;

import org.chai.R;
import org.chai.activities.tasks.NewOrderActivity;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.User;
import org.chai.model.UserDao;
import org.chai.model.Village;
import org.chai.model.VillageDao;
import org.chai.rest.Place;
import org.chai.rest.RestClient;
import org.chai.util.AccountManager;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.List;
import java.util.UUID;

public class LoginActivity extends BaseActivity {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private UserDao userDao;
    private VillageDao villageDao;
    private String role = User.ROLE_DETAILER;
    Toolbar toolbar;

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        skipLogin = true; //This is a login screen - don't check for logins

        super.onCreate(savedInstanceState);

        if(AccountManager.offlineLogin(this, false)){
            Intent i = new Intent(LoginActivity.this, NewOrderActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }

        Mint.initAndStartSession(LoginActivity.this, "8255bd80");

        setContentView(R.layout.login_activity);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialiseGreenDao();
        List<Village> villages = villageDao.loadAll();
        Activity activity = this;

        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
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
                                if (remoteUser != null) {
                                    User newUser = new User(null);
                                    newUser.setUuid(UUID.randomUUID().toString());
                                    newUser.setUserName(remoteUser.getUserName());
                                    newUser.setPassword(Utils.encrypeString(pass));
                                    newUser.setRole(remoteUser.getRole());
                                    userDao.insert(newUser);
                                    role = remoteUser.getRole();
                                    islogin = true;
                                }else{
                                    islogin = false;
                                }
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
                                            Utils.showError(LoginActivity.this, "Error:", "Couldnt Login, Please check your Username or Password");
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
        progressDialog.setMessage("Logging in, Please wait...");
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
        AccountManager.saveUsername(user, this);
        AccountManager.savePassword(pass, this);
        startActivity(i);
    }


    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            userDao = daoSession.getUserDao();
            villageDao = daoSession.getVillageDao();
        } catch (Exception ex) {
            Toast.makeText(this, "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

