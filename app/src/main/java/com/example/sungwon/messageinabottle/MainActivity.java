package com.example.sungwon.messageinabottle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
    }

    private void setupView() {
        mBottomNav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(mBottomNav);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_main_menu:
                        mBottomNav.setItemBackgroundResource(R.color.orange);
                        break;
                    case R.id.action_memory:
                        mBottomNav.setItemBackgroundResource(R.color.gray);
                        break;
                    case R.id.action_send:
                        mBottomNav.setItemBackgroundResource(R.color.colorPrimary);
                        break;
                    case R.id.action_setting:
                        mBottomNav.setItemBackgroundResource(R.color.gray2);
                        break;
                }
                return true;
            }
        });
    }
}
