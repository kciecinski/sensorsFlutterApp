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
  double distanceToSchool;
  bool shouldBeInSchool;
  bool inSchool;
  bool isWithoutEtui;
  bool isPhoneHidden;
  bool isInMotion;

  @override
  void initState() {
    super.initState();
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
      var childState = await _childStateService.getChildState();
      var _distanceToSchool = double.parse(childState["distanceToSchool"])*1000;
      bool _shouldBeInSchool = (childState["shouldBeInSchool"] == "true");
      bool _inSchool = (childState["inSchool"] == "true");
      bool _isWithoutEtui = (childState['isWithoutEtui'] == "true");
      bool _isPhoneHidden = (childState['isPhoneHidden'] == "true");
      bool _isInMotion = (childState['isInMotion'] == "true");
      setState(() {
        distanceToSchool = _distanceToSchool;
        shouldBeInSchool = _shouldBeInSchool;
        inSchool = _inSchool;
        isWithoutEtui = _isWithoutEtui;
        isPhoneHidden = _isPhoneHidden;
        isInMotion = _isInMotion;
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
          subtitle: Text(distanceToSchool != null ? distanceToSchool.toStringAsFixed(2)+" m" : "-"),
        ),
        ListTile(
          title: Text("W szkole"),
          subtitle: Text(inSchool != null ? (inSchool ? "Tak" : "Nie") : "-"),
        ),
        ListTile(
          title: Text("Powinnien być w szkole"),
          subtitle: Text(shouldBeInSchool != null ? (shouldBeInSchool ? "Tak" : "Nie") : "-"),
        ),
        ListTile(
          title: Text("Etui zdjęte"),
          subtitle: Text(isWithoutEtui != null ? (isWithoutEtui ? "Tak" : "Nie") : "-"),
        ),
        ListTile(
          title: Text("Telefon schowany"),
          subtitle: Text(isPhoneHidden != null ? (isPhoneHidden ? "Tak" : "Nie") : "-"),
        ),
        ListTile(
          title: Text("Telefon w ruchu"),
          subtitle: Text(isInMotion != null ? (isInMotion ? "Tak" : "Nie") : "-"),
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