package vn.com.grooo.mediacreator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by trungcs on 10/20/17.
 */

public class TestFacebookSdk extends AppCompatActivity {
    private Button btnLogin;
    private Button btnShare;
    private Button btnLogout;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private boolean isRun = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        callbackManager = CallbackManager.Factory.create();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fb);
        FacebookSdk.isInitialized();
        btnLogin = (Button) findViewById(R.id.btnloginFB);
        btnShare = (Button) findViewById(R.id.btnShareFB);
        btnLogout = (Button) findViewById(R.id.btnLogout);

//        if (AccessToken.getCurrentAccessToken() != null) {
//            LoginManager.getInstance().logOut();
//        }



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LoginManager.getInstance().logOut();
//                Toast.makeText(TestFacebookSdk.this,"Logout ....",Toast.LENGTH_LONG).show();
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                        String email = object.getString("email");
                                    String birthday = object.getString("birthday");
                                    Log.d("INFORFB", email + " " + birthday);
                                } catch (JSONException e) {
                                    Log.d("INFORFB", "Error " + e.getMessage());
                                    e.printStackTrace();
                                }
                               // 01/31/1980 format
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }
        });


//        if (AccessToken.getCurrentAccessToken() != null){
//            Log.d("Successssss","Login with Previous Account !!!");
//            LoginManager.getInstance().logOut();
//        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }


//                LoginManager.getInstance().
//                        logInWithReadPermissions(TestFacebookSdk.this,
//                                Arrays.asList("public_profile", "user_friends","email"));
                LoginManager.getInstance().logInWithPublishPermissions(
                        TestFacebookSdk.this,
                        Arrays.asList("publish_actions"));

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() != null){
                    (new  MyAssynTask()).execute();
                } else {
                    LoginManager.getInstance().logInWithPublishPermissions(
                            TestFacebookSdk.this,
                            Arrays.asList("publish_actions"));
                }

//                Bitmap bmp  = BitmapFactory.decodeResource(getResources(),
//                        R.drawable.rainbow);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.PNG, 80, stream);
//                byte[] byteArray = stream.toByteArray();
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Successssss", "Login : " + loginResult.getAccessToken().getToken());
                        accessToken = loginResult.getAccessToken();
                        (new  MyAssynTask()).execute();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(TestFacebookSdk.this, "Login Cancel",
                                Toast.LENGTH_LONG).show();
                        Log.d("Successssss", "Cancle : " );
                        (new  MyAssynTask()).execute();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(TestFacebookSdk.this, exception.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.d("Successssss", "Errror " );
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private class MyAssynTask extends AsyncTask<Void ,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            if (accessToken == null){
                accessToken = AccessToken.getCurrentAccessToken();
            }
            Bundle postParams = new Bundle();
            Bitmap bmp  = BitmapFactory.decodeResource(getResources(),
                    R.drawable.rainbow);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 80, stream);
            byte[] byteArray = stream.toByteArray();
            postParams.putByteArray("picture",byteArray);
            postParams.putString("caption", "my picture");

            GraphRequest request = new GraphRequest(accessToken,
                    "me/photos",
                    postParams, HttpMethod.POST, null);

            GraphResponse response = request.executeAndWait();
            FacebookRequestError error = response.getError();
            if (error != null) {
                Log.d("FaceBook errrr", error.toString());
                return null;
            }
            return null;
        }
    }
}
