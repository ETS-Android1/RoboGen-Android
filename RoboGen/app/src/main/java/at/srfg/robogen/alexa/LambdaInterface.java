package at.srfg.robogen.alexa;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
import at.srfg.robogen.alexa.data.AndroidIdentification;
import at.srfg.robogen.alexa.data.UserDataInfo;

public interface LambdaInterface {

    /**
     * Invoke lambda function "echo". The function name is the method name
     */
    @LambdaFunction
    UserDataInfo Chatbot_Stress_Exercise(AndroidIdentification androidID);

    /**
     * Invoke lambda function "echo". The functionName in the annotation
     * overrides the default which is the method name
     */
    @LambdaFunction(functionName = "Chatbot_Stress_Exercise")
    void noChatbot_Stress_Exercise(AndroidIdentification androidID);
}
