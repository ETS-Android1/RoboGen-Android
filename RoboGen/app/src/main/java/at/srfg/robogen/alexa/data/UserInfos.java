package at.srfg.robogen.alexa.data;

public class UserInfos {

    private String firstName;
    private String lastName;

    public UserInfos() {}

    public UserInfos(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
