import 'dart:async';

import 'package:flutter/material.dart';
import 'package:wbudy_apka/service/ChildStateService.dart';

class ChildStateDisplay extends StatefulWidget{
  ChildStateDisplay();


  @override
  _ChildStateDisplayState createState() => _ChildStateDisplayState();
}

class _ChildStateDisplayState extends State<ChildStateDisplay> {
  Timer _everySecond;
  ChildStateService _childStateService = ChildStateService();
  double distanceToSchool;
  bool shouldBeInSchool;
  bool inSchool;
  String isWithoutEtui;
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
      String _isWithoutEtui = childState['isWithoutEtui'];
      bool _isPhoneHidden = (childState['isPhoneHidden'] == "true");
      bool _isInMotion = (childState['isInMotion'] == "true");
      setState(() {
        if(_isWithoutEtui == 'DO_NOT_HAVE_MAGNETIC' || _isWithoutEtui == 'DO_NOT_HAVE_ANY'){
          isWithoutEtui = "Telefon nie posiada etui";
        }
        else if(_isWithoutEtui == 'WITH') {
          isWithoutEtui = "Nie";
        }
        else if(_isWithoutEtui == 'WITHOUT') {
          isWithoutEtui = "Tak";
        }
        distanceToSchool = _distanceToSchool;
        shouldBeInSchool = _shouldBeInSchool;
        inSchool = _inSchool;
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
          subtitle: Text(isWithoutEtui != null ? (isWithoutEtui) : "-"),
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