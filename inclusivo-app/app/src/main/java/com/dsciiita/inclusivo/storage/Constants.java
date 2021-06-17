package com.dsciiita.inclusivo.storage;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.dsciiita.inclusivo.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Constants {
    public static int FILTER_PAGE_SIZE = 20;

    public static int PLACEHOLDER_IMAGE = R.drawable.placeholder_square;

    public static String FEMALE_URL = "https://imgur.com/jxPu6aF.jpg";
    public static String LGBTQI_URL = "https://imgur.com/MygR67G.png";
    public static String VETERAN_URL = "https://imgur.com/aRVmfM1.jpg";
    public static String WORKING_MOTHER_URL = "https://imgur.com/4zo39Yt.jpg";
    public static String SPECIALLY_ABLED_URL = "https://imgur.com/Nfy0VPy.jpg";

    public static String PUNE_URL = "https://imgur.com/IfXDEUG.jpg";
    public static String MUMBAI_URL = "https://imgur.com/NJZBoIz.jpg";
    public static String DELHI_URL = "https://imgur.com/WMuvGDA.jpg";
    public static String GURGAON_URL = "https://imgur.com/ln7UWq8.jpg";
    public static String CHENNAI_URL = "https://imgur.com/nHLwjZH.jpg";
    public static String BENGALURU_URL = "https://imgur.com/aigq4T4.jpg";

    public static HashMap<String, String> buildMap(){
        HashMap<String, String> genderMap = new HashMap<>();
        genderMap.put("her", "their");
        genderMap.put("him", "them");
        genderMap.put("his", "theirs");
        genderMap.put("hers", "theirs");
        genderMap.put("she'd", "they’d");
        genderMap.put("he'd", "they’d");
        genderMap.put("he", "they");
        genderMap.put("she", "they");
        genderMap.put("she'll", "they'll");
        genderMap.put("he'll", "they’ll");
        genderMap.put("she's", "they’re");
        genderMap.put("he's", "they’re");
        genderMap.put("herself", "themselves");
        genderMap.put("himself", "themselves");
        genderMap.put("boy", "child");
        genderMap.put("girl", "child");
        genderMap.put("women", "people");
        genderMap.put("girls", "people");
        genderMap.put("gals", "people");
        genderMap.put("ladies", "people");
        genderMap.put("man", "people");
        genderMap.put("guys", "people");
        genderMap.put("boys", "people");
        genderMap.put("dudes", "people");
        genderMap.put("men", "people");
        genderMap.put("gents", "people");
        genderMap.put("gentlemen", "people");
        genderMap.put("woman", "person");
        genderMap.put("lady", "person");
        genderMap.put("gal", "person");
        genderMap.put("babe", "person");
        genderMap.put("bimbo", "person");
        genderMap.put("chick", "person");
        genderMap.put("guy", "person");
        genderMap.put("lad", "person");
        genderMap.put("fellow", "person");
        genderMap.put("dude", "person");
        genderMap.put("bro", "person");
        genderMap.put("gentleman", "person");
        genderMap.put("mother tongue", "native tongue");
        genderMap.put("father tongue", "native tongue");
        genderMap.put("chairwoman", "chairperson");
        genderMap.put("chairman", "chairperson");
        genderMap.put("committee woman", "committee member");
        genderMap.put("committee man", "committee member");
        genderMap.put("sisterhood", "community");
        genderMap.put("brotherhood", "community");
        genderMap.put("businesswoman", "business person");
        genderMap.put("salary woman", "business person");
        genderMap.put("businessman", "business person");
        genderMap.put("salary man", "business person");
        genderMap.put("alumnae", "graduates");
        genderMap.put("alumni", "graduates");
        genderMap.put("daughter", "child");
        genderMap.put("son", "child");
        genderMap.put("daughters", "children");
        genderMap.put("sons", "children");
        genderMap.put("females", "humans");
        genderMap.put("males", "humans");
        genderMap.put("femininity", "humanity");
        genderMap.put("manliness", "humanity");
        genderMap.put("spokeswoman", "spokesperson");
        genderMap.put("spokesman", "spokesperson");
        genderMap.put("spokeswomen", "spokespersons");
        genderMap.put("spokesmen", "spokespersons");
        genderMap.put("sportswoman", "sports person");
        genderMap.put("sportsman", "sports person");
        genderMap.put("sportswomen", "sports persons");
        genderMap.put("sportsmen", "sports persons");
        genderMap.put("maiden name", "birth name");
        genderMap.put("miss", "ms");
        genderMap.put("mrs", "ms");
        genderMap.put("manmade", "artificial");
        genderMap.put("freshman", "fresher");
        genderMap.put("freshwoman", "fresher");
        genderMap.put("housewife", "homemaker");
        genderMap.put("housewives", "homemakers");
        genderMap.put("manpower", "workforce");
        genderMap.put("mankind", "humankind");
        genderMap.put("sportsmanship", "fairness");
        genderMap.put("homosexual", "gay, lesbian");
        genderMap.put("sexual preference", "sexual orientation");

        return genderMap;
    }


}
