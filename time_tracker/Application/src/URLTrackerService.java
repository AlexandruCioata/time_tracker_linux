import org.apache.log4j.Logger;
import oscommons.IOSType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by admin on 11/23/16.
 */
public class URLTrackerService implements Runnable {

    private IOSType osType = null;
    private long timeout = 3000;

    private String scriptPath = "";
    private String outputFolderPath = "";
    private String outputURLFilename = "";

    private static ConcurrentLinkedDeque<DataCollectionStructure> synchronizedCollectedResults;

    public static volatile boolean isStopped;

    AppConfig configuration = null;

    private final static Logger logger = Logger.getLogger(URLTrackerService.class);

    public URLTrackerService(IOSType type,
                               AppConfig configuration)
    {
        this.osType = type;
        this.configuration = configuration;

        //todo:
        this.scriptPath = this.configuration.getGetVisitedSiteScriptPath();
        this.outputFolderPath = this.configuration.getImagesLocalRootFolder();
        this.outputURLFilename = this.configuration.getVisitedSitesFilename();

        synchronizedCollectedResults = new ConcurrentLinkedDeque<>();

        isStopped = false;
    }

    public void run()
    {

        if(osType!=null)
        {
            while(!isStopped)
            {
                try
                {
                    getURL(scriptPath, outputFolderPath, outputURLFilename);
                }
                catch(Exception e)
                {
                    logger.error("Exception in UserInteractionService: ", e);
                }

                try
                {
                    Thread.sleep(timeout);
                }
                catch(Exception e)
                {
                    logger.error("Exception in AppAndSites: ", e);
                }
            }
        }
        else
        {
            logger.error("osTYpe is null!");
        }
    }


    public static ConcurrentLinkedDeque<DataCollectionStructure> getDataAndResetCollector()
    {
        ConcurrentLinkedDeque<DataCollectionStructure> result = new ConcurrentLinkedDeque<>();

        result.addAll(synchronizedCollectedResults);
        synchronizedCollectedResults.clear();

        return result;
    }

    public void getURL(String scriptPath, String outputFolderPath, String outputAppsFilename) throws Exception
    {

        String outputLine = this.osType.getActiveURL(scriptPath,null);
        System.out.println(outputLine);

        String last = "";
        if(synchronizedCollectedResults.size() > 0)
        {
            last = synchronizedCollectedResults.getLast().data;
        }

        if(last.equals(outputLine))
        {
            synchronizedCollectedResults.getLast().counter++;
        }
        else
        {
            synchronizedCollectedResults.addLast(new DataCollectionStructure(outputLine,0));
        }

        //TODO:
        File outputFile = new File(outputFolderPath + "/" + outputAppsFilename);

        try{

            BufferedWriter bufferedWriter = null;

            if(outputFile.exists())
            {
                bufferedWriter = new BufferedWriter(new FileWriter(outputFile,true));
            }
            else
            {
                bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            }

            bufferedWriter.write(outputLine + "\r\n");
            bufferedWriter.close();
        }
        catch(Exception e)
        {
            logger.error(e);
        }
    }


    public String preprocessCurrentAppName(String line)
    {
        String result = line;

        //0x04800002  0 15665  mihai-To-be-filled-by-O-E-M Google - Google Chrome
        String[] parts = line.split(" +");
        if(parts.length > 4)
        {
            String lastPart = parts[4];

            result = line.substring(line.indexOf(lastPart));
        }

        //System Settings
        //java - Splitting a string with multiple spaces - Stack Overflow - Google Chrome
        parts = result.split("-");
        if(parts.length > 1)
        {
            result = result.substring(result.indexOf(parts[parts.length-1]));
        }

        result = result.trim();

        return result;
    }

    public static void stop()
    {
        isStopped = true;
    }

}
