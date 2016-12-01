package communication;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by admin on 11/23/16.
 */
public class GlobalDataCollector implements Serializable{

    private ConcurrentLinkedDeque<DataCollectionStructure> applicationTrackerData;
    private ConcurrentLinkedDeque<DataCollectionStructure> windowTitleServiceData;
    private ConcurrentLinkedDeque<DataCollectionStructure> interactionTimeServiceData;
    private ConcurrentLinkedDeque<DataCollectionStructure> urlTrackerData;
    private byte[] takenScreenshotAsByteArray;

    public GlobalDataCollector(Builder builder)
    {
        this.applicationTrackerData = builder.applicationTrackerData;
        this.windowTitleServiceData = builder.windowTitleServiceData;
        this.takenScreenshotAsByteArray = builder.takenScreenshotAsByteArray;
        this.interactionTimeServiceData = builder.interactionTimeServiceData;
        this.urlTrackerData = builder.urlTrackerData;
    }

    public static class Builder
    {
        public ConcurrentLinkedDeque<DataCollectionStructure> applicationTrackerData = null;
        public ConcurrentLinkedDeque<DataCollectionStructure> windowTitleServiceData = null;
        private ConcurrentLinkedDeque<DataCollectionStructure> interactionTimeServiceData = null;
        private ConcurrentLinkedDeque<DataCollectionStructure> urlTrackerData = null;
        public byte[] takenScreenshotAsByteArray = null;

        public Builder setApplicationData(
                ConcurrentLinkedDeque<DataCollectionStructure> appData)
        {
            this.applicationTrackerData = appData;
            return this;
        }

        public Builder setWindowTitleServiceData(
                ConcurrentLinkedDeque<DataCollectionStructure> titleData)
        {
            this.windowTitleServiceData = titleData;
            return this;
        }

        public Builder setTakenScreenshotData(
                byte[] screenshotData)
        {
            this.takenScreenshotAsByteArray = screenshotData;
            return this;
        }

        public Builder setInteractionTimeServiceData(
                ConcurrentLinkedDeque<DataCollectionStructure> interactionTimeServiceData)
        {
            this.interactionTimeServiceData = interactionTimeServiceData;
            return this;
        }

        public Builder setURLTrackerServiceData(
                ConcurrentLinkedDeque<DataCollectionStructure> urlTrackerData)
        {
            this.urlTrackerData = urlTrackerData;
            return this;
        }

        public GlobalDataCollector build()
        {
            return new GlobalDataCollector(this);
        }

    }

    public byte[] getTakenScreenshotAsByteArray()
    {
        return takenScreenshotAsByteArray;
    }

    @Override
    public String toString()
    {
        String result = "";

        result += takenScreenshotAsByteArray.toString() + "\r\n" +
                applicationTrackerData.toString()  + "\r\n" +
                windowTitleServiceData.toString()  + "\r\n" +
                interactionTimeServiceData.toString()  + "\r\n" +
                urlTrackerData.toString()  + "\r\n";

        return result;
    }


}
