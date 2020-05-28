
import 'dart:collection';

import 'package:flutter/services.dart';

class LocationService {
  static LocationService _instance = null;
  factory LocationService() {
    if(_instance == null) {
      _instance = new LocationService._constructor();
    }
    print(_instance);
    return _instance;
  }
  LocationService._constructor() {}
  final String _isPositionAvailableMethod = "isPositionAvailable";
  final String _getPositionMethod = "getPosition";
  final String _startGpsMethod = "startGps";
  final MethodChannel _platform = const MethodChannel('samples.flutter.dev/gps');

  Future startGps() async {
    await _platform.invokeMethod(_startGpsMethod);
  }

  Future<dynamic> getPosition() async {
    return await _platform.invokeMethod(_getPositionMethod);
  }

  Future<bool> isPositionAvailable() async {
    return await _platform.invokeMethod(_isPositionAvailableMethod);
  }

}