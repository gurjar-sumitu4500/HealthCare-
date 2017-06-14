package com.example.sumit.myapplication4;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckConnectionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    View view;
    Button bn, sharefile;
    BluetoothAdapter b_adapter;
    int BLUETOOTH_REQUEST = 1;
    Spinner spinner;
    TextView readfile;
    File file;
    Calendar c = Calendar.getInstance();
    SimpleDateFormat date2 = new SimpleDateFormat("ddMMyyyy");
    String Date2 = date2.format(c.getTime());









    public CheckConnectionFragment() {


        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_check_data, container, false);
        bn = (Button) view.findViewById(R.id.bluetooth_button);
        spinner = (Spinner) view.findViewById(R.id.files);
        spinner.setOnItemSelectedListener(this);
        getFilenames();
        readfile = (TextView) view.findViewById(R.id.file_Content);
        readfile.setMovementMethod(new ScrollingMovementMethod());
        sharefile = (Button) view.findViewById(R.id.sharefile);




        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_adapter = BluetoothAdapter.getDefaultAdapter();
                if (b_adapter == null) {

                    Toast.makeText(getContext(), "No Bluetooth Adapter Found!", Toast.LENGTH_LONG).show();

                } else {
                    if (!b_adapter.isEnabled()) {
                        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(i, BLUETOOTH_REQUEST);


                    }else if(b_adapter.isEnabled()){

                        b_adapter.disable();
                        Toast.makeText(getContext(), "BLUETOOTH is Successfully Turned OFF!", Toast.LENGTH_LONG).show();
                        bn.setText("Switch On Bluetooth");



                    }
                }
            }
        });
        sharefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharefilemethod();
            }
        });



        return view;
    }





    public void onActivityResult(int request_code, int result_code, Intent data){

        if(request_code==BLUETOOTH_REQUEST){

            if(result_code==RESULT_OK){
                Toast.makeText(getContext(), "BLUETOOTH SUCCESSFULLY TURNED ON!", Toast.LENGTH_LONG).show();
                bn.setText("Switch OFF Bluetooth!");

            }
            if(result_code==RESULT_CANCELED){
                Toast.makeText(getContext(), "BLUETOOTH TURN ON FAILED!", Toast.LENGTH_LONG).show();
            }
        }


    }



    @Override
    public void onClick(View v) {

    }

    public void getFilenames() {
        String[] filenames = getActivity().fileList();
        List<String> list = new ArrayList<String>();
        for(int i = 0; i<filenames.length; i++){
           //Log.d("Filename ", filenames[i]);
            list.add(filenames[i]);
        }
        ArrayAdapter<String> filenameAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, list);
        spinner.setPrompt("Select a file!");
        spinner.setAdapter(filenameAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getItemAtPosition(position).toString();
        Toast.makeText(getContext(), "Selected item is " + item, Toast.LENGTH_SHORT).show();
        readfile.setText(null);
        readfilecontent(item);
    }


    public void sharefilemethod(){
        File file = new File(getContext().getFilesDir(), Date2+".txt");
        Intent i = new Intent(Intent.ACTION_SEND);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.putExtra(Intent.EXTRA_SUBJECT, "Title");
        i.putExtra(Intent.EXTRA_TEXT, "Content");
        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        i.setType("text/plain");
        startActivity(Intent.createChooser(i, "Your email id"));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void readfilecontent(String filename){
        File file = new File(getContext().getFilesDir(), filename);
        try{
            InputStream fis=new FileInputStream(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while((line=br.readLine())!=null){
                readfile.append(line);
            }

            br.close();
        }
        catch(Exception e){
            System.err.println("Error: Target File Cannot Be Read");
        }

    }
}


