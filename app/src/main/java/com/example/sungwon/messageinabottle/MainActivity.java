package com.example.sungwon.messageinabottle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView mBottomNav;
    TextView mTestText;
    FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
    }

    private void setupView() {
        mBottomNav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        mTestText = (TextView)findViewById(R.id.testText);
        mContainer = (FrameLayout)findViewById(R.id.fragment_container);
        BottomNavigationViewHelper.disableShiftMode(mBottomNav);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_main_menu:
                        mBottomNav.setItemBackgroundResource(R.color.orange);
                        mTestText.setText("Main Menu Selected!!!");
                        break;
                    case R.id.action_memory:
                        mBottomNav.setItemBackgroundResource(R.color.gray);
                        mTestText.setText("Memory Selected!!");
                        break;
                    case R.id.action_send:
                        mBottomNav.setItemBackgroundResource(R.color.colorPrimary);
                        mTestText.setText("Send Selected!");
                        break;
                    case R.id.action_setting:
                        mBottomNav.setItemBackgroundResource(R.color.gray2);
                        mTestText.setText("Boring ol' Setting selected.");
                        break;
                }
                return true;
            }
        });
    }
}
