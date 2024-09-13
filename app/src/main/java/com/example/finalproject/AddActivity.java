package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    Spinner spinner1,spinner2;
    EditText edt,edt2;
    TextView selectedDateTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        edt = (EditText) findViewById(R.id.numberInput);
        edt2 = (EditText) findViewById(R.id.textInput);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        selectedDateTextView = findViewById(R.id.selectedDateTextView);

        today();
    }
    private void today(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; //每次都忘了加1...
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String todayDate = year + "-" + month + "-" + day;
        selectedDateTextView.setText(todayDate);
    }
    public void showDatePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    selectedDateTextView.setText(selectedDate);
                },
                year, month, day);

        datePickerDialog.show();
    }
    public void onSave(View v){
        Intent it2 = new Intent();
        it2.putExtra("支出收入",spinner1.getSelectedItem().toString());
        it2.putExtra("總額",Integer.parseInt(String.valueOf(edt.getText())));
        it2.putExtra("類別",spinner2.getSelectedItem().toString());
        it2.putExtra("備註",edt2.getText().toString());
        it2.putExtra("日期", selectedDateTextView.getText().toString());

        setResult(RESULT_OK,it2);
        finish();
    }
    public void onCancel(View v){
        setResult(RESULT_CANCELED);
        finish();
    }
}