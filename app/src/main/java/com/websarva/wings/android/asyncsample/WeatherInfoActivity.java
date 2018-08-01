package com.websarva.wings.android.asyncsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
        }
    }

}
