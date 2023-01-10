package com.android.jidian.repair.base;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiaoming
 * date: 2023/1/10 14:50
 * description:
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {

        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                Log.d("BaseActivity", "finishAll: " + activity.getClass().getSimpleName());
                activity.finish();
            }
        }
    }
}
