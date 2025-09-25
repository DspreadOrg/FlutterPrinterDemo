import 'dart:collection';
import 'dart:convert';
import 'dart:typed_data';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_printer_qpos/flutter_printer_qpos.dart';
import 'package:flutter_printer_qpos/QPOSPrintModel.dart';
import '../Utils.dart';

class PrintItemsPage extends StatefulWidget {
  @override
  _PrintState createState() => _PrintState();
}

class _PrintState extends State<PrintItemsPage> {
  FlutterPrinterQpos _flutterPrinterQpos = FlutterPrinterQpos();
  StreamSubscription? _subscription;

  @override
  void initState() {
    super.initState();
    _flutterPrinterQpos.initPrinter();
    _subscription = _flutterPrinterQpos.onPosPrintListenerCalled!
        .listen((QPOSPrintModel datas) {
      parasPrintListener(datas);
    });
  }

  @override
  void dispose() {
    super.dispose();
    //取消监听
    if (_subscription != null) {
      _subscription!.cancel();
    }
  }

  @override
  Widget build(BuildContext context) {
    var content;
    Widget buttonSection = Container(
      child: Column(
        children: [
          ElevatedButton(
            style: ElevatedButton.styleFrom(
              minimumSize: const Size(250, 40),
              shape: const StadiumBorder(),
              textStyle: const TextStyle(fontSize: 18),
            ),
            onPressed: () async {
              printText();
            },
            child: Text("Print Text"),
          ),
          ElevatedButton(
            style: ElevatedButton.styleFrom(
              minimumSize: const Size(250, 40),
              shape: const StadiumBorder(),
              textStyle: const TextStyle(fontSize: 18),
            ),
            onPressed: () async {
              printBarcode();
            },
            child: Text("Print Barcode"),
          ),
          ElevatedButton(
            style: ElevatedButton.styleFrom(
              minimumSize: const Size(250, 40),
              shape: const StadiumBorder(),
              textStyle: const TextStyle(fontSize: 18),
            ),
            onPressed: () async {
              printQRcode();
            },
            child: Text("Print QRcode"),
          ),
          ElevatedButton(
            style: ElevatedButton.styleFrom(
              minimumSize: const Size(250, 40),
              shape: const StadiumBorder(),
              textStyle: const TextStyle(fontSize: 18),
            ),
            onPressed: () async {
              printPicture();
            },
            child: Text("Print Picture"),
          ),
          ElevatedButton(
            style: ElevatedButton.styleFrom(
              minimumSize: const Size(250, 40),
              shape: const StadiumBorder(),
              textStyle: const TextStyle(fontSize: 18),
            ),
            onPressed: () async {
              printReceipt();
            },
            child: Text("Print Receipt"),
          ),
          ElevatedButton(
            style: ElevatedButton.styleFrom(
              minimumSize: const Size(250, 40),
              shape: const StadiumBorder(),
              textStyle: const TextStyle(fontSize: 18),
            ),
            onPressed: () async {
              close();
            },
            child: Text("Close Printer"),
          )
        ],
      ),
    );
    return MaterialApp(
      home: Scaffold(
          appBar: AppBar(
            title: const Text('Print page'),
          ),
          body: ListView(
            children: [
              buttonSection,
            ],
            padding: const EdgeInsets.all(5.0),
          )),
    );
  }

  void printText() {
    _flutterPrinterQpos.setAlign(PrintLine.CENTER.name);
    _flutterPrinterQpos.setFontSize(16);
    _flutterPrinterQpos.setFontStyle(FontStyle.BOLD.name);
    _flutterPrinterQpos.setPrintStyle();
    _flutterPrinterQpos.setFooter(100);
    _flutterPrinterQpos.printText("12312312345678");
  }

  void printBarcode() {
    _flutterPrinterQpos.setFooter(200);
    _flutterPrinterQpos.printBarCode(Symbology.CODE_128.name, "400", "100",
        "test123", PrintLine.CENTER.name);

  }

  void printQRcode() {
    _flutterPrinterQpos.setFooter(100);
    _flutterPrinterQpos.printQRCode(
        ErrorLevel.L.name, "300", "test123", PrintLine.CENTER.name);

  }

  Future<void> printPicture() async {
    // final ByteData bytes = await rootBundle.load('assets/images/1/image.jpg');
    final ByteData bytes = await rootBundle.load('configs/test_store.jpg');
    final bitmip = bytes.buffer.asUint8List(0);
    _flutterPrinterQpos.setFooter(100);
    _flutterPrinterQpos.printBitmap(bitmip);
  }

  Future<void> printReceipt() async {
    _flutterPrinterQpos.addPrintLintStyle(PrintLine.CENTER.index.toString(),
        "16", FontStyle.BOLD.index.toString());
    _flutterPrinterQpos.addtext("DSPREAD");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.CENTER.index.toString(),
        "12", FontStyle.BOLD.index.toString());
    _flutterPrinterQpos.addtext("DSPREAD Technologies de Mexico, sapi de cv");
    _flutterPrinterQpos.addtext("Tel.(33) 2005 - 0207");

    _flutterPrinterQpos
        .addtext("Av. San Miguel 38, San Juan de Ocotn, C.P.45019.");
    _flutterPrinterQpos.addtext("Zapopan, Jalisco, Mexico.");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.CENTER.index.toString(),
        "16", FontStyle.NORMAL.index.toString());
    _flutterPrinterQpos.addtext("COPIA");

    final ByteData bytes = await rootBundle.load('configs/pos_picture.png');
    final bitmip = bytes.buffer.asUint8List(0);
    _flutterPrinterQpos.addBitmap(bitmip);

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.CENTER.index.toString(),
        "16", FontStyle.BOLD.index.toString());
    _flutterPrinterQpos.addtext("V E N T A  C H I P");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.CENTER.index.toString(),
        "12", FontStyle.NORMAL.index.toString());
    _flutterPrinterQpos.addtext("12/21/2023");

    _flutterPrinterQpos
        .addtext("- - - - - - - - - - - - - - - - - - - - - - - - - ");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.CENTER.index.toString(),
        "18", FontStyle.BOLD.index.toString());
    _flutterPrinterQpos.addtext("Pata Salada");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.CENTER.index.toString(),
        "14", FontStyle.NORMAL.index.toString());
    _flutterPrinterQpos.addtext("CALLE SAN JORGE 227, RESIDENCIA");

    _flutterPrinterQpos.addtext("CONJUNTO PATRIA,45160 Zapopan, Jalisco.");

    _flutterPrinterQpos.addtext("Mxico");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.CENTER.index.toString(),
        "16", FontStyle.BOLD.index.toString());

    _flutterPrinterQpos.addtext("VENTA");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.CENTER.index.toString(),
        "14", FontStyle.NORMAL.index.toString());

    _flutterPrinterQpos.addtext("No.de Terminal .44444444");

    _flutterPrinterQpos.addtext("No.Afiliacidn -7659945");

    _flutterPrinterQpos
        .addtext("- - - - - - - - - - - - - - - - - - - - - - - - - ");

    _flutterPrinterQpos.addTexts("No. de tarjeta", "Vigencia", "1", "1",
        PrintLine.LEFT.index.toString(), PrintLine.RIGHT.index.toString());

    _flutterPrinterQpos.addTexts("6467", "01/30", "1", "1",
        PrintLine.LEFT.index.toString(), PrintLine.RIGHT.index.toString());

    _flutterPrinterQpos
        .addtext("-  - - - - - - - - - - - - - - - - - - - - - - - ");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.CENTER.index.toString(),
        "18", FontStyle.BOLD.index.toString());

    _flutterPrinterQpos.addtext("Transaccion Aprobada");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.LEFT.index.toString(), "14",
        FontStyle.NORMAL.index.toString());

    _flutterPrinterQpos.addtext("DEBITOVISA------");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.LEFT.index.toString(), "14",
        FontStyle.NORMAL.index.toString());

    _flutterPrinterQpos.addtext("BANCOMER");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.LEFT.index.toString(), "14",
        FontStyle.NORMAL.index.toString());

    _flutterPrinterQpos.addtext("AUT: 290307");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.LEFT.index.toString(), "14",
        FontStyle.NORMAL.index.toString());

    _flutterPrinterQpos.addtext("REF:920567812703");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.LEFT.index.toString(), "14",
        FontStyle.NORMAL.index.toString());

    _flutterPrinterQpos.addtext("NUMCONTROL:");

    _flutterPrinterQpos.addPrintLintStyle(PrintLine.LEFT.index.toString(), "14",
        FontStyle.NORMAL.index.toString());
    _flutterPrinterQpos.addtext("IM21081619398D322149946A79B66");

    _flutterPrinterQpos.addQRCode("300", Barcode2D.QR_CODE.name, "123",
        PrintLine.CENTER.index.toString());
    _flutterPrinterQpos.addBarCode(Barcode1D.CODE_128.name, "400", "100",
        "context", PrintLine.CENTER.index.toString());
    _flutterPrinterQpos
        .addtext("                                                ");
    _flutterPrinterQpos
        .addtext("                                                ");
    _flutterPrinterQpos.printReceipt();
  }

  void close() {
    _flutterPrinterQpos.close();
  }

  void parasPrintListener(QPOSPrintModel datas) {
    String? method = datas.method;
    List<String> paras = new List.empty();
    String? parameters = datas.parameters;
    if (parameters != null && parameters.length > 0) {
      paras = parameters.split("||");
    }
    if (method == "printResult") {
      print("printResult:" + parameters!);
      String isSuccess = paras.elementAt(0);
      String status = paras.elementAt(1);
      print("printResult status:" + status);
    } else {
      print("method:" + method!);
    }
  }

  void showCupertinoDialogSure(status) {
    var dialog = CupertinoAlertDialog(
      title: Text('Printer Tip', style: TextStyle(fontSize: 20)),
      content: Text(
        status,
        style: TextStyle(fontSize: 16),
      ),
      actions: <Widget>[
        CupertinoButton(
          child: Text("Close"),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
      ],
    );

    showDialog(
        barrierDismissible: false, context: context, builder: (_) => dialog);
  }
}
