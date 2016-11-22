import org.apache.log4j.Logger;
import oscommons.IOSType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by admin on 11/22/16.
 */
public class WindowTitleTracker implements Runnable {

    private IOSType osType = null;
    private long timeout = 3000;

    private String scriptPath = "";
    private String outputFolderPath = "";
    private String outputTitlesFilename = "";

    public static volatile boolean isStopped;

    AppConfig configuration = null;

    private final static Logger logger = Logger.getLogger(WindowTitleTracker.class);

    public WindowTitleTracker(IOSType type,
                               AppConfig configuration)
    {
        this.osType = type;
        this.configuration = configuration;

        //todo:
        this.scriptPath = this.configuration.getGetFocusedWindowTitleScriptPath();
        this.outputFolderPath = this.configuration.getImagesLocalRootFolder();
        this.outputTitlesFilename = this.configuration.getGetWindowTitleFilename();

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
                    getActiveWindowTitle(scriptPath, outputFolderPath, outputTitlesFilename);
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

    public void getActiveWindowTitle(String scriptPath, String outputFolderPath, String outputAppsFilename)
    {
        String outputLine = this.osType.getActiveWindowTitle(scriptPath, null);

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
