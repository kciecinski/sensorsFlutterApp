import 'dart:io';

import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';
import 'package:wbudy_apka/widgets/SensorDataDisplay.dart';

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key}) : super(key: key);

  final String title = "Sensors";


  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  List<Map> _sensorsInfo = [
    {"Name": "Gyroscope", "Method": "getGyroscopeValues"},
    {"Name": "Acceleromert", "Method": "getAccelerometrValues"},
    {"Name": "MagneticField", "Method": "getMagneticFieldValues"},
    {"Name": "Light", "Method": "getLightValues"}
  ];
  final _scaffoldKey = new GlobalKey<ScaffoldState>();

  writeLog(String logText) async {
    final Directory directory = await getExternalStorageDirectory();
    final File file = File('${directory.path}/logs.txt');
    await file.writeAsString(logText, mode: FileMode.append);
  }

  onSnackbar(String text, [Color bgColor]) {
    _scaffoldKey.currentState.showSnackBar(
        SnackBar(content: Text(text), backgroundColor: bgColor,)
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title)
      ),
      key: _scaffoldKey,
      body: SingleChildScrollView(child:
      ListView(
          shrinkWrap: true,
          children:[
            ListView(
                shrinkWrap: true,
                children: _sensorsInfo.map((sensorInfo) => SesnsorDataDisplay(sensorMethod: sensorInfo['Method'], sensorName: sensorInfo['Name'], onWriteLog: writeLog, onSnackbar: onSnackbar,) ).toList()
            )
          ]
      )
      ),
    );
  }
}