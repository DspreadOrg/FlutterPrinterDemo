package com.dspread.flutter_printer_qpos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.action.printerservice.PrintStyle;

import com.dspread.print.device.PrinterDevice;
import com.dspread.print.device.PrinterManager;
import com.dspread.print.device.bean.PrintLineStyle;
import com.dspread.print.widget.PrintLine;

import com.dspread.print.widget.BitmapPrintLine;
import com.dspread.print.widget.PrintLine;
import com.dspread.print.widget.PrinterLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Typeface;

import io.flutter.plugin.common.EventChannel;

public class PosPrinterPluginHandler {
    protected static PrinterDevice mPrinter;
    private static PrintLineStyle printLineStyle;

    private static Context mContext;
    private static MyPrinterListener myPrinterListener = new MyPrinterListener();
    static EventChannel.EventSink mEvents;

    public static void initEventSender(EventChannel.EventSink events, Object arguments) {
        TRACE.d("initEventSender");

        mEvents = events;

    }

    public static void initPrinter(Context context) {
        mContext = context;
        PrinterManager instance = PrinterManager.getInstance();
        mPrinter = instance.getPrinter();
        mPrinter.initPrinter(mContext);
        mPrinter.setPrintListener(myPrinterListener);
        printLineStyle = new PrintLineStyle();
    }

    public static void close(){
        if(mPrinter != null) {
            mPrinter.close();
        }
    }

    public static void setAlign(String align) {
        if (align.equals("LEFT")) {
            printLineStyle.setAlign(PrintLine.LEFT);
        } else if (align.equals("CENTER")) {
            printLineStyle.setAlign(PrintLine.CENTER);
        } else if (align.equals("RIGHT")) {
            printLineStyle.setAlign(PrintLine.RIGHT);
        }

    }

    public static void setFontStyle(String bold) {
        if (bold.equals("NORMAL")) {
            printLineStyle.setFontStyle(PrintStyle.FontStyle.NORMAL);
        } else if (bold.equals("BOLD")) {
            printLineStyle.setFontStyle(PrintStyle.FontStyle.BOLD);
        } else if (bold.equals("ITALIC")) {
            printLineStyle.setFontStyle(PrintStyle.FontStyle.ITALIC);
        } else if (bold.equals("BOLD_ITALIC")) {
            printLineStyle.setFontStyle(PrintStyle.FontStyle.BOLD_ITALIC);
        }
    }

    public static void setFontSize(int fontsize) {
        printLineStyle.setFontSize(fontsize);
    }

    public static void setPrintStyle() {
        mPrinter.setPrintStyle(printLineStyle);
    }

    public static void setPrintDensity(int printDensityLevel) {
        try {
            mPrinter.setPrinterDensity(printDensityLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printText(String text) {
        try {
            mPrinter.printText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printBarCode(String symbology, int width, int height, String content, String position) {
        if (symbology.equals("CODE_128")) {

        }
        int positionValue = 0;
        if (position.equals("LEFT")) {
            positionValue = PrintLine.LEFT;
        } else if (position.equals("CENTER")) {
            positionValue = PrintLine.CENTER;
        } else if (position.equals("RIGHT")) {
            positionValue = PrintLine.RIGHT;
        }
        try {
            mPrinter.printBarCode(mContext, symbology, width, height, content, positionValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printQRCode(String errorLevel, int width, String content, String position) {
        if (errorLevel.equals("L")) {

        }
        int positionValue = 0;
        if (position.equals("LEFT")) {
            positionValue = PrintLine.LEFT;
        } else if (position.equals("CENTER")) {
            positionValue = PrintLine.CENTER;
        } else if (position.equals("RIGHT")) {
            positionValue = PrintLine.RIGHT;
        }
        try {
            mPrinter.printQRCode(mContext, errorLevel, width, content, positionValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printBitmap(Bitmap bitmap) {
        try {
          /*  PrinterLayout printerLayout = new PrinterLayout(mContext);
            BitmapPrintLine bitmapPrintLine = new BitmapPrintLine(bitmap, PrintLine.CENTER, true);
            printerLayout.addBitmap(bitmapPrintLine);
            Bitmap bitmap1 = printerLayout.viewToBitmap();*/
            mPrinter.printBitmap(mContext, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addText(String text) {
        try {
            mPrinter.addText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void print(Context context) {
        try {
            mPrinter.print(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addPrintLintStyle(int align, int fontSize, int fontStyle) {
        try {
            PrintLineStyle bean = new PrintLineStyle();
            bean.setFontStyle(fontStyle);
            bean.setFontSize(fontSize);
            bean.setAlign(align);
            mPrinter.addPrintLintStyle(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addTexts(String textLeft, String textRight, int rowLeft, int rowRight, int positionLeft, int positionRight) {
        try {
            mPrinter.addTexts(new String[]{textLeft, textRight}, new int[]{rowLeft, rowRight}, new int[]{positionLeft, positionRight});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addBitmap(Bitmap bitmap) {
        try {
//            Bitmap  bitmapSmall = setImgSize(bitmap,10);
            mPrinter.addBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addBitmap(Bitmap bitmap, boolean isConvert) {
        try {
            TRACE.d("handler addBitmap:isConvert:"+isConvert);
//            Bitmap  bitmapSmall = setImgSize(bitmap,10);
            PrinterLayout printerLayout = new PrinterLayout(mContext);
            BitmapPrintLine bitmapPrintLine = new BitmapPrintLine(bitmap, PrintLine.CENTER, isConvert);
            printerLayout.addBitmap(bitmapPrintLine);
            Bitmap bitmap1 = printerLayout.viewToBitmap();
            mPrinter.addBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setFooter(int height) {
        try {
            TRACE.d("setFooter height--:"+height);
            mPrinter.setFooter(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addQRCode(int size, String qrName, String context, int position) {
        try {
            mPrinter.addQRCode(size, qrName, context, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addBarCode(Context context, String barName, int width, int height,String content, int position) {
        try {
            mPrinter.addBarCode(context,barName,width,height,content,position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Bitmap setImgSize(Bitmap bm, float scale) {
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float k = ((float) scale) / width;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(k, k);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }


    public static byte[] HexStringToByteArray(String hexString) {//
        if (hexString == null || hexString.equals("")) {
            return new byte[]{};
        }
        if (hexString.length() == 1 || hexString.length() % 2 != 0) {
            hexString = "0" + hexString;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}

