package com.uiwjs;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.app.EnvUtils;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Callback;

import java.util.Map;

public class AlipayModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public AlipayModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "Alipay";
    }

    // @ReactMethod
    // public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
    //     // TODO: Implement some actually useful functionality
    //     callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    // }

    @ReactMethod
    public void setAlipaySandbox(Boolean isSandbox) {
        if (isSandbox) {
            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        } else {
            EnvUtils.setEnv(EnvUtils.EnvEnum.ONLINE);
        }
    }
    @ReactMethod
    public void alipay(final String orderInfo, final Callback promise) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(getCurrentActivity());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                WritableMap map = Arguments.createMap();
                map.putString("memo", result.get("memo"));
                map.putString("result", result.get("result"));
                map.putString("resultStatus", result.get("resultStatus"));
                promise.invoke(map);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

}
