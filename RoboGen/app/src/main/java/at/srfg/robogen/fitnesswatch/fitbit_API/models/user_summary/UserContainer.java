package at.srfg.robogen.fitnesswatch.fitbit_API.models.user_summary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserContainer {

    @SerializedName("user")
    @Expose
    private User user;

    /**
     * @return The user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUser(User user) {
        this.user = user;
    }

}
