package at.srfg.robogen.alexa.data;

public class ShownIntervention {

    private String[] selectedPath;
    private String shownIntervention;
    private String additionalLink;
    private boolean remember;


    public String[] getSelectedPath() {
        return selectedPath;
    }
    public void setSelectedPath(String[] selectedPath) {
        this.selectedPath = selectedPath;
    }

    public String getShownIntervention() {
        return shownIntervention;
    }
    public void setShownIntervention(String shownIntervention) {
        this.shownIntervention = shownIntervention;
    }

    public String getAdditionalLink() {
        return additionalLink;
    }
    public void setAdditionalLink(String additionalLink) {
        this.additionalLink = additionalLink;
    }

    public boolean isRemember() {
        return remember;
    }
    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}
