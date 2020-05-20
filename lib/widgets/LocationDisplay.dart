import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class LocationDisplay extends StatefulWidget {

  LocationDisplay();

  final String isPositionAvailableMethod = "isPositionAvailable";
  final String getPositionMethod = "getPosition";

  @override
  _LocationDisplayState createState() => _LocationDisplayState();
}

class _LocationDisplayState extends State<LocationDisplay> {
  Timer _everySecond;

  static const platform = const MethodChannel('samples.flutter.dev/gps');
  Map _values = {};

  @override
  void initState() {
    super.initState();
    Map result = {};
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
      try {
        result = await platform.invokeMethod(widget.isPositionAvailableMethod);
      } on PlatformException catch (e) {
        print(e);
      }
      if(result.containsKey(widget.isPositionAvailableMethod)) {
        if(result[widget.isPositionAvailableMethod].toString() == "true")
        {
          try {
            result = await platform.invokeMethod(widget.getPositionMethod);
          } on PlatformException catch (e) {
            print(e);
          }
          if(result.containsKey(widget.getPositionMethod)) {
            setState(() {
              _values = {widget.getPositionMethod:result[widget.getPositionMethod]};
            });
          }
        }
        else
          setState(() {
            _values = {widget.isPositionAvailableMethod:result[widget.isPositionAvailableMethod]};
          });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text("location"),
      subtitle: Text(_values.toString()),
    );
  }
}
