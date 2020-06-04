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
  bool _shouldBeInSchool;
  bool _inSchool;

  @override
  void initState() {
    super.initState();
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
      var childState = await _childStateService.getChildState();
      var distanceToSchool = double.parse(childState["distanceToSchool"])*1000;
      bool shouldBeInSchool = (childState["shouldBeInSchool"] == "true");
      bool inSchool = (childState["inSchool"] == "true");
      setState(() {
        _distanceToSchool = distanceToSchool;
        _shouldBeInSchool = shouldBeInSchool;
        _inSchool = inSchool;
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
          subtitle: Text(_distanceToSchool != null ? _distanceToSchool.toStringAsFixed(2)+" m" : "-"),
        ),
        ListTile(
          title: Text("W szkole"),
          subtitle: Text(_inSchool != null ? (_inSchool ? "Tak" : "Nie") : "-"),
        ),
        ListTile(
          title: Text("Powinnien być w szkole"),
          subtitle: Text(_shouldBeInSchool != null ? (_shouldBeInSchool ? "Tak" : "Nie") : "-"),
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