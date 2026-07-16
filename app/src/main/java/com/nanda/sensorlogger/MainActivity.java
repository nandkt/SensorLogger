package com.nanda.sensorlogger;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private float filteredX = 0;
    private float filteredY = 0;
    private float filteredZ = 9.81f;

    private static final float ALPHA = 0.97f;

    TextView txtX, txtY, txtZ, txtStatus;
    Button btnSave,btnHistory,btnDelete;

    SensorManager sensorManager;
    Sensor accelerometer;

    float x, y, z;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtX = findViewById(R.id.txtX);
        txtY = findViewById(R.id.txtY);
        txtZ = findViewById(R.id.txtZ);
        txtStatus = findViewById(R.id.txtStatus);

        btnSave = findViewById(R.id.btnSave);
        btnHistory = findViewById(R.id.btnHistory);
        btnDelete=findViewById(R.id.btnDelete);

        databaseHelper = new DatabaseHelper(this);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if(sensorManager != null){
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        btnSave.setOnClickListener(v -> {

            String status;

            if(Math.abs(x) > 2 || Math.abs(y) > 2){
                status = "Bergerak";
            }else{
                status = "Stabil";
            }

            float saveX = Math.round(x * 1000f) / 1000f;
            float saveY = Math.round(y * 1000f) / 1000f;
            float saveZ = Math.round(z * 1000f) / 1000f;

            String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            String jam = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            boolean result = databaseHelper.insertData(
                    tanggal,
                    jam,
                    saveX,
                    saveY,
                    saveZ,
                    status
            );

            if(result){
                Toast.makeText(this,"Data berhasil disimpan",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Gagal menyimpan data",Toast.LENGTH_SHORT).show();
            }

        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Konfirmasi")
                    .setMessage("Apakah Anda yakin ingin menghapus semua data?")
                    .setPositiveButton("YA", (dialog, which) -> {

                        databaseHelper.deleteAllData();

                        Toast.makeText(MainActivity.this,
                                "Semua data berhasil dihapus",
                                Toast.LENGTH_SHORT).show();

                    })

                    .setNegativeButton("BATAL", null)

                    .show();

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(accelerometer != null){
            sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        filteredX = ALPHA * filteredX + (1 - ALPHA) * event.values[0];
        filteredY = ALPHA * filteredY + (1 - ALPHA) * event.values[1];
        filteredZ = ALPHA * filteredZ + (1 - ALPHA) * event.values[2];

        x = filteredX;
        y = filteredY;
        z = filteredZ;

        float displayX = x;
        float displayY = y;
        float displayZ = z;

        if (Math.abs(displayX) < 0.10f) displayX = 0;
        if (Math.abs(displayY) < 0.10f) displayY = 0;
        if (Math.abs(displayZ - 9.81f) < 0.10f) displayZ = 9.81f;

        txtX.setText(String.format("%.3f", x));
        txtY.setText(String.format("%.3f", y));
        txtZ.setText(String.format("%.3f", z));

        if(Math.abs(x)>2 || Math.abs(y)>2){
            txtStatus.setText("🔴 BERGERAK");
            txtStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }else{
            txtStatus.setText("🟢 STABIL");
            txtStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}