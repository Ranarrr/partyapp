package com.partyspottr.appdir.classes;

public class Chauffeur {
    private String m_fornavn;
    private String m_etternavn;
    private double m_rating;
    private String m_brukernavn;
    private int m_age;
    private int m_capacity;

    public Chauffeur() {

    }

    public Chauffeur(String fornavn, String etternavn, double rating, String brukernavn, int age, int capacity) {
        m_fornavn = fornavn;
        m_etternavn = etternavn;
        m_rating = rating;
        m_brukernavn = brukernavn;
        m_age = age;
        m_capacity = capacity;
    }

    public String getM_fornavn() {
        return m_fornavn;
    }

    public void setM_fornavn(String m_fornavn) {
        this.m_fornavn = m_fornavn;
    }

    public String getM_etternavn() {
        return m_etternavn;
    }

    public void setM_etternavn(String m_etternavn) {
        this.m_etternavn = m_etternavn;
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
}
