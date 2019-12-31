package ali.naseem.signup;

import android.app.Application;

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Utils.getInstance().initialize(getApplicationContext());
    }
}
