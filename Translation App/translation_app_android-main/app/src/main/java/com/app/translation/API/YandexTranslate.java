package com.app.translation.API;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class YandexTranslate extends AsyncTask<String, Void, String> {

    /*
     * Your Yandex API Key
     */

    private final String API_KEY = "b1gkdh9dd3vs1l48qumq";

    /*
     * Yandex API Service URL
     */

    private final String YANDEX_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=";


    @Override
    protected String doInBackground(String... params){

        /*
         * The text which will be translated
         */

        String textToTranslate = params[0];

        /*
         * The source language to be translated from
         */

        final String SOURCE_LANGUAGE = params[1];

        /*
         * The wished language to be translated to
         */

        final String TARGET_LANGUAGE = params[2];

        try {

            /*
             * Encode the word in UTF-8
             * Use it if you get encoding problems
             */

            textToTranslate = URLEncoder.encode(textToTranslate, "UTF-8");

            /*
             * Create the URL object with the url value
             */

            URL url = new URL(YANDEX_URL + API_KEY + "&text=" + textToTranslate + "&lang=" + SOURCE_LANGUAGE + "-" + TARGET_LANGUAGE);

            /*
             * Create Http Connection, Input Stream, Buffered Reader and jsonString
             */

            HttpURLConnection connection  = (HttpURLConnection) url.openConnection();

            InputStream inputStream       = connection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

            String jsonString;

            /*
             * Create string builder and insert retrieved JSON result into it
             */

            StringBuilder stringBuilder = new StringBuilder();

            while ((jsonString = bufferedReader.readLine()) != null) {

                stringBuilder.append(jsonString + "\n");
            }

            /*
             * Close and disconnect
             */

            bufferedReader.close();

            inputStream.close();

            connection.disconnect();

            /*
             * Trim the response and remove any special chars from it
             * The response has the form of: {"code":200,"lang":"de-ar","text":["blabla"]}
             */

            JSONObject obj = new JSONObject(stringBuilder.toString().trim());

            if(obj.getString("code").equals("200")) {

                String resultString = obj.getString("text");

                /*
                 * Escape  ,"[]  from the result
                 */

                resultString = resultString.replaceAll("[,\"\\[\\]]", "");

                return resultString;
            }

            /*
             * Return empty string if there is any error (response code is other than 200)
             */

            else return "";

        } catch (Exception e){

            Log.e("Yandex Response ", e.getMessage());

            return "";
        }
    }
}
