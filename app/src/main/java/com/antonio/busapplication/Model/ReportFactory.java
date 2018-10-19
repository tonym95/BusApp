package com.antonio.busapplication.Model;

import com.antonio.busapplication.Model.CSVReport;
import com.antonio.busapplication.Model.PDFReport;
import com.antonio.busapplication.Model.Report;

/**
 * Created by Antonio on 4/17/2017.
 */

public class ReportFactory {

    public Report getReport(String type) {
        if(type.equals("csv")) {
            return new CSVReport();
        } else if (type.equals("pdf")) {
            return new PDFReport();
        } else
            return null;
    }
}
