package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonImage();
        database();

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(myAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(getString(R.string.money_fragment));
                            break;
                        case 1:
                            tab.setText(getString(R.string.view_fragment));
                            break;
                        case 2:
                            tab.setText(getString(R.string.chart_fragment));
                            break;
                        case 3:
                            tab.setText(getString(R.string.more_fragment));
                            break;
                    }
                }).attach();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent it) {
        super.onActivityResult(requestCode, resultCode, it);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String str1 = it.getStringExtra("支出收入");
                int int1 = it.getIntExtra("總額",0);
                String str2 = it.getStringExtra("類別");
                String str3 = it.getStringExtra("備註");
                String str4 = it.getStringExtra("日期");

                String insertQuery = "INSERT INTO dataTable (inOut, money, moneyType, text, date) VALUES (?,?,?,?,?)";
                db.execSQL(insertQuery, new Object[]{str1,int1,str2,str3,str4});
            }
        }
    }
    private void buttonImage(){
        ImageButton button = findViewById(R.id.button);
        try {
            InputStream stream = getAssets().open("addImage.png");
            Bitmap bitmap = BitmapFactory.decodeStream(stream);

            int width = bitmap.getWidth() * 5;
            int height = bitmap.getHeight() * 5;
            Bitmap largerBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

            button.setImageBitmap(largerBitmap);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void database(){
        db = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
        String createTable = "CREATE TABLE IF NOT EXISTS dataTable(inOut VARCHAR(10),money INTEGER,moneyType VARCHAR(10),text VARCHAR(100),date DATE)";

        db.execSQL(createTable);
    }
    public void clickNew(View V){
        Intent it = new Intent(this, AddActivity.class);
        startActivityForResult(it,1);
    }
}

class MyAdapter extends FragmentStateAdapter {

    private static final int INT_PAGES = 4;

    public MyAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MoneyFragment();
            case 1:
                return new viewFragment();
            case 2:
                return new ChartFragment();
            case 3:
                return new moreFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return INT_PAGES;
    }
}