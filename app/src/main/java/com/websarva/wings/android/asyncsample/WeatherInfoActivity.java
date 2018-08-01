package com.websarva.wings.android.asyncsample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        //  画面部品ListViewを取得
        ListView lvCityList = findViewById(R.id.lvCityList);
        //  SimpleAdapterで使用するListオブジェクトを用意。
        List<Map<String,String>> cityList = new ArrayList<>();
        //  都市データを格納するMapオブジェクトの用意とcityListへの登録。
        Map<String,String> city = new HashMap<>();
        city.put("name","宮崎");
        city.put("id","450010");
        cityList.add(city);

        city = new HashMap<>();
        city.put("name","延岡");
        city.put("id","450020");
        cityList.add(city);

        city = new HashMap<>();
        city.put("name","都城");
        city.put("id","450030");
        cityList.add(city);

        city = new HashMap<>();
        city.put("name","高千穂");
        city.put("id","450040");
        cityList.add(city);

        city = new HashMap<>();
        city.put("name","那覇");
        city.put("id","471010");
        cityList.add(city);

        city = new HashMap<>();
        city.put("name","名護");
        city.put("id","471020");
        cityList.add(city);

        city = new HashMap<>();
        city.put("name","久米島");
        city.put("id","471030");
        cityList.add(city);

        city = new HashMap<>();
        city.put("name","南大東");
        city.put("id","472000");
        cityList.add(city);

        city = new HashMap<>();
        city.put("name","宮古島");
        city.put("id","473000");
        cityList.add(city);

        city = new HashMap<>();
        city.put("name","石垣島");
        city.put("id","474010");
        cityList.add(city);

        city = new HashMap<>();
        city.put("name","与那国島");
        city.put("id","474020");
        cityList.add(city);

        //  SimpleAdapterで使用するfrom-to用変数の用意
        String[] from = {"name"};
        int[] to = {android.R.id.text1};

        //  SimpleAdapterを生成。
        SimpleAdapter adapter = new SimpleAdapter(WeatherInfoActivity.this,cityList,
                android.R.layout.simple_expandable_list_item_1,from,to);

        //  ListViewにSimpleAdapterを設定
        lvCityList.setAdapter(adapter);

        //  ListViewにリスナを設定
        lvCityList.setOnItemClickListener(new ListItemClickListener());

    }

    /**
     * リストが選択された時の処理が記述されたメンバクラス
     */
    private class ListItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //  ListViewでタップされた行の都市名と都市IDを取得
            Map<String,String> item = (Map<String,String>)parent.getItemAtPosition(position);
            String cityName = item.get("name");
            String cityId = item.get("id");
            //  取得した都市名をtvCityNameに設定。
            TextView tvCityName = findViewById(R.id.tvCityName);
            tvCityName.setText(cityName + "の天気：");
            //  天気情報を表示するTextViewを取得。
            TextView tvWeatherTelop = findViewById(R.id.tvWeatherTelop);
            //  天気詳細情報を表示するTextViewを取得。
            TextView tvWeatherDesc = findViewById(R.id.tvWeatherDesc);
            //  WeatherInfoReceiverをnew。引数として上で取得したTextViewを渡す。
            WeatherInfoReceiver receiver = new WeatherInfoReceiver(tvWeatherTelop,tvWeatherDesc);
            //  WeatherInfoReceiverを実行。
            receiver.execute(cityId);
        }
    }

    /**
     * トップレベルクラスとしても作成出来るが、WeatherInfoActivity内でしか使用しないためメンバクラスとして作成
     */
    private class WeatherInfoReceiver extends AsyncTask<String, String, String>{
        /**
         * 現在の天気を表示する画面部品フィールド
         */
        private TextView _tvWeatherTelop;

        /**
         * 天気の詳細を表示する画面部品フィールド
         */
        private TextView _tvWeatherDesc;

        /**
         * コンストラクタ。
         * お天気情報を表示する画面部品をあらかじめ取得してフィールドに格納している。
         */
        public WeatherInfoReceiver(TextView tvWeatherTelop, TextView tvWeatherDesc){
            _tvWeatherTelop = tvWeatherTelop;
            _tvWeatherDesc = tvWeatherDesc;
        }


        @Override
        protected String doInBackground(String... params) {
            //  可変長引数の１個目（インデックス０）を取得。これが都市ID.
            String id = params[0];
            //  都市IDを使って接続URL文字列を作成。
            String urlStr = "http://weather.livedoor.com/forecast/webservice/json/v1?city=" + id;
            //  天気情報サービスから取得したJSON文字列。天気情報が格納されている。
            String result = "";
            //  HTTP接続を行うHttpURLConnectionオブジェクトを宣言。finallyで確実に解放するためにtry外で宣言。
            HttpURLConnection con = null;
            //  HTTP接続のレスポンスデータとして取得するInputStreamオブジェクトを宣言。同じくtry外で宣言。
            InputStream is = null;
            try{
                //  URLオブジェクト生成。
                URL url = new URL(urlStr);
                //  URLオブジェクトからHttpURLConnectionオブジェクトを取得。
                con = (HttpURLConnection) url.openConnection();
                //  HTTP接続メソッドを設定。
                con.setRequestMethod("GET");
                //  接続
                con.connect();
                //  HttpURLConnectionオブジェクトからレスポンスデータを取得。
                is = con.getInputStream();
                //  レスポンスデータであるInputStreamオブジェクトを文字列に変換。
                result = is2String(is);
            }
            //  例外処理
            catch (MalformedURLException ex){

            }
            catch (IOException ex){

            }
            //  後処理
            finally {
                //  HttpURLConnectionオブジェクトがnullでないなら解放。
                if(con != null){
                    con.disconnect();
                }
                //  InputStreamオブジェクトがnullでないなら解放
                if(is != null){
                    try{
                        is.close();
                    }
                    //  例外処理
                    catch (IOException ex){

                    }
                }

            }
            return result;
        }

        @Override
        public void onPostExecute(String result){
            //  天気情報用文字列変数を用意。
            String telop = "";
            String desc = "";

            //  ここに天気情報JSON文字列を解析する処理を記述。

            //天気情報よう文字列をTextViewにセット。
            _tvWeatherTelop.setText(telop);
            _tvWeatherDesc.setText(desc);

        }

    }

}
