import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/model/LatLong.dart';
import 'package:wbudy_apka/service/ChildStateService.dart';
import 'package:wbudy_apka/service/ConfigurationService.dart';
import 'package:wbudy_apka/service/LocationService.dart';

class ChildStateDisplay extends StatefulWidget{
  ChildStateDisplay();


  @override
  _ChildStateDisplayState createState() => _ChildStateDisplayState();
}

class _ChildStateDisplayState extends State<ChildStateDisplay> {
  Timer _everySecond;
  ChildStateService _childStateService = ChildStateService();
  LocationService _locationService = LocationService();
  ConfigurationService _configurationService = ConfigurationService();
  double _distanceToSchool;
  LatLong _schoolPos = LatLong(0,0);
  LatLong _currentPos = LatLong(0,0);

  @override
  void initState() {
    super.initState();
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
      var distanceToSchool = await _childStateService.distanceToSchool();

      var schoolPos = await _configurationService.getSchoolLocation();
      var currentPos = (await _locationService.getPosition())['latlong'] as LatLong;

      setState(() {
        _distanceToSchool = distanceToSchool;
        _schoolPos = schoolPos;
        _currentPos = currentPos;
      });
    });
  }

  @override
  build(BuildContext context) {
    return ListView(
      shrinkWrap: true,
      children: <Widget>[
        ListTile(
          title: Text("Odległość do szkoły"),
          subtitle: Text(_distanceToSchool.toStringAsFixed(2)+" m"),
        )
      ],
    );
  }

  @override
  void dispose() {
    super.dispose();
    _everySecond.cancel();
  }


}