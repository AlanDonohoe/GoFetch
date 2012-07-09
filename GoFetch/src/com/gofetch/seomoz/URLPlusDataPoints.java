package com.gofetch.seomoz;

import java.util.Comparator;

/**
 *
 * @author alandonohoe class representing backlink URLs, with PA, DA, URL,
 * anchor text and target URL implements the Comparable interface so that we can
 * sort arrays of these objects.
 */
public class URLPlusDataPoints implements Comparable<URLPlusDataPoints> {

    private String score; // red, green or amber
    private String backLinkPA; // Page Authority
    private String backLinkDA; // domain authority
    private String uu; // backlink URL
    private String cleanURL; // URL thats been cleaned of buggy escape characters
    private String lt; // anchor text
    private String luuu; // target URL
    private String domainName; // domain....
    private static String targetUrlDA;
    private static String targetUrlPA;

    public static String getTargetUrlDA() {
        return targetUrlDA;
    }

    public static void setTargetUrlDA(String targetUrlDA) {
        URLPlusDataPoints.targetUrlDA = targetUrlDA;
    }

    public static String getTargetUrlPA() {
        return targetUrlPA;
    }

    public static void setTargetUrlPA(String targetUrlPA) {
        URLPlusDataPoints.targetUrlPA = targetUrlPA;
    }

    /**
     * URL thats been cleaned of buggy escape characters, and is now Gephi
     * friendly
     */
    public String getCleanURL() {
        return cleanURL;
    }

    /**
     * URL thats been cleaned of buggy escape characters, and is now Gephi
     * friendly
     */
    public void setCleanURL(String cleanURL) {
        this.cleanURL = cleanURL;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getLt() { // get anchor text
        return lt;
    }

    public void setLt(String lt) { // set anchor text
        this.lt = lt;
    }

    public String getLuuu() { // get target URL
        return luuu;
    }

    public void setLuuu(String luuu) { // set target URL
        this.luuu = luuu;
    }

    // backlink URL
    public String getUu() {
        return uu;
    }

    // backlink URL
    public void setUu(String uu) {
        this.uu = uu;
    }

    public String getBackLinkAnchorText() {
        return lt;
    }

    public void setBackLinkAnchorText(String backLinkAnchorText) {
        this.lt = backLinkAnchorText;
    }

    public String getBackLinkDA() {
        return backLinkDA;
    }

    public void setBackLinkDA(String backLinkDA) {
        this.backLinkDA = backLinkDA;
    }

    public String getBackLinkPA() {
        return backLinkPA;
    }

    public int getBackLinkPAInt() {
        return java.lang.Integer.parseInt(backLinkPA);
    }

    public int getBackLinkDAInt() {
        return java.lang.Integer.parseInt(backLinkDA);
    }

    public void setBackLinkPA(String backLinkPA) {
        this.backLinkPA = backLinkPA;
    }

    public String getBackLinkURL() {
        return uu;
    }

    public void setBackLinkURL(String backLinkURL) {
        uu = backLinkURL;
    }

    /**
     * 
     * @param compareBackLink 
     * @return 
     * Sorts an array of URLPlusDataPoints by domain authority in ascending order
     */
    public int compareTo(URLPlusDataPoints compareBackLink) {
        
        int compareBackLinkDA = ((URLPlusDataPoints) compareBackLink).getBackLinkDAInt();

//      //ascending order
        return this.getBackLinkDAInt() - compareBackLinkDA;

//      //descending order
//      //return compareBackLinkDA - this.getBackLinkDAInt();

    }
    
}
