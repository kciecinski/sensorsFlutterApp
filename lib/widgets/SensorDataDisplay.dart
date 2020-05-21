import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class SesnsorDataDisplay extends StatefulWidget {
  
SesnsorDataDisplay({this.sensorMethod, this.sensorName});

  final String sensorMethod;
  final String sensorName;

  @override
  _SesnsorDataDisplayState createState() => _SesnsorDataDisplayState();
}

class _SesnsorDataDisplayState extends State<SesnsorDataDisplay> {
  Timer _everySecond;

  static const platform = const MethodChannel('samples.flutter.dev/sensors');
  Map _values = {};

  @override
  void initState() {
    super.initState();
    Map result = {};
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
      try {
        result = await platform.invokeMethod(widget.sensorMethod);
      } on PlatformException catch (e) {
        print(e);
      }
      if(result.containsKey('value')) {
        _setSingleValue(val: result['value']);
      } else {
        _setValues(x: result['x'], y: result['y'], z:result['z']);
      }
    });
  }

  void _setValues({double x, double y , double z}) {
    setState(() {
      _values = {"x": x?.toStringAsPrecision(3), "y":y?.toStringAsPrecision(3), "z":z?.toStringAsPrecision(3)};
    });
  }

    void _setSingleValue({double val}) {
    setState(() {
      _values = {"value": val?.toStringAsPrecision(3)};
    });
  }

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(widget.sensorName),
      subtitle: Text(_values.toString()),
    );
  }
}
