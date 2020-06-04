import 'package:wbudy_apka/model/LatLong.dart';
import 'package:wbudy_apka/service/ConfigurationService.dart';
import 'package:wbudy_apka/service/LocationService.dart';

class ChildStateService {
  static ChildStateService _instance = null;

  factory ChildStateService() {
    if (_instance == null) {
      _instance = new ChildStateService._constructor();
    }
    return _instance;
  }
  ConfigurationService _configurationService = ConfigurationService();
  LocationService _locationService = LocationService();
  ChildStateService._constructor() {}

  Future<double> distanceToSchool() async {
    if(await _locationService.isPositionAvailable() && await _configurationService.getDeviceOwner() == ConfigurationService.OwnerChild) {
      var result = await _locationService.getPosition();
      LatLong currentPos = result['latlong'];
      LatLong schoolPos = await _configurationService.getSchoolLocation();
      return schoolPos.distanceInKilometers(currentPos)*1000;
    }
    return 0;
  }
}