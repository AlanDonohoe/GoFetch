package com.gofetch.seomoz;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Performs helpful text parsing on url, img and anchor text that would often cause problems in program...
 * 
 * @author alandonohoe
 */
public class TextParser {

    public static final String TAG = "TextParser"; // used for logging errors
    private static final Logger log = Logger.getLogger(TAG); // used for logging errors
    private String errorMsg;                            // used for logging errors

    public static String getDomainNameFromURL(String url) {

        int firstDot, firstSlash;

        firstDot = url.indexOf('.');
        firstDot++;

        firstSlash = url.indexOf('/', firstDot);

        return url.substring(firstDot, firstSlash);
    }


    /*
     * removes any \n and \r with ""
     */
    public static String removeFormatting(String text) {

        String cleanText = text.replaceAll("\n", "");
        cleanText = cleanText.replaceAll("\r", "");

        return cleanText;
    }

    /*
     * removes any "," with ""
     */
    public static String removeCommaFromText(String text) {

        String cleanText = text.replaceAll(",", "");

        return cleanText;
    }

    /*
     * removes any " from text:
     */
    public static String removeQuotationMarks(String text) {

        String cleanText = text.replaceAll("\"", "");

        return cleanText;
    }

    /*
     * replaces any verbose img anchor text with tidy: "(img) [No Anchor Text]"
     * and removes and commas and \n and reduces its size down to 30 chars if
     * longer than this....
     */
    public static String tidyImgAnchorText(String imgText) {

        if (imgText.contains("<img")) {
            return ("(img) [No Anchor Text]");
        } else {

            String cleanText = removeCommaFromText(imgText);
            cleanText = removeFormatting(cleanText);
            cleanText = removeQuotationMarks(cleanText);

            if (cleanText.length() > 30) {
                cleanText = cleanText.substring(0, 29);
            }

            return cleanText;
        }

    }

    /*
     * if theres no http:// prefix on beginning of url - then this adds it
     */
    public static String addHTTPPrefix(String preText) {
        
        String postText = null;
        
        if(preText.startsWith("http")){
            return preText;
        }else{
            postText = "http://" +  preText;
            return postText;
        }
            
    }

}
