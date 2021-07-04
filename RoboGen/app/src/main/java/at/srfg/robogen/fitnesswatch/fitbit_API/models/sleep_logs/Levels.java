package at.srfg.robogen.fitnesswatch.fitbit_API.models.sleep_logs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Levels {

    @SerializedName("summary")
    @Expose
    private LevelSummary summary;

    @SerializedName("data")
    @Expose
    private List<LevelData> data = new ArrayList<LevelData>();

    @SerializedName("shortData")
    @Expose
    private List<LevelData> shortData = new ArrayList<LevelData>();


    public LevelSummary getSummary() {
        return summary;
    }

    public void setSummary(LevelSummary summary) {
        this.summary = summary;
    }

    public List<LevelData> getData() {
        return data;
    }

    public void setData(List<LevelData> data) {
        this.data = data;
    }

    public List<LevelData> getShortData() {
        return shortData;
    }

    public void setShortData(List<LevelData> shortData) {
        this.shortData = shortData;
    }
}
