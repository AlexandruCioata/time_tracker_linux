import org.apache.log4j.Logger;
import oscommons.IOSType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by admin on 11/21/16.
 */
public class UserInteractionService implements Runnable {

    private IOSType osType = null;
    private long timeout = 3000;

    private static long INACTIVITY_TIMEOUT = 5000;

    private String scriptPath = "";
    private String outputFolderPath = "";
    private String outputIdleFilename = "";

    private static ConcurrentLinkedDeque<DataCollectionStructure> synchronizedCollectedResults;

    public static volatile boolean isStopped;

    AppConfig configuration = null;

    private final static Logger logger = Logger.getLogger(UserInteractionService.class);

    public UserInteractionService(IOSType type,
                               AppConfig configuration)
    {
        this.osType = type;
        this.configuration = configuration;

        //todo:
        this.scriptPath = this.configuration.getUserInteractionIdleScriptPath();
        this.outputFolderPath = this.configuration.getImagesLocalRootFolder();
        this.outputIdleFilename = this.configuration.getUserInteractionIdleOutputFilename();

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
                    getUserIdleTime(scriptPath,outputFolderPath,outputIdleFilename);
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
                    logger.error("Exception in UserInteractionService: ", e);
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

    public void getUserIdleTime(String scriptPath, String outputFolderPath, String outputIdleFilename) throws Exception
    {
        //String outputLine = this.osType.executeCommandsFromScriptAndPrintOutput(scriptPath, null);
        String outputLine = this.osType.getUserIdleTime(scriptPath, outputFolderPath, outputIdleFilename);

        String output = "";
        Integer lastActiveTime = Integer.parseInt(outputLine);
        if(lastActiveTime < INACTIVITY_TIMEOUT)
        {
            output = "ACTIVE";
        }
        else
        {
            output = "INACTIVE";
        }

        String last = "";
        if(synchronizedCollectedResults.size() > 0)
        {
            last = synchronizedCollectedResults.getLast().data;
        }

        if(last.equals(output))
        {
            synchronizedCollectedResults.getLast().counter++;
        }
        else
        {
            synchronizedCollectedResults.addLast(new DataCollectionStructure(output,0));
        }
        //TODO:


        writeDataToFile(outputFolderPath + "/" + outputIdleFilename, output);
    }

    private static void writeDataToFile(String outputFilename, String stringData) throws Exception
    {
        File outputFile = new File(outputFilename);
        BufferedWriter bufferedWriter = null;

        if(outputFile.exists())
        {
            bufferedWriter = new BufferedWriter(new FileWriter(outputFile,true));
        }
        else
        {
            bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
        }

        bufferedWriter.write(stringData + "\r\n");

        bufferedWriter.close();
    }


    public static void stop()
    {
        isStopped = true;
    }


}
