
import 'package:permission_handler/permission_handler.dart';

class PermissionService {
  static PermissionService _instance = null;
  factory PermissionService() {
    if(_instance == null) {
      _instance = new PermissionService._constructor();
    }
    print(_instance);
    return _instance;
  }
  PermissionService._constructor() {}

  List<PermissionGroup> _gpsPermissionsList = List.of({PermissionGroup.location,PermissionGroup.locationAlways,PermissionGroup.locationWhenInUse});

  Future<bool> askForPermissions(List<PermissionGroup> listPermissions) async {
    bool granted = true;
    var permissions = await PermissionHandler().requestPermissions(listPermissions);
    permissions.forEach((p, val) {
      if(val != PermissionStatus.granted && p != PermissionGroup.location) {
        granted = false;
      }
    });
    return granted;
  }

  Future<bool> askForGPSPermissions() async {
    print(_gpsPermissionsList);
    return askForPermissions(_gpsPermissionsList);
  }
}