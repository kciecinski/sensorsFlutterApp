
import 'package:flutter/services.dart';

class OtherService {
  static OtherService _instance = null;

  factory OtherService() {
    if (_instance == null) {
      _instance = new OtherService._constructor();
    }
    return _instance;
  }

  OtherService._constructor() {}
  static const String _isAvailableMethod = "isAvailable";
  static const String _startService = "startService";
  static const String _stopService = "stopService";
  static const MethodChannel _platform = const MethodChannel('samples.flutter.dev/other');

  Future<bool> isAvailable() async {
    return await _platform.invokeMethod(_isAvailableMethod);
  }
  Future startService() async {
    await _platform.invokeMethod(_startService);
  }
  Future stopService() async {
    await _platform.invokeMethod(_stopService);
  }
}