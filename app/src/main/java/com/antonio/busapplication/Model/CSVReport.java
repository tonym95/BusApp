package com.antonio.busapplication.Model;

import android.content.Context;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 4/17/2017.
 */

public class CSVReport implements Report {
    @Override
    public void generate(Context context, List<Bus> buses) {
        FileWriter filewr = null;
        try {
            filewr = new FileWriter("data/data/com.antonio.busapplication/files/users.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }


        CSVWriter csvWriter = new CSVWriter(filewr);

        List<String[]> data = new ArrayList<String[]>();

        String[] columnNames = new String[3];

        columnNames[0] = "Bus name";
        columnNames[1] = "Bus type";
        columnNames[2] = "Route id";

        data.add(columnNames);

        csvWriter.writeNext(columnNames);

        data.clear();

        for(Bus b : buses) {
            data.add(busToStringArray(b));
        }

        csvWriter.writeAll(data);

        try {
            csvWriter.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    private String[] busToStringArray(Bus b) {
        String busName = b.getName();
        String busType = b.getType();
        String routeId = b.getRoute().getId().toString();

        String[] busString = new String[3];

        busString[0] = busName;
        busString[1] = busType;
        busString[2] = routeId;

        return busString;
    }
}
