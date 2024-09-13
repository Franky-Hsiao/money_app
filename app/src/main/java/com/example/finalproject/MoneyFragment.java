package com.example.finalproject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class MoneyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView selectedDateTextView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SQLiteDatabase db;

    String selectedDate;
    public MoneyFragment() {
        // Required empty public constructor
    }

    public static MoneyFragment newInstance(String param1, String param2) {
        MoneyFragment fragment = new MoneyFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_money, container, false);

        selectedDateTextView = rootView.findViewById(R.id.selectedDateTextView2);
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

                            updateListViewWithData(selectedDate);
                        },
                        year, month, day);

                datePickerDialog.show();
            }
        });


        today();
        return rootView;
    }
    private void updateListViewWithData(String selectedDate) {
        try {
            db = requireContext().openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
            String query = "SELECT inOut, moneyType, text, money FROM dataTable WHERE date = ?";

            Cursor cursor = db.rawQuery(query, new String[]{selectedDate});
            ArrayList<String> dataList = new ArrayList<>();
            while (cursor.moveToNext()) {
                int inOutIndex = cursor.getColumnIndex("inOut");
                int moneyTypeIndex = cursor.getColumnIndex("moneyType");
                int textIndex = cursor.getColumnIndex("text");
                int moneyIndex = cursor.getColumnIndex("money");

                if (inOutIndex != -1 && moneyTypeIndex != -1 && textIndex != -1 && moneyIndex != -1) {
                    String inOut = cursor.getString(inOutIndex);
                    String moneyType = cursor.getString(moneyTypeIndex);
                    String text = cursor.getString(textIndex);
                    int money = cursor.getInt(moneyIndex);

                    String inOutString = getString(R.string.inout_Page) + inOut;
                    String moneyTypeString = getString(R.string.class_Page) + moneyType;
                    String moneyString = getString(R.string.money_Page) + money;
                    String textString = getString(R.string.text_Page) + text;

                    String dataString = inOutString + ", " + moneyTypeString + ", " + moneyString + "\n" + textString;

                    dataList.add(dataString);
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, dataList);

            ListView listView = requireView().findViewById(R.id.lv);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void today(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String todayDate = year + "-" + month + "-" + day;
        selectedDateTextView.setText(todayDate);
        updateListViewWithData(todayDate);
    }

}