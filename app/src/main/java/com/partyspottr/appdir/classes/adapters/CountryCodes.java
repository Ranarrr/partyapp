package com.partyspottr.appdir.classes.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.partyspottr.appdir.R;
import com.partyspottr.appdir.ui.MainActivity;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Ranarrr on 24-Feb-18.
 *
 * @author Alexander Bitnes
 */

public class CountryCodes extends BaseAdapter {

    private static final String[] m_fullCountry = {
            "Andorra",
            "United Arab Emirates",
            "Afghanistan",
            "Albania",
            "Armenia",
            "Netherlands Antilles",
            "Angola",
            "Antarctica",
            "Argentina",
            "Austria",
            "Australia",
            "Aruba",
            "Azerbaijan",
            "Bosnia and Herz.",
            "Bangladesh",
            "Belgium",
            "Burkina Faso",
            "Bulgaria",
            "Bahrain",
            "Burundi",
            "Benin",
            "Saint Barthelemy",
            "Brunei",
            "Bolivia",
            "Brazil",
            "Bhutan",
            "Botswana",
            "Belarus",
            "Belize",
            "Canada",
            "Cocos Islands",
            "Congo, The Democr. Rep.",
            "Central African Rep.",
            "Republic of the Congo",
            "Switzerland",
            "Cote d'Ivoire",
            "Cook Islands",
            "Chile",
            "Cameroon",
            "China",
            "Colombia",
            "Costa Rica",
            "Cuba",
            "Cape Verde",
            "Christmas Island",
            "Cyprus",
            "Czech Republic",
            "Germany",
            "Djibouti",
            "Denmark",
            "Algeria",
            "Ecuador",
            "Estonia",
            "Egypt",
            "Eritrea",
            "Spain",
            "Ethiopia",
            "Finland",
            "Fiji",
            "Falkland Islands",
            "Micronesia, Fed. States",
            "Faroe Islands",
            "France",
            "Gabon",
            "United Kingdom",
            "Georgia",
            "Ghana",
            "Gibraltar",
            "Greenland",
            "Gambia",
            "Guinea",
            "Equatorial Guinea",
            "Greece",
            "Guatemala",
            "Guinea-Bissau",
            "Guyana",
            "Hong Kong",
            "Honduras",
            "Croatia",
            "Haiti",
            "Hungary",
            "Indonesia",
            "Ireland",
            "Israel",
            "Isle of Man",
            "India",
            "Iraq",
            "Iran",
            "Italy",
            "Jordan",
            "Japan",
            "Kenya",
            "Kyrgyzstan",
            "Cambodia",
            "Kiribati",
            "Comoros",
            "North Korea",
            "South Korea",
            "Kuwait",
            "Kazakhstan",
            "Laos",
            "Lebanon",
            "Liechtenstein",
            "Sri Lanka",
            "Liberia",
            "Lesotho",
            "Lithuania",
            "Luxembourg",
            "Latvia",
            "Libya",
            "Morocco",
            "Monaco",
            "Moldova",
            "Montenegro",
            "Madagascar",
            "Marshall Islands",
            "Macedonia",
            "Mali",
            "Myanmar",
            "Mongolia",
            "Macau",
            "Mauritania",
            "Malta",
            "Mauritius",
            "Maldives",
            "Malawi",
            "Mexico",
            "Malaysia",
            "Mozambique",
            "Namibia",
            "New Caledonia",
            "Niger",
            "Nigeria",
            "Nicaragua",
            "Netherlands",
            "Norway",
            "Nepal",
            "Nauru",
            "Niue",
            "New Zealand",
            "Oman",
            "Panama",
            "Peru",
            "French Polynesia",
            "Papua New Guinea",
            "Philippines",
            "Pakistan",
            "Poland",
            "Saint Pierre and Miquelon",
            "Pitcairn",
            "Puerto Rico",
            "Portugal",
            "Palau",
            "Paraguay",
            "Qatar",
            "Romania",
            "Serbia",
            "Russia",
            "Rwanda",
            "Saudi Arabia",
            "Solomon Islands",
            "Seychelles",
            "Sudan",
            "Sweden",
            "Singapore",
            "Saint Helena",
            "Slovenia",
            "Slovakia",
            "Sierra Leone",
            "San Marino",
            "Senegal",
            "Somalia",
            "Suriname",
            "Sao Tome and Princ.",
            "El Salvador",
            "Syria",
            "Swaziland",
            "Chad",
            "Togo",
            "Thailand",
            "Tajikistan",
            "Tokelau",
            "East Timor",
            "Turkmenistan",
            "Tunisia",
            "Tonga",
            "Turkey",
            "Tuvalu",
            "Taiwan",
            "Tanzania",
            "Ukraine",
            "Uganda",
            "United States",
            "Uruguay",
            "Uzbekistan",
            "Vatican City",
            "Venezuela",
            "Vietnam",
            "Vanuatu",
            "Wallis and Futuna",
            "Samoa",
            "Yemen",
            "Mayotte",
            "South Africa",
            "Zambia",
            "Zimbabwe"
    };

    private static final String[] m_Countries = {
            "AD",
            "AE",
            "AF",
            "AL",
            "AM",
            "AN",
            "AO",
            "AQ",
            "AR",
            "AT",
            "AU",
            "AW",
            "AZ",
            "BA",
            "BD",
            "BE",
            "BF",
            "BG",
            "BH",
            "BI",
            "BJ",
            "BL",
            "BN",
            "BO",
            "BR",
            "BT",
            "BW",
            "BY",
            "BZ",
            "CA",
            "CC",
            "CD",
            "CF",
            "CG",
            "CH",
            "CI",
            "CK",
            "CL",
            "CM",
            "CN",
            "CO",
            "CR",
            "CU",
            "CV",
            "CX",
            "CY",
            "CZ",
            "DE",
            "DJ",
            "DK",
            "DZ",
            "EC",
            "EE",
            "EG",
            "ER",
            "ES",
            "ET",
            "FI",
            "FJ",
            "FK",
            "FM",
            "FO",
            "FR",
            "GA",
            "GB",
            "GE",
            "GH",
            "GI",
            "GL",
            "GM",
            "GN",
            "GQ",
            "GR",
            "GT",
            "GW",
            "GY",
            "HK",
            "HN",
            "HR",
            "HT",
            "HU",
            "ID",
            "IE",
            "IL",
            "IM",
            "IN",
            "IQ",
            "IR",
            "IT",
            "JO",
            "JP",
            "KE",
            "KG",
            "KH",
            "KI",
            "KM",
            "KP",
            "KR",
            "KW",
            "KZ",
            "LA",
            "LB",
            "LI",
            "LK",
            "LR",
            "LS",
            "LT",
            "LU",
            "LV",
            "LY",
            "MA",
            "MC",
            "MD",
            "ME",
            "MG",
            "MH",
            "MK",
            "ML",
            "MM",
            "MN",
            "MO",
            "MR",
            "MT",
            "MU",
            "MV",
            "MW",
            "MX",
            "MY",
            "MZ",
            "NA",
            "NC",
            "NE",
            "NG",
            "NI",
            "NL",
            "NO",
            "NP",
            "NR",
            "NU",
            "NZ",
            "OM",
            "PA",
            "PE",
            "PF",
            "PG",
            "PH",
            "PK",
            "PL",
            "PM",
            "PN",
            "PR",
            "PT",
            "PW",
            "PY",
            "QA",
            "RO",
            "RS",
            "RU",
            "RW",
            "SA",
            "SB",
            "SC",
            "SD",
            "SE",
            "SG",
            "SH",
            "SI",
            "SK",
            "SL",
            "SM",
            "SN",
            "SO",
            "SR",
            "ST",
            "SV",
            "SY",
            "SZ",
            "TD",
            "TG",
            "TH",
            "TJ",
            "TK",
            "TL",
            "TM",
            "TN",
            "TO",
            "TR",
            "TV",
            "TW",
            "TZ",
            "UA",
            "UG",
            "US",
            "UY",
            "UZ",
            "VA",
            "VE",
            "VN",
            "VU",
            "WF",
            "WS",
            "YE",
            "YT",
            "ZA",
            "ZM",
            "ZW"
    };

    private static final String[] m_Codes = {
            "376",
            "971",
            "93",
            "355",
            "374",
            "599",
            "244",
            "672",
            "54",
            "43",
            "61",
            "297",
            "994",
            "387",
            "880",
            "32",
            "226",
            "359",
            "973",
            "257",
            "229",
            "590",
            "673",
            "591",
            "55",
            "975",
            "267",
            "375",
            "501",
            "1",
            "61",
            "243",
            "236",
            "242",
            "41",
            "225",
            "682",
            "56",
            "237",
            "86",
            "57",
            "506",
            "53",
            "238",
            "61",
            "357",
            "420",
            "49",
            "253",
            "45",
            "213",
            "593",
            "372",
            "20",
            "291",
            "34",
            "251",
            "358",
            "679",
            "500",
            "691",
            "298",
            "33",
            "241",
            "44",
            "995",
            "233",
            "350",
            "299",
            "220",
            "224",
            "240",
            "30",
            "502",
            "245",
            "592",
            "852",
            "504",
            "385",
            "509",
            "36",
            "62",
            "353",
            "972",
            "44",
            "91",
            "964",
            "98",
            "39",
            "962",
            "81",
            "254",
            "996",
            "855",
            "686",
            "269",
            "850",
            "82",
            "965",
            "7",
            "856",
            "961",
            "423",
            "94",
            "231",
            "266",
            "370",
            "352",
            "371",
            "218",
            "212",
            "377",
            "373",
            "382",
            "261",
            "692",
            "389",
            "223",
            "95",
            "976",
            "853",
            "222",
            "356",
            "230",
            "960",
            "265",
            "52",
            "60",
            "258",
            "264",
            "687",
            "227",
            "234",
            "505",
            "31",
            "47",
            "977",
            "674",
            "683",
            "64",
            "968",
            "507",
            "51",
            "689",
            "675",
            "63",
            "92",
            "48",
            "508",
            "870",
            "1",
            "351",
            "680",
            "595",
            "974",
            "40",
            "381",
            "7",
            "250",
            "966",
            "677",
            "248",
            "249",
            "46",
            "65",
            "290",
            "386",
            "421",
            "232",
            "378",
            "221",
            "252",
            "597",
            "239",
            "503",
            "963",
            "268",
            "235",
            "228",
            "66",
            "992",
            "690",
            "670",
            "993",
            "216",
            "676",
            "90",
            "688",
            "886",
            "255",
            "380",
            "256",
            "1",
            "598",
            "998",
            "39",
            "58",
            "84",
            "678",
            "681",
            "685",
            "967",
            "262",
            "27",
            "260",
            "263"
    };

    private Context m_Context;

    public CountryCodes( Context cxt )
    {
        super();

        m_Context = cxt;
    }

    /**
     * Get full country string from country sign
     *
     * @param twochar_country: Any string from m_Countries
     * @return A String object from m_fullCountry list, null if not found.
     */
    public static String getFullCountry( String twochar_country ) {
        int index = getCountryIndex(twochar_country);
        return m_fullCountry[index];
    }

    public static String getCountrySign( String fullCountry ) {
        for(int i = 0; i < m_fullCountry.length; i++ ) {
            if(m_fullCountry[i].equals(fullCountry)) {
                return getCountry(i);
            }
        }

        return "";
    }

    /**
     * Get phone code from country sign.
     *
     * @param country: two-chars country sign to fetch ("US", "IT", "GB", ...)
     * @return string of matching phone code ("1", "39", "44", ...). null if none matches.
     */
    public static String getCode( String country )
    {
        int index = getCountryIndex( country );
        return index == -1? null: getCode(index);
    }

    /**
     * Get international code at provided index.
     *
     * @param index: array index
     * @return international code
     */
    public static String getCode( int index )
    {
        return m_Codes[index];
    }

    /**
     * Get country signs from phone code.
     * More countries may match the same code.
     *
     * @param code: phone code to fetch ("1", "39", "44", ...)
     * @return list of uppercase country signs (["US","PR","CA"], ["IT","VA"], ["GB","IM"], ...)
     *          Empty list if none matches.
     */
    public static ArrayList<String> getCountry( String code )
    {
        ArrayList<String> matches = new ArrayList<>();
        getCountry(code, matches);
        return matches;
    }

    /**
     * Memory cheap version of country fetching: uses user provided list as output which outside
     * could be recycled on desire.
     *
     * @param code: country sign to fetch
     * @param matches: list to fill with matches, used as output
     */
    public static void getCountry( String code, ArrayList<String> matches )
    {
        matches.clear();

        for( int i=0; i<m_Codes.length; ++i )
            if( m_Codes[i].equals( code ) )
                matches.add(getCountry(i));
    }

    /**
     * Returns country sign at specified index of internal array.
     *
     * @param index: index to fetch
     * @return country sign
     */
    public static String getCountry( int index )
    {
        return m_Countries[index];
    }

    /**
     * Looks for country sign array index.
     *
     * @param country: country sign to search
     * @return array index. -1 if none matches.
     */
    public static int getCountryIndex( String country )
    {
        String search = country.toUpperCase(Locale.getDefault());

        for( int i=0; i < m_Countries.length; ++i )
            if( m_Countries[i].equals( search ) )
                return i;

        return -1;
    }

    public static int getFullCountryIndex(String FullCountry) {
        for( int i=0; i < m_fullCountry.length; ++i )
            if( m_fullCountry[i].equals( FullCountry ) )
                return i;

        return -1;
    }

    @Override
    public int getCount() {
        return m_Codes.length;
    }

    @Override
    public Object getItem(int index) {
        return String.format(Locale.ENGLISH, "%s,%s,%s", m_Codes[index], m_Countries[index], m_fullCountry[index]);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View recycleView, ViewGroup viewGroup) {
        TextView view;
        if( recycleView == null ) {
            view = new TextView(m_Context);
            view.setPadding((int) m_Context.getResources().getDimension(R.dimen._10sdp), (int) m_Context.getResources().getDimension(R.dimen._10sdp), (int) m_Context.getResources().getDimension(R.dimen._10sdp),
                    (int) m_Context.getResources().getDimension(R.dimen._10sdp));
        }
        else {
            view = (TextView)recycleView;
        }

        view.setTextColor(m_Context.getResources().getColor(R.color.white));

        view.setTextSize(m_Context.getResources().getDimension(R.dimen._4ssp));

        view.setTypeface(MainActivity.typeface);

        view.setText(String.format(Locale.ENGLISH, "%s (+%s)", m_Countries[index], m_Codes[index]));

        return view;
    }
}