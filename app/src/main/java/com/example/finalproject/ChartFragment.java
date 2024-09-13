package com.example.finalproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;

public class ChartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView selectedDateTextView,selectedDateTextView2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String selectedDate, selectedDate2;

    public ChartFragment() {
        // Required empty public constructor
    }

    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);


        selectedDateTextView = rootView.findViewById(R.id.selectedDateTextView3);
        selectedDateTextView2 = rootView.findViewById(R.id.selectedDateTextView4);

        selectedDateTextView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Calendar calendar = Calendar.getInstance();
                                                        int year = calendar.get(Calendar.YEAR);
                                                        int month = calendar.get(Calendar.MONTH);
                                                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                                                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                                                requireContext(),
                                                                (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
                                                                    selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                                                                    selectedDateTextView.setText(selectedDate);
                                                                    pie(rootView,selectedDate,selectedDate2);
                                                                },
                                                                year, month, day);

                                                        datePickerDialog.show();

                                                    }
                                                });
        selectedDateTextView2.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         Calendar calendar = Calendar.getInstance();
                                                         int year = calendar.get(Calendar.YEAR);
                                                         int month = calendar.get(Calendar.MONTH);
                                                         int day = calendar.get(Calendar.DAY_OF_MONTH);

                                                         DatePickerDialog datePickerDialog = new DatePickerDialog(
                                                                 requireContext(),
                                                                 (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
                                                                     selectedDate2 = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                                                                     selectedDateTextView2.setText(selectedDate2);
                                                                     pie(rootView,selectedDate,selectedDate2);
                                                                 },
                                                                 year, month, day);

                                                         datePickerDialog.show();
                                                         pie(rootView,selectedDate,selectedDate2);
                                                     }
                                                 });
        today();
        pie(rootView,selectedDate,selectedDate2);

        return rootView;

    }
    private void today(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String todayDate = year + "-" + month + "-" + day;
        selectedDate=todayDate;
        selectedDate2=todayDate;
        selectedDateTextView.setText(todayDate);
        selectedDateTextView2.setText(todayDate);
    }
    private void pie(View rootView,String str1,String str2){
        PieChart pieChart = rootView.findViewById(R.id.pieChart);

        ArrayList<PieEntry> entries = new ArrayList<>();
        int intTmp;

        intTmp=calculateTotal(rootView, str1, str2, "支出", "飲食");
        if(intTmp!=0){
            entries.add(new PieEntry(intTmp,"飲食"));
        }
        intTmp=calculateTotal(rootView, str1, str2, "支出", "購物");
        if(intTmp!=0){
            entries.add(new PieEntry(intTmp,"購物"));
        }
        intTmp=calculateTotal(rootView, str1, str2, "支出", "投資");
        if(intTmp!=0){
            entries.add(new PieEntry(intTmp,"投資"));
        }
        intTmp=calculateTotal(rootView, str1, str2, "支出", "車費");
        if(intTmp!=0){
            entries.add(new PieEntry(intTmp,"車費"));
        }
        intTmp=calculateTotal(rootView, str1, str2, "支出", "工作");
        if(intTmp!=0){
            entries.add(new PieEntry(intTmp,"工作"));
        }
        intTmp=calculateTotal(rootView, str1, str2, "支出", "其他");
        if(intTmp!=0){
            entries.add(new PieEntry(intTmp,"其他"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "支出");

        dataSet.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,Color.BLACK,Color.GRAY);

        PieData data = new PieData(dataSet);

        data.setDrawValues(true);
        data.setValueTextSize(12f);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();

        PieChart pieChart2 = rootView.findViewById(R.id.secondPieChart);

        ArrayList<PieEntry> entries2 = new ArrayList<>();
        intTmp=calculateTotal(rootView, str1, str2, "收入", "飲食");
        if(intTmp!=0){
            entries2.add(new PieEntry(intTmp,"飲食"));
        }
        intTmp=calculateTotal(rootView, str1, str2, "收入", "購物");
        if(intTmp!=0){
            entries2.add(new PieEntry(intTmp,"購物"));
        }
        intTmp=calculateTotal(rootView, str1, str2, "收入", "投資");
        if(intTmp!=0){
            entries2.add(new PieEntry(intTmp,"投資"));
        }
        intTmp=calculateTotal(rootView, str1, str2, "收入", "車費");
        if(intTmp!=0){
            entries2.add(new PieEntry(intTmp,"車費"));
        }
        intTmp=calculateTotal(rootView, str1, str2, "收入", "工作");
        if(intTmp!=0){
            entries2.add(new PieEntry(intTmp,"工作"));
        }
        intTmp=calculateTotal(rootView, str1, str2, "收入", "其他");
        if(intTmp!=0){
            entries2.add(new PieEntry(intTmp,"其他"));
        }

        PieDataSet dataSet2 = new PieDataSet(entries2, "收入");

        dataSet2.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,Color.BLACK,Color.GRAY);

        PieData data2 = new PieData(dataSet2);

        data2.setDrawValues(true);
        data2.setValueTextSize(12f);
        pieChart2.setData(data2);
        pieChart2.getDescription().setEnabled(false);
        pieChart2.invalidate();
    }
    private int calculateTotal(View rootView, String str1, String str2, String str3, String str4) {
        String query = "SELECT SUM(money) AS totalAmount FROM dataTable WHERE date BETWEEN ? AND ? AND inOut = ? AND moneyType = ?";
        int totalAmount=0;
        try {
            SQLiteDatabase db = requireContext().openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery(query, new String[]{str2,str1,str3,str4});
            Log.d("Amount", "Total amount for specified criteria: " + cursor.getCount());
            if (cursor != null && cursor.moveToFirst() && cursor.getColumnIndex("totalAmount")!=-1) {
                totalAmount = cursor.getInt(cursor.getColumnIndex("totalAmount"));
                Log.d("TotalAmount", "Total amount for specified criteria: " + totalAmount);
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalAmount;
    }
}