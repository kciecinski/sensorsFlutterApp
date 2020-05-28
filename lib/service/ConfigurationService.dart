import 'package:shared_preferences/shared_preferences.dart';

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
    return false;
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
}