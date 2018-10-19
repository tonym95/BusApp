package com.antonio.busapplication.Model;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Antonio on 4/7/2017.
 */

@IgnoreExtraProperties
public class Bus extends Observable {

    private Long id;
    private String name;
    private String type;

    private LatLng coords;

    @Exclude
    private Route route;


    private final static String busesPath = "buses.xml";

    private final static String findBusStatement = "SELECT Bus_id, Bus_name, Route_id, Bus_type" +
            "FROM Buses" +
            "WHERE Bus_name = ?";

    public Bus(Long id, Route route, String name, String type) {
        this.id = id;
        this.route = route;
        this.name = name;
        this.type = type;
    }

    public Bus(String name, String type, Route route) {
        this.name = name;
        this.type = type;
        this.route = route;
    }

    public Bus(String name, String type, LatLng coords) {
        this.name = name;
        this.type = type;
        this.coords = coords;
    }

    public Bus(String name, LatLng coords) {
        this.name = name;
        this.coords = coords;
    }

    public Bus(Long id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;

    }

    public static void writeXml(Context context, List<Bus> buses) throws IOException {
        XmlSerializer serializer = Xml.newSerializer();

        FileOutputStream fileos = context.getApplicationContext().openFileOutput("buses_file.xml", Context.MODE_PRIVATE);

        StringWriter sw = new StringWriter();

        try {
            serializer.setOutput(sw);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "buses");
            for(Bus b : buses) {
                serializer.startTag("", "bus");
                serializer.startTag("", "bus_id");
                serializer.text(b.getId().toString());
                serializer.endTag("", "bus_id");
                serializer.startTag("", "bus_name");
                serializer.text(b.getName());
                serializer.endTag("", "bus_name");
                serializer.startTag("", "bus_type");
                serializer.text(b.getType());
                serializer.endTag("", "bus_type");
                serializer.startTag("", "route");
                serializer.startTag("", "id");
                serializer.text(b.getRoute().getId().toString());
                serializer.endTag("", "id");
                for (Station s : b.getRoute().getRouteStations()) {
                    serializer.startTag("", "station");
                    serializer.startTag("", "station_name");
                    serializer.text(s.getName());
                    serializer.endTag("", "station_name");
                    serializer.endTag("", "station");
                }
                serializer.endTag("", "route");
                serializer.endTag("", "bus");
            }
            serializer.startTag("", "buses");
            serializer.endDocument();
            String dataWrite = sw.toString();
            fileos.write(dataWrite.getBytes());
        } catch(Exception e) {}

        fileos.close();
    }

    public static List<Bus> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Bus> entries = new ArrayList<Bus>();

        parser.require(XmlPullParser.START_TAG, null, "buses");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("bus")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }


    public static Bus readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "bus");
        String busName = null;
        String busType = null;
        Route route = null;
        Long busId = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("bus_name")) {
                busName = readBusName(parser);
            } else if (name.equals("bus_type")) {
                busType = readBusType(parser);
            } else if (name.equals("route")) {
                route = readBusRoute(parser);
            } else if (name.equals("bus_id")) {
                busId = readBusID(parser);
            }
            else {
                skip(parser);
            }
        }

        return new Bus(busId, route, busName, busType);
    }

    public static String readBusName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "bus_name");
        String busName = readText(parser);
        Log.i("User:", busName);
        parser.require(XmlPullParser.END_TAG, null, "bus_name");
        return busName;
    }

    public static String readBusType(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "bus_type");
        String busType = readText(parser);
        Log.i("User:", busType);
        parser.require(XmlPullParser.END_TAG, null, "bus_type");
        return busType;
    }

    public static Long readBusID(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "bus_id");
        Long busId = Long.parseLong(readText(parser));
        //Log.i("User:", busId);
        parser.require(XmlPullParser.END_TAG, null, "bus_id");
        return busId;
    }

    public static Route readBusRoute(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<Station> entries = new ArrayList<Station>();
        Long routeId = null;
        parser.require(XmlPullParser.START_TAG, null, "route");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("station")) {
                entries.add(readStation(parser));
            } else if (name.equals("id")) {
                routeId = readRouteId(parser);
            } else {
                skip(parser);
            }
        }
        return new Route(routeId, entries);
    }

    private static Long readRouteId(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, null, "id");
        Long routeId = Long.parseLong(readText(parser));
        //Log.i("User:", busId);
        parser.require(XmlPullParser.END_TAG, null, "id");
        return routeId;
    }

    public static Station readStation(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "station");
        String stationName = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("station_name")) {
                stationName = readStationName(parser);
            } else {
                skip(parser);
            }
        }

        return new Station(stationName);
    }

    public static String readStationName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "station_name");
        String stationName = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "station_name");
        return stationName;
    }

    public static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    public static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


    public static List<Bus> readXmlFile(Context context, boolean fromAssets) throws XmlPullParserException, IOException {
        XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
        xppf.setNamespaceAware(true);
        XmlPullParser parser = xppf.newPullParser();


        //File myXML = new File(path);
        // FileInputStream fis = new FileInputStream();
        InputStream is = null;
        FileInputStream fileis = null;
        try {
            if(!fromAssets) {
                fileis = context.getApplicationContext().openFileInput("buses_file.xml");
                parser.setInput(new InputStreamReader(fileis));
            }
            else {
                AssetManager as = context.getAssets();
                is = as.open("buses.xml");
                parser.setInput(new InputStreamReader(is));
            }
            parser.nextTag();
            return readFeed(parser);

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(!fromAssets)
                    fileis.close();
                else
                is.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return readFeed(parser);
    }


    @Override
    public String toString() {
        return this.name + this.route;

    }


    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public LatLng getCoords() {
        return coords;
    }

    public void setCoords(LatLng coords) {
        this.coords = coords;
        setChanged();
    }
}
