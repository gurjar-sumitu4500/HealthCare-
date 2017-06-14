package com.example.sumit.myapplication4;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class PreviousDataFragment extends Fragment {

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0 ,lastY=0;
    View view;
    TextView information;
    Calendar c = Calendar.getInstance();
    SimpleDateFormat dateandtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat date2 = new SimpleDateFormat("ddMMyyyy");
    SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
    String Dateandtime = dateandtime.format(c.getTime());
    String Date = date.format(c.getTime());
    String Date2 = date2.format(c.getTime());
    String Time = time.format(c.getTime());
    TextView Fl;








    public PreviousDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_representing_data, container, false);    // we get graph view instance
        information = (TextView) view.findViewById(R.id.information);
        Fl = (TextView) view.findViewById(R.id.file);
        GraphView graph = (GraphView) view.findViewById(R.id.graph23);
        // data
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        series.setThickness(5);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPointInterface) {
                Toast.makeText(getContext(), "On Data Point Clicked: " + dataPointInterface, Toast.LENGTH_LONG ).show();
            }
        });
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(2);

        graph.getGridLabelRenderer().setNumHorizontalLabels(5);
        graph.getGridLabelRenderer().setNumVerticalLabels(8);
        graph.getGridLabelRenderer().setTextSize(12f);
        graph.getGridLabelRenderer().reloadStyles();


        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(30);

        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(70);
        viewport.setMaxY(140);
        viewport.setScrollable(true);
        viewport.setScalable(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // we're going to simulate real time with thread that append data to the graph
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                for (int i = 0; i < 30; i++) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                addEntry();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }catch (NullPointerException e2){

                    }
                }
            }
        }).start();
    }

    // add random data to graph
    private void addEntry() throws IOException {
        // here, we choose to display max 10 points on the viewport and we scroll to end

        int X = lastX++;
        int Y = (int) (RANDOM.nextInt(10) + 90);
        int z = (int) RANDOM.nextInt(10) + 90;
        series.appendData(new DataPoint(X, Y), false, 30);
        information.setText("Pulse Rate = "+ Y + "\n" + "Oxygen Saturation = " + z +"%");
        writedata("\n At Time " + Time + " : Pulse Rate = "+ Y + "\n" + " Oxygen Saturation = " + z +"% \n");
        readData();


    }
    public void writedata(String s)
    {
        File file = new File(getContext().getFilesDir(), Date2+".txt");
        try {
            if(!file.exists()) {
                file.createNewFile();
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fw);
                bufferedWriter.write(s);
                bufferedWriter.close();
            }else{
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fw);
                bufferedWriter.write(s);
                bufferedWriter.close();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    public void readData(){
        File file = new File(getContext().getFilesDir(), Date2+".txt");
        try{
            InputStream fis=new FileInputStream(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
             String line = null;
            while((line=br.readLine())!=null){
                Fl.append(line);
            }

            br.close();
        }
        catch(Exception e){
            System.err.println("Error: Target File Cannot Be Read");
        }

    }
}