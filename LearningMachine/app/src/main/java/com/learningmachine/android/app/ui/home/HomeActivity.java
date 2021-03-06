package com.learningmachine.android.app.ui.home;

import android.support.v4.app.Fragment;

import com.learningmachine.android.app.R;
import com.learningmachine.android.app.ui.LMSingleFragmentActivity;

public class HomeActivity extends LMSingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return HomeFragment.newInstance();
    }

    @Override
    public String getActionBarTitle() {
        return getString(R.string.home_issuers);
    }
}
