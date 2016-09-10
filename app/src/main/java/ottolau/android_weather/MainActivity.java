package ottolau.android_weather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button weatherBtn;
    TextView weatherStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherBtn = (Button) findViewById(R.id.weatherBtn);
        weatherStatus = (TextView) findViewById(R.id.weatherStatus);

        weatherBtn.setOnClickListener((v) -> {
            new RetrieveHoustonCurrTemp().execute("");
        });
    }

    private class RetrieveHoustonCurrTemp extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... Params) {
            StringBuffer responseString = new StringBuffer();
            try {
                // Get the current weather information about Houston Texas online
                String urlString = "http://api.openweathermap.org/data/2.5/forecast/city?id=4699066&APPID=d1fbd4864de3b36481556a08ebb069b4";
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                InputStream response = urlConnection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    responseString.append(inputLine);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseString.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null){
                // Retrieve current temperature
                String target = "\"temp\":";
                int idxOfCurrentTempStart = result.indexOf(target) + target.length();
                int idxOfCurrentTempEnd = result.indexOf(",", idxOfCurrentTempStart);
                double currTemp = Double.parseDouble(result.substring(idxOfCurrentTempStart,idxOfCurrentTempEnd));

                // Convert temperature from Kelvin to Celsius
                currTemp -= 273.15;

                // Display the temperature in the view
                String displayCurrTemp = String.format("%s %s%s", Integer.toString((int)currTemp),(char) 0x00B0, "C");
                weatherStatus.setText(displayCurrTemp);
            }
            else{
                weatherStatus.setText("Failed to retrieve the current weather for Houston.");
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
