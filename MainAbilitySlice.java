package com.example.fuckyou.slice;

import com.example.fuckyou.Bean.Data;
import com.example.fuckyou.Bean.DataStream;
import com.example.fuckyou.Bean.Datapoints;
import com.example.fuckyou.Bean.JsonRootBean;
import com.example.fuckyou.ResourceTable;
import com.google.gson.Gson;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

import java.util.List;

public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    public HiLogLabel label = new HiLogLabel(HiLog.LOG_APP,0x00201,"TAG");

    private DirectionalLayout.LayoutConfig layoutConfig;
    public DirectionalLayout layout = new DirectionalLayout(this);

    public final String url = "http://api.heclouds.com/devices/"; // api url
    public static final String DeviceID = "961736642";  //ONENET设备ID
    public static final String ApiKey = "sYfl57sFa8ScMQAHqQhafEP=9iU="; //APIKEYONENET
    public static final String humistream = "humidity";//onenet平台上湿度数据流的名字
    public static final String tempstream = "temperature";//onenet平台上温度数据流的名字

    public Button button = new Button(getContext());
    public Text temperature = new Text(getContext());
    public Text humidity = new Text(getContext());

    public String value_temp, value_humi;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        layout.setOrientation(Component.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        layout.setWidth(ComponentContainer.LayoutConfig.MATCH_PARENT);
        layout.setHeight(ComponentContainer.LayoutConfig.MATCH_PARENT);

        layoutConfig= new DirectionalLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
        layoutConfig.alignment = LayoutAlignment.HORIZONTAL_CENTER;

        initSlice();
        setUIContent(layout);
    }

    public void initSlice(){
        temperature.setText("温度：暂无信息");
        humidity.setText("湿度：暂无信息");
        temperature.setPadding(0, 40, 0, 40);
        temperature.setTextSize(50);
        humidity.setPadding(0, 40, 0, 40);
        humidity.setTextSize(50);

        temperature.setWidth(ComponentContainer.LayoutConfig.MATCH_CONTENT);
        temperature.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
        temperature.setLayoutConfig(layoutConfig);
        humidity.setWidth(ComponentContainer.LayoutConfig.MATCH_CONTENT);
        humidity.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
        humidity.setLayoutConfig(layoutConfig);

        button.setText("GET");
        button.setClickedListener(this::onClick);
        button.setTextSize(100);
        button.setPadding(32, 32, 32, 32);
        button.setLayoutConfig(layoutConfig);

        layout.addComponent(temperature);
        layout.addComponent(humidity);
        layout.addComponent(button);
        setUIContent(layout);

    }
    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    public void Get() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //接受温度
                    OkHttpClient client1 = new OkHttpClient();
                    Request request1 = new Request.Builder().url(url + DeviceID + "/datapoints?datastream_id=" + humistream).header("api-key", ApiKey).build();
                    Response response1 = client1.newCall(request1).execute();
                    String responseData1 = response1.body().string();

                    //数据流json解析
                    parseJSONWithGSON(responseData1);

                    JsonRootBean app = new Gson().fromJson(responseData1, JsonRootBean.class);
                    List<DataStream.Datastreams> streams = app.getData().getDatastreams();
                    List<Datapoints> points = streams.get(0).getDatapoints();
                    value_humi = points.get(0).getValue();

                    //更新UI
                    getUITaskDispatcher().asyncDispatch(() -> {
                            humidity.setText("湿度：" + value_humi + "%");;

                    });


                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    //接受温度
                    OkHttpClient client2 = new OkHttpClient();
                    Request request2 = new Request.Builder().url(url + DeviceID + "/datapoints?datastream_id=" + tempstream).header("api-key", ApiKey).build();
                    Response response2 = client2.newCall(request2).execute();
                    String responseData2 = response2.body().string();

                    //数据流json解析
                    parseJSONWithGSON(responseData2);

                    JsonRootBean app = new Gson().fromJson(responseData2, JsonRootBean.class);
                    List<DataStream.Datastreams> streams = app.getData().getDatastreams();
                    List<Datapoints> points = streams.get(0).getDatapoints();
                    value_temp = points.get(0).getValue();

                    //更新UI
                    getUITaskDispatcher().asyncDispatch(() -> {
                       temperature.setText("温度:" + value_temp + "°C");;

                    });

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                setUIContent(layout);
            }
        }).start();
    }

    //解析json
    private void parseJSONWithGSON(String jsonData) {
        JsonRootBean app = new Gson().fromJson(jsonData, JsonRootBean.class);
        List<DataStream.Datastreams> streams = app.getData().getDatastreams();
        List<Datapoints> points = streams.get(0).getDatapoints();
        for (int i = 0; i < points.size(); i++) {
            String time = points.get(i).getAt();
            String value = points.get(i).getValue();
            HiLog.debug(label,"time="+time);
            HiLog.debug(label,"value="+value);
        }
    }


    @Override
    public void onClick(Component component){
        Get();
    }

}
