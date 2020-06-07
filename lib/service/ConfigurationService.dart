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
  static String get OwnerParent { return "Parent"; }
  static String get OwnerChild { return "Child"; }
  final String _getSchoolPositionMethod = "getSchoolPosition";
  final String _setSchoolPositionMethod = "setSchoolPosition";
  final String _isAppConfiguredMethod = "isAppConfigured";
  final String _setAppConfiguredMethod = "setAppConfigured";
  final String _getDeviceOwnerMethod = "getDeviceOwner";
  final String _setDeviceOwnerMethod = "setDeviceOwner";
  final String _getSchoolStartAtMethod = "getSchoolStartAt";
  final String _setSchoolStartAtMethod = "setSchoolStartAt";
  final String _getSchoolEndAtMethod = "getSchoolEndAt";
  final String _setSchoolEndAtMethod = "setSchoolEndAt";
  final MethodChannel _platform = const MethodChannel("samples.flutter.dev/configuration");

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