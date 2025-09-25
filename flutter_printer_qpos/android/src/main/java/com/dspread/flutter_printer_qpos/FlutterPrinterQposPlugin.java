package com.dspread.flutter_printer_qpos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Activity;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;

import com.dspread.flutter_printer_qpos.TRACE;

import java.util.Map;


/**
 * FlutterPrinterQposPlugin
 */
public class FlutterPrinterQposPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler, EventChannel.StreamHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private Context mContext;
    private MethodChannel methodChannel;
    private EventChannel eventChannel;
    private Activity activity;

    public FlutterPrinterQposPlugin() {

    }

    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        // 获取 activity
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        // activity 销毁时的处理
        activity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
        // 重新绑定到 activity，例如横竖屏切换
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        // activity 销毁时的处理，例如横竖屏切换
        activity = null;
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        TRACE.d("FlutterPrinterQposPlugin：onAttachedToEngine");

        this.mContext = flutterPluginBinding.getApplicationContext();
        methodChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_printer_qpos");
        eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_printer_qpos_event");
        eventChannel.setStreamHandler(this);
        methodChannel.setMethodCallHandler(this);
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        TRACE.d("onListen");
        PosPrinterPluginHandler.initEventSender(events, arguments);
    }

    @Override
    public void onCancel(Object arguments) {
        TRACE.d("onCancel");
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        TRACE.d("FlutterPrinterQposPlugin：onMethodCall" + call.method);
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("initPrinter")) {
            TRACE.d("initPrinter");
            PosPrinterPluginHandler.initPrinter(activity);
        } else if (call.method.equals("close")) {
            TRACE.d("close");
            PosPrinterPluginHandler.close();
        } else if (call.method.equals("setAlign")) {
            TRACE.d("setAlign");
            String align = call.argument("align");
            PosPrinterPluginHandler.setAlign(align);
        } else if (call.method.equals("setFontStyle")) {
            TRACE.d("setFontStyle");
            String bold = call.argument("bold");
            PosPrinterPluginHandler.setAlign(bold);
        } else if (call.method.equals("setFontSize")) {
            TRACE.d("setFontSize");
            int fontsize = call.argument("fontsize");
            PosPrinterPluginHandler.setFontSize(fontsize);
        } else if (call.method.equals("setPrintStyle")) {
            TRACE.d("setPrintStyle");
            PosPrinterPluginHandler.setPrintStyle();
        } else if (call.method.equals("setPrintDensity")) {
            TRACE.d("setPrintDensity");
            int printDensityLevel = call.argument("printDensityLevel");
            PosPrinterPluginHandler.setPrintDensity(printDensityLevel);
        } else if (call.method.equals("printText")) {
            TRACE.d("printText");
            String text = call.argument("text");
            PosPrinterPluginHandler.printText(text);
        } else if (call.method.equals("printBarCode")) {
            String symbology = call.argument("symbology");
            int width = Integer.parseInt(call.argument("width"));
            int height = Integer.parseInt(call.argument("height"));
            String content = call.argument("content");
            String position = call.argument("position");
            PosPrinterPluginHandler.printBarCode(symbology, width, height, content, position);
        } else if (call.method.equals("printQRCode")) {
            TRACE.d("printQRCode");
            String errorLevel = call.argument("errorLevel");
            int width = Integer.parseInt(call.argument("width"));
            String content = call.argument("content");
            String position = call.argument("position");
            PosPrinterPluginHandler.printQRCode(errorLevel, width, content, position);
        } else if (call.method.equals("printBitmap")) {
            TRACE.d("printBitmap");
            byte[] bitmapByte = call.argument("bitmap");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
            PosPrinterPluginHandler.printBitmap(bitmap);
        } else if (call.method.equals("addText")) {
            TRACE.d("addText");
            String text = call.argument("text");
            PosPrinterPluginHandler.addText(text);
        } else if (call.method.equals("print")) {
            TRACE.d("print");
            PosPrinterPluginHandler.print(activity);
        } else if (call.method.equals("addPrintLintStyle")) {
            TRACE.d("print");
            int align = Integer.parseInt(call.argument("align"));
            int fontSize = Integer.parseInt(call.argument("fontSize"));
            int fontstyle = Integer.parseInt(call.argument("fontstyle"));
            PosPrinterPluginHandler.addPrintLintStyle(align, fontSize, fontstyle);
        } else if (call.method.equals("addTexts")) {
            TRACE.d("addTexts");
            String textLeft = call.argument("textLeft");
            String textRight = call.argument("textRight");
            int rowLeft = Integer.parseInt(call.argument("rowLeft"));
            int rowRight = Integer.parseInt(call.argument("rowRight"));
            int positionLeft = Integer.parseInt(call.argument("positionLeft"));
            int positionRight = Integer.parseInt(call.argument("positionRight"));
            PosPrinterPluginHandler.addTexts(textLeft, textRight, rowLeft, rowRight, positionLeft, positionRight);
        } else if (call.method.equals("addBitmap")) {
            byte[] bitmapByte = call.argument("bitmap");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
            Boolean  isConvert = call.argument("isConvert");
            if (isConvert != null) {
                TRACE.d("isConvert exsit");
//                isConvert = (boolean) isConvert;
                PosPrinterPluginHandler.addBitmap(bitmap, isConvert);
            } else {
                TRACE.d("isConvert not exsit");
                PosPrinterPluginHandler.addBitmap(bitmap);
            }
        } else if (call.method.equals("setFooter")) {
           // int height = Integer.parseInt(call.argument("height"));
            int height = call.argument("height");
            PosPrinterPluginHandler.setFooter(height);
        }else if (call.method.equals("addQRCode")){
            int size = Integer.parseInt(call.argument("size"));
            String qrName = call.argument("qrName");
            String context = call.argument("context");
            int position = Integer.parseInt(call.argument("position"));
            PosPrinterPluginHandler.addQRCode(size,qrName,context,position);
        }else if(call.method.equals("addBarCode")){
            String barName = call.argument("BarName");
            int width = Integer.parseInt(call.argument("width"));
            int height = Integer.parseInt(call.argument("height"));
            String context = call.argument("context");
            int position = Integer.parseInt(call.argument("position"));
            PosPrinterPluginHandler.addBarCode(activity,barName, width, height,context, position);
        }else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        TRACE.d("FlutterPrinterQposPlugin：onDetachedFromEngine");
        mContext = null;
        PosPrinterPluginHandler.initPrinter(null);
        methodChannel.setMethodCallHandler(null);
        methodChannel = null;
        eventChannel.setStreamHandler(null);
        eventChannel = null;
    }
}
