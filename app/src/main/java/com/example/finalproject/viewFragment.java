package com.example.finalproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link viewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class viewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView selectedDateTextView,selectedDateTextView2;
    private String mParam1;
    private String mParam2;
    String selectedDate, selectedDate2;
    TextView in,out;

    public viewFragment() {
        // Required empty public constructor
    }
    public static viewFragment newInstance(String param1, String param2) {
        viewFragment fragment = new viewFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_view, container, false);

        in = rootView.findViewById(R.id.textView6);
        out = rootView.findViewById(R.id.textView8);
        selectedDateTextView = rootView.findViewById(R.id.selectedDateTextView3);
        selectedDateTextView2 = rootView.findViewById(R.id.selectedDateTextView5);

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
                            calculateTotal(rootView, selectedDate, selectedDate2);
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
                            calculateTotal(rootView, selectedDate, selectedDate2);
                        },
                        year, month, day);

                datePickerDialog.show();

            }
        });
        today();
        calculateTotal(rootView, selectedDate, selectedDate2);
        return rootView;
    }
    private void calculateTotal(View rootView, String str1, String str2) {
        String query = "SELECT SUM(money) AS totalAmount FROM dataTable WHERE date BETWEEN ? AND ? AND inOut = '收入' ";
        int totalAmount=0;
        try {
            SQLiteDatabase db = requireContext().openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery(query, new String[]{str2,str1});
            Log.d("Amount", "Total amount for specified criteria: " + cursor.getCount());
            if (cursor != null && cursor.moveToFirst() && cursor.getColumnIndex("totalAmount")!=-1) {
                totalAmount = cursor.getInt(cursor.getColumnIndex("totalAmount"));
                in.setText(String.valueOf(totalAmount));
                Log.d("TotalAmount", "Total amount for specified criteria: " + totalAmount);
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String query2 = "SELECT SUM(money) AS totalAmount FROM dataTable WHERE date BETWEEN ? AND ? AND inOut = '支出' ";
        int totalAmount2=0;
        try {
            SQLiteDatabase db = requireContext().openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
            Cursor cursor2 = db.rawQuery(query2, new String[]{str2,str1});
            Log.d("Amount", "Total amount for specified criteria: " + cursor2.getCount());
            if (cursor2 != null && cursor2.moveToFirst() && cursor2.getColumnIndex("totalAmount")!=-1) {
                totalAmount2 = cursor2.getInt(cursor2.getColumnIndex("totalAmount"));
                out.setText(String.valueOf(totalAmount2));
                Log.d("TotalAmount", "Total amount for specified criteria: " + totalAmount2);
            }

            if (cursor2 != null) {
                cursor2.close();
            }
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
        selectedDate=todayDate;
        selectedDate2=todayDate;
        selectedDateTextView.setText(todayDate);
        selectedDateTextView2.setText(todayDate);
    }
}