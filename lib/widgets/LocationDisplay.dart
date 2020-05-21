import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class LocationDisplay extends StatefulWidget{

  LocationDisplay();


  @override
  _LocationDisplayState createState() => _LocationDisplayState();
}

class _LocationDisplayState extends State<LocationDisplay> {
  Timer _everySecond;

  final String isPositionAvailableMethod = "isPositionAvailable";
  final String getPositionMethod = "getPosition";

  static const platform = const MethodChannel('samples.flutter.dev/gps');
  Map _values = {};

  @override
  void initState() {
    super.initState();
    Map result = {};
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
    try {
      result = await platform.invokeMethod(getPositionMethod);
    } on PlatformException catch (e) {
      print(e);
    }
    setState(() {
      _values = result;
    });
  }
  );
  }

  @override
  build(BuildContext context) {
    return ListTile(
      title: Text("location"),
      subtitle: Text(_values.toString()),
    );
  }
}
