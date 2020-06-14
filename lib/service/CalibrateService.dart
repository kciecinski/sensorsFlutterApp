
import 'package:flutter/services.dart';

class CalibrateService {
  static CalibrateService _instance = null;

  factory CalibrateService() {
    if (_instance == null) {
      _instance = new CalibrateService._constructor();
    }
    return _instance;
  }

  CalibrateService._constructor() {}
  static const String _startCalibrateWithoutEtuiMethod = "startCalibrateWithoutEtui";
  static const String _isCalibratedWithoutEtuiMethod = "isCalibratedWithoutEtui";
  static const String _isCalibratingWithoutEtuiMethod = "isCalibratingWithoutEtui";
  static const MethodChannel _platform = const MethodChannel('samples.fultter.dev/calibrate');

  Future startCalibrateWithoutEtui() async {
    await _platform.invokeMethod(_startCalibrateWithoutEtuiMethod);
  }
  Future<bool> isCalibratedWithoutEtui() async {
    return await _platform.invokeMethod(_isCalibratedWithoutEtuiMethod);
  }
  Future<bool> isCalibratingWithoutEtui() async {
    return await _platform.invokeMethod(_isCalibratingWithoutEtuiMethod);
  }
}