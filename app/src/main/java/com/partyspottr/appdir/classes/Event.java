package com.partyspottr.appdir.classes;

import android.util.Base64;

import com.google.gson.Gson;
import com.partyspottr.appdir.BuildConfig;
import com.partyspottr.appdir.enums.EventStilling;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private long datefrom;
    private long dateto;
    private int agerestriction;
    private String hostStr;
    private List<Participant> participants;
    private List<Requester> requests;
    private int maxparticipants;
    private String postalcode;
    private String description;
    private boolean showguestlist;
    private boolean showaddress;
    private boolean hasimage;

    private static final String nameofeventElem = "nameofevent";
    private static final String addressElem = "address";
    private static final String townElem = "town";
    private static final String countryElem = "country";
    private static final String privateEventElem = "privateEvent";
    private static final String longitudeElem = "longitude";
    private static final String latitudeElem = "latitude";
    private static final String datefromElem = "datefrom";
    private static final String datetoElem = "dateto";
    private static final String agerestrictionElem = "agerestriction";
    private static final String hostStrElem = "hostStr";
    private static final String participantsElem = "participants";
    private static final String requestsElem = "requests";
    private static final String maxparticipantsElem = "maxparticipants";
    private static final String postalcodeElem = "postalcode";
    private static final String descriptionElem = "description";
    private static final String showguestlistElem = "showguestlist";
    private static final String showaddressElem = "showaddress";
    private static final String hasimageElem = "hasimage";

    public String EventToJSON() {
        try {
            JSONObject ret = new JSONObject();
            ret.put("eventId", eventId);
            ret.put(nameofeventElem, nameofevent);
            ret.put(addressElem, address);
            ret.put(townElem, town);
            ret.put(countryElem, country);
            ret.put(privateEventElem, privateEvent);
            ret.put(longitudeElem, longitude);
            ret.put(latitudeElem, latitude);
            ret.put(datefromElem, datefrom);
            ret.put(datetoElem, dateto);
            ret.put(agerestrictionElem, agerestriction);
            ret.put(hostStrElem, hostStr);
            ret.put(participantsElem, new Gson().toJson(participants));
            ret.put(requestsElem, requests);
            ret.put(maxparticipantsElem, maxparticipants);
            ret.put(postalcodeElem, postalcode);
            ret.put(descriptionElem, description);
            ret.put(showguestlistElem, showguestlist);
            ret.put(showaddressElem, showaddress);
            ret.put(hasimageElem, hasimage);
            ret.put("socketElem", Base64.encodeToString(BuildConfig.JSONParser_Socket.getBytes(), Base64.DEFAULT));
            return ret.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    public Event() {
        this(0, "", "", "", "", false, 0.0, 0.0, 0, 0, 0, new ArrayList<Participant>(), 0, "", "", "", false, false, new ArrayList<Requester>(), false);
    }

    public Event(String name) {
        this(0, name, "", "", "", false, 0.0, 0.0, 0, 0, 0, new ArrayList<Participant>(), 0,
                "0", "", "", false, false, new ArrayList<Requester>(), false);
    }

    public Event(String name, String adress, String cntry, String host) {
        this(0, name, adress, cntry, host, false, 0.0, 0.0, 0, 0, 0, new ArrayList<Participant>(), 0,
                "0", "", "", false, false, new ArrayList<Requester>(), false);
    }

    public Event(long eventid, String name, String adress, String cntry, String host, boolean privateevent, double longtude, double latude, long datfrom, long datto, int agerestr,
                 List<Participant> particpants, int maxparticpants, String postcode, String twn, String desc, boolean showgstlist, boolean showadress, List<Requester> foresprsler, boolean hsimage) {
        eventId = eventid;
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
            this.country = str.split(", ")[2];

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
            if(participant == null)
                continue;

            if(participant.getBrukernavn().equals(brukernavn)) {
                return true;
            }
        }

        return false;
    }

    public void addRequest(Requester requester) {
        requests.add(requester);
    }

    public void addRequestToParticipant(Requester requester) {
        participants.add(new Participant(requester.getBrukernavn(), requester.getCountry(), requester.getTown(), requester.isPremium() ? EventStilling.PREMIUM : EventStilling.GJEST));
    }

    public void removeRequest(String brukernavn) {
        for(int i = 0; i < requests.size(); i++) {
            if(requests.get(i).getBrukernavn().equals(brukernavn)) {
                requests.remove(i);
                return;
            }
        }
    }

    public Requester getRequesterByUsername(String username) {
        for(Requester requester : requests) {
            if(requester.getBrukernavn().equals(username)) {
                return requester;
            }
        }

        return null;
    }

    public boolean isBrukerRequesting(String brukernavn) {
        for(Requester requester : requests) {
            if(requester.getBrukernavn().equals(brukernavn)) {
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

    public long getDatefrom() {
        return datefrom;
    }

    public void setDatefrom(long datefrom) {
        this.datefrom = datefrom;
    }

    public long getDateto() {
        return dateto;
    }

    public void setDateto(long dateto) {
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

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
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
