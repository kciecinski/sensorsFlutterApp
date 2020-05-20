import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
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
      home: MyHomePage(title: 'Sensors Data'),
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


  @override
  void initState() {
    requestPermissionAndStartLocationService();
  }

  Future requestPermissionAndStartLocationService() async {
    List<PermissionGroup> permissonsList = List.of({PermissionGroup.location,PermissionGroup.locationAlways,PermissionGroup.locationWhenInUse});
    await PermissionHandler().requestPermissions(permissonsList);
    const platform = const MethodChannel('samples.flutter.dev/gps');
    platform.invokeMethod('startGps');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: SingleChildScrollView(child: 
        ListView(
          shrinkWrap: true,
          children:[
            ListView(
                shrinkWrap: true,
                children: _sensorsInfo.map((sensorInfo) => SesnsorDataDisplay(sensorMethod: sensorInfo['Method'], sensorName: sensorInfo['Name']) ).toList()
            ),
            LocationDisplay()
          ]
        )
      ),
    );
  }

}