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
  ConfigurationService._constructor();
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
  static const String _isHaveEtuiMethod = "isHaveEtui";
  static const String _setHaveEtuiMethod = "setHaveEtui";
  static const String _isConfiguredEtuiMethod = "isConfiguredEtui";
  static const String _setConfiguredEtuiMethod = "setConfiguredEtui";
  static const String _isConfiguredSchoolMethod = "isConfiguredSchool";
  static const String _setConfiguredSchoolMethod = "setConfiguredSchool";
  static const String _isConfiguredMotionDetectorMethod = "isConfiguredMotionDetector";
  static const String _setConfiguredMotionDetectorMethod = "setConfiguredMotionDetector";

  static const MethodChannel _platform = const MethodChannel("samples.flutter.dev/configuration");

  Future<bool> isAppConfigured() async {
    return await _platform.invokeMethod(_isAppConfiguredMethod);
  }

  Future setAppConfigured(bool configured) async {
    print("setAppConfigured");
    await _platform.invokeMethod(_setAppConfiguredMethod,{'configured':configured});
  }

  Future<LatLong> getSchoolPosition() async {
    var result = await _platform.invokeMethod(_getSchoolPositionMethod);
    return LatLong(result['lat'],result['long']);
  }

  Future setSchoolLocation(LatLong latLong) async {
    print("setSchoolLocation");
    await _platform.invokeMethod(_setSchoolPositionMethod,{'lat':latLong.latitude, "long": latLong.longtitude});
  }

  Future<String> getDeviceOwner() async {
    return await _platform.invokeMethod(_getDeviceOwnerMethod);
  }

  Future setDeviceOwner(String deviceOwner) async {
    print("setDeviceOwner");
    await _platform.invokeMethod(_setDeviceOwnerMethod,{"deviceOwner":deviceOwner});
  }

  Future<TimeOfDay> getSchoolStartAt() async {
    var result = await _platform.invokeMethod(_getSchoolStartAtMethod);
    var h = result['h'];
    var m = result['m'];
    return TimeOfDay(hour: h, minute: m);
  }

  Future setSchoolStartAt(TimeOfDay timeOfDay) async {
    print("setSchoolStartAt");
    await _platform.invokeMethod(_setSchoolStartAtMethod,{"h":timeOfDay.hour,"m":timeOfDay.minute});
  }

  Future<TimeOfDay> getSchoolEndAt() async {
    var result = await _platform.invokeMethod(_getSchoolEndAtMethod);
    var h = result['h'];
    var m = result['m'];
    return TimeOfDay(hour: h, minute: m);
  }

  Future setSchoolEndAt(TimeOfDay timeOfDay) async {
    print("setSchoolEndAt");
    await _platform.invokeMethod(_setSchoolEndAtMethod,{"h":timeOfDay.hour,"m":timeOfDay.minute});
  }

  Future<bool> isHaveEtui() async {
    return await _platform.invokeMethod(_isHaveEtuiMethod);
  }

  Future setHaveEtui(bool value) async {
    print("setHaveEtui");
    await _platform.invokeMethod(_setHaveEtuiMethod,{"value":value});
  }

  Future<bool> isConfiguredAll() async {
    var etui = await isConfiguredEtui();
    var school = await isConfiguredSchool();
    var motiondetector = await isConfiguredMotionDetector();
    return etui && school && motiondetector;
  }

  Future<bool> isConfiguredEtui() async {
    return await _platform.invokeMethod(_isConfiguredEtuiMethod);
  }

  Future setConfiguredEtui(bool value) async {
    print("setConfiguredEtui");
    await _platform.invokeMethod(_setConfiguredEtuiMethod,{"value":value});
  }
  Future<bool> isConfiguredSchool() async {
    return await _platform.invokeMethod(_isConfiguredSchoolMethod);
  }

  Future setConfiguredSchool(bool value) async {
    print("setConfiguredSchool");
    await _platform.invokeMethod(_setConfiguredSchoolMethod,{"value":value});
  }

  Future<bool> isConfiguredMotionDetector() async {
    return await _platform.invokeMethod(_isConfiguredMotionDetectorMethod);
  }

  Future setConfiguredMotionDetector(bool value) async {
    print("setConfiguredMotionDetector");
    await _platform.invokeMethod(_setConfiguredMotionDetectorMethod,{"value":value});
  }
}