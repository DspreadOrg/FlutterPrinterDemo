
import 'dart:async';
import 'dart:convert';
import 'dart:typed_data';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:meta/meta.dart' show visibleForTesting;

import 'package:flutter_printer_qpos/QPOSPrintModel.dart';

enum PrintLine {
  LEFT,
  CENTER,
  RIGHT
}

enum FontStyle {
  NORMAL,
  BOLD,
  ITALIC,
  BOLD_ITALIC
}

enum Symbology {
  CODE_128,
  CODABAR,
  CODE_39,
  EAN_8,
  EAN_13,
  UPC_A,
  UPC_E
}

enum ErrorLevel {
  L,
  M,
  Q,
  H
}


class FlutterPrinterQpos {

  factory FlutterPrinterQpos(){
    if (_instance == null) {
      final MethodChannel methodChannel = const MethodChannel('flutter_printer_qpos');

      final EventChannel eventChannel = const EventChannel('flutter_printer_qpos_event');

      _instance = FlutterPrinterQpos.private(methodChannel, eventChannel);
    }
    return _instance!;
  }

  static FlutterPrinterQpos? _instance;

  final MethodChannel _methodChannel;
  final EventChannel _eventChannel;

  Stream<QPOSPrintModel>? _onPosPrintListenerCalled;

  Stream<QPOSPrintModel>? get onPosPrintListenerCalled {
    if(_onPosPrintListenerCalled == null) {
      _onPosPrintListenerCalled = _eventChannel
          .receiveBroadcastStream()
          .map((dynamic event) => _parsePosPrintListenerCall(event));
    }
    return _onPosPrintListenerCalled;
  }

  QPOSPrintModel _parsePosPrintListenerCall(String state) {
    QPOSPrintModel qposPrintModel = QPOSPrintModel.fromJson(json.decode(state));
    return qposPrintModel;
  }

  // static Future<String?> get platformVersion async {
  //   final String? version = await _methodChannel.invokeMethod('getPlatformVersion');
  //   return version;
  // }

  /// This constructor is only used for testing and shouldn't be accessed by
  /// users of the plugin. It may break or change at any time.
  @visibleForTesting
  FlutterPrinterQpos.private(this._methodChannel, this._eventChannel);

  void initPrinter(){
    _methodChannel.invokeMethod('initPrinter');
  }

  void setAlign(String align){
    Map<String, String> params = Map<String, String>();
    params['align'] = align;
    print('dart:setAlign'+align);
    _methodChannel.invokeMethod('setAlign',params);
  }

  void setFontStyle(String bold){
    Map<String, String> params = Map<String, String>();
    params['bold'] = bold;
    print('dart:setFontStyle'+bold);
    _methodChannel.invokeMethod('setFontStyle',params);
  }

  void setFontSize(int fontsize){
    Map<String, int> params = Map<String, int>();
    params['fontsize'] = fontsize;
    print('dart:setFontSize'+fontsize.toString());
    _methodChannel.invokeMethod('setFontSize',params);
  }

  void setPrintStyle(){
    print('dart:setFontSize');
    _methodChannel.invokeMethod('setPrintStyle');
  }

  void printDensityLevel(int printDensityLevel){
    Map<String, int> params = Map<String, int>();
    params['printDensityLevel'] = printDensityLevel;
    print('dart:printDensityLevel'+printDensityLevel.toString());
    _methodChannel.invokeMethod('printDensityLevel',params);
  }

  void printText(String text){
    Map<String, String> params = Map<String, String>();
    params['text'] = text;
    print('dart:printText'+text);
    _methodChannel.invokeMethod('printText',params);
  }

  void printBarCode(String symbology, String width, String height,String content,String position){
    Map<String, String> params = Map<String, String>();
    params['symbology'] = symbology;
    params['width'] = width;
    params['height'] = height;
    params['content'] = content;
    params['position'] = position;
    print('dart:printBarCode'+symbology+" "+width+" "+height+" "+content+" "+position);
    _methodChannel.invokeMethod('printBarCode',params);
  }

  void printQRCode(String errorLevel, String width, String content,String position){
    Map<String, String> params = Map<String, String>();
    params['errorLevel'] = errorLevel;
    params['width'] = width;
    params['content'] = content;
    params['position'] = position;
    print('dart:printQRCode'+errorLevel+" "+width+" "+content+" "+position);
    _methodChannel.invokeMethod('printQRCode',params);
  }

  void printBitmap(Uint8List bitmap){
    Map<String, Uint8List> params = Map<String, Uint8List>();
    params['bitmap'] = bitmap;
    print('dart:printText'+bitmap.toString());
    _methodChannel.invokeMethod('printBitmap',params);
  }


}
