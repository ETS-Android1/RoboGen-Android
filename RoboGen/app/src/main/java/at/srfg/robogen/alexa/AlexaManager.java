package at.srfg.robogen.alexa;

// follow this example: https://docs.aws.amazon.com/aws-mobile/latest/developerguide/how-to-android-lambda.html

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

import at.srfg.robogen.alexa.data.UserInfos;

// allow function invoke access in 'LambdaAndroidAccess' - Permission
// https://console.aws.amazon.com/iam/home?region=us-east-1#/policies
public class AlexaManager {

    // see: https://stackoverflow.com/questions/44481490/where-to-find-identity-pool-id-in-cognito
    private static final String IDENTITY_POOL_ID = "eu-west-1:f11fa863-e1cf-4ecb-ab78-8919df424e25";

    private Activity m_actParent;
    private Context m_ctxParent;

    private static final String TAG = "BluetoothReadService";

    /*******************************************************************************
     * CTOR
     ******************************************************************************/
    public AlexaManager(Activity act, Context ctx)
    {
        m_actParent = act;
        m_ctxParent = ctx;
    }

    /*******************************************************************************
     * will initialize the lambda function invocation
     ******************************************************************************/
    public void InitAlexaInvocation()
    {
        // Create an instance of CognitoCachingCredentialsProvider
        CognitoCachingCredentialsProvider credentialsProvider =
                new CognitoCachingCredentialsProvider(m_actParent.getApplicationContext(), IDENTITY_POOL_ID, Regions.EU_WEST_1);

        // Create a LambdaInvokerFactory, to be used to instantiate the Lambda proxy
        LambdaInvokerFactory factory =
                new LambdaInvokerFactory(m_actParent.getApplicationContext(), Regions.EU_WEST_1, credentialsProvider);

        // Create the Lambda proxy object with default Json data binder.
        // You can provide your own data binder by implementing LambdaDataBinder
        final LambdaInterface lambdaInterface = factory.build(LambdaInterface.class);

        // Create an instance of the POJO to transfer data
        final UserInfos nameInfo = new UserInfos("John", "Doe");

        // The Lambda function invocation results in a network call
        // Make sure it is not called from the main thread
        new AsyncTask<UserInfos, Void, String>() {

            @Override
            protected String doInBackground(UserInfos... params) {

                // invoke "echo" method. In case it fails, it will throw a LambdaFunctionException
                try {
                    return lambdaInterface.echo(params[0]);
                }
                catch (LambdaFunctionException lfe) {
                    Log.e(TAG, "Failed to invoke echo", lfe);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {

                if (result != null) {
                    Toast.makeText(m_actParent, result, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(nameInfo);
    }
}
