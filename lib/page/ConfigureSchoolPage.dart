import 'dart:collection';
import 'package:flutter/material.dart';
import 'package:nominatim_location_picker/nominatim_location_picker.dart';
import 'package:wbudy_apka/model/LatLong.dart';
import 'package:wbudy_apka/service/ConfigurationService.dart';

class ConfigureSchoolPage extends StatefulWidget {
  ConfigureSchoolPage({Key key}) : super(key: key);
  ConfigurationService configurationService = ConfigurationService();
  @override
  _ConfigureSchoolPageState createState() => _ConfigureSchoolPageState();
}

class _ConfigureSchoolPageState extends State<ConfigureSchoolPage> {

  TimeOfDay schoolStartsAt = TimeOfDay.fromDateTime(DateTime.now());
  bool schoolStartsAtSelected = false;
  TimeOfDay schoolEndsAt = TimeOfDay.fromDateTime(DateTime.now());
  bool schoolEndsAtSelected = false;
  double latitude = 0;
  double longitude = 0;
  bool selectedPlace = false;
  bool schoolConfigured = false;
  bool get selectedAll {
    return selectedPlace && schoolEndsAtSelected && schoolStartsAtSelected;
  }

  Future loadConfiguration() async {
    var _schoolConfigured = await widget.configurationService.isConfiguredSchool();
    if(_schoolConfigured) {
      var _schoolStartAt = await widget.configurationService.getSchoolStartAt();
      var _schoolEndAt = await widget.configurationService.getSchoolEndAt();
      var _schoolPosition = await widget.configurationService.getSchoolPosition();
      setState(() {
        schoolStartsAt = _schoolStartAt;
        schoolStartsAtSelected = true;
        schoolEndsAt = _schoolEndAt;
        schoolEndsAtSelected = true;
        selectedPlace = true;
        latitude = _schoolPosition.latitude;
        longitude = _schoolPosition.longtitude;
        widget.configurationService.setConfiguredSchool(selectedAll);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    loadConfiguration();
    MaterialLocalizations localizations = MaterialLocalizations.of(context);
    return WillPopScope(
      onWillPop: () async => selectedAll,
      child: Scaffold(
        appBar: AppBar(
          title: Text("Szkoła"),
          automaticallyImplyLeading: selectedAll,
        ),
        body: SingleChildScrollView(child:
        ListView(
            shrinkWrap: true,
            children: [
              ListTile(
                title: Text("Kiedy zaczynają się zajęcia?"),
              ),
              ListTile(
                title: FlatButton(
                  color: Colors.blueGrey,
                  textColor: Colors.white,
                  child: Text(schoolStartsAtSelected ? localizations.formatTimeOfDay(schoolStartsAt) : "Wybierz godzinę"),
                  onPressed: () {
                    showTimePicker(context: context, initialTime: schoolStartsAt).then((selected) => {
                      if(selected != null){
                        widget.configurationService.setSchoolStartAt(selected),
                        setState((){
                          schoolStartsAt = selected;
                          schoolStartsAtSelected = true;
                          widget.configurationService.setConfiguredSchool(selectedAll);
                        })
                      }
                    });
                  },
                ),
              ),
              ListTile(
                title: Text("Kiedy kończą się zajęcia?"),
              ),
              ListTile(
                title: FlatButton(
                  color: Colors.blueGrey,
                  textColor: Colors.white,
                  child: Text(schoolEndsAtSelected ? localizations.formatTimeOfDay(schoolEndsAt) : "Wybierz godzinę"),
                  onPressed: () {
                    showTimePicker(context: context, initialTime: schoolEndsAt).then((selected) => {
                      if(selected != null){
                        widget.configurationService.setSchoolEndAt(selected),
                        setState((){
                          schoolEndsAt = selected;
                          schoolEndsAtSelected = true;
                          widget.configurationService.setConfiguredSchool(selectedAll);
                        })
                      }
                    });
                  },
                ),
              ),
              ListTile(
                title: Text("Gdzie znajduję się szkoła?"),
              ),
              ListTile(
                title: FlatButton(
                    color: Colors.blueGrey,
                    textColor: Colors.white,
                    child: Text(selectedPlace ? latitude.toString()+" , "+longitude.toString() : "Wybierz miejsce"),
                    onPressed: () {
                      showDialog(context: context, builder: (BuildContext ctx) {
                        return NominatimLocationPicker(
                          searchHint: 'Gdzie znajduję się szkoła',
                          awaitingForLocation: "Oczekiwanie na lokalizacje",
                        );
                      }).then((obj) {
                        LinkedHashMap<dynamic,dynamic> pos = obj;
                        var latLng = pos['latlng'];
                        double _latitude = latLng.latitude;
                        double _longitude = latLng.longitude;
                        setState(() {
                          latitude = _latitude;
                          longitude = _longitude;
                          selectedPlace = true;
                          widget.configurationService.setConfiguredSchool(selectedAll);
                        });
                        widget.configurationService.setSchoolLocation(LatLong(_latitude,_longitude));
                      });
                    }
                ),
              )
            ]
        )
        ),
      ),
    );
  }

}