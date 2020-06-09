import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/model/LatLong.dart';

class ConfigurationService{
  static ConfigurationService _instance = null;
  factory ConfigurationService() {
    if(_instance == null) {
      _instance = new ConfigurationService._constructor();
    }
    return _instance;
  }
  ConfigurationService._constructor() {}
  static const String OwnerParent = "Parent";
  static const String OwnerChild = "Child";
  static const String _getSchoolPositionMethod = "getSchoolPosition";
  static const String _setSchoolPositionMethod = "setSchoolPosition";
  static const String _isAppConfiguredMethod = "isAppConfigured";
  static const String _setAppConfiguredMethod = "setAppConfigured";
  static const String _getDeviceOwnerMethod = "getDeviceOwner";
  static const String _setDeviceOwnerMethod = "setDeviceOwner";
  static const String _getSchoolStartAtMethod = "getSchoolStartAt";
  static const String _setSchoolStartAtMethod = "setSchoolStartAt";
  static const String _getSchoolEndAtMethod = "getSchoolEndAt";
  static const String _setSchoolEndAtMethod = "setSchoolEndAt";
  static const MethodChannel _platform = const MethodChannel("samples.flutter.dev/configuration");

  Future<bool> isAppConfigured() async {
    return await _platform.invokeMethod(_isAppConfiguredMethod);
  }

  Future setAppConfigured(bool configured) async {
    await _platform.invokeMethod(_setAppConfiguredMethod,{'configured':configured});
  }

  Future<LatLong> getSchoolPosition() async {
    var result = await _platform.invokeMethod(_getSchoolPositionMethod);
    return LatLong(result['lat'],result['long']);
  }

  Future setSchoolLocation(LatLong latLong) async {
    await _platform.invokeMethod(_setSchoolPositionMethod,{'lat':latLong.latitude, "long": latLong.longtitude});
  }

  Future<String> getDeviceOwner() async {
    return await _platform.invokeMethod(_getDeviceOwnerMethod);
  }

  Future setDeviceOwner(String deviceOwner) async {
    await _platform.invokeMethod(_setDeviceOwnerMethod,{"deviceOwner":deviceOwner});
  }

  Future<TimeOfDay> getSchoolStartAt() async {
    var result = await _platform.invokeMethod(_getSchoolStartAtMethod);
    var h = result['h'];
    var m = result['m'];
    return TimeOfDay(hour: h, minute: m);
  }

  Future setSchoolStartAt(TimeOfDay timeOfDay) async {
    await _platform.invokeMethod(_setSchoolStartAtMethod,{"h":timeOfDay.hour,"m":timeOfDay.minute});
  }

  Future<TimeOfDay> getSchoolEndAt() async {
    var result = await _platform.invokeMethod(_getSchoolEndAtMethod);
    var h = result['h'];
    var m = result['m'];
    return TimeOfDay(hour: h, minute: m);
  }

  Future setSchoolEndAt(TimeOfDay timeOfDay) async {
    await _platform.invokeMethod(_setSchoolEndAtMethod,{"h":timeOfDay.hour,"m":timeOfDay.minute});
  }

}