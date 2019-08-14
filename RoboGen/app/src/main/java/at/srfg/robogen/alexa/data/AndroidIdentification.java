package at.srfg.robogen.alexa.data;

/*******************************************************************************
 * this storage class is used to identify the caller of the lambda function
 * if these infos are part of the skill init context, the skill is called by the
 * Android App instead of the usual skill invocation
 ******************************************************************************/
public class AndroidIdentification {

    private String firstName;
    private String lastName;

    public AndroidIdentification() {}

    public AndroidIdentification(String firstName, String lastName) {
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
