package com.antonio.busapplication.Model;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Xml;

import com.antonio.busapplication.R;
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
import java.util.Observer;

/**
 * Created by Antonio on 4/7/2017.
 */

@IgnoreExtraProperties
public class User implements Observer {

    private final static String usersPath = "users.xml";
    private static Boolean firstTimeWriting = true;

    private String email;
    private String password;
    private Boolean adminRights;

    private String busName;

    private Bus bus;
    private ArrayList<Bus> busesToShow;
    private Location location;


    public User() {}

    public User(String email) {
        this.email = email;
    }

    public User(String email, String password, Boolean adminRights) {
        this.email = email;
        this.password = password;
        this.adminRights = adminRights;
    }

    public User(Bus bus, ArrayList<Bus> buses) {
        this.bus = bus;
        busesToShow = new ArrayList<Bus>(buses);

    }

    public User(String busName, Location location) {

        this.location = new Location(location);
        this.busName = busName;
    }


    public ArrayList<Bus> searchBus(String name) {
        ArrayList<Bus> buses = new ArrayList<>();

        for(Bus b : busesToShow) {
            if(b.getName().equals(name))
                buses.add(b);
        }

        return buses;
    }

    public static void writeXml(Context context, List<User> users) throws IOException {
        XmlSerializer serializer = Xml.newSerializer();
        FileOutputStream fileos = context.getApplicationContext().openFileOutput("users_file.xml", Context.MODE_PRIVATE);

        StringWriter sw = new StringWriter();

        try {
            serializer.setOutput(sw);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "users");
            for(User u : users) {
                serializer.startTag("", "user");
                serializer.startTag("", "email");
                serializer.text(u.getEmail());
                serializer.endTag("", "email");
                serializer.startTag("", "password");
                serializer.text(u.getPassword());
                serializer.endTag("", "password");
                serializer.startTag("", "admin_rights");
                serializer.text(u.getAdminRights().toString());
                serializer.endTag("", "admin_rights");
                serializer.endTag("", "user");
            }
            serializer.endTag("", "users");
            serializer.endDocument();
            String dataWrite = sw.toString();
            fileos.write(dataWrite.getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        }

        fileos.close();
    }


    public static List<User> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList<User>();

        parser.require(XmlPullParser.START_TAG, null, "users");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("user")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }


    public static User readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "user");
        String username = null;
        String password = null;
        Boolean adminRights = false;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("email")) {
                username = readUsername(parser);
            } else if (name.equals("password")) {
                password = readPassword(parser);
            } else if (name.equals("admin_rights")) {
                adminRights = readAdminRights(parser);
            } else {
                skip(parser);
            }
        }

        return new User(username, password, adminRights);
    }

    private static String readUsername(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "email");
        String username = readText(parser);
        Log.i("User:", username);
        parser.require(XmlPullParser.END_TAG, null, "email");
        return username;
    }

    private static String readPassword(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "password");
        String password = readText(parser);
        Log.i("Password", password);
        parser.require(XmlPullParser.END_TAG, null, "password");
        return password;
    }
    
    private static Boolean readAdminRights(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "admin_rights");
        Boolean adminRights = Boolean.parseBoolean(readText(parser));
        parser.require(XmlPullParser.END_TAG, null, "admin_rights");
        return adminRights;
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


    public static List<User> readXmlFile(Context context, boolean fromAssets) throws XmlPullParserException, IOException {
        XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
        xppf.setNamespaceAware(true);
        XmlPullParser parser = xppf.newPullParser();


        //File myXML = new File(path);
        // FileInputStream fis = new FileInputStream();
        InputStream is = null;
        FileInputStream fileis = null;
        try {
            if(!fromAssets) {
                fileis = context.getApplicationContext().openFileInput("users_file.xml");
                parser.setInput(new InputStreamReader(fileis));
            } else {
                AssetManager as = context.getAssets();
                is = as.open("users.xml");
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!email.equals(user.email)) return false;
        if (!password.equals(user.password)) return false;
        if (!adminRights.equals(user.adminRights)) return false;
        if (bus != null ? !bus.equals(user.bus) : user.bus != null) return false;
        if (busesToShow != null ? !busesToShow.equals(user.busesToShow) : user.busesToShow != null)
            return false;
        return location != null ? location.equals(user.location) : user.location == null;

    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + adminRights.hashCode();
        return result;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdminRights() {
        return adminRights;
    }

    public void setAdminRights(Boolean adminRights) {
        this.adminRights = adminRights;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    @Override
    public void update(Observable observable, Object o) {
        Bus bus = (Bus) observable;

        Context context = (Context) o;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notifyId = 1;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                        .setContentTitle("Bus " + bus.getName())
                        .setContentText("Latitude: " + bus.getCoords().latitude + ", Longitude: " + bus.getCoords().longitude);


        notificationManager.notify(notifyId, mBuilder.build());

    }

/*    public void setUid(String uid) {
        this.uid = uid;
    }*/

}
