package at.srfg.robogen.alexa;

// follow this example: https://docs.aws.amazon.com/aws-mobile/latest/developerguide/how-to-android-lambda.html

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

import at.srfg.robogen.R;
import at.srfg.robogen.alexa.data.AndroidIdentification;
import at.srfg.robogen.alexa.data.UserDataInfo;

// allow function invoke access in 'LambdaAndroidAccess' - Permission
// https://console.aws.amazon.com/iam/home?region=us-east-1#/policies
public class AlexaManager {

    // see: https://stackoverflow.com/questions/44481490/where-to-find-identity-pool-id-in-cognito
    private static final String IDENTITY_POOL_ID = "eu-west-1:f11fa863-e1cf-4ecb-ab78-8919df424e25";

    private Activity m_actParent;
    private Context m_ctxParent;

    private ArrayAdapter<String> m_arrAdapter;

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
    public void InitAlexaSkillInvocation()
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
        final AndroidIdentification androidID = new AndroidIdentification("Android", "RoboGen");

        // The Lambda function invocation results in a network call
        // Make sure it is not called from the main thread
        new AsyncTask<AndroidIdentification, Void, UserDataInfo>() {

            @Override
            protected UserDataInfo doInBackground(AndroidIdentification... params) {

                // invoke "echo" method. In case it fails, it will throw a LambdaFunctionException
                try {
                    return lambdaInterface.Chatbot_Stress_Exercise(params[0]);
                }
                catch (LambdaFunctionException lfe) {
                    Log.e(TAG, "Failed to invoke chatbot skill", lfe);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(UserDataInfo result) {

                if (result != null) {

                    m_arrAdapter = new ArrayAdapter<String>(m_actParent.getBaseContext(), R.layout.alexa_datalist_entry);
                    m_arrAdapter.add("Name des Anwenders/in: " + result.getUserName());
                    m_arrAdapter.add("Benutzer/in ist an einem Gespräch interessiert: " + result.getUserWantsToTalk());
                    m_arrAdapter.add("Benutzer/in fühlt sich gestresst: " + result.isStressed());
                    m_arrAdapter.add("Benutzer/in hat Diabetes: " + result.isHasDiabetes());
                    m_arrAdapter.add("Es ist noch ein Wordpress-Thema offen: " + result.getRequiredTopic());
                    m_arrAdapter.add("Stress-Grund: " + result.getStressReasons());
                    m_arrAdapter.add("Stressquelle: " + result.getStressSources());
                    m_arrAdapter.add("Sport-Ausmaß: " + result.getSportVolume());
                    m_arrAdapter.add("Grund für zu wenig Sport: " + result.getSportUnderstateReason());
                    m_arrAdapter.add("Grund für zu viel Sport: " + result.getSportOverstateReason());
                    m_arrAdapter.add("Grund warum Sport keinen Spaß macht: " + result.getSportUnhappyReason());
                    m_arrAdapter.add("Weitere Sport-Informationen: " + result.getSportAdditionalReasons());

                    ListView listView = (ListView) m_actParent.findViewById(R.id.alexa_datalist);
                    listView.setAdapter(m_arrAdapter);

                    Toast.makeText(m_actParent, "User-Daten erfolgreich bezogen von Alexa Skill", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(androidID);
    }
}
