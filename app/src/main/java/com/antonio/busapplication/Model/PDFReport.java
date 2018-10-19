package com.antonio.busapplication.Model;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.graphics.pdf.PdfDocument.Page;
import android.text.TextPaint;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Antonio on 4/17/2017.
 */

public class PDFReport implements Report {


    @Override
    public void generate(Context context, List<Bus> buses) {

        PdfDocument pdfReport = new PdfDocument();

        PageInfo pageInfo = new PageInfo.Builder(100, 100, 1).create();

        Page page = pdfReport.startPage(pageInfo);

        TextPaint textPaint = new TextPaint();
        OutputStream os = null;

        try {
            os = context.getApplicationContext().openFileOutput("buses.pdf", Context.MODE_PRIVATE);

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int y = 20;
        int pageNo = 2;
        boolean pageFinished = false;

        textPaint.setTextSize(12f);

        page.getCanvas().drawText("Bus name, Bus Type, Route id", 10, 10, textPaint);



        for(Bus b : buses) {
            pageFinished = false;
            page.getCanvas().drawText(b.getName() + ", " + b.getType() + ", " + b.getRoute().getId(), 10, y, textPaint);
            y += 10;
            if(y == 90) {
                pdfReport.finishPage(page);
                pageInfo = new PageInfo.Builder(100, 100, pageNo).create();
                page = pdfReport.startPage(pageInfo);
                y = 10;
                pageNo++;
                pageFinished = true;
            }
        }

        if(!pageFinished)
            pdfReport.finishPage(page);

        try {
            pdfReport.writeTo(os);
            pdfReport.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
