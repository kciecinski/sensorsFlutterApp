import 'dart:io';

import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';
import 'package:wbudy_apka/service/SensorService.dart';
import 'package:wbudy_apka/widgets/SensorDataDisplay.dart';

class SensorsPage extends StatefulWidget {
  SensorsPage({Key key}) : super(key: key);

  final String title = "Sensors";


  @override
  _SensorsPageState createState() => _SensorsPageState();
}

class _SensorsPageState extends State<SensorsPage> {

  SensorService sensorService = SensorService();
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
                children: sensorService.sensorNames.map((sensorName) => SesnsorDataDisplay(sensorName: sensorName, onWriteLog: writeLog, onSnackbar: onSnackbar,) ).toList()
            )
          ]
      )
      ),
    );
  }
}