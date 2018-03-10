package com.partyspottr.appdir.classes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Ranarrr on 28-Jan-18.
 *
 * @author Ranarrr
 */

public class Event {
    private long eventId;
    private String nameofevent;
    private String address;
    private String town;
    private String country;
    private boolean privateEvent;
    private Double longitude;
    private Double latitude;
    private GregorianCalendar datefrom;
    private GregorianCalendar dateto;
    private int agerestriction;
    private String hostStr;
    private List<Participant> participants;
    private List<Requester> requests;
    private int maxparticipants;
    private int postalcode;
    private String description;
    private boolean showguestlist;
    private boolean showaddress;
    private boolean hasimage;

    public Event(String name) {
        this(name, "", "", "", false, 0.0, 0.0, null, null, 0, new ArrayList<Participant>(), 0,
                0, "", "", false, false, new ArrayList<Requester>(), false);
    }

    public Event(String name, String adress, String cntry, String host) {
        this(name, adress, cntry, host, false, 0.0, 0.0, null, null, 0, new ArrayList<Participant>(), 0,
                0, "", "", false, false, new ArrayList<Requester>(), false);
    }

    public Event(String name, String adress, String cntry, String host, boolean privateevent, double longtude, double latude, GregorianCalendar datfrom, GregorianCalendar datto, int agerestr,
                 List<Participant> particpants, int maxparticpants, int postcode, String twn, String desc, boolean showgstlist, boolean showadress, List<Requester> foresprsler, boolean hsimage) {
        nameofevent = name;
        address = adress;
        country = cntry;
        hostStr = host;
        privateEvent = privateevent;
        longitude = longtude;
        latitude = latude;
        datefrom = datfrom;
        dateto = datto;
        agerestriction = agerestr;
        participants = particpants;
        maxparticipants = maxparticpants;
        postalcode = postcode;
        town = twn;
        description = desc;
        showguestlist = showgstlist;
        showaddress = showadress;
        requests = foresprsler;
        hasimage = hsimage;
    }

    public void ParseFromGoogleReq(JSONObject jsonObject) {
        try {
            this.longitude = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            this.latitude = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");

            String str = jsonObject.getJSONArray("results").getJSONObject(0).getString("formatted_address");
            this.address = str.split(",")[0];

            for(int i = 0; i < jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").length(); i++) {
                JSONObject temp = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(i);
                if(temp.getJSONArray("types").getString(0).equals("postal_town")) {
                    this.town = temp.getString("long_name");
                    break;
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isBrukerInList(String brukernavn) {
        for(Participant participant : participants) {
            if(participant.getBrukernavn().equals(brukernavn)) {
                return true;
            }
        }

        return false;
    }

    public String getNameofevent() {
        return nameofevent;
    }

    public void setNameofevent(String nameofevent) {
        this.nameofevent = nameofevent;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public GregorianCalendar getDatefrom() {
        return datefrom;
    }

    public void setDatefrom(GregorianCalendar datefrom) {
        this.datefrom = datefrom;
    }

    public GregorianCalendar getDateto() {
        return dateto;
    }

    public void setDateto(GregorianCalendar dateto) {
        this.dateto = dateto;
    }

    public int getAgerestriction() {
        return agerestriction;
    }

    public void setAgerestriction(int agerestriction) {
        this.agerestriction = agerestriction;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isPrivateEvent() {
        return privateEvent;
    }

    public void setPrivateEvent(boolean privateEvent) {
        this.privateEvent = privateEvent;
    }

    public String getHostStr() {
        return hostStr;
    }

    public void setHostStr(String hostStr) {
        this.hostStr = hostStr;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public int getMaxparticipants() {
        return maxparticipants;
    }

    public void setMaxparticipants(int maxparticipants) {
        this.maxparticipants = maxparticipants;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public int getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(int postalcode) {
        this.postalcode = postalcode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isShowguestlist() {
        return showguestlist;
    }

    public void setShowguestlist(boolean showguestlist) {
        this.showguestlist = showguestlist;
    }

    public boolean isShowaddress() {
        return showaddress;
    }

    public void setShowaddress(boolean showaddress) {
        this.showaddress = showaddress;
    }

    public List<Requester> getRequests() {
        return requests;
    }

    public void setRequests(List<Requester> requests) {
        this.requests = requests;
    }

    public boolean isHasimage() {
        return hasimage;
    }

    public void setHasimage(boolean hasimage) {
        this.hasimage = hasimage;
    }
}
