import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:wbudy_apka/model/LatLong.dart';

class ConfigurationService{
  static ConfigurationService _instance = null;
  factory ConfigurationService() {
    if(_instance == null) {
      _instance = new ConfigurationService._constructor();
    }
    print(_instance);
    return _instance;
  }
  ConfigurationService._constructor() {
    print("ConfigurationService constructor");
    _prefsLoaded = false;
  }

  static String get OwnerParent { return "Parent"; }
  static String get OwnerChild { return "Child"; }

  bool _prefsLoaded;
  SharedPreferences _prefs;

  Future reloadPreferences() async {
    this._prefs = await SharedPreferences.getInstance();
    this._prefsLoaded = true;
  }

  Future<bool> isAppConfigured() async {
    if(!_prefsLoaded) {
      await reloadPreferences();
    }
    var key = "appConfigured";
    if (_prefs.containsKey(key)) {
      return _prefs.getBool(key);
    } else {
      _prefs.setBool(key, false);
      return false;
    }
  }

  Future setAppConfigured(bool configured) async {
    if(!_prefsLoaded) {
      await reloadPreferences();
    }
    var key = "appConfigured";
    _prefs.setBool(key, configured);
  }

  Future<String> getDeviceOwner() async {
    if(!_prefsLoaded) {
      await reloadPreferences();
    }
    var key = "deviceOwner";
    if (_prefs.containsKey(key)) {
      return _prefs.getString(key);
    }
    throw(key + "is not configured");
  }

  Future setDeviceOwner(String deviceOwner) async {
    if(!_prefsLoaded) {
      await reloadPreferences();
    }
    var key = "deviceOwner";
    _prefs.setString(key, deviceOwner);
  }

  Future<TimeOfDay> getSchoolStartAt() async {
    if(!_prefsLoaded) {
      await reloadPreferences();
    }
    var keyH = "hourStart";
    var keyM = "minuteStart";
    if(_prefs.containsKey(keyH) && _prefs.containsKey(keyM)) {
      var h = _prefs.getInt(keyH);
      var m = _prefs.getInt(keyM);
      return TimeOfDay(hour: h, minute: m);
    }
    throw("Value not exist");
  }

  Future setSchoolStartAt(TimeOfDay timeOfDay) async {
    if(!_prefsLoaded) {
      await reloadPreferences();
    }
    var keyH = "hourStart";
    var keyM = "minuteStart";
    _prefs.setInt(keyH, timeOfDay.hour);
    _prefs.setInt(keyM, timeOfDay.minute);
  }

  Future<TimeOfDay> getSchoolEndAt() async {
    if(!_prefsLoaded) {
      await reloadPreferences();
    }
    var keyH = "hourEnd";
    var keyM = "minuteEnd";
    if(_prefs.containsKey(keyH) && _prefs.containsKey(keyM)) {
      var h = _prefs.getInt(keyH);
      var m = _prefs.getInt(keyM);
      return TimeOfDay(hour: h, minute: m);
    }
    throw("Value not exist");
  }

  Future setSchoolEndAt(TimeOfDay timeOfDay) async {
    if(!_prefsLoaded) {
      await reloadPreferences();
    }
    var keyH = "hourEnd";
    var keyM = "minuteEnd";
    _prefs.setInt(keyH, timeOfDay.hour);
    _prefs.setInt(keyM, timeOfDay.minute);
  }

  Future<LatLong> getSchoolLocation() async {
    if(!_prefsLoaded) {
      await reloadPreferences();
    }
    var keyLat = "latSchool";
    var keyLong = "longSchool";
    if(_prefs.containsKey(keyLat) && _prefs.containsKey(keyLong)) {
      var lat = _prefs.getDouble(keyLat);
      var long = _prefs.getDouble(keyLong);
      return new LatLong(lat, long);
    }
    throw("Value not exist");
  }

  Future setSchoolLocation(LatLong latLong) async {
    if(!_prefsLoaded) {
      await reloadPreferences();
    }
    var keyLat = "latSchool";
    var keyLong = "longSchool";
    _prefs.setDouble(keyLat, latLong.latitude);
    _prefs.setDouble(keyLong, latLong.longtitude);
  }
}