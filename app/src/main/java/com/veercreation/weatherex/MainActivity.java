package com.veercreation.weatherex;
import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    TextView temperatureTextView , atmosphereText;
    EditText cityInput;

    //class for background thread async task
    public class DownloadingWeather extends AsyncTask<String , Void , String> {

        @Override
        protected String doInBackground(String... urls) {
            URL url ;
            HttpsURLConnection httpsURLConnection = null;
            String result = "";

            try {
                url = new URL(urls[0]);
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                InputStream in = httpsURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while(data!=-1){
                    char current = (char) data;
                    result+=current;
                    data = reader.read();
                }
                return result;

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject mainJsonObject = new JSONObject(s);

                //json object which had only the current data
                JSONObject CurrentJSONObject = mainJsonObject.getJSONObject("current");
                String temperatureString = CurrentJSONObject.getString("temp_c")+" C";
                temperatureTextView.setText(temperatureString);

                JSONObject conditionObject = CurrentJSONObject.getJSONObject("condition");
                String skyCondition = "It's " + conditionObject.getString("text") + " Sky";
                atmosphereText.setText(skyCondition);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureTextView = findViewById(R.id.temperatureTextView);
        cityInput = findViewById(R.id.inputCityText);
        atmosphereText = findViewById(R.id.atmosphereTextView);


    }

    public void getData(View view){
        DownloadingWeather weatherTask = new DownloadingWeather();
        String city = String.valueOf(cityInput.getText());
        String url = "https://api.weatherapi.com/v1/current.json?key=9c0f3203757049a1bdf61654212411&q="+city;
        String result = null;
        try {
            result = weatherTask.execute(url).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}