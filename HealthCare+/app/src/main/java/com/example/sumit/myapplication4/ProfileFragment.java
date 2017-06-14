package com.example.sumit.myapplication4;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    View view;
    Button Submit , Show;
    TextView data, name;
    EditText FirstName, LastName, DateOfBirth, Email, PhoneNumber, Address ;
    Spinner Gender;
    String firstName, lastName, dateOfBirth, email, phoneNumber, address, gender;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        Submit = (Button) view.findViewById(R.id.submit);
        name = (TextView) view.findViewById(R.id.name);
        FirstName = (EditText) view.findViewById(R.id.first_name);
        LastName = (EditText) view.findViewById(R.id.last_name);
        DateOfBirth = (EditText) view.findViewById(R.id.Date_of_birth);
        Gender = (Spinner) view.findViewById(R.id.gender);
        Email = (EditText) view.findViewById(R.id.email_address);
        PhoneNumber = (EditText) view.findViewById(R.id.phone_number);
        Address = (EditText) view.findViewById(R.id.address);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName = FirstName.getText().toString();
                lastName = LastName.getText().toString();
                dateOfBirth = DateOfBirth.getText().toString();
                email = Email.getText().toString();
                phoneNumber = PhoneNumber.getText().toString();
                address = Address.getText().toString();
                gender = Gender.getSelectedItem().toString();
                name.setText(email);

                SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE );
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("firstname", firstName);
                editor.putString("lastname", lastName);
                editor.putString("dateofbirth", dateOfBirth);
                editor.putString("email", email);
                editor.putString("phonenumber", phoneNumber);
                editor.putString("address", address);
                editor.putString("gender", gender);

                editor.apply();
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();

            }
        });

        return view;
    }

}

