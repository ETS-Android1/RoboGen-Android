package at.srfg.robogen.alexa.data;

public class UserDataInfo {

    private boolean showDiabetes;
    private boolean showSenior;
    private ShownIntervention[] shownInterventions;

    public UserDataInfo() {}

    public boolean isShowDiabetes() {
        return showDiabetes;
    }
    public void setShowDiabetes(boolean showDiabetes) {
        this.showDiabetes = showDiabetes;
    }

    public boolean isShowSenior() {
        return showSenior;
    }
    public void setShowSenior(boolean showSenior) {
        this.showSenior = showSenior;
    }

    public ShownIntervention[] getShownInterventions() {
        return shownInterventions;
    }
    public void setShownInterventions(ShownIntervention[] shownInterventions) {
        this.shownInterventions = shownInterventions;
    }
}
