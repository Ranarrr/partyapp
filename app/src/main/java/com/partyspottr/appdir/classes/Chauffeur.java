package com.partyspottr.appdir.classes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Base64;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.partyspottr.appdir.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranarrr on 09-Apr-18.
 *
 * @author Ranarrr
 */

public class Chauffeur {
    private double m_rating;
    private String m_brukernavn;
    private int m_age;
    private int m_capacity;
    private long chauffeur_time_from;
    private long chauffeur_time_to;
    private List<Car> listOfCars;

    private static final String carlistElem = "carlistElem";
    private static final String time_fromElem = "timeDrivingFrom";
    private static final String time_toElem = "timeDrivingTo";
    private static final String rating = "rating";
    private static final String capacity = "capacity";
    private static final String age = "age";

    public static final Type listOfCarsType = new TypeToken<List<Car>>(){}.getType();

    private SharedPreferences m_sharedPreferences;

    public Chauffeur() {}

    public Chauffeur(SharedPreferences sharedPreferences) {
        m_sharedPreferences = sharedPreferences;
        HentChauffeur();
    }

    public Chauffeur(double rating, String brukernavn, int age, int capacity) {
        m_rating = rating;
        m_brukernavn = brukernavn;
        m_age = age;
        m_capacity = capacity;
    }

    public void addCar(Car car) {
        listOfCars.add(car);
        LagreChauffeur();
    }

    public String ChauffeurJSONString() {
        String ret = "";

        try {
            JSONObject json = new JSONObject();
            json.put("brukernavnElem", m_brukernavn);
            json.put(carlistElem, new Gson().toJson(listOfCars));
            json.put(time_fromElem, chauffeur_time_from);
            json.put(time_toElem, chauffeur_time_to);
            json.put(rating, m_rating);
            json.put(capacity, m_capacity);
            json.put(age, m_age);
            json.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            ret = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public void HentChauffeur() {
        chauffeur_time_from = m_sharedPreferences.getLong(time_fromElem, 0);
        chauffeur_time_to = m_sharedPreferences.getLong(time_toElem, 0);
        m_rating = m_sharedPreferences.getFloat(rating, 0.0f);
        m_capacity = m_sharedPreferences.getInt(capacity, 0);
        m_age = m_sharedPreferences.getInt(age, 0);
        m_brukernavn = Bruker.get().getBrukernavn();

        if(m_sharedPreferences.getString(Chauffeur.carlistElem, "").equals(""))
            listOfCars = new ArrayList<>();
        else
            listOfCars = new Gson().fromJson(m_sharedPreferences.getString(Chauffeur.carlistElem, ""), Chauffeur.listOfCarsType);

        Bruker.get().setHascar(listOfCars.size() > 0);

    }

    public void LagreChauffeur() {
        SharedPreferences.Editor editor = m_sharedPreferences.edit();
        editor.putLong(time_fromElem, chauffeur_time_from);
        editor.putLong(time_toElem, chauffeur_time_to);
        editor.putString(carlistElem, new Gson().toJson(listOfCars));
        editor.putFloat(rating, (float) m_rating);
        editor.putInt(age, m_age);
        editor.putInt(capacity, m_capacity);
        editor.apply();
    }

    public static void getChauffeur(Activity activity, final String brukernavn, final Chauffeur out) {
        StringRequest stringRequest = new StringRequest(Utilities.getGETMethodArgStr("get_chauffeur", "socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT),
                "username", brukernavn), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    if(json.getInt("success") == 1) {
                        out.setM_brukernavn(brukernavn);
                        Bruker.get().setHascar(true);
                        out.setChauffeur_time_from(Long.valueOf(json.getString("timeDrivingFrom")));
                        out.setChauffeur_time_to(Long.valueOf(json.getString("timeDrivingTo")));
                        List<Car> cars = new Gson().fromJson(json.getString("carlistElem"), listOfCarsType);
                        out.setListOfCars(cars);
                        Bruker.get().setCurrent_car(cars.get(0));
                        out.setM_age(Integer.valueOf(json.getString("age")));
                        out.setM_capacity(Integer.valueOf(json.getString("capacity")));
                        out.setM_rating(Double.valueOf(json.getString("rating")));
                    } else {
                        Bruker.get().setHascar(false);
                        Bruker.get().setCurrent_car(null);
                        Bruker.get().getChauffeur().setListOfCars(new ArrayList<Car>());
                        Bruker.get().getChauffeur().LagreChauffeur();
                        Bruker.get().LagreBruker();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(stringRequest);
    }

    public double getM_rating() {
        return m_rating;
    }

    public void setM_rating(double m_rating) {
        this.m_rating = m_rating;
    }

    public String getM_brukernavn() {
        return m_brukernavn;
    }

    public void setM_brukernavn(String m_brukernavn) {
        this.m_brukernavn = m_brukernavn;
    }

    public int getM_age() {
        return m_age;
    }

    public void setM_age(int m_age) {
        this.m_age = m_age;
    }

    public int getM_capacity() {
        return m_capacity;
    }

    public void setM_capacity(int m_capacity) {
        this.m_capacity = m_capacity;
    }

    public long getChauffeur_time_from() {
        return chauffeur_time_from;
    }

    public void setChauffeur_time_from(long chauffeur_time_from) {
        this.chauffeur_time_from = chauffeur_time_from;
    }

    public long getChauffeur_time_to() {
        return chauffeur_time_to;
    }

    public void setChauffeur_time_to(long chauffeur_time_to) {
        this.chauffeur_time_to = chauffeur_time_to;
    }

    public List<Car> getListOfCars() {
        return listOfCars;
    }

    public void setListOfCars(List<Car> listOfCars) {
        this.listOfCars = listOfCars;
    }
}
