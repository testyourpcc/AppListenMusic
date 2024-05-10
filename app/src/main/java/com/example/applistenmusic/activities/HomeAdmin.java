package com.example.applistenmusic.activities;

import static android.graphics.Color.*;

import static com.example.applistenmusic.activities.Home.PREFS_NAME;
import static com.example.applistenmusic.activities.Home.VISIT_COUNT_KEY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applistenmusic.R;
import com.example.applistenmusic.adapters.MenuAdapter;
import com.example.applistenmusic.models.MenuItem;
import com.example.applistenmusic.models.Song;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeAdmin extends AppCompatActivity {
    private CombinedChart mChart;
    private BarChart barChart;
    private List<String> xLabels;
    private Button btnExport;

    ImageView HomeFeature, HomeFeatureClose, Home,imageViewFeature,Account;
    LinearLayout menu;
    List<MenuItem> menuItems;
    RecyclerView recyclerViewMenuBar;
    private BarData barData;

    public HomeAdmin() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        setcontrol();
        
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(1, "Users", "playlist"));
        menuItems.add(new MenuItem(2, "Artists", "artist"));
        menuItems.add(new MenuItem(3, "Songs", "song"));
        menuItems.add(new MenuItem(4, "Albums", "album"));
        menuItems.add(new MenuItem(5, "Genres", "downloaded"));
        MenuAdapter adapterMenuItem = new MenuAdapter(menuItems);
        LinearLayoutManager layoutManagerMenuItem = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewMenuBar.setLayoutManager(layoutManagerMenuItem);
        recyclerViewMenuBar.setAdapter(adapterMenuItem);

        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(HomeAdmin.this, AccountInfoAdmin.class);
                startActivity(playIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        HomeFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu.getVisibility() == View.VISIBLE) {
                    menu.setVisibility(View.INVISIBLE);
                    adapterMenuItem.setmData(new ArrayList<>());
                } else {
                    menu.setVisibility(View.VISIBLE);
                    adapterMenuItem.setmData(menuItems);
                }
            }
        });

        imageViewFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu.getVisibility() == View.VISIBLE) {
                    menu.setVisibility(View.INVISIBLE);
                    adapterMenuItem.setmData(new ArrayList<>());
                } else {
                    menu.setVisibility(View.VISIBLE);
                    adapterMenuItem.setmData(menuItems);
                }
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        HomeFeatureClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu.getVisibility() == View.VISIBLE) {
                    menu.setVisibility(View.INVISIBLE);
                    adapterMenuItem.setmData(new ArrayList<>());
                } else {
                    menu.setVisibility(View.VISIBLE);
                    adapterMenuItem.setmData(menuItems);
                }
            }
        });

        adapterMenuItem.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id, int songId) {
                switch (id) {
                    //user
                    case 1: {
                        Intent playIntent = new Intent(HomeAdmin.this, UserManager.class);
                        startActivity(playIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        break;
                    }
                    // Arist
                    case 2: {
                        Intent playIntent = new Intent(HomeAdmin.this, ArtistManager.class);
                        startActivity(playIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    }
                    // Song
                    case 3: {
                        Intent playIntent = new Intent(HomeAdmin.this, SongManagement.class);
                        startActivity(playIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        break;
                    }
                    // album
                    case 4: {
                        Intent playIntent = new Intent(HomeAdmin.this, AlbumManagement.class);
                        startActivity(playIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        break;
                    }
                    // genres
                    case 5: {
                    }
                }

            }

        });


        // Khởi tạo combineChart
        mChart = (CombinedChart) findViewById(R.id.combinedChart);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        btnExport = findViewById(R.id.buttonExport);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePDF();
            }
        });


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // Giá trị tối thiểu là 1
        leftAxis.setAxisMaximum(10f); // Giá trị tối đa là 10
        leftAxis.setTextColor(BLACK);

        // Khởi tạo BarChart
        barChart = findViewById(R.id.barChart);
        barChart.getDescription().setEnabled(false);
        barChart.setBackgroundColor(Color.WHITE);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);

        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setEnabled(false);


        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMinimum(0f);

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
        xAxis.setTextColor(BLACK);
        xAxis.setValueFormatter((value, axis) -> xLabels.get((int) value % xLabels.size()));

        Legend legend = mChart.getLegend();
        legend.setTextColor(BLACK);

        CombinedData data = new CombinedData();
        LineDataSet lineDataSet = (LineDataSet) dataChart();
        LineData lineData = new LineData(lineDataSet);
        data.setData(lineData);

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        mChart.setData(data);
        mChart.invalidate();

        // Khởi tạo trục X cho bar Chart
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setAxisMinimum(0f);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setEnabled(true);
        barChart.getXAxis().setTextColor(BLACK);
        barChart.getXAxis().setValueFormatter((value, axis) -> xLabels.get((int) value % xLabels.size()));
        barChart.getLegend().setTextColor(BLACK);

        barData = new BarData();
        barData.addDataSet(barDataChart());
        data.setData(barData);
        barChart.invalidate();

        float combinedMax = Math.max(data.getXMax(), barData.getXMax());
        xAxis.setAxisMaximum(combinedMax + 0.25f);


    }

    private void savePDF() {
        mChart.post(() -> {
            // Tạo tài liệu PDF
            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(mChart.getWidth(), mChart.getHeight(), 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            // Vẽ biểu đồ đầu tiên lên nửa trang đầu tiên của canvas
            mChart.draw(canvas);

            // Tạo một canvas mới để vẽ biểu đồ thứ hai lên nửa trang thứ hai của canvas
            Canvas secondCanvas = new Canvas();
            secondCanvas.translate(mChart.getWidth(), 0); // Di chuyển canvas đến nửa trang thứ hai
            barChart.draw(secondCanvas);

            // Kết thúc trang
            document.finishPage(page);

            // Lưu tài liệu PDF vào bộ nhớ
            savePDFToStorage(document);
        });
    }

    private void savePDFToStorage(PdfDocument document) {
        // Thư mục lưu trữ
        File directory = new File(Environment.getExternalStorageDirectory(), "Documents");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Tên tệp PDF và đường dẫn đầy đủ
        String fileName = "chart.pdf";
        File file = new File(directory, fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            document.writeTo(outputStream);
            document.close();
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "PDF saved successfully", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save PDF", Toast.LENGTH_SHORT).show();
        }
    }



    private LineDataSet dataChart() {
        // Lấy số lượt truy cập từ SharedPreferences và tạo dữ liệu cho biểu đồ
        List<Entry> lineEntries = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        for (int i = 0; i < 7; i++) {
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int visitCount = preferences.getInt(dayOfMonth + VISIT_COUNT_KEY, 0);

            lineEntries.add(new Entry(i, visitCount)); // Dữ liệu dạng line

            calendar.add(Calendar.DAY_OF_MONTH, -1); // Lùi về 1 ngày
        }

        // Tạo dataset cho line chart
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Login Count");
        lineDataSet.setColor(GREEN);
        lineDataSet.setCircleColor(GREEN);
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueTextColor(GREEN);

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSet;
    }

    private static BarDataSet barDataChart() {
        BarData d = new BarData();
        int[] data = new int[]{2, 3, 4, 6, 5, 2, 3};

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int index = 0; index < 7; index++) {
            entries.add(new BarEntry(index, data[index]));
        }

        BarDataSet set = new BarDataSet(entries, "SongPlay");
        set.setColor(GREEN);
        set.setColor(GREEN);
        set.setValueTextColor(GREEN);
        set.setValueTextSize(10f);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return set;
    }


    public void setcontrol() {
        recyclerViewMenuBar = findViewById(R.id.recyclerViewMenuBar);
        menu = findViewById(R.id.menu);
        Home = findViewById(R.id.imageViewHome);
        imageViewFeature = findViewById(R.id.imageViewFeature);
        Account = findViewById(R.id.imageViewAccount);
        HomeFeature = findViewById(R.id.imageViewFeatureOpen);
        HomeFeatureClose =  findViewById(R.id.imageViewMenuClose);

    }
}