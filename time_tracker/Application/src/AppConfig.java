import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

/**
 * Created by mihai on 9/2/2016.
 */
public class AppConfig{

    private int computerNumber = -1;

    private int maxNoScreenshotsStored = -1;

    private String imagesLocalRootFolder = "";

    //maximum time (s) accepted for no interaction with the pc
    private long interactionWithPCTimeOut = -1;

    public String getVisitedSitesFilename() {
        return visitedSitesFilename;
    }

    public String getAccessedAppsFilename() {
        return accessedAppsFilename;
    }

    public String getGetFocusedApplicationScriptPath() {
        return getFocusedApplicationScriptPath;
    }

    private String visitedSitesFilename= "";

    public String getGetVisitedSiteScriptPath() {
        return getVisitedSiteScriptPath;
    }

    private String getVisitedSiteScriptPath= "";

    private String getFocusedApplicationScriptPath= "";
    private String accessedAppsFilename= "";



    private String getFocusedWindowTitleScriptPath= "";
    private String getWindowTitleFilename= "";


    private String adminPassword = "";


    private String updatesManagerAppName="";

    private long TIMEOUT_UPLOAD_LOG_FILES = 0;


    private String remoteRootLogPath = "";
    private String logFileName = "";


    //pc interaction info
    private String userInteractionIdleScriptPath = "";
    private String userInteractionIdleOutputFilename = "";

    //private FTPCredentials credentials;

    private final static Logger logger = Logger.getLogger(AppConfig.class);

    public AppConfig(Properties properties)
    {


        if(properties == null || properties.isEmpty())
        {
            logger.error("There are no properties or properties is empty!");
            return;
        }

        /*
         * set application parameters from these properties
         * */
        try
        {
            computerNumber = Integer.parseInt(
                    properties.getProperty("computerNumber").trim());
        }
        catch(Exception e)
        {
            System.out.println("Please insert a positive integer in configuration file for the computer number");
            e.printStackTrace();

            logger.error("Please insert a positive integer in configuration file for the computer number",e);

            System.exit(0);
        }

        try
        {
            maxNoScreenshotsStored = Integer.parseInt(
                    properties.getProperty("maxNoScreenshotsStored").trim());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.error("this.properties.getProperty(\"maxNoScreenshotsStored\").trim());\n",e);

            maxNoScreenshotsStored = 5000;
        }

        imagesLocalRootFolder = properties.getProperty("imagesLocalRootFolder");

        try
        {
            interactionWithPCTimeOut = Integer.parseInt(
                    properties.getProperty("interactionWithPCTimeOut").trim());
        }
        catch(Exception e)
        {
            e.printStackTrace();

            logger.error("Exception in MainApplication -> ",e);

            interactionWithPCTimeOut = 60;
        }

        visitedSitesFilename = properties.getProperty("visitedSitesFilename");
        getVisitedSiteScriptPath = properties.getProperty("getVisitedSiteScriptPath");

        getFocusedApplicationScriptPath = properties.getProperty("getFocusedApplicationScriptPath");
        accessedAppsFilename = properties.getProperty("accessedAppsFilename");

        getFocusedWindowTitleScriptPath = properties.getProperty("getFocusedWindowTitleScriptPath");
        getWindowTitleFilename = properties.getProperty("getWindowTitleFilename");

        userInteractionIdleScriptPath = properties.getProperty("userInteractionIdleScriptPath");
        userInteractionIdleOutputFilename = properties.getProperty("userInteractionIdleOutputFilename");


        /*
        * Uploader configuration
        * */
        try
        {
            TIMEOUT_UPLOAD_LOG_FILES = Integer.parseInt(
                    properties.getProperty("TIMEOUT_UPLOAD_LOG_FILES").trim());
        }
        catch(Exception e)
        {
            e.printStackTrace();

            logger.error("Exception -> ",e);

            TIMEOUT_UPLOAD_LOG_FILES = 3600;
        }

        remoteRootLogPath = properties.getProperty("remoteRootLogPath");
        logFileName = properties.getProperty("logFileName");

    }

    public static void main(String[] args) {

        String log4jConfigFilename = "log4j.properties";
        PropertyConfigurator.configure(log4jConfigFilename);

        Properties prop = MainApplication.readProperties("appConfig.properties");

        AppConfig config = new AppConfig(prop);


        config.displayAllConfigs();

    }


    private void displayAllConfigs()
    {
        System.out.println(computerNumber);
        System.out.println(maxNoScreenshotsStored);
        System.out.println(imagesLocalRootFolder);

        System.out.println(interactionWithPCTimeOut);
        System.out.println(adminPassword);

        System.out.println(TIMEOUT_UPLOAD_LOG_FILES);
        System.out.println(remoteRootLogPath);
        System.out.println(logFileName);

/*
        System.out.println("credentials");
        System.out.println(credentials.user);
        System.out.println(credentials.pass);
        System.out.println(credentials.server);
        System.out.println(credentials.port);
*/

    }


    public int getComputerNumber() {
        return computerNumber;
    }

    public int getMaxNoScreenshotsStored() {
        return maxNoScreenshotsStored;
    }

    public String getImagesLocalRootFolder() {
        return imagesLocalRootFolder;
    }

    public long getInteractionWithPCTimeOut() {
        return interactionWithPCTimeOut;
    }


    public String getAdminPassword() {
        return adminPassword;
    }


/*    public FTPCredentials getCredentials() {
        return credentials;
    }*/

    public long getTIMEOUT_UPLOAD_LOG_FILES() {
        return TIMEOUT_UPLOAD_LOG_FILES;
    }

    public String getRemoteRootLogPath() {
        return remoteRootLogPath;
    }

    public String getLogFileName() {
        return logFileName;
    }


    public String getUpdatesManagerAppName() {
        return updatesManagerAppName;
    }

    public String getUserInteractionIdleOutputFilename() {
        return userInteractionIdleOutputFilename;
    }

    public String getUserInteractionIdleScriptPath() {
        return userInteractionIdleScriptPath;
    }

    public String getGetFocusedWindowTitleScriptPath() {
        return getFocusedWindowTitleScriptPath;
    }

    public String getGetWindowTitleFilename() {
        return getWindowTitleFilename;
    }
}
