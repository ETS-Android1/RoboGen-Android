package at.srfg.robogen.fitnesswatch.fitbit_API.models.heartrate_intraday;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesHeartIntra {

    @SerializedName("dataset")
    @Expose
    private Dataset[] dataset;

    @SerializedName("datasetInterval")
    @Expose
    private Integer datasetInterval;

    @SerializedName("datasetType")
    @Expose
    private String datasetType;



    /**
     * @return The activities
     */
    public Dataset[] getActivities() {
        return dataset;
    }

    /**
     * @param dataset The activities
     */
    public void setActivities(Dataset[] dataset) {
        this.dataset = dataset;
    }


    /**
     * @return The goals
     */
    public Integer getDatasetInterval() {
        return datasetInterval;
    }

    /**
     * @param interval The goals
     */
    public void setDatasetInterval(Integer interval) {
        this.datasetInterval = interval;
    }


    /**
     * @return The goals
     */
    public String getDatasetType() {
        return datasetType;
    }

    /**
     * @param type The goals
     */
    public void setDatasetType(String type) {
        this.datasetType = type;
    }
}
