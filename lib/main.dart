import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

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

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  Map<String, double> _gyro = {};
  Timer _everySecond;
  static const platform = const MethodChannel('samples.flutter.dev/gyroscope');

  void _setGryoscopeValues({double x, double y , double z}) {
    setState(() {
      _gyro = {"x": x, "y":y, "z":z};
    });
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    Map result = {};
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
      try {
        result = await platform.invokeMethod('getGyroscopeValues');
      } on PlatformException catch (e) {
        print(e);
      }
      _setGryoscopeValues(x: result['x'], y: result['y'], z: result['z']);
    });
  }



  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(_gyro.toString()),
          ],
        ),
      ),
    );
  }
}
