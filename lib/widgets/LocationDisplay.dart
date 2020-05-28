import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/service/LocationService.dart';

class LocationDisplay extends StatefulWidget{

  LocationDisplay();


  @override
  _LocationDisplayState createState() => _LocationDisplayState();
}

class _LocationDisplayState extends State<LocationDisplay> {
  Timer _everySecond;
  LocationService _locationService;
  Map _values = {};

  @override
  void initState() {
    super.initState();
    Map result = {};
    _locationService = new LocationService();
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
    try {
      result = await _locationService.getPosition();
    } on PlatformException catch (e) {
      print(e);
    }
    setState(() {
      _values = result;
    });
  }
  );
  }

  @override
  build(BuildContext context) {
    return ListTile(
      title: Text("location"),
      subtitle: Text(_values.toString()),
    );
  }
}
