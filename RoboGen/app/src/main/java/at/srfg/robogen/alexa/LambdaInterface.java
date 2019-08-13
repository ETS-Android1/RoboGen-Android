package at.srfg.robogen.alexa;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

import at.srfg.robogen.alexa.data.UserInfos;

public interface LambdaInterface {

    /**
     * Invoke lambda function "echo". The function name is the method name
     */
    @LambdaFunction
    String echo(UserInfos userInfos);

    /**
     * Invoke lambda function "echo". The functionName in the annotation
     * overrides the default which is the method name
     */
    @LambdaFunction(functionName = "echo")
    void noEcho(UserInfos userInfos);
}
