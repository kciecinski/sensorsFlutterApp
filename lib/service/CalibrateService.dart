
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
  static const String _isCalibratingWithoutEtuiMethod = "isCalibratingWithoutEtui";
  static const String _startCalibrateMotionDetectMethod = "startCalibrateMotionDetect";
  static const String _isCalibratingMotionDetectMethod = "isCalibratingMotionDetect";

  static const MethodChannel _platform = const MethodChannel('samples.fultter.dev/calibrate');

  Future startCalibrateWithoutEtui() async {
    await _platform.invokeMethod(_startCalibrateWithoutEtuiMethod);
  }
  Future startCalibrateMotionDetect() async {
    await _platform.invokeMethod(_startCalibrateMotionDetectMethod);
  }
  Future<bool> isCalibratingWithoutEtui() async {
    return await _platform.invokeMethod(_isCalibratingWithoutEtuiMethod);
  }
  Future<bool> isCalibratingMotionDetect() async {
    return await _platform.invokeMethod(_isCalibratingMotionDetectMethod);
  }
}