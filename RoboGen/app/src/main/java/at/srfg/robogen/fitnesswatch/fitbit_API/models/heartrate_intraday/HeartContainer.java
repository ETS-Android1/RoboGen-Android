package at.srfg.robogen.fitnesswatch.fitbit_API.models.heartrate_intraday;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeartContainer {

    @SerializedName("activities-heart")
    @Expose
    private ActivitiesHeart[] actHeart;

    @SerializedName("activities-heart-intraday")
    @Expose
    private ActivitiesHeartIntra actHeartIntra;


    /**
     * @return The heart activity
     */
    public ActivitiesHeart[] getActivityHeart() {
        return actHeart;
    }

    /**
     * @param heart The heart activity
     */
    public void setActivityHeart(ActivitiesHeart[] heart) {
        this.actHeart = heart;
    }


    /**
     * @return The heartIntra activity
     */
    public ActivitiesHeartIntra getActivityHeartIntra() {
        return actHeartIntra;
    }

    /**
     * @param heartIntra The heartIntra activity
     */
    public void setActivityHeartIntra(ActivitiesHeartIntra heartIntra) { this.actHeartIntra = heartIntra; }
}
