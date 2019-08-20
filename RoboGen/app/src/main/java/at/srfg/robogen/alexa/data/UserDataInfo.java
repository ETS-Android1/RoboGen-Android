package at.srfg.robogen.alexa.data;

public class UserDataInfo {

   private String  userName;
   private String  userWantsToTalk;
   private boolean  isStressed;
   private boolean  hasDiabetes;
   private String  requiredTopic;
   private String  stressReasons;
   private String  stressSources;
   private String  sportVolume;
   private String  sportUnderstateReason;
   private String  sportOverstateReason;
   private String  sportUnhappyReason;
   private String  sportAdditionalReasons;

    public UserDataInfo() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserWantsToTalk() {
        return userWantsToTalk;
    }

    public void setUserWantsToTalk(String userWantsToTalk) {
        this.userWantsToTalk = userWantsToTalk;
    }

    public boolean isStressed() {
        return isStressed;
    }

    public void setStressed(boolean stressed) {
        isStressed = stressed;
    }

    public boolean isHasDiabetes() {
        return hasDiabetes;
    }

    public void setHasDiabetes(boolean hasDiabetes) {
        this.hasDiabetes = hasDiabetes;
    }

    public String getRequiredTopic() {
        return requiredTopic;
    }

    public void setRequiredTopic(String requiredTopic) {
        this.requiredTopic = requiredTopic;
    }

    public String getStressReasons() {
        return stressReasons;
    }

    public void setStressReasons(String stressReasons) {
        this.stressReasons = stressReasons;
    }

    public String getStressSources() {
        return stressSources;
    }

    public void setStressSources(String stressSources) {
        this.stressSources = stressSources;
    }

    public String getSportVolume() {
        return sportVolume;
    }

    public void setSportVolume(String sportVolume) {
        this.sportVolume = sportVolume;
    }

    public String getSportUnderstateReason() {
        return sportUnderstateReason;
    }

    public void setSportUnderstateReason(String sportUnderstateReason) {
        this.sportUnderstateReason = sportUnderstateReason;
    }

    public String getSportOverstateReason() {
        return sportOverstateReason;
    }

    public void setSportOverstateReason(String sportOverstateReason) {
        this.sportOverstateReason = sportOverstateReason;
    }

    public String getSportUnhappyReason() {
        return sportUnhappyReason;
    }

    public void setSportUnhappyReason(String sportUnhappyReason) {
        this.sportUnhappyReason = sportUnhappyReason;
    }

    public String getSportAdditionalReasons() {
        return sportAdditionalReasons;
    }

    public void setSportAdditionalReasons(String sportAdditionalReasons) {
        this.sportAdditionalReasons = sportAdditionalReasons;
    }

}
