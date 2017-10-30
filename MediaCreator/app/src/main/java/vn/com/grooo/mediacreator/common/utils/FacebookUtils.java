package vn.com.grooo.mediacreator.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

//import com.facebook.AccessToken;
//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.HttpMethod;
//import com.facebook.appevents.AppEventsLogger;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.share.model.ShareLinkContent;
//import com.facebook.share.model.SharePhoto;
//import com.facebook.share.model.SharePhotoContent;
//import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by NgocThai on 13/05/2017.
 */

public class FacebookUtils {

//    private static final String TAG = FacebookUtils.class.getSimpleName();
//
//    private CallbackManager mCallbackManager;
//    private ResultLogin resultLogin;
//
//    public void createLoginFacebook(Context context) {
//        FacebookSdk.sdkInitialize(context.getApplicationContext());
//        AppEventsLogger.activateApp(context);
//        mCallbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                resultLogin.updateResult(loginResult);
//            }
//
//            @Override
//            public void onCancel() {
//                resultLogin.loginError("Cancel");
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                Log.i(TAG, "onError: error " + exception.toString());
//            }
//        });
//    }
//
//    public void setActivityResult(int requestCode, int resultCode, Intent data) {
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//
//    public boolean isLogin() {
//        final boolean[] result = new boolean[1];
//        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//                if (currentAccessToken != null) {
//                    result[0] = true;
//                } else {
//                    result[0] = false;
//                }
//            }
//        };
//        return result[0];
//    }
//
//    public void login(Activity context) {
//        LoginManager.getInstance().logInWithReadPermissions(context, Arrays.asList("public_profile", "user_friends", "email"));
//    }
//
//    public void logout() {
//        if (AccessToken.getCurrentAccessToken() == null) {
//            return; // already logged out
//        }
//        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
//                .Callback() {
//            @Override
//            public void onCompleted(GraphResponse graphResponse) {
//
//                LoginManager.getInstance().logOut();
//
//            }
//        }).executeAsync();
//    }
//
////    public void setTransferObjectResult(TransferObjectResult transferObjectResult) {
////        this.transferObjectResult = transferObjectResult;
////    }
//
//
////    public void getProfile(final LoginResult loginResult) {
////        final AccessToken accessToken = loginResult.getAccessToken();
////        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
////            @Override
////            public void onCompleted(JSONObject user, GraphResponse graphResponse) {
////                if (graphResponse.getError() != null) {
////                    // has error
////                } else {
//////                    LoginFacebookObject facebookObject = new LoginFacebookObject();
//////                    facebookObject.setId(user.optString("id"));
//////                    facebookObject.setName(user.optString("name"));
//////                    facebookObject.setAvatar_id(Utils.urlProfileImage(loginResult.getAccessToken().getUserId()));
//////                    facebookObject.setAccess_token(loginResult.getAccessToken().getToken());
//////                    facebookObject.setEmail(user.optString("email"));
//////                    transferObjectResult.updateDataLoginFacebook(facebookObject);
////                }
////            }
////        });
////        Bundle parameters = new Bundle();
////        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location, name");
////        request.setParameters(parameters);
////        request.executeAsync();
////    }
//
//
//    public void setResultLogin(ResultLogin resultLogin) {
//        this.resultLogin = resultLogin;
//    }
//
//    public interface ResultLogin {
//        void updateResult(LoginResult loginResult);
//
//        void loginError(String s);
//    }
}
