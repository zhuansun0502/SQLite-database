package com.example.myapplication;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.MyDatabase.MyDatabaseHelper;

import static com.example.myapplication.MyDatabase.MyDatabaseHelper.DATABASE_NAME;

public class MainActivity extends AppCompatActivity {

    Button addButton, searchButton, viewAll, updateButton, deleteButton;
    MyDatabaseHelper db;
    EditText addFName, addLName, addMarks;
    EditText searFName, searLName, searMarks;
    EditText id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Database");

        addButton = findViewById(R.id.addButton);
        searchButton = findViewById(R.id.searchButton);
        viewAll = findViewById(R.id.viewAll);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        db = new MyDatabaseHelper(this);

        addFName = findViewById(R.id.fName);
        addLName = findViewById(R.id.lName);
        addMarks = findViewById(R.id.marks);
        searFName = findViewById(R.id.sFName);
        searLName = findViewById(R.id.sLName);
        searMarks = findViewById(R.id.sMarks);
        id = findViewById(R.id.id);
        setAddButton();
        setSearchButton();
        setViewButton();
        setUpdateButton();
        setDeleteButton();
    }

    public void setAddButton(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.allData();
                String sFName, sLName, sMarks;
                if(addFName.getText().toString().equals("") || addLName.getText().toString().equals("") || addMarks.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill all the add info!", Toast.LENGTH_LONG).show();
                    clear();
                    return;
                }
                while(cursor.moveToNext()){
                    sFName = cursor.getString(cursor.getColumnIndex("FNAME"));
                    sLName = cursor.getString(cursor.getColumnIndex("LNAME"));
                    sMarks = cursor.getString(cursor.getColumnIndex("MARKS"));
                    if(sFName.equals(addFName.getText().toString()) && sLName.equals(addLName.getText().toString()) && sMarks.equals(addMarks.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Data already in the table!", Toast.LENGTH_LONG).show();
                        clear();
                        return;
                    }
                }

                boolean success = db.insertData(addFName.getText().toString(), addLName.getText().toString(), addMarks.getText().toString());
                if(success) {
                    clear();
                    Toast.makeText(getApplicationContext(), "Add successful!", Toast.LENGTH_LONG).show();
                }
                else {
                    clear();
                    Toast.makeText(getApplicationContext(), "Add failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setSearchButton(){
        searchButton.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.allData();
                int index;
                String sFName, sLName, sMarks, searchResult;
                while(cursor.moveToNext()) {
                    index = cursor.getInt(cursor.getColumnIndex("ID"));
                    sFName = cursor.getString(cursor.getColumnIndex("FNAME"));
                    sLName = cursor.getString(cursor.getColumnIndex("LNAME"));
                    sMarks = cursor.getString(cursor.getColumnIndex("MARKS"));
                    searchResult = index + "\n" + "First Name: " + sFName + "\n" + "Last Name: " + sLName + "\n" + "Phone: " + sMarks + "\n\n";
                    if(sFName.equals(searFName.getText().toString()) && searLName.getText().toString().equals("") && searMarks.getText().toString().equals("")) {
                        display("Search Result:", searchResult);
                        clear();
                        return;
                    }
                    else if(searFName.getText().toString().equals("") && sLName.equals(searLName.getText().toString()) && searMarks.getText().toString().equals("")){
                        display("Search Result:", searchResult);
                        clear();
                        return;
                    }
                    else if(searFName.getText().toString().equals("") && searLName.getText().toString().equals("") && sMarks.equals(searMarks.getText().toString())){
                        display("Search Result:", searchResult);
                        clear();
                        return;
                    }
                    else if(sFName.equals(searFName.getText().toString()) && sLName.equals(searLName.getText().toString()) && sMarks.equals(searMarks.getText().toString())){
                        display("Search Result:", searchResult);
                        clear();
                        return;
                    }
                    else if(searFName.getText().toString().equals("") && searLName.getText().toString().equals("") && searMarks.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter search info!", Toast.LENGTH_LONG).show();
                        clear();
                        return;
                    }
                }
                cursor.moveToFirst();
                clear();
                Toast.makeText(getApplicationContext(), "Not found!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setViewButton(){
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor allData = db.allData();
                if(allData.getCount() == 0){
                    clear();
                    Toast.makeText(getApplicationContext(), "No data in table!", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(allData.moveToNext()) {
                    buffer.append(allData.getInt(0) + "\n");
                    buffer.append("First Name: " + allData.getString(1) + "\n");
                    buffer.append("Last Name: " + allData.getString(2) + "\n");
                    buffer.append("Phone: " + allData.getString(3) + "\n\n");
                }
                display("My info", buffer.toString());
                clear();
            }
        });
    }

    public void setUpdateButton(){
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor allData = db.allData();
                if(id.getText().toString().equals("") || addFName.getText().toString().equals("") || addLName.getText().toString().equals("") || addMarks.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill all the update info!", Toast.LENGTH_LONG).show();
                    clear();
                    return;
                }
                if(Integer.parseInt(id.getText().toString()) <= 0 || Integer.parseInt(id.getText().toString()) > allData.getCount()){
                    Toast.makeText(getApplicationContext(), "ID does not exist!", Toast.LENGTH_LONG).show();
                    clear();
                    return;
                }
                boolean updated = db.updateData(id.getText().toString(), addFName.getText().toString(), addLName.getText().toString(), addMarks.getText().toString());
                if(updated) {
                    Toast.makeText(getApplicationContext(), "Data updated!", Toast.LENGTH_LONG).show();
                    clear();
                    return;
                }
                else{
                    Toast.makeText(getApplicationContext(), "Update failed!", Toast.LENGTH_LONG).show();
                    clear();
                    return;
                }
            }
        });
    }

    public void setDeleteButton(){
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor allData = db.allData();
                if(id.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please provide the delete ID!", Toast.LENGTH_LONG).show();
                    clear();
                    return;
                }
                int rowDeleted = db.deleteData(id.getText().toString());
                if(rowDeleted > 0){
                    Toast.makeText(getApplicationContext(), "Data deleted!", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    Toast.makeText(getApplicationContext(), "Delete failed!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    public void display(String title, String data){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(data);
        builder.show();
    }

    public void clear(){
        addFName.getText().clear();
        addLName.getText().clear();
        addMarks.getText().clear();
        searFName.getText().clear();
        searLName.getText().clear();
        searMarks.getText().clear();
        id.getText().clear();
    }
}
