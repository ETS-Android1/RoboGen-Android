package at.srfg.robogen.fitnesswatch.fitbit_API.models.heartrate_intraday;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivitiesHeart {

    @SerializedName("dateTime")
    @Expose
    private String dateTime;

    @SerializedName("value")
    @Expose
    private Value value;


    /**
     * @return The dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime The dateTime
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @return The value
     */
    public Value getValue() {
        return value;
    }

    /**
     * @param value The value
     */
    public void setValue(Value value) {
        this.value = value;
    }
}