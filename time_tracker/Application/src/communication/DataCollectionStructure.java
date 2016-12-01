package communication;

import java.io.Serializable;

/**
 * Created by admin on 11/22/16.
 */
public class DataCollectionStructure implements Serializable {

    public String data;
    public int counter;

    public DataCollectionStructure(String data, int counter)
    {
        this.data = data;
        this.counter = counter;
    }

    @Override
    public String toString()
    {
        return data + " -> " + counter;
    }


}
