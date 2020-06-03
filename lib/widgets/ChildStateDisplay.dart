import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/service/ChildStateService.dart';

class ChildStateDisplay extends StatefulWidget{
  ChildStateDisplay();


  @override
  _ChildStateDisplayState createState() => _ChildStateDisplayState();
}

class _ChildStateDisplayState extends State<ChildStateDisplay> {
  Timer _everySecond;
  ChildStateService _childStateService = ChildStateService();
  double _distanceToSchool;

  @override
  void initState() {
    super.initState();
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
      var distanceToSchool = await _childStateService.distanceToSchool();
      setState(() {
        _distanceToSchool = distanceToSchool;
      });
    });
  }

  @override
  build(BuildContext context) {
    return ListView(
      shrinkWrap: true,
      children: <Widget>[
        ListTile(
          title: Text("ŹLE SIĘ LICZY. Odległość do szkoły"),
          subtitle: Text(_distanceToSchool.toString()+" m"),
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