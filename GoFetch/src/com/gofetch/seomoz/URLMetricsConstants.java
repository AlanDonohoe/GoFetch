package com.gofetch.seomoz;

/**
 * 
 * A constants class for URLMetrics Service
 * 
 * @author Radeep Solutions / Alan Donohoe
 */
public class URLMetricsConstants 
{
	public static final long URLMETRICS_COL_ALL = 0L;
	public static final long URLMETRICS_COL_TITLE = 1L;
	public static final long URLMETRICS_COL_URL = 4L;
	public static final long URLMETRICS_COL_SUBDOMAIN = 8L;
	public static final long URLMETRICS_COL_ROOT_DOMAIN = 16L;
	public static final long URLMETRICS_COL_EXTERNAL_LINKS = 32L;
	public static final long URLMETRICS_COL_SUBDMN_EXTERNAL_LINKS = 64L;
	public static final long URLMETRICS_COL_ROOTDMN_EXTERNAL_LINKS = 128L;
	public static final long URLMETRICS_COL_JUICE_PASSING_LINKS = 256L;
	public static final long URLMETRICS_COL_SUBDMN_LINKS = 512L;
	public static final long URLMETRICS_COL_ROOTDMN_LINKS = 1024L;
	public static final long URLMETRICS_COL_LINKS = 2048L;
	public static final long URLMETRICS_COL_SUBDMN_SUBDMN_LINKS = 4096L;
	public static final long URLMETRICS_COL_ROOTDMN_ROOTDMN_LINKS = 8192L;
	public static final long URLMETRICS_COL_MOZRANK = 16384L;
	public static final long URLMETRICS_COL_SUBDMN_MOZRANK = 32768L;
	public static final long URLMETRICS_COL_ROOTDMN_MOZRANK = 65536L;
	public static final long URLMETRICS_COL_MOZTRUST = 131072L;
	public static final long URLMETRICS_COL_SUBDMN_MOZTRUST = 262144L;
	public static final long URLMETRICS_COL_ROOTDMN_MOZTRUST = 524288L;
	public static final long URLMETRICS_COL_EXTERNAL_MOZRANK = 1048576L;
	public static final long URLMETRICS_COL_SUBDMN_EXTERNALDMN_JUICE = 2097152L;
	public static final long URLMETRICS_COL_ROOTDMN_EXTERNALDMN_JUICE = 4194304L;
	public static final long URLMETRICS_COL_SUBDMN_DOMAIN_JUICE = 8388608L;
	public static final long URLMETRICS_COL_ROOTDMN_DOMAIN_JUICE = 16777216L;
	public static final long URLMETRICS_COL_CANONICAL_URL = 268435456L;
	public static final long URLMETRICS_COL_HTTP_STATUS_CODE = 536870912L;
	public static final long URLMETRICS_COL_LINKS_TO_SUBDMN = 4294967296L;
	public static final long URLMETRICS_COL_LINKS_TO_ROOTDMN = 8589934592L;
	public static final long URLMETRICS_COL_ROOTDMN_LINKS_SUBDMN = 17179869184L;
	public static final long URLMETRICS_COL_PAGE_AUTHORITY = 34359738368L;
	public static final long URLMETRICS_COL_DOMAIN_AUTHORITY = 68719476736L;
	
	// variations of the above summed:
    //url-metrics param values:
    public static final Long URL_METRICS_ALL_FREE_PARAMS = 103616137253L; // this sets the cols param value of the url-metrics call to get all possible free data back
    public static final Long URL_METRICS_PA_DA_TITLE_NOOFEXTLINKS = 103616086049L; // = pa, da, no of external links, HTTP Status Code, Title,
    public static final Long URL_METRICS_PA_DA  = 103079215104L; //= just pa and da
    //public static final Long URL_METRICS_NOOFEXTLINKS = 32L;
}
