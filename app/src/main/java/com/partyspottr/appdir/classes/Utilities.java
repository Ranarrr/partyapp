package com.partyspottr.appdir.classes;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.R;
import com.partyspottr.appdir.classes.adapters.ChatPreviewAdapter;
import com.partyspottr.appdir.classes.adapters.EventAdapter;
import com.partyspottr.appdir.classes.adapters.FriendListAdapter;
import com.partyspottr.appdir.classes.networking.UpdateUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ranarrr on 02-Feb-18.
 *
 * @author Ranarrr
 */

public class Utilities {
    public static final int SEND_SMS_REQUEST_CODE = 1002;
    public static final int LOCATION_REQUEST_CODE = 1000;
    public static final int READ_EXTERNAL_STORAGE_CODE = 1001;
    public static final int SELECT_IMAGE_CODE = 1003;
    public static final int SELECT_PROFILE_IMAGE_CODE = 1004;

    public static boolean hasNetwork(Context c) {
        if(Settings.Global.getInt(c.getContentResolver(), "airplane_mode_on", 0) != 0)
            return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
            return false;

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public static GregorianCalendar getDateFromString(String str, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());

        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        try {
            gregorianCalendar.setTime(simpleDateFormat.parse(str));
            return gregorianCalendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void setUnChecked(ToggleButton... views) {
        for (ToggleButton v : views) {
            v.setChecked(false);
        }
    }

    public static Location getLatLng(Activity activity) {
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "Please allow Partyspottr to find your location.", Toast.LENGTH_LONG).show(); // TODO : Translation
            System.exit(0);
        }

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            Criteria criteria = new Criteria();
            String bestprovider = locationManager.getBestProvider(criteria, false);

            Location location = locationManager.getLastKnownLocation(bestprovider);

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if(location == null) {
                Toast.makeText(activity, "Please try again later..", Toast.LENGTH_LONG).show(); // TODO : Translation
                return null;
            } else
                return location;
        } else {
            Toast.makeText(activity, "Please try again later..", Toast.LENGTH_LONG).show(); // TODO : Translation
            return null;
        }
    }

    /**
     * This method is used for gettting the latest position of the user currently logged in.
     *
     * @param activity Activity to be used for checking permissions and getting location service5 see Context.LOCATION_SERVICE.
     */
    public static void getPosition(Activity activity) {
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "Please allow Partyspottr to find your location.", Toast.LENGTH_LONG).show(); // TODO : Translation
            System.exit(0);
        }

        Geocoder geocoder = new Geocoder(activity, Locale.ENGLISH);
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            Criteria criteria = new Criteria();
            String bestprovider = locationManager.getBestProvider(criteria, false);

            Location location = locationManager.getLastKnownLocation(bestprovider);

            if(location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if(location != null) {
                try {
                    for(Address address : geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1)) {
                        if(address != null) {
                            Bruker.get().setCountry(address.getCountryName());
                            Bruker.get().setTown(address.getLocality());
                            Bruker.get().LagreBruker();
                            UpdateUser updateUser = new UpdateUser();
                            updateUser.execute();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void onSearchEventsClickAlle(final Activity activity) {
        final ListView listView = activity.findViewById(R.id.lvalle_eventer);
        ImageButton searchevents = activity.findViewById(R.id.search_events);
        final EditText search_alle_eventer = activity.findViewById(R.id.search_alle_eventer);

        if(listView == null || searchevents == null || search_alle_eventer == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSearchEventsClickAlle(activity);
                }
            }, 300);
            return;
        }

        searchevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bruker.get().getListOfEvents() != null && !Bruker.get().getListOfEvents().isEmpty()) {
                    search_alle_eventer.setVisibility(search_alle_eventer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                    if(search_alle_eventer.getVisibility() == View.VISIBLE) {
                        search_alle_eventer.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(s.toString().isEmpty()) {
                                    listView.setAdapter(new EventAdapter(activity, Bruker.get().getListOfEvents()));
                                    return;
                                }

                                if(s.toString().length() <= 2) {
                                    listView.setAdapter(new EventAdapter(activity, Bruker.get().getListOfEvents()));
                                    return;
                                }

                                List<Event> list = new ArrayList<>();
                                for(Event event : Bruker.get().getListOfEvents()) {
                                    if(event.getHostStr().toLowerCase().contains(s.toString().toLowerCase())) {
                                        list.add(event);
                                        continue;
                                    }

                                    if(event.getNameofevent().toLowerCase().contains(s.toString().toLowerCase()))
                                        list.add(event);
                                }

                                listView.setAdapter(new EventAdapter(activity, list));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {}
                        });
                    }
                } else {
                    Toast.makeText(activity, "The list is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void onSearchMineEventer(final Activity activity) {
        final ListView lvmine_eventer = activity.findViewById(R.id.lvmine_eventer);
        final EditText search_mine_eventer = activity.findViewById(R.id.search_mine_eventer);
        final ImageButton search_events = activity.findViewById(R.id.search_events);

        if(lvmine_eventer == null || search_events == null || search_mine_eventer == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSearchMineEventer(activity);
                }
            }, 300);
            return;
        }

        search_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bruker.get().getListOfMyEvents() != null && !Bruker.get().getListOfMyEvents().isEmpty()) {
                    search_mine_eventer.setVisibility(search_mine_eventer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                    if(search_mine_eventer.getVisibility() == View.VISIBLE) {
                        search_mine_eventer.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(s.toString().isEmpty()) {
                                    lvmine_eventer.setAdapter(new EventAdapter(activity, Bruker.get().getListOfMyEvents()));
                                    return;
                                }

                                if(s.toString().length() <= 2) {
                                    lvmine_eventer.setAdapter(new EventAdapter(activity, Bruker.get().getListOfMyEvents()));
                                    return;
                                }

                                List<Event> list = new ArrayList<>();
                                for(Event event : Bruker.get().getListOfMyEvents()) {
                                    if(event.getHostStr().toLowerCase().contains(s.toString().toLowerCase())) {
                                        list.add(event);
                                        continue;
                                    }

                                    if(event.getNameofevent().toLowerCase().contains(s.toString().toLowerCase()))
                                        list.add(event);
                                }

                                lvmine_eventer.setAdapter(new EventAdapter(activity, list));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {}
                        });
                    }
                } else {
                    Toast.makeText(activity, "You don't have any events!", Toast.LENGTH_SHORT).show(); // TODO: fix translation
                }
            }
        });
    }

    public static void onSearchMineChats(final Activity activity) {
        final ListView lvmine_chats = activity.findViewById(R.id.lv_chat);
        final EditText search_chats = activity.findViewById(R.id.search_chat);
        final ImageButton search_events = activity.findViewById(R.id.search_events);

        if(lvmine_chats == null || search_events == null || search_chats == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSearchMineChats(activity);
                }
            }, 300);
            return;
        }

        search_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bruker.get().getChatMessageList() != null && !Bruker.get().getChatMessageList().isEmpty()) {
                    search_chats.setVisibility(search_chats.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                    if(search_chats.getVisibility() == View.VISIBLE) {
                        search_chats.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(s.toString().isEmpty()) {
                                    lvmine_chats.setAdapter(new ChatPreviewAdapter(activity, Bruker.get().getChatMessageList()));
                                    return;
                                }

                                if(s.toString().length() <= 2) {
                                    lvmine_chats.setAdapter(new ChatPreviewAdapter(activity, Bruker.get().getChatMessageList()));
                                    return;
                                }

                                List<ChatPreview> list = new ArrayList<>();
                                for(ChatPreview preview : Bruker.get().getChatMessageList()) {
                                    if(preview.isGroupchat() && !preview.getGroupname().isEmpty() && preview.getGroupname().toLowerCase().contains(s.toString().toLowerCase())) {
                                        list.add(preview);
                                        continue;
                                    }

                                    for(Chatter chatter : preview.getChatters()) {
                                        if(chatter.getBrukernavn().equals(Bruker.get().getBrukernavn()))
                                            continue;

                                        if(chatter.getBrukernavn().toLowerCase().contains(s.toString().toLowerCase())) {
                                            list.add(preview);
                                            continue;
                                        }

                                        if(chatter.getEtternavn().toLowerCase().contains(s.toString().toLowerCase())) {
                                            list.add(preview);
                                            continue;
                                        }

                                        if(chatter.getFornavn().toLowerCase().contains(s.toString().toLowerCase()))
                                            list.add(preview);
                                    }
                                }

                                lvmine_chats.setAdapter(new ChatPreviewAdapter(activity, list));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {}
                        });
                    }
                } else {
                    Toast.makeText(activity, "The list is empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void onSearchVenner(final Activity activity) {
        final ListView lv_venner = activity.findViewById(R.id.lv_venner);
        final EditText search_venner = activity.findViewById(R.id.search_venner);
        final ImageButton search_events = activity.findViewById(R.id.search_events);

        if(lv_venner == null || search_events == null || search_venner == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSearchVenner(activity);
                }
            }, 300);
            return;
        }

        search_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bruker.get().getFriendList() != null && !Bruker.get().getFriendList().isEmpty()) {
                    search_venner.setVisibility(search_venner.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                    if(search_venner.getVisibility() == View.VISIBLE) {
                        search_venner.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if(s.toString().isEmpty()) {
                                    lv_venner.setAdapter(new FriendListAdapter(activity, Bruker.get().getFriendList()));
                                    return;
                                }

                                if(s.toString().length() <= 2)
                                    return;

                                List<Friend> list = new ArrayList<>();
                                for(Friend friend : Bruker.get().getFriendList()) {
                                    if(friend.getBrukernavn().toLowerCase().contains(s.toString().toLowerCase())) {
                                        list.add(friend);
                                        continue;
                                    }

                                    if(friend.getFornavn().toLowerCase().contains(s.toString().toLowerCase())) {
                                        list.add(friend);
                                        continue;
                                    }

                                    if(friend.getEtternavn().toLowerCase().contains(s.toString().toLowerCase()))
                                        list.add(friend);
                                }

                                lv_venner.setAdapter(new FriendListAdapter(activity, list));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {}
                        });
                    }
                } else {
                    Toast.makeText(activity, "The list is empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This is used to calculate the age of a user.
     *
     * @param DOB Date-Of-Birth calendar to be used in calculation.
     * @return Returns age.
     */
    public static int calcAge(Calendar DOB) {
        int result;

        Calendar now = Calendar.getInstance();

        int year1 = now.get(Calendar.YEAR);
        int year2 = DOB.get(Calendar.YEAR);
        result = year1 - year2;
        int month1 = now.get(Calendar.MONTH) + 1;
        int month2 = DOB.get(Calendar.MONTH);
        if (month2 > month1) {
            result--;
        } else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = DOB.get(Calendar.DAY_OF_MONTH);
            if (day2 > day1) {
                result--;
            }
        }

        return result;
    }

    public static int getDateStrChat(GregorianCalendar calendar) {
        GregorianCalendar today = new GregorianCalendar();
        today.setTime(new Date(System.currentTimeMillis()));

        if(calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH) && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            return 1;
            //return "Today";
        }

        if((today.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH)) == 1 && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            return 2;
            //return "Yesterday";
        }

        if((today.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH)) < 7 && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            return 3;
            //return String.format(Locale.ENGLISH, "%s", calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
        }

        if(calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            return 4;
            //return String.format(Locale.ENGLISH, "%d %s", calendar.get(Calendar.DAY_OF_MONTH), calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }

        if(calendar.get(Calendar.MONTH) != today.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            return 4;
            //return String.format(Locale.ENGLISH, "%d %s", calendar.get(Calendar.DAY_OF_MONTH), calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }

        if(calendar.get(Calendar.YEAR) != today.get(Calendar.YEAR)) {
            return 5;
            //return String.format(Locale.ENGLISH, "%d %s %d", calendar.get(Calendar.DAY_OF_MONTH), calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()), calendar.get(Calendar.YEAR));
        }

        return 0;
    }

    /**
     * This is used to prevent viewing an unloaded layout
     *
     * @param views the views passed to make visible
     */
    public static void makeVisible(View ... views) {
        if(views == null || views.length == 0) {
            return;
        }

        for(View view : views) {
            if(view == null)
                continue;

            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param name The provided name to be used as a key in the NameValuePair.
     * @param strings Args to be used in the GET request, i == key, i + 1 == value.
     * @return Returns full pair string to be used in a GET request.
     */
    public static String getGETMethodArgStr(String name, String ... strings) {
        JSONObject json = new JSONObject();

        for(int i = 0; i < strings.length; i += 2) {
            String strName = strings[i];
            String strValue = strings[i+1];

            try {
                json.put(strName, strValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return BuildConfig.DBMS_URL + "?" + name + "=" + json.toString();
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /*private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(byte[] bmp, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bmp, 0, bmp.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bmp, 0, bmp.length, options);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }*/
}