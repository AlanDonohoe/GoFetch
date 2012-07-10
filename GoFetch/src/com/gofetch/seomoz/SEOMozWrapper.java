/**
 * This class encapsulates the services and data provided by SEOMoz. 
 * 
 * Member data - allLinksList - a list of all the links pointing to the target url, as returned by SEOMoz API.
 * 			   - selectLinksList - a list of select links pointing to the target url, typically..used...???
 * 			   - uniqueDomainsList - a list of links that represent at least 1 link from every unique domain from the allLinksDAPA
 * 
 * Handles the following capabilities of the SEOMoz API:
 *
 * Authentication - this occurs automatically when the object is constructed The
 * access ID and Secret key must be passed to the constructor.
 * Would typically construct using constants found in Constants class.
 * 
 * getTotalLinksData - Returns all the links pointing to target url, and places them as list
 *  in member data - allLinksList - for further use...
 *  
 * getSelectURLLinkData - more select version of above - fills data returned from SEOMoz in 
 *  member data  -  selectLinksList
 *  
 * extractUniqueDoms - this loops through allLinksList and extracts 1 link from each unique domain
 * 	- places these links in member data - uniqueDomainsList
 *  
 */
package com.gofetch.seomoz;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

/**
 *
 * @author alandonohoe
 */
public class SEOMozWrapper {

    public static final String TAG = "SEOMoz object";   // used for logging errors
    private Logger log = Logger.getLogger(TAG);         // used for logging errors
    private String errorMsg;                            // used for logging errors
    private String accessID;							// SEOMoz server authentication
    private String secretKey;							// SEOMoz server authentication
    private Authenticator authenticator;				// SEOMoz server authentication
    private boolean usingSEOMozFreeAPI;					// if this is set to true, then the limitations of free API are used - eg: we can ONLY ever get 1000 links
    
    
    private ArrayList<URLPlusDataPoints> allLinksList = new ArrayList<URLPlusDataPoints>();       // array of all links to target URL.
    //TODO: look into whether selectLinksDAPA is used.... seems superceded by uniqueDomains...
    private ArrayList<URLPlusDataPoints> selectLinksList = new ArrayList<URLPlusDataPoints>();    // array of selected backlinks with Page Auth and Domain Auth.
    private ArrayList<URLPlusDataPoints> uniqueDomainsList = new ArrayList<URLPlusDataPoints>();      // array of unique linking domains 
    
    private int noOfTotalLinks;							// total no of links pointing to the target, accessible via SEOMoz API
    private int noOfUniqueDomains;						// total no of unique domains, as determined by this app - not SEOMoz API.
    private static String targetUrlPA;					// target url's PA
    private static String targetUrlDA;					// target url's DA
    

    public SEOMozWrapper(String accessID, String secretKey) {

        this.accessID = accessID;
        this.secretKey = secretKey;

        Authenticate();
    }

    /**
     *
     * @return target URL's DA - its set in retrieveAuthorityData
     */
    public static String getTargetUrlDA() {
        return targetUrlDA;
    }

    /**
     *
     * @return target URL's PA - its set in retrieveAuthorityData
     */
    public static String getTargetUrlPA() {
        return targetUrlPA;
    }

    public ArrayList<URLPlusDataPoints> getUniqueDomainLinks() {

        return uniqueDomainsList;
    }

    public ArrayList<URLPlusDataPoints> getAllLinks() {

        return allLinksList;
    }

    public ArrayList<URLPlusDataPoints> getSelectLinks() {

        return selectLinksList;
    }
    
    public ArrayList<URLPlusDataPoints> getUniqueDomains() {
        return uniqueDomainsList;
    }
    
    private void Authenticate() {

        authenticator = new Authenticator();
        authenticator.setAccessID(accessID);
        authenticator.setSecretKey(secretKey);
    }

    public void usingSEOMozFreeAPI(boolean SEOMozFreeAPI) {
        usingSEOMozFreeAPI = SEOMozFreeAPI;
    }

    /**
     * Queries the SEOMoz server for the no of external links to the target url,
     * if called before just returns this value - saves unnec. hitting the server 
     * @param url - target url
     * @return - no of external links to target url.
     */
    public int getNoOfExternalLinks(String url) {

        if (noOfTotalLinks > 0)// if we have total no of links from SEOMoz already - just return
        {
            return noOfTotalLinks;
        }

        // else... query SEOMoz server.....
        URLMetricsService urlMetrics = new URLMetricsService();

        urlMetrics.setAuthenticator(authenticator);

        String response = urlMetrics.getUrlMetrics(url);

        Gson gson = new Gson();
        UrlResponse res = gson.fromJson(response, UrlResponse.class);

        noOfTotalLinks = Integer.parseInt(res.getUeid());

        return noOfTotalLinks;
    }

    /**
     *
     * @param url - target url. Must not have "http://" in front, just
     * "www.abcetc...."
     * @param scope - page to page, domain to domain, etc - see SEOMoz API
     * documentation
     * @param filters - selects which args you want SEOMoz server to return
     * @param sort - sort results by PA or DA, etc
     * @param ....
     *
     * @return an array of links
     */
    public ArrayList<URLPlusDataPoints> getSelectURLLinkData(String url, String scope, String filters, String sort, long sourceCols, long targetCols, long linkCols, int offset, int noOfLinks) {

        LinksService linksService = new LinksService();
        linksService.setAuthenticator(authenticator);

        // check we're not asking for above limit:
        if (noOfLinks > Constants.LINK_QUERY_MAX) {
            noOfLinks = Constants.LINK_QUERY_MAX;
        }

        String response = linksService.getLinks(url, scope, filters, sort, sourceCols, targetCols, linkCols, offset, noOfLinks);

        if (response.length() > 2) { // check for "[]" = empty reponse

            Type linksListType = new TypeToken<ArrayList<URLPlusDataPoints>>() {
            }.getType(); // get type for GSON

            Gson gson = new Gson();

            selectLinksList = gson.fromJson(response, linksListType);

            tidyLinkText(selectLinksList);
        }
        return selectLinksList;

    }

    /**
     *
     * @param url
     * @param scope
     * @param filters
     * @param sort
     * @param sourceCols
     * @param targetCols
     * @param linkCols
     * @return an ArrayList of ALL the backlinks to the target URL.
     *
     * Fills and returns ref to member data- allLinksList - array of backlinks
     * to target "url", with associated datapoints - makes a clean copy of the
     * url (minus any commas) in the cleanURL member data for each url.
     */
    public ArrayList<URLPlusDataPoints> getTotalLinksData(String url, String scope, String filters, String sort, long sourceCols, long targetCols, long linkCols) {


        int offset = 0;
        boolean calledOnce = false; // used in timer delay to hit SEOMoz server multiple times....
        boolean moreLinksLeft = true; // flag set to false when SEOMoz not sending back any more links...

        ArrayList<URLPlusDataPoints> tempLinks;

        allLinksList.clear(); // make sure we are starting with empty list to avoid duplication

        while (moreLinksLeft) {


            if (calledOnce) /// no need to call the first time.. 
            {
                try {
                    Thread.sleep(Constants.SEOMOZ_SERVER_DELAY);  
                } catch (InterruptedException ex) {

                    // from: http://stackoverflow.com/questions/9139128/a-sleeping-thread-is-getting-interrupted-causing-loss-of-connection-to-db
                    Thread.currentThread().interrupt(); // restore interrupted status

                    errorMsg = SEOMozWrapper.TAG + " threw " + ex.getMessage();
                    log.info(errorMsg);
                    ex.printStackTrace();
                }

            }
            calledOnce = true;

            tempLinks = getSelectURLLinkData(url, scope, filters, sort, sourceCols, targetCols, linkCols, offset, Constants.LINK_QUERY_MAX);


            if (tempLinks.isEmpty()) { //if no_more_links 

                moreLinksLeft = false; //No more links from SEOMoz  - set flag
                noOfTotalLinks = allLinksList.size();

            } // if we have got back less than the max amount allowed, indicates we have all the links SEOMoz has to offer
            //OR we're using free API - we can ONLY ever get 1000 links
            else if ((tempLinks.size() < Constants.LINK_QUERY_MAX) || usingSEOMozFreeAPI) {

                moreLinksLeft = false; //No more links from SEOMoz  - set flag
                noOfTotalLinks = tempLinks.size();
                allLinksList.addAll(tempLinks); //append templinks to allLinksDAPA
            } else { /// still more links to get.... so...
                allLinksList.addAll(tempLinks); //append templinks to allLinksDAPA

                offset = allLinksList.size(); // move along to query next links...  and go back for more backlinks
            }

        }

        tidyLinkText(allLinksList);
        extractDomNameFromURL();

        return allLinksList;
    }

    /**
     * Removes any \n and commas from img and urls passed, and also replaces any
     * lengthy img text with tidy <img> text If URL does not start with http://
     * this is added - as required downstream by the gefx viewer
     *
     * @param linkListToClean - datapoint that will have its img and url text
     * tidied up
     */
    private void tidyLinkText(ArrayList<URLPlusDataPoints> linkListToClean) {

        String cleanAnchorText;
        String cleanURLText;

        for (int i = 0; i < linkListToClean.size(); i++) {

            cleanAnchorText = TextParser.tidyImgAnchorText(linkListToClean.get(i).getBackLinkAnchorText());
            cleanURLText = TextParser.removeCommaFromText(linkListToClean.get(i).getBackLinkURL());

            cleanURLText = TextParser.addHTTPPrefix(cleanURLText);

            linkListToClean.get(i).setCleanURL(cleanURLText);
            linkListToClean.get(i).setBackLinkAnchorText(cleanAnchorText);

        }
    }

    /**
     * Runs through all the links in current profile and extracts their domain name,
     * then assigns this name to each links member data DomainName
     */
    private void extractDomNameFromURL() {

        String domName;

        for (int i = 0; i < allLinksList.size(); i++) {

            domName = TextParser.getDomainNameFromURL(allLinksList.get(i).getBackLinkURL());

            allLinksList.get(i).setDomainName(domName);

        }
    }

    /**
     * Loops through all the URL backlinks and copies the first URL encountered
     * from unique domains into uniqueDomains member
     *
     * @return
     */
    public void extractUniqueDoms() {

        //TODO: work through the algo of getting all unique domains, plus no of links each has pointing to target here
    	//	that is - need to accommodate multiple wordpress domains... etc - speak to Gary and check...
        if (allLinksList.isEmpty() || (allLinksList.get(0).getDomainName().isEmpty())) {
            return; // throw error??
        }

        String currentDomName;
        boolean containsDomName = false;

        // set up the initial case - so list is not empty:
        uniqueDomainsList.add(allLinksList.get(0));

        try {


            for (int i = 1; i < allLinksList.size(); i++) {
                //run through all links for each dom name....
                currentDomName = allLinksList.get(i).getDomainName();

                // now run through current list of domian names and check if above name is not in there, add if so
                for (int b = 0; b < uniqueDomainsList.size(); b++) {

                    if (uniqueDomainsList.get(b).getDomainName().equals(currentDomName)) {
                        // if the dom name is NOT already in unique dom name list, add and return...
                        containsDomName = true;
                    }

                }

                if (!containsDomName) {
                    uniqueDomainsList.add(allLinksList.get(i));
                }
                //reset flag at end of each loop:
                containsDomName = false;

            }

        } catch (Exception e) {
            errorMsg = SEOMozWrapper.TAG + " threw " + e.getMessage();
            log.info(errorMsg);
            e.printStackTrace();
        }

        noOfUniqueDomains = uniqueDomainsList.size();

    }


    /*
     * calls the URL metric SEOMoz API on each of the backlink URLs.
     * 
     */

    @SuppressWarnings("SleepWhileInLoop")
    public void retrieveAuthorityData(ArrayList<URLPlusDataPoints> urlList) {
    	

        // logging stuff
        String logString; // used to write to log, the long countdown of getting PA and DA data for each link from SEOMoz server...
        String currentURL;
        int urlListSize = urlList.size();
        //... end logging

        URLMetricsService urlMetricsService = new URLMetricsService(authenticator);
        Gson gson = new Gson();
        String response;
        String stringDA, stringPA;
        UrlResponse res; 
        long authorityBitMask = URLMetricsConstants.URLMETRICS_COL_ALL; //URLMetricsConstants.URLMETRICS_COL_DOMAIN_AUTHORITY 
        																// | URLMetricsConstants.URLMETRICS_COL_PAGE_AUTHORITY;

        // first set the target URL's DA and PA:
        response = urlMetricsService.getUrlMetrics(urlList.get(0).getLuuu(), authorityBitMask);

        res = gson.fromJson(response, UrlResponse.class);

        // remove the digits after the decimal point
        stringPA = removepostDecimalDigits(res.getUpa());
        stringDA = removepostDecimalDigits(res.getPda());

        URLPlusDataPoints.setTargetUrlPA(stringPA);
        URLPlusDataPoints.setTargetUrlDA(stringDA);

        try {

            for (int i = 0; i < urlList.size(); i++) {

                /////////////////////////////////////
                //TODO: add caching... 
                // Check cache of data here, read up caching, check if each node's id (= unique URL)
                //has been called already, then skip calling the SEOMoz server....

                // if(existsInCache(selectLinksDAPA[i].getUu()){} else { call SEOMoz server code below instead}
                //////////////////////

                //////////////////////
                //TODO: fix for teh SEOMoz throttle limit. need to cache....
                //  see: https://seomoz.zendesk.com/entries/459198-rate-limiting-throttling-and-avoiding-time-outs

                // for bug fix: see: http://forums.java.net/node/808052 - need to adjust timeout....
                Thread.sleep(Constants.SEOMOZ_SERVER_DELAY);    

                response = urlMetricsService.getUrlMetrics(urlList.get(i).getUu(), authorityBitMask);

                if (!response.isEmpty()) {// replace below if clause with this, to make more efficient... 
                    res = gson.fromJson(response, UrlResponse.class);


                    // remove the digits after the decimal point
                    stringPA = removepostDecimalDigits(res.getUpa());
                    stringDA = removepostDecimalDigits(res.getPda());

                    urlList.get(i).setBackLinkPA(stringPA);
                    urlList.get(i).setBackLinkDA(stringDA);

                    //////////////
                    //logging
                    currentURL = urlList.get(i).getUu();
                    logString = i + " out of " + urlListSize + " -BackLink PA and DA data got for " + currentURL + " DA = " + stringDA + " PA = " + stringPA;
                    log.info(logString);
                    //
                    //////////////////
                    
                    /////////
                    //TODO: to prevent loss of all data, write to disk every time we have another 10 rows
                    if(0== i%10){
                        
                    }
                    
                    //
                    /////////
                } else { // res == null - problem getting response from SEOMoz server - then remove this entry from linked list

                    urlListSize--;
                    //////////////
                    //logging
                    currentURL = urlList.get(i).getUu();
                    logString = i + " deleting: " + currentURL + " - due to error getting data from SEOMoz and reducing no of links to " + urlListSize;
                    log.info(logString);
                    //
                    //////////////////

                    urlList.remove(i);
                    i--; // make sure we get the next entry's DA and PA


                }


            }
        } catch (InterruptedException ex) { // from: http://stackoverflow.com/questions/9139128/a-sleeping-thread-is-getting-interrupted-causing-loss-of-connection-to-db
            Thread.currentThread().interrupt(); // restore interrupted status
            errorMsg = SEOMozWrapper.TAG + " threw " + ex.getMessage();
            log.info(errorMsg);
            ex.printStackTrace();
        } catch (Exception e) {
            errorMsg = SEOMozWrapper.TAG + " threw " + e.getMessage();
            log.info(errorMsg);
            e.printStackTrace();
        }

    }

  
    /**
     * 
     * @param stringToClean - decimal number as a string.
     * @return integer version of the number passed - as string.
     */
    private String removepostDecimalDigits(String stringToClean) {

        String cleanString;

        int indexOfDecPoint = stringToClean.indexOf('.');
        if (indexOfDecPoint > 0) // in case there's no decimal place
        {
            cleanString = stringToClean.substring(0, indexOfDecPoint);
        } else {
            cleanString = stringToClean;
        }

        return cleanString;
    }

    /**
     *   
     * assigns red green or amber to backlink - at the moment, based on its DA
     * proxy for algo scoring - move this functionality out of this class, and
     * set up something clever based on server scalping and APIing backlinks for
     * a page rank like score..????
     *
     * @param urlList - ArrayList of URLs to be assigned a RGA score.
     * @param setAllColoursAsAmber - if true, then all links will be scored Amber, else if false - they will be scored green if their DA > 66, 
     * 																							 - amber if DA < 66 && > 33
     * 																							 - red if DA < 33.
     */
    public void performScoring(ArrayList<URLPlusDataPoints> urlList, boolean setAllColoursAsAmber) {

        int i = 0; // declared here so it can be used for error reporting o/s the for loop...

        try {

            for (; i < urlList.size(); i++) {

                if (setAllColoursAsAmber) {
                    urlList.get(i).setScore("amber");
                } else {
                    // implement some other scoring process here....
                    float domainAuth = Float.parseFloat(urlList.get(i).getBackLinkDA());

                    if (domainAuth > 66) {
                        urlList.get(i).setScore("green");
                    } else if (domainAuth <= 66 && domainAuth > 33) {
                        urlList.get(i).setScore("amber");
                    } else {
                        urlList.get(i).setScore("red");
                    }

                }
            }
        } catch (Exception e) {
            errorMsg = SEOMozWrapper.TAG + " threw " + e.getMessage();
            errorMsg += "\n No: " + i + " URL that caused the error: " + urlList.get(i).getBackLinkURL();
            log.info(errorMsg);
            e.printStackTrace();
        }
    }

    /*
     * Arranges uniqueDomains array by DA, then divides it into equal ranges
     * based on noOfUniqDom, selecting one link from each unique domain from
     * each range of DAs and places into "selectLinksDAPA"
     */
    public ArrayList<URLPlusDataPoints> fillSelLinksUniqDomRange(int noOfUniqDomRequired) {

        float count, step;
        int index;
        // sets the width of ranges / no of frames that we divide the array into to get 
        //  equally dispersed selection of links from....

        noOfUniqueDomains = uniqueDomainsList.size(); // make sure we're up to date

        if (0 == noOfUniqueDomains) {
            noOfUniqueDomains = uniqueDomainsList.size();
        }

        if (noOfUniqueDomains < noOfUniqDomRequired) {
            noOfUniqDomRequired = noOfUniqueDomains;
            step = 1;

        } else {
            //as integer for array access below
            step = (float) noOfUniqueDomains / noOfUniqDomRequired;
        }

        count = 0;

        // sort by ascending DA.... 
        Collections.sort(uniqueDomainsList);

        //clear out selected links array
        selectLinksList.clear();

        // run though the unique domains, selecting a link from each "frame"
        for (int i = 0; i < noOfUniqDomRequired; i++) {

            index = Math.round(count);

            //check for array out of bounds with the rounding up of last count
            if (index > uniqueDomainsList.size()) {
                index = uniqueDomainsList.size();
            }
            selectLinksList.add(uniqueDomainsList.get(Math.round(index)));
 
            count += step;
        }

        return selectLinksList;

    }
}
