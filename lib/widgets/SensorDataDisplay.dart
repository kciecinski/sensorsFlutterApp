import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/service/SensorService.dart';

class SesnsorDataDisplay extends StatefulWidget {
  
SesnsorDataDisplay({this.sensorName, this.onSnackbar, this.onWriteLog});

  final String sensorName;
  final Function onSnackbar;
  final Function onWriteLog;

  @override
  _SesnsorDataDisplayState createState() => _SesnsorDataDisplayState();
}

class _SesnsorDataDisplayState extends State<SesnsorDataDisplay> {
  Timer _everySecond;

  SensorService sensorService = SensorService();
  Map _values = {};
  bool isChanged = false;

  @override
  void initState() {
    super.initState();
    Map result = {};
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
      try {
        result = await sensorService.getValues(widget.sensorName);
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

  String buildLog(String text) {
    return "${DateTime.now()} ${text} \n";
  }
/*
  Future<void> checkLight() async {
    if(double.parse(_values['value']) >= 4.0) {
      if (isChanged == false) {
        widget.onWriteLog(buildLog("Telefon wyjęty z kiszeni"));
        isChanged = true;
      }
    } else {
      if (isChanged == true) {
        widget.onWriteLog(buildLog("Telefon w kiszeni"));
        isChanged = false;
      }
    }
  }
*/
  @override
  void dispose() {
    super.dispose();
    _everySecond.cancel();
  }

  @override
  Widget build(BuildContext context) {
    /*if(widget.sensorName == "Light") {
      checkLight();
      return ListTile(
        title: double.parse(_values['value']) >= 4.0 ? Text("Schowaj telefon urwisie") : Text("Telefon schowany"),
      );
    }
    */
    return ListTile(
      title: Text(widget.sensorName),
      subtitle: Text(_values.toString()),
    );
  }
}
