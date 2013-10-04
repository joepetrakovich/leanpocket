package com.appshroom.leanpocket.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.dto.Lane;
import com.appshroom.leanpocket.helpers.Consts;

import java.util.List;

public class LanePickerActivity extends Activity {

    private List<Lane> lanes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lane_picker);

        lanes = getIntent().getParcelableArrayListExtra(Consts.LANE_LIST_EXTRAS);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lane_picker, menu);
        return true;
    }

}
