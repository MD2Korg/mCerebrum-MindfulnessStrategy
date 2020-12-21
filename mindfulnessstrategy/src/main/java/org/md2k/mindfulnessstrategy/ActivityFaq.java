package org.md2k.mindfulnessstrategy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.md2k.datakitapi.DataKitAPI;
import org.md2k.datakitapi.datatype.DataTypeInt;
import org.md2k.datakitapi.datatype.DataTypeString;
import org.md2k.datakitapi.datatype.DataTypeStringArray;
import org.md2k.datakitapi.exception.DataKitException;
import org.md2k.datakitapi.messagehandler.OnConnectionListener;
import org.md2k.datakitapi.source.datasource.DataSource;
import org.md2k.datakitapi.source.datasource.DataSourceBuilder;
import org.md2k.datakitapi.source.datasource.DataSourceClient;
import org.md2k.datakitapi.time.DateTime;
import org.md2k.mcerebrum.system.cerebralcortexwebapi.models.stream.DataDescriptor;

import java.util.ArrayList;
import java.util.HashMap;

import mehdi.sakout.fancybuttons.FancyButton;

public class ActivityFaq extends Activity {

    FancyButton back;
    DataKitAPI dataKitAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        WebView wv1=(WebView)findViewById(R.id.webView);
        wv1.setWebViewClient(new MyBrowser());
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.loadUrl("file:///android_asset/faq.html");


        back = (FancyButton) findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dataKitAPI = DataKitAPI.getInstance(this);
        try {
            dataKitAPI.connect(new OnConnectionListener() {
                @Override
                public void onConnected() {
                    try {
                        DataSourceBuilder d = new DataSourceBuilder().setType("UI_ACTIVITY").setDataDescriptors(create());
                        DataSourceClient c = dataKitAPI.register(d);
                        DataTypeStringArray dataTypeStringArray = new DataTypeStringArray(DateTime.getDateTime(), new String[]{"ACTIVITY_FAQ", "BUTTON_FAQ"});
                        dataKitAPI.insert(c, dataTypeStringArray);
                        Log.d("abc","faq button clicked");
                        dataKitAPI.disconnect();
                    }catch (Exception e){
                        Log.d("error","error");
                    }

                }
            });
        } catch (DataKitException e) {
            Log.d("error","error");
            e.printStackTrace();
        }
    }
    ArrayList<HashMap<String, String>> create(){
        HashMap<String, String> a = new HashMap<>();
        a.put("NAME", "PAGE");
        HashMap<String, String> b = new HashMap<>();
        b.put("NAME", "WIDGET");
        ArrayList<HashMap<String, String>> res =new ArrayList<>();
        res.add(a);
        res.add(b);
        return res;
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
