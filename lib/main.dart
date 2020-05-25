import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:path_provider/path_provider.dart';
import 'package:wbudy_apka/page/SplashPage.dart';
import 'package:wbudy_apka/service/PermissionService.dart';
import 'package:wbudy_apka/widgets/SensorDataDisplay.dart';
import 'package:wbudy_apka/widgets/LocationDisplay.dart';
import 'package:permission_handler/permission_handler.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blueGrey,
      ),
      home: SplashPage(),
      routes: <String,WidgetBuilder> {
        '/home': (BuildContext context) => MyHomePage(title: 'Sensors Data')
      }
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;


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

  @override
  void initState() {
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      requestPermissionAndStartLocationService();
    });
  }

  Future requestPermissionAndStartLocationService() async {
    var permissionsService = PermissionService();
    bool granted = await permissionsService.askForGPSPermissions();
    if(granted) {
      const platform = const MethodChannel('samples.flutter.dev/gps');
      platform.invokeMethod('startGps');
    }
  }

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
        title: Text(widget.title),
        automaticallyImplyLeading: false,
      ),
      key: _scaffoldKey,
      body: SingleChildScrollView(child: 
        ListView(
          shrinkWrap: true,
          children:[
            ListView(
                shrinkWrap: true,
                children: _sensorsInfo.map((sensorInfo) => SesnsorDataDisplay(sensorMethod: sensorInfo['Method'], sensorName: sensorInfo['Name'], onWriteLog: writeLog, onSnackbar: onSnackbar,) ).toList()
            ),
            LocationDisplay()
          ]
        )
      ),
    );
  }

}