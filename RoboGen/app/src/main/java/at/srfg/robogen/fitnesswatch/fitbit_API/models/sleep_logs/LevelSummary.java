package at.srfg.robogen.fitnesswatch.fitbit_API.models.sleep_logs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LevelSummary {

    @SerializedName("deep")
    @Expose
    private SummaryDetails deep;

    @SerializedName("light")
    @Expose
    private SummaryDetails light;

    @SerializedName("rem")
    @Expose
    private SummaryDetails rem;

    @SerializedName("wake")
    @Expose
    private SummaryDetails wake;


    public SummaryDetails getDeep() {
        return deep;
    }

    public void setDeep(SummaryDetails deep) {
        this.deep = deep;
    }

    public SummaryDetails getLight() {
        return light;
    }

    public void setLight(SummaryDetails light) {
        this.light = light;
    }

    public SummaryDetails getRem() {
        return rem;
    }

    public void setRem(SummaryDetails rem) {
        this.rem = rem;
    }

    public SummaryDetails getWake() {
        return wake;
    }

    public void setWake(SummaryDetails wake) {
        this.wake = wake;
    }
}
