package com.warungkopidigital.penjualkopi.stockhawkjenal.ui;
/**
 * Created by penjualkopi on 17/12/17.
 */
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.warungkopidigital.penjualkopi.stockhawkjenal.R;
import com.warungkopidigital.penjualkopi.stockhawkjenal.data.Contract;
import com.warungkopidigital.penjualkopi.stockhawkjenal.data.DbHelper;
import com.warungkopidigital.penjualkopi.stockhawkjenal.data.ModelData;
import com.warungkopidigital.penjualkopi.stockhawkjenal.data.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.warungkopidigital.penjualkopi.stockhawkjenal.data.Contract.Quote.COLUMN_SYMBOL;
import static com.warungkopidigital.penjualkopi.stockhawkjenal.data.Contract.Quote.makeUriForStock;

public class StockDetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, OnChartGestureListener,
        OnChartValueSelectedListener {


    List<ModelData> stockList = new ArrayList<>();
    public static final String SYMBOL = "symbol";
    private static final int STOCK_LOADER = 0;
    private JSONArray dataJsonArray;
    //    @BindView(R.id.recycler_stock_fluctuation)
//    RecyclerView recyclerStockFluctuation;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    @BindView(R.id.linechart)
    LineChart mChart;
//    private StockDetailsAdapter adapter;

    private String mCurrentStockSymbol;
    SQLiteDatabase mDatabase;
    ArrayList<Entry> yVals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stock_details);
        ButterKnife.bind(this);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);

        mCurrentStockSymbol = getIntent().getExtras().getString(SYMBOL);

        mDatabase = openOrCreateDatabase(DbHelper.NAME, MODE_PRIVATE, null);
//        toolbar.setTitle(mCurrentStockSymbol);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        initRecyclerView();
        showEmployeesFromDatabase(mCurrentStockSymbol);
        setData();

//        getSupportLoaderManager().initLoader(STOCK_LOADER, null, this);
    }

    private void showEmployeesFromDatabase(String simbol) {

        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorEmployees = mDatabase.rawQuery("SELECT * FROM quotes WHERE " + COLUMN_SYMBOL + "='" + simbol + "'", null);

        //if the cursor has some data
        if (cursorEmployees.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                stockList.add(new ModelData(
                        cursorEmployees.getInt(0),
                        cursorEmployees.getString(1),
                        cursorEmployees.getDouble(2),
                        cursorEmployees.getDouble(3),
                        cursorEmployees.getDouble(4),
                        cursorEmployees.getString(5)
                ));
            } while (cursorEmployees.moveToNext());
        }
        //closing the cursor
        cursorEmployees.close();

        Log.i("hasil sqlite", new Gson().toJson(stockList));
        if (stockList.size() > 0) {
            String[] h = stockList.get(0).getHistory().split("\n");
            int g=0;
            for (String st : h) {
                Log.i("hasil spli", st);
                String[] w = st.split(",");
                if (w.length > 0)
                    if (w[0] != null)
                        if (w[1] != null)
                            yVals.add(new Entry(g,Float.parseFloat(w[1].replace(" ",""))));
                            g++;
                            Log.i("hasil spli", w[0] + " dan " + w[1]);
            }
        }
    }

    private ArrayList<String> setXAxisValues() {
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("10");
        xVals.add("20");
        xVals.add("30");
        xVals.add("30.5");
        xVals.add("40");

        return xVals;
    }

    private ArrayList<Entry> setYAxisValues() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(60, 0));
        yVals.add(new Entry(48, 1));
        yVals.add(new Entry(70.5f, 2));
        yVals.add(new Entry(100, 3));
        yVals.add(new Entry(180.9f, 4));

        return yVals;
    }

    private void setData() {
        ArrayList<String> xVals = setXAxisValues();
//        yVals = setYAxisValues();

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "DataSet 1");
        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        // set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(dataSets);

        // set data
        mChart.setData(data);
        Uri urlsymbol = makeUriForStock(mCurrentStockSymbol);
        Log.d("uri", urlsymbol.toString());
        String test = Contract.Quote.getStockFromUri(urlsymbol);
        Log.d("detail", urlsymbol.toString());

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Contract.Quote.makeUriForStock(mCurrentStockSymbol),
                Contract.Quote.QUOTE_COLUMNS,
                null, null, Contract.Quote.COLUMN_SYMBOL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            cursor.moveToPosition(0);
            try {
                dataJsonArray = new JSONArray(cursor.getString(Contract.Quote.POSITION_HISTORY));
                Log.d("blabla", dataJsonArray.toString());
                int mDateCount = dataJsonArray.length();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        adapter.setCursor(null);
    }

    private void setDisplayModeMenuItemIcon(MenuItem item) {
        if (PrefUtils.getDisplayMode(this)
                .equals(getString(R.string.pref_display_mode_absolute_key))) {
            item.setIcon(R.drawable.ic_percentage);
        } else {
            item.setIcon(R.drawable.ic_dollar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_settings, menu);
        MenuItem item = menu.findItem(R.id.action_change_units);
        setDisplayModeMenuItemIcon(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_change_units:
                PrefUtils.toggleDisplayMode(this);
                setDisplayModeMenuItemIcon(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
