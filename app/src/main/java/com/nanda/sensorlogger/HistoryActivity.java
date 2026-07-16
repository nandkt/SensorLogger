package com.nanda.sensorlogger;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    TextView txtHistory;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        txtHistory = findViewById(R.id.txtHistory);

        databaseHelper = new DatabaseHelper(this);

        tampilkanData();
    }

    private void tampilkanData() {

        Cursor cursor = databaseHelper.getAllData();

        if(cursor.getCount()==0){

            txtHistory.setText("Belum ada data.");

            return;

        }

        StringBuilder builder = new StringBuilder();

        while(cursor.moveToNext()){

            builder.append("ID : ").append(cursor.getInt(0)).append("\n");
            builder.append("Tanggal : ").append(cursor.getString(1)).append("\n");
            builder.append("Jam : ").append(cursor.getString(2)).append("\n\n");
            builder.append("X : ").append(cursor.getFloat(3)).append("\n");
            builder.append("Y : ").append(cursor.getFloat(4)).append("\n");
            builder.append("Z : ").append(cursor.getFloat(5)).append("\n");
            builder.append("Status : ").append(cursor.getString(6)).append("\n");
            builder.append("\n=============================\n\n");

        }

        txtHistory.setText(builder.toString());

    }

}