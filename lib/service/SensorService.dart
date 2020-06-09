import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/service/OtherService.dart';

class SensorService {
  static SensorService _instance = null;

  factory SensorService() {
    if (_instance == null) {
      _instance = new SensorService._constructor();
    }
    return _instance;
  }

  SensorService._constructor() {}

  List<Map> _sensorsInfo = [
    {"Name": "Gyroscope", "Method": "getGyroscopeValues"},
    {"Name": "Acceleromert", "Method": "getAccelerometrValues"},
    {"Name": "MagneticField", "Method": "getMagneticFieldValues"},
    {"Name": "Light", "Method": "getLightValues"}
  ];
  static const _platform = const MethodChannel('samples.flutter.dev/sensors');

  List<String> get sensorNames {
    return _sensorsInfo.map<String>((info) {return info['Name'];}).toList();
  }

  Future<Map> getValues(String sensorName) async {
    var sensorInfo = _sensorsInfo.firstWhere((element) => element['Name'] == sensorName);
    if(!sensorInfo.containsKey("Method")){
      throw("Invalid sensor info");
    }
    var method = sensorInfo['Method'];
    return await _platform.invokeMethod(method);
  }
}