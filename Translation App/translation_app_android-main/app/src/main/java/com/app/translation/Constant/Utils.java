package com.app.translation.Constant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

public class Utils {
   static public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
   }

   static public List<String> getLanguageCodeList(){
       List<String> languageList = new ArrayList<>();
       languageList.add("Bulgarian - BG");
       languageList.add("Czech - CS");
       languageList.add("Danish - DA");
       languageList.add("German - DE");
       languageList.add("Greek - EL");
       languageList.add("English - EN");
       languageList.add("Spanish - ES");
       languageList.add("Estonian - ET");
       languageList.add("Finnish - FI");
       languageList.add("French - FR");
       languageList.add("Hungarian - HU");
       languageList.add("Italian - IT");
       languageList.add("Japanese - JA");
       languageList.add("Lithuanian - LT");
       languageList.add("Latvian - LV");
       languageList.add("Dutch - NL");
       languageList.add("Polish - PL");
       languageList.add("Portuguese (all Portuguese varieties mixed) - PT");
       languageList.add("Romanian - RO");
       languageList.add("Russian - RU");
       languageList.add("Slovak - SK");
       languageList.add("Slovenian - SL");
       languageList.add("Swedish - SV");
       languageList.add("Chinese - ZH");

       /*languageList.add("Azerbaijani - az");
       languageList.add("Albanian - sq");
       languageList.add("Amharic - am");
       languageList.add("English - en");
       languageList.add("Arabic - ar");
       languageList.add("Armenian - hy");
       languageList.add("Mari - mhr");
       languageList.add("Afrikaans - af");
       languageList.add("Mongolian - mn");
       languageList.add("Basque - eu");
       languageList.add("German - de");
       languageList.add("Bashkir - ba");
       languageList.add("Nepalese - ne");
       languageList.add("Belarusian - be");
       languageList.add("Norwegian - no");
       languageList.add("Bengal - bn");
       languageList.add("Punjabi - pa");
       languageList.add("Burmese - my");
       languageList.add("Papiamento - pap");
       languageList.add("Bulgarian - bg");
       languageList.add("Persian - fa");
       languageList.add("Bosnian - bs");
       languageList.add("Polish - pl");
       languageList.add("Welsh - cy");
       languageList.add("Portuguese - pt");
       languageList.add("Hungarian - hu");
       languageList.add("Romanian - ro");
       languageList.add("Vietnamese - vi");
       languageList.add("Russian - ru");
       languageList.add("Haitian (Creole) - ht");
       languageList.add("Cebuano - ceb");
       languageList.add("Galician - gl");
       languageList.add("Serbian - sr");
       languageList.add("Dutch - nl");
       languageList.add("Sinhalese - si");
       languageList.add("Hill Mari - mrj");
       languageList.add("Slovak - sk");
       languageList.add("Greek - el");
       languageList.add("Slovenian - sl");
       languageList.add("Georgian - ka");
       languageList.add("Swahili - sw");
       languageList.add("Gujarati - gu");
       languageList.add("Sundanese - su");
       languageList.add("Danish - da");
       languageList.add("Tajik - tg");
       languageList.add("Hebrew - he");
       languageList.add("Thai - th");
       languageList.add("Yiddish - yi");
       languageList.add("Tagalog - tl");
       languageList.add("Indonesian - id");
       languageList.add("Tamil - ta");
       languageList.add("Irish - ga");
       languageList.add("Tartar - tt");
       languageList.add("Italian - it");
       languageList.add("Telugu - te");
       languageList.add("Icelandic - is");
       languageList.add("Turkish - tr");
       languageList.add("Spanish - es");
       languageList.add("Udmurt - udm");
       languageList.add("Kazakh - kk");
       languageList.add("Uzbek - uz");
       languageList.add("Kannada - kn");
       languageList.add("Ukrainian - uk");
       languageList.add("Catalan - ca");
       languageList.add("Urdu - ur");
       languageList.add("Kirghiz - ky");
       languageList.add("Finnish - fi");
       languageList.add("Chinese - zh");
       languageList.add("French - fr");
       languageList.add("Korean - ko");
       languageList.add("Hindi - hi");
       languageList.add("Xhosa - xh");
       languageList.add("Croatian - hr");
       languageList.add("Khmer - km");
       languageList.add("Czech - cs");
       languageList.add("Laotian - lo");
       languageList.add("Swedish - sv");
       languageList.add("Latin - la");
       languageList.add("Scottish - gd");
       languageList.add("Latvian - lv");
       languageList.add("Estonian - et");
       languageList.add("Lithuanian - lt");
       languageList.add("Esperanto - eo");
       languageList.add("Luxembourg - lb");
       languageList.add("Javanese - jv");
       languageList.add("Malagasy - mg");
       languageList.add("Japanese - ja");
       languageList.add("Malay - ms");
       languageList.add("Maltese - mt");
       languageList.add("Macedonian - mk");
       languageList.add("Maori - mi");
       languageList.add("Marathi - mr");*/
       return languageList;
   }


}
