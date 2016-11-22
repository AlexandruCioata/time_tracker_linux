import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import oscommons.IOSType;
import oscommons.OSFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * Created by mihai on 4/12/16.
 */
public class MainApplication {

    public static String uniqueFileName ="";

    private static boolean isStarted = false;

    private AppConfig configuration = null;

    private IOSType OSType = null;


    //TESTING!!


    private final static Logger logger = Logger.getLogger(MainApplication.class);

    public MainApplication()
    {
        /* Get application configuration properties
         from configuration file provided in command line arguments
        */
        String configFile = "/home/admin/google_drive/Birou/workspace/time_tracker_linux/time_tracker/appConfig.properties";
        Properties prop = readProperties(configFile);

        /*
        * Load log4j configuration
        * from the same config properties of application
        * */
        PropertyConfigurator.configure(configFile);

        if(prop == null || prop.isEmpty())
        {
            System.out.println("There is no properties in the configuration file provided");
            logger.warn("There is no properties in the configuration file provided");

            return;
        }

        /*
        * Build AppConfig class from loaded properties
        * */
        this.configuration = new AppConfig(prop);

        /*
        * Choosing local operating system type
        * */
        String operatingSystem = System.getProperty("os.name");
        OSFactory osFactory = new OSFactory(this.configuration.getAdminPassword());

        this.OSType = osFactory.createOSType(operatingSystem);

    }

    public MainApplication(Properties properties)
    {
        /*
        * Build AppConfig class from loaded properties
        * */
        this.configuration = new AppConfig(properties);

        /*
        * Choosing local operating system type
        * */
        String operatingSystem = System.getProperty("os.name");
        OSFactory osFactory = new OSFactory(this.configuration.getAdminPassword());

        this.OSType = osFactory.createOSType(operatingSystem);



    }

    public static Properties readProperties(String configFile)
    {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(configFile);

            // load a properties file
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();

            logger.error("Exception in MainApplication->readProperties -> ",ex);

        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return prop;
    }

    public void startServices()
    {
        if (!isStarted)
        {
            initApplication();
            run();

            isStarted = true;
        }
    }

    public void stopServices()
    {
        ApplicationsTracker.stop();
        ScreenshotManager.stop();
        UserInteractionService.stop();
        WindowTitleTracker.stop();

        isStarted = false;
    }

    public void getData()
    {
        ConcurrentLinkedDeque<DataCollectionStructure> results = new ConcurrentLinkedDeque<>();


        results.addAll(ApplicationsTracker.getDataAndResetCollector());


        String data = results.toString();

        try
        {
            writeDataToFile(this.configuration.getImagesLocalRootFolder() + "/ceva.txt", data);
        }
        catch(Exception e)
        {
            logger.error("Exception: ",e);
        }
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

    public static void main(String args[]) throws Exception
    {

        if(args.length == 0)
        {
            System.out.println("Incorrect arguments! " +
                    "Please give the configuration file path " +
                    "as the first parameter at command line");

            return;
        }


        /* Get application configuration properties
         from configuration file provided in command line arguments
        */
        String configFile = args[0];
        Properties prop = readProperties(configFile);

        /*
        * Load log4j configuration
        * from the same config properties of application
        * */
        PropertyConfigurator.configure(configFile);

        if(prop == null || prop.isEmpty())
        {
            System.out.println("There is no properties in the configuration file provided");
            logger.warn("There is no properties in the configuration file provided");

            return;
        }

        MainApplication application = new MainApplication(prop);

        application.initApplication();
        application.run();
    }

    public void run()
    {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        /*
        * function responsible for tracking the current application
        * running on the computer
        * */
        startApplicationsTrackerService(executor);

        /*
        * function responsible for tracking the current active window title
        * */
        //startWindowTitleTrackerService(executor);

        /*
        * function responsible for checking the user interaction time
        * with the pc
        * */
        //startUserInteractionService(executor);

        /*
        * take screenshots and save them locally
        * */
        //startScreenshotManagerService(executor);

        executor.shutdown();
    }

    public void startWindowTitleTrackerService(ThreadPoolExecutor executor)
    {

        //TODO:
        WindowTitleTracker windowTitleTracker = new WindowTitleTracker(this.OSType, configuration);

        logger.info("execute.windowTitleTracker..");
        executor.execute(windowTitleTracker );
    }

    public void startScreenshotManagerService(ThreadPoolExecutor executor)
    {
        ScreenshotManager screenshotManager = new ScreenshotManager(configuration);

        logger.info("execute.screenshotManager..");
        executor.execute(screenshotManager);
    }

    public void startApplicationsTrackerService(ThreadPoolExecutor executor)
    {
        ApplicationsTracker applicationsTracker = new ApplicationsTracker(this.OSType,configuration);

        logger.info("execute.applicationsTracker..");
        executor.execute(applicationsTracker);

    }

    public void startUserInteractionService(ThreadPoolExecutor executor)
    {
        UserInteractionService userInteractionService = new UserInteractionService(this.OSType,configuration);

        logger.info("execute.userInteractionService..");
        executor.execute(userInteractionService);
    }

    private void generateUniqueComputerName()
    {
        if(this.configuration.getComputerNumber()>0)
        {
            uniqueFileName = "computer_" + this.configuration.getComputerNumber();
        }
        else
        {
            System.out.println("Incorrect computer number! This number should be a positive Integer");
            logger.error("MainApplication -> generateUniqueComputerName: " +
                    "Incorrect computer number! This number should be a positive Integer!");

            System.exit(0);
        }

    }

    private void initApplication()
    {
        logger.info("MainApplication.generateUniqueComputerName()..");
        generateUniqueComputerName();

        //make if not already exists a root cache folder
        File imagesRootFolder = new File(this.configuration.getImagesLocalRootFolder());
        if(!imagesRootFolder.exists())
        {
            imagesRootFolder.mkdir();
        }
    }



}
