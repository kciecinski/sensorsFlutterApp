import 'dart:async';
import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/model/LatLong.dart';
import 'package:wbudy_apka/service/LocationService.dart';

class LocationDisplay extends StatefulWidget{

  LocationDisplay();


  @override
  _LocationDisplayState createState() => _LocationDisplayState();
}

class _LocationDisplayState extends State<LocationDisplay> {
  Timer _everySecond;
  LocationService _locationService;
  HashMap<String,dynamic> _values = HashMap<String,dynamic>();
  bool _nmeaWorks = false;

  @override
  void initState() {
    super.initState();
    HashMap<String,dynamic> result = HashMap<String,dynamic>();
    _locationService = new LocationService();
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
    bool nmeaWorks = false;
    try {
      result = await _locationService.getPosition();
      if(result['isPositionAvailable'] == true) {
        nmeaWorks  = await _locationService.isNMEAWorks();
      }
    } on PlatformException catch (e) {
      print(e);
    }
    setState(() {
      _values = result;
      _nmeaWorks = nmeaWorks;
    });
  }
  );
  }


  @override
  void dispose() {
    super.dispose();
    _everySecond.cancel();
  }

  @override
  build(BuildContext context) {
    if(_values.isEmpty) {
      return ListView(
        shrinkWrap: true,
        children: <Widget>[
          ListTile(
            title: Text("Dane nie dostępne"),
          )
        ],
      );
    } else if(_values['isPositionAvailable'] == false) {
      return ListView(
        shrinkWrap: true,
        children: <Widget>[
          ListTile(
            title: Text("Pozycja nie dostępna"),
          )
        ],
      );
    }
    double lat = (_values['latlong'] as LatLong).latitude;
    double long = (_values['latlong'] as LatLong).longtitude;
    return ListView(
      shrinkWrap: true,
      children: <Widget>[
        ListTile(
            title: Text("Czy dane pchodzą z komunikatów NMEA"),
            subtitle: Text(_nmeaWorks ? "Tak" : "Nie")
        ),
        ListTile(
          title: Text("Wysokość"),
          subtitle: Text(_values['altitude'].toString()+" m")
        ),
        ListTile(
          title: Text("Współrzędne format użwany przez androida"),
          subtitle: Text(lat.toString()+ " "+long.toString()),
        ),
        ListTile(
          title: Text("Współrzędne"),
          subtitle: Text((_values['latlong'] as LatLong).displayStringCordinante()),
        )
      ]
    );
  }
}
