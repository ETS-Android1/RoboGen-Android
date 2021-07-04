package at.srfg.robogen.alexa;

// follow this example: https://docs.aws.amazon.com/aws-mobile/latest/developerguide/how-to-android-lambda.html

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

import at.srfg.robogen.R;
import at.srfg.robogen.alexa.data.AndroidIdentification;
import at.srfg.robogen.alexa.data.ShownIntervention;
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

                    m_arrAdapter = new ArrayAdapter<String>(m_actParent.getBaseContext(), R.layout.alexa_datalist_entry){
                        public View getView(int position, View view, ViewGroup viewGroup)
                        {
                            View v = super.getView(position, view, viewGroup);
                            ((TextView)v).setText(Html.fromHtml((String)this.getItem(position)));
                            ((TextView)v).setMovementMethod(LinkMovementMethod.getInstance());
                            ((TextView)v).setLinkTextColor(Color.parseColor("#5E8ACB")); // R.color.colorPrimary
                            return v;
                        }
                    };

                    m_arrAdapter.add("Themen mit Bezug auf Diabetes: " + (result.isShowDiabetes()? "Ja":"Nein"));
                    m_arrAdapter.add("Themen mit Bezug für Senioren/Seniorinnen: " + (result.isShowSenior()? "Ja":"Nein"));

                    for(ShownIntervention intervention: result.getShownInterventions())
                    {
                        if(intervention.isRemember())
                        {
                            m_arrAdapter.add(intervention.getShownIntervention() + ": \n" +
                                    "<b><a href=\"" + intervention.getAdditionalLink() + "\">Link zum gemerkten Thema</a></b>");
                        }
                    }


                    ListView listView = (ListView) m_actParent.findViewById(R.id.alexa_datalist);
                    listView.setAdapter(m_arrAdapter);

                    Toast.makeText(m_actParent, "User-Daten erfolgreich bezogen von Alexa Skill", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(androidID);
    }
}
