package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private LoginButton facebookLoginButton;
    private TwitterLoginButton twitterLoginButton;
    private Button caButton;
    private Button goToHashtagSearchButton;
    private TwitterSession twitterSession;
    private static CallbackManager facebookCallbackManager;
    private static String EMAIL = "email";
    private static AccessToken accessToken;

    public static AccessToken getAccessToken() {
        return accessToken;
    }


    private String TAG = "Test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_main);

        facebookLoginButton = findViewById(R.id.login_button);
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);

        caButton = findViewById(R.id.toShareActivity);
        caButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShare();
            }
        });
        goToHashtagSearchButton = findViewById(R.id.goToHashtagSearchButton);
        goToHashtagSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearch();
            }
        });

        facebookLoginButton.setPermissions(Arrays.asList(EMAIL));

        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        facebookLoginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "Success, token :"+ loginResult.toString());
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "Cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, "Error");
            }

        });

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.v("twlogin", "starting log in");
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                twitterSession = session;

            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(MainActivity.this,"Login failed!", Toast.LENGTH_LONG);
            }
        });

        accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

    }



    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            super.onActivityResult(requestCode, resultCode, data);
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }


        AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken == null)
            {
                Toast.makeText(MainActivity.this,"Logged out of Facebook",Toast.LENGTH_LONG).show();
            }
            else
            {
                loadUserProfile(currentAccessToken);
            }

        }
        };

    private void loadUserProfile(AccessToken newAccessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");

                    Toast.makeText(MainActivity.this,"Logged in!", Toast.LENGTH_LONG).show();

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }
    private void goToShare() {
        Intent i = new Intent(this, ShareActivity.class);
        startActivity(i);

    }
    private void goToSearch() {
        Intent i = new Intent(this, HashtagSearchActivity.class);
        startActivity(i);

    }

   /* @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("access_token", accessToken);
    }
*/








}