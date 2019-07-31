package at.srfg.robogen.fitnesswatch.fitbit_API.models.heartrate_intraday;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("customHeartRateZones")
    @Expose
    private HeartRateZone[] customHeartRateZones;

    @SerializedName("heartRateZones")
    @Expose
    private HeartRateZone[] heartRateZones;


    /**
     * @return The customHeartRateZones
     */
    public HeartRateZone[] getCustomHeartRateZones() {
        return customHeartRateZones;
    }

    /**
     * @param customHeartRateZones The customHeartRateZones
     */
    public void setCustomHeartRateZones(HeartRateZone[] customHeartRateZones) {
        this.customHeartRateZones = customHeartRateZones;
    }

    /**
     * @return The heartRateZones
     */
    public HeartRateZone[] getHeartRateZones() {
        return heartRateZones;
    }

    /**
     * @param heartRateZones The heartRateZones
     */
    public void setHeartRateZones(HeartRateZone[] heartRateZones) {
        this.heartRateZones = heartRateZones;
    }
}
