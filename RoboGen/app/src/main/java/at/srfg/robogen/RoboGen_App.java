package at.srfg.robogen;

import at.srfg.robogen.fitnesswatch.FitbitAuthApplication;

/*******************************************************************************
 * The RoboGen App is a FitBit Auth Application
 * This is the application class, which will be present throughout the entire
 * application run, no matter in which activity we currently are
 *
 * This class holds the AI_Center, which is responsible for all the
 * combined logic within the app, that needs data from different sources
 ******************************************************************************/
public class RoboGen_App extends FitbitAuthApplication {

    private RoboGen_Manager m_cRoboGenManager;

    public RoboGen_Manager getRoboGenManager() {
        if (m_cRoboGenManager == null) {
            m_cRoboGenManager = new RoboGen_Manager();
        }
        return m_cRoboGenManager;
    }

    public void setRoboGenManager(RoboGen_Manager center) {
        this.m_cRoboGenManager = center;
    }
}