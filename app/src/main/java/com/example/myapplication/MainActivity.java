package com.example.myapplication;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.myapplication.activity.ImportDataActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

//    private TabLayout tabLayout;
    private ViewPager viewPager;
    MainFragment mainFragment;
    ContenFragment contenFragment;
    UserInfoFragment userInfoFragment;
    TextView appTitle;
    String tabTitle[] ={"书架", "个人中心"};
    Button importButton;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        
        
        
        
//        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        appTitle = findViewById(R.id.appTitle);
        importButton=findViewById(R.id.layoutmain_impotrBtn);
        mainFragment = new MainFragment();
        contenFragment = new ContenFragment();
        userInfoFragment = new UserInfoFragment();
        appTitle.setText(tabTitle[0]);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ImportDataActivity.class));
            }
        });
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return mainFragment;
                } else {
                    return userInfoFragment;
                }

            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitle[position];
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                appTitle.setText(tabTitle[position]);
                importButton.setVisibility(position==0?View.VISIBLE:View.GONE);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        tabLayout.setupWithViewPager(viewPager);

    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menu_bookself:
               viewPager.setCurrentItem(0);
                break;
            case R.id.menu_personal:
                viewPager.setCurrentItem(1);
                break;
        }
        return true;
    }
}
