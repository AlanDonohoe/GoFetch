package com.gofetch.seomoz;

import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author alandonohoe class representing backlink URLs, with PA, DA, URL,
 * anchor text and target URL implements the Comparable interface so that we can
 * sort arrays of these objects.
 */

public class URLPlusDataPoints implements Comparable<URLPlusDataPoints> {

	//moved to seoMozData object....
	
    //private String score; // red, green or amber
    //private String backLinkPA; // Page Authority
   // private String backLinkDA; // domain authority
	
    private String uu; // backlink URL
    private String cleanURL; // URL thats been cleaned of buggy escape characters
    private String lt; // anchor text
    private String luuu; // target URL
    private String domainName; // domain....
    private static String targetUrlDA; //class wide member - this backlink's target's DA
    private static String targetUrlPA; //class wide member - this backlink's target's PA
    
    SEOMozURLData seoMozData = new SEOMozURLData();

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
        //return score;
        return seoMozData.getScore();
    }

    public void setScore(String score) {
        //this.score = score;
    	seoMozData.setScore(score);
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
    public String getURLAddress() {
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
        //return backLinkDA;
    	
    	return seoMozData.getBackLinkDAString();
    }

    public void setBackLinkDA(String backLinkDA) {
       // this.backLinkDA = backLinkDA;
    	
    	seoMozData.setBackLinkDA(Integer.parseInt(backLinkDA));
    }

    public String getBackLinkPA() {
        //return backLinkPA;
    	
    	return seoMozData.getBackLinkPAString();
    }

    public int getBackLinkPAInt() {
        //return java.lang.Integer.parseInt(backLinkPA);
    	
    	return seoMozData.getBackLinkPA();
    }

    public int getBackLinkDAInt() {
        //return java.lang.Integer.parseInt(backLinkDA);
    	
    	return seoMozData.getBackLinkDA();
    }

    public void setBackLinkPA(String backLinkPA) {
        //this.backLinkPA = backLinkPA;
    	seoMozData.setBackLinkDA(Integer.parseInt(backLinkPA));
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
