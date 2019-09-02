package at.srfg.robogen.fitnesswatch.fitbit_API.models.sleep_logs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sleep {

    @SerializedName("dateOfSleep")
    @Expose
    String dateOfSleep;

    @SerializedName("duration")
    @Expose
    Integer duration;

    @SerializedName("efficiency")
    @Expose
    Integer efficiency;

    @SerializedName("levels")
    @Expose
    Levels levels;

    @SerializedName("logId")
    @Expose
    Long logId;

    @SerializedName("minutesAfterWakeup")
    @Expose
    Integer minutesAfterWakeup;

    @SerializedName("minutesAsleep")
    @Expose
    Integer minutesAsleep;

    @SerializedName("minutesAwake")
    @Expose
    Integer minutesAwake;

    @SerializedName("minutesToFallAsleep")
    @Expose
    Integer minutesToFallAsleep;

    @SerializedName("startTime")
    @Expose
    String startTime;

    @SerializedName("timeInBed")
    @Expose
    Integer timeInBed;

    @SerializedName("type")
    @Expose
    String type;




    public String getDateOfSleep() {
        return dateOfSleep;
    }

    public void setDateOfSleep(String dateOfSleep) {
        this.dateOfSleep = dateOfSleep;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Integer efficiency) {
        this.efficiency = efficiency;
    }

    public Levels getLevels() {
        return levels;
    }

    public void setLevels(Levels levels) {
        this.levels = levels;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Integer getMinutesAfterWakeup() {
        return minutesAfterWakeup;
    }

    public void setMinutesAfterWakeup(Integer minutesAfterWakeup) {
        this.minutesAfterWakeup = minutesAfterWakeup;
    }

    public Integer getMinutesAsleep() {
        return minutesAsleep;
    }

    public void setMinutesAsleep(Integer minutesAsleep) {
        this.minutesAsleep = minutesAsleep;
    }

    public Integer getMinutesAwake() {
        return minutesAwake;
    }

    public void setMinutesAwake(Integer minutesAwake) {
        this.minutesAwake = minutesAwake;
    }

    public Integer getMinutesToFallAsleep() {
        return minutesToFallAsleep;
    }

    public void setMinutesToFallAsleep(Integer minutesToFallAsleep) {
        this.minutesToFallAsleep = minutesToFallAsleep;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getTimeInBed() {
        return timeInBed;
    }

    public void setTimeInBed(Integer timeInBed) {
        this.timeInBed = timeInBed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
