package com.jujinziben.duty;

import android.app.Application;

import com.jujinziben.duty.util.DisplayUtils;
import com.jujinziben.duty.util.PrefUtils;
import com.jujinziben.duty.util.Toastor;

/**
 * Created by liu on 2017/9/7.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PrefUtils.init(this);
        Toastor.init(this);
        DisplayUtils.init(this);
    }
}
