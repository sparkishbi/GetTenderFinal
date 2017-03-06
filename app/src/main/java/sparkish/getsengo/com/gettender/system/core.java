package sparkish.getsengo.com.gettender.system;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

import sparkish.getsengo.com.gettender.BuildConfig;

/**
 * Created by cyclone on 06/07/15.
 */

public class core {
    private SecureRandom random = new SecureRandom();
    private Context context;
    private Toast ts_temp;
    private String android_id;
    public static HttpURLConnection conn;

    public core(Context context) {
        this.context = context;
        android_id = Settings.Secure.getString(this.context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public String get_id_android() {
        return android_id;
    }

    public void clearAlert() {
        try {
            ts_temp.cancel();
        } catch (Exception e) {
        }
    }

    public void alert(String msg, Integer duration, View view) {
        /*clearAlert();
        ts_temp.makeText(this.context, msg, Toast.LENGTH_LONG).show();*/

        final Snackbar snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(100);  // show multiple line
        textView.setTextColor(Color.parseColor("#FEF59F"));
        textView.setVerticalScrollBarEnabled(true);
        textView.setTextSize(13);

        if (duration > 0) {
            snackbar.setDuration(duration);
        }

        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        snackbar.setActionTextColor(Color.WHITE);

        snackbar.show();
    }

    public void print_r(String[] data) {
        System.out.println(data.toString());

        for (int i = 0; i < data.length; i++) {
            if (isset_array(data, i)) {
                if (data[i] != null) {
                    //Log.w("key : " + i, data[i]);
                }
            }
        }
    }

    public void print_r(ArrayList data) {
        for (int i = 0; i < data.size(); i++) {
            //System.out.println(Arrays.deepToString(new Object[]{data.get(i)}));
            //Log.w("DBG", String.valueOf(data.get(i)));
        }
    }

    public void print_r(String[][] data) {
        System.out.println(data.toString());

        for (int i = 0; i < data.length; i++) {
            for (int ii = 0; ii < data[i].length; ii++) {
                //Log.w("key : " + ii, (data[i][ii] == null ? "null" : data[i][ii]));
            }
        }
    }

    public void print_r(String[][][] data) {
        System.out.println(data.toString());

        for (int i = 0; i < data.length; i++) {
            for (int ii = 0; ii < data[i].length; ii++) {
                for (int iii = 0; iii < data[ii].length; iii++) {
                    //Log.w("key : " + iii, data[i][ii][iii]);
                }
            }
        }

    }

    public void print_r(List<String> data) {
        System.out.println(data);
    }

    HashMap<String, String> myKeyValues = new HashMap<String, String>();
    Stack<String> key_path = new Stack<String>();

    public void print_r(JSONObject json) {
        Iterator<?> json_keys = json.keys();

        while (json_keys.hasNext()) {
            String json_key = (String) json_keys.next();

            try {
                key_path.push(json_key);
                print_r(json.getJSONObject(json_key));
            } catch (JSONException e) {
                // Build the path to the key
                String key = "";
                for (String sub_key : key_path) {
                    key += sub_key + ".";
                }
                key = key.substring(0, key.length() - 1);

                /*try {
                    Log.w("key : " + key, json.getString(json_key));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }*/
                key_path.pop();
                try {
                    myKeyValues.put(key, json.getString(json_key));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (key_path.size() > 0) {
            key_path.pop();
        }
    }

    public void print_r(JSONArray json) {
        for (int i = 0; i < json.length(); i++) {
            try {
                Log.w("key : " + i, json.getString(i));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void print_r(HashMap hasmap) {
        for (Object variableName : hasmap.keySet()) {
            try {
                if (hasmap.get(variableName) != null && !hasmap.get(variableName).equals("")) {
                    Log.w("key : " + variableName, (String) hasmap.get(variableName));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public String getRandom() {
        return new BigInteger(130, random).toString(32);
    }

    public String getId() {
        return UUID.randomUUID().toString();
    }

    public Float parseFloat(String param) {
        Float ret = 0F;
        String msg = "";

        try {
            ret = Float.parseFloat(param);
        } catch (Exception e) {
            //Log.w("is Not float", param);
        }

        return ret;
    }

    public Double parseDouble(String param) {
        Double ret = 0D;
        String msg = "";

        try {
            ret = Double.parseDouble(param);
        } catch (Exception e) {
            //Log.w("is Not double", param);
        }

        return ret;
    }

    public boolean isConnectionAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    /*&& netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()*/) {
                return true;
            }
        }

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        } else {
            // not connected to the internet
            return false;
        }

        return false;
    }

    public String getVersion() {
        PackageManager manager = this.context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    this.context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return info.versionName;
    }

    public String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    public String urlEncodeUTF8(Map<?,?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }

    public void ajax(String url, HashMap<String, String> data, final Object callBackClass, final Method callBackMethodSuccess, final Method callBackMethodError){

        data.put("ai", android_id);
        data.put("vr", String.valueOf(BuildConfig.VERSION_NAME));
        data.put("vc", String.valueOf(BuildConfig.VERSION_CODE));


        url += "?&"+this.urlEncodeUTF8(data);

        //Log.w("HASILNE",url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.w("HASILNE",response);

                        try {
                            callBackMethodSuccess.invoke(callBackClass, response);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Something went wrong!");
                error.printStackTrace();

                try {
                    callBackMethodError.invoke(callBackClass, error.toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

        Volley.newRequestQueue(this.context).getCache().clear();
        Volley.newRequestQueue(this.context).add(stringRequest);
    }

    public Integer getTimeout(){
        db Q = new db(context);

        Integer timeoutAjax = 0;
        try {
            timeoutAjax = Integer.valueOf(Q.getConfig("timeoutAjax"));
            if (timeoutAjax == null || timeoutAjax.equals("")) {
                timeoutAjax = 10;//12 detik defaultnya di bagi 2
            }
        } catch (Exception e) {
        }

        timeoutAjax = timeoutAjax * 1000 / 2;

        return timeoutAjax;
    }

    public JSONObject parseJSON(String json) {
        JSONObject response = new JSONObject();

        try {
            json = json.trim();
            response = new JSONObject(json);
        } catch (JSONException e) {
            //Log.w("error parseJSON", e);
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public boolean isset_array(String[] data, int index) {
        if(data!=null) {
            try {
                data[index] = data[index];
                return true;
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean isset_array(String[][] data, int index1, int index2) {
        if(data!=null) {
            try {
                data[index1][index2] = data[index1][index2];
                return true;
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }else{
            return false;
        }
    }

    public String convertdate(String date, String from, String to, Locale loc) {
        date = parsingDate(date);

        SimpleDateFormat sdf = new SimpleDateFormat(from, loc);

        Date datenew = null;
        try {
            datenew = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dt1 = new SimpleDateFormat(to, loc);

        return dt1.format(datenew);
    }

    public String parsingDate(String date) {
        return parsingDate(date, false);
    }

    public String parsingDate(String date, boolean reversible) {
        String[][] map = new String[24][48];

        map[0][0] = "jan";
        map[0][1] = "01";

        map[1][0] = "feb";
        map[1][1] = "02";

        map[2][0] = "mar";
        map[2][1] = "03";

        map[3][0] = "apr";
        map[3][1] = "04";

        map[4][0] = "may";
        map[4][1] = "05";

        map[5][0] = "jun";
        map[5][1] = "06";

        map[6][0] = "jul";
        map[6][1] = "07";

        map[7][0] = "aug";
        map[7][1] = "08";

        map[8][0] = "sep";
        map[8][1] = "09";

        map[9][0] = "oct";
        map[9][1] = "10";

        map[10][0] = "nov";
        map[10][1] = "11";

        map[11][0] = "dec";
        map[11][1] = "12";

        map[12][0] = "january";
        map[12][1] = "01";

        map[13][0] = "february";
        map[13][1] = "02";

        map[14][0] = "march";
        map[14][1] = "03";

        map[15][0] = "April";
        map[15][1] = "04";

        map[16][0] = "may";
        map[16][1] = "05";

        map[17][0] = "june";
        map[17][1] = "06";

        map[18][0] = "july";
        map[18][1] = "07";

        map[19][0] = "august";
        map[19][1] = "08";

        map[20][0] = "september";
        map[20][1] = "09";

        map[21][0] = "october";
        map[21][1] = "10";

        map[22][0] = "november";
        map[22][1] = "11";

        map[23][0] = "december";
        map[23][1] = "12";


        date = date.toLowerCase();

        for (Integer i = 0; i < map.length; i++) {
            if (reversible == true) {
                date = date.replace(map[i][1], map[i][0]);
            } else {
                date = date.replace(map[i][0], map[i][1]);
            }
        }

        return date;
    }

    public String getNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        return dateFormat.format(date).toString();
    }

    public String getDate(Long epoch_time){
        //System.currentTimeMillis();

        Date date = new Date(epoch_time);
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatted = format.format(date);

        return formatted;
    }

    public String[] deleteArray(String[] array, String value_deleted) {
        if (array.length > 0) {
            String ret[] = new String[array.length];

            Integer idx = 0;
            for (String val : array) {
                if (val != value_deleted) {
                    ret[idx] = val;
                    idx++;
                }
            }
            return ret;
        } else {
            return array;
        }
    }

    public String clearHTML(String res) {
        return res.replaceAll("\\<.*?\\>", "");
    }

    public Integer difDateDay(String date1, String date2) {
        Integer ret = 0;

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        Date newerDate = null;
        Date olderDate = null;
        try {
            newerDate = format.parse(date2);
            olderDate = format.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ret = (int) ((newerDate.getTime() - olderDate.getTime()) / (1000 * 60 * 60 * 24));

        return ret;
    }


    public String strJoin(String[] aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = aArr.length; i < il; i++) {
            if (aArr[i] != null && !aArr[i].equals("null")) {
                //if(!aArr[i].trim().equals("")) {
                if (i > 0) {
                    sbStr.append(sSep);
                }
                sbStr.append(aArr[i]);
                //}
            }
        }
        return sbStr.toString();
    }

    public String[] clearEmptyArray(String[] array) {
        ArrayList al = new ArrayList();
        Integer fill = 0;

        for (Integer i = 0; i < array.length; i++) {
            if (!array[i].equals("null")) {
                al.add(fill, array[i]);

                fill++;
            }
        }

        String[] ret = new String[fill + 1];
        for (Integer i = 0; i == fill; i++) {
            ret[i] = String.valueOf(al.get(i));
        }

        return ret;
    }

    public String StringToBase64(String str) {
        byte[] b64 = null;

        try {
            b64 = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(b64, Base64.DEFAULT);
    }

    public String Base64ToString(String str) {
        byte[] data = Base64.decode(str, Base64.DEFAULT);
        String ret = "";
        try {
            ret = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ret;
    }

}


