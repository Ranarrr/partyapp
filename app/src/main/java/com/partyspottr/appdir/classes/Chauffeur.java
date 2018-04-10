package com.partyspottr.appdir.classes;

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

    public Chauffeur() {

    }

    public Chauffeur(double rating, String brukernavn, int age, int capacity) {
        m_rating = rating;
        m_brukernavn = brukernavn;
        m_age = age;
        m_capacity = capacity;
    }

    public void addCar(Car car) {
        listOfCars.add(car);
        Bruker.get().LagreBruker();
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
