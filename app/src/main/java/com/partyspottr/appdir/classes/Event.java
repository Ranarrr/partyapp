package com.partyspottr.appdir.classes;

import com.partyspottr.appdir.enums.Categories;
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
    private String description;
    private boolean showguestlist;
    private boolean showaddress;
    private boolean hasimage;
    private Categories category;

    public Event() {
        this(0, "", "", "", "", false, 0.0, 0.0, 0, 0, 0, new ArrayList<Participant>(), 0, "", "", false, false, new ArrayList<Requester>(), false, Categories.PARTY);
    }

    public Event(String name) {
        this(0, name, "", "", "", false, 0.0, 0.0, 0, 0, 0, new ArrayList<Participant>(), 0,
                "", "", false, false, new ArrayList<Requester>(), false, Categories.PARTY);
    }

    public Event(String name, String adress, String cntry, String host) {
        this(0, name, adress, cntry, host, false, 0.0, 0.0, 0, 0, 0, new ArrayList<Participant>(), 0,
                "", "", false, false, new ArrayList<Requester>(), false, Categories.PARTY);
    }

    public Event(long eventid, String name, String adress, String cntry, String host, boolean privateevent, double longtude, double latude, long datfrom, long datto, int agerestr,
                 List<Participant> particpants, int maxparticpants, String twn, String desc, boolean showgstlist, boolean showadress, List<Requester> foresprsler, boolean hsimage, Categories catgory) {
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
        town = twn;
        description = desc;
        showguestlist = showgstlist;
        showaddress = showadress;
        requests = foresprsler;
        hasimage = hsimage;
        category = catgory;
    }

    public void CopyEvent(Event otherEvent) {
        setEventId(otherEvent.getEventId());
        setTown(otherEvent.getTown());
        setShowguestlist(otherEvent.isShowguestlist());
        setShowaddress(otherEvent.isShowaddress());
        setParticipants(otherEvent.getParticipants());
        setRequests(otherEvent.getRequests());
        setMaxparticipants(otherEvent.getMaxparticipants());
        setLongitude(otherEvent.getLongitude());
        setLatitude(otherEvent.getLatitude());
        setPrivateEvent(otherEvent.isPrivateEvent());
        setHasimage(otherEvent.isHasimage());
        setDescription(otherEvent.getDescription());
        setDateto(otherEvent.getDateto());
        setDatefrom(otherEvent.getDatefrom());
        setCountry(otherEvent.getCountry());
        setAgerestriction(otherEvent.getAgerestriction());
        setAddress(otherEvent.getAddress());
        setNameofevent(otherEvent.getNameofevent());
        setCategory(otherEvent.getCategory());
    }

    public void ParseFromGoogleReq(JSONObject jsonObject) {
        try {
            this.longitude = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            this.latitude = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");

            String[] str = jsonObject.getJSONArray("results").getJSONObject(0).getString("formatted_address").split(",");
            this.address = str[0];
            if(str.length > 2)
                this.country = str[2];
            else
                this.country = str[1];

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

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }
}
