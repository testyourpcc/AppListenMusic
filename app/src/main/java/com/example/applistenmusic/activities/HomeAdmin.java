package com.example.applistenmusic.activities;

import static android.graphics.Color.*;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applistenmusic.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeAdmin extends AppCompatActivity implements OnChartValueSelectedListener {
    private CombinedChart mChart;
    private BarChart barChart;
    private List<String> xLabels;

    public HomeAdmin() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        // Khởi tạo combineChart
        mChart = (CombinedChart) findViewById(R.id.combinedChart);
        mChart.getDescription().setEnabled(false);
        mChart.setOnChartValueSelectedListener(this);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // Giá trị tối thiểu là 1
        leftAxis.setAxisMaximum(10f); // Giá trị tối đa là 10
        leftAxis.setTextColor(WHITE);

        // Khởi tạo BarChart
        barChart = findViewById(R.id.barChart);
        barChart.getDescription().setEnabled(false);
        barChart.setOnChartValueSelectedListener(this);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisLeft().setAxisMaximum(10f);
        barChart.getAxisLeft().setTextColor(WHITE);

        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setEnabled(false);


        xLabels = new ArrayList<>();
        xLabels.add("Mon");
        xLabels.add("Tue");
        xLabels.add("Wed");
        xLabels.add("Thu");
        xLabels.add("Fri");
        xLabels.add("Sat");
        xLabels.add("Sun");

        // Khởi tạo trục X cho combine Chart
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(WHITE);
        xAxis.setValueFormatter((value, axis) -> xLabels.get((int) value % xLabels.size()));

        Legend legend = mChart.getLegend();
        legend.setTextColor(WHITE);

        // Khởi tạo trục X cho bar Chart
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setAxisMinimum(0f);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setEnabled(true);
        barChart.getXAxis().setTextColor(WHITE);
        barChart.getXAxis().setValueFormatter((value, axis) -> xLabels.get((int) value % xLabels.size()));

        barChart.getLegend().setTextColor(WHITE);

        // Khởi tạo Firebase Realtime Database
        DatabaseReference loginRef = FirebaseDatabase.getInstance().getReference("loginCounts");

        // Lắng nghe sự thay đổi của dữ liệu từ Firebase Realtime Database
        loginRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseData", "DataSnapshot: " + snapshot.getValue());
                updateChart(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to read value.", error.toException());
            }
        });
    }

    private void updateChart(DataSnapshot dataSnapshot) {
        LineData lineData = new LineData();
        ArrayList<Entry> entries = new ArrayList<>();

        // Lặp qua từng ngày trong tuần và kiểm tra nếu có dữ liệu trong Firebase thì thêm vào entries
        for (int i = 0; i < xLabels.size(); i++) {
            DataSnapshot daySnapshot = dataSnapshot.child(xLabels.get(i));
            long loginCount = daySnapshot.exists() ? daySnapshot.getChildrenCount() : 0;
            entries.add(new Entry(i, loginCount));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Login Count");
        lineDataSet.setColor(GREEN);
        lineDataSet.setCircleColor(GREEN);
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueTextColor(GREEN);

        lineData.addDataSet(lineDataSet);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);

        // Tạo dữ liệu cho BarChart từ dữ liệu của CombinedChart
        BarData barData = createBarData(dataSnapshot);

        // Cập nhật dữ liệu cho BarChart
        barChart.setData(barData);
        barChart.invalidate();


        mChart.setData(combinedData);
        mChart.getXAxis().setAxisMaximum(combinedData.getXMax() + 0.25f);
        mChart.invalidate();
    }

    private BarData createBarData(DataSnapshot dataSnapshot) {
        BarData barData = new BarData();

        // Tạo dữ liệu BarEntry từ dữ liệu của CombinedChart
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < xLabels.size(); i++) {
            DataSnapshot daySnapshot = dataSnapshot.child(xLabels.get(i));
            long loginCount = daySnapshot.exists() ? daySnapshot.getChildrenCount() : 0;
            barEntries.add(new BarEntry(i, loginCount));
        }

        // Tạo và cấu hình BarDataSet
        BarDataSet barDataSet = new BarDataSet(barEntries, "Login Count");
        barDataSet.setColor(GREEN);

        // Thêm BarDataSet vào BarData
        barData.addDataSet(barDataSet);

        return barData;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Toast.makeText(this, "Value: "
                + e.getY()
                + ", index: "
                + h.getX()
                + ", DataSet index: "
                + h.getDataSetIndex(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}