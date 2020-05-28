import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:nominatim_location_picker/nominatim_location_picker.dart';
import 'package:wbudy_apka/model/LatLong.dart';
import 'package:wbudy_apka/service/ConfigurationService.dart';

class FirstStartPage extends StatefulWidget {
  FirstStartPage({Key key}) : super(key: key);

  @override
  _FirstStartPageState createState() => _FirstStartPageState();
}

class _FirstStartPageState extends State<FirstStartPage> {

  void _handlerChangedDeviceOwner(String deviceOwner) {
    setState(() {
      _selectedDeviceOwner = deviceOwner;
    });
  }

  int _currentStep = 0;
  String _selectedDeviceOwner = ConfigurationService.OwnerChild;
  TimeOfDay _schoolStartsAt = TimeOfDay.fromDateTime(DateTime.now());
  bool _schoolStartsAtSelected = false;
  TimeOfDay _schoolEndsAt = TimeOfDay.fromDateTime(DateTime.now());
  bool _schoolEndsAtSelected = false;
  double _latitude = 0;
  double _longitude = 0;
  bool _selectedPlace = false;


  bool get _canContinueAdditionalQuestion {
    if(_selectedDeviceOwner == ConfigurationService.OwnerChild) {
      return _schoolStartsAtSelected && _schoolEndsAtSelected && _selectedPlace;
    }
    else {
      return true;
    }
  }

  List<Step> get _steps {
    List<Step> steps = new List<Step>();
    StepState state =  (_currentStep > steps.length) ? StepState.complete : StepState.indexed;
    steps.add(
        Step(
        title: Text('Kim jesteś ?'),
        state: state,
        content: ListView(
          shrinkWrap: true,
          children: [
            Row(
              children: <Widget>[
                Text("Rodzic"),
                Radio(
                    value: ConfigurationService.OwnerParent,
                    groupValue: _selectedDeviceOwner,
                    onChanged: _handlerChangedDeviceOwner
                )
              ],
            ),
            Row(
              children: <Widget>[
                Text("Uczeń"),
                Radio(
                    value: ConfigurationService.OwnerChild,
                    groupValue: _selectedDeviceOwner,
                    onChanged: _handlerChangedDeviceOwner
                )
              ],
            )
          ],
        )
    ));

    if(_selectedDeviceOwner == ConfigurationService.OwnerChild) {
      StepState state =  (_currentStep > steps.length) ? StepState.complete : StepState.indexed;
      MaterialLocalizations localizations = MaterialLocalizations.of(context);
      steps.add(
          Step(
              title: Text("Parę dodatkowych pytań"),
              state: state,
              content: ListView(
                shrinkWrap: true,
                children: <Widget>[
                  Text("Kiedy zaczynają się zajęcia?"),
                  RaisedButton(
                      child: Text(_schoolStartsAtSelected ? localizations.formatTimeOfDay(_schoolStartsAt) : "Wybierz godzinę"),
                      onPressed: () {
                        showTimePicker(context: context, initialTime: _schoolStartsAt).then((selected) => {
                          if(selected != null)
                            setState((){
                              _schoolStartsAt = selected;
                              _schoolStartsAtSelected = true;
                            })
                        });
                      },
                  ),
                  Text("Kiedy kończą się zajęcia?"),
                  RaisedButton(
                      child: Text(_schoolEndsAtSelected ? localizations.formatTimeOfDay(_schoolEndsAt) : "Wybierz godzinę"),
                      onPressed: () {
                        showTimePicker(context: context, initialTime: _schoolEndsAt).then((selected) => {
                          if(selected != null)
                            setState((){
                              _schoolEndsAt = selected;
                              _schoolEndsAtSelected = true;
                            })
                        });
                      },
                  ),
                  Text("Gdzie znajduję się szkoła?"),
                  RaisedButton(
                    child: Text(_selectedPlace ? _latitude.toString()+" , "+_longitude.toString() : "Wybierz miejsce"),
                    onPressed: () {
                      showDialog(context: context, builder: (BuildContext ctx) {
                          return NominatimLocationPicker(
                              searchHint: 'Gdzie znajduję się szkoła',
                              awaitingForLocation: "Oczekiwanie na lokalizacje",
                          );
                        }).then((obj) {
                          LinkedHashMap<dynamic,dynamic> pos = obj;
                          var latLng = pos['latlng'];
                          double latitude = latLng.latitude;
                          double longitude = latLng.longitude;
                          setState(() {
                            _latitude = latitude;
                            _longitude = longitude;
                            _selectedPlace = true;
                          });
                      });
                    }
                  )
                ]
              )
          ));
    } else {
      steps.add(
        Step(
          title: Text("Parę dodatkowych pytań"),
          state: StepState.complete,
          isActive: false,
          content: Text("Przejdź dalej - tutaj nie ma nic dla ciebie")
        ));
    }

    state =  (_currentStep > steps.length) ? StepState.complete : StepState.indexed;
    steps.add(
        Step(
        title: Text("Gotowe"),
        state: state,
        content: ListView(
          shrinkWrap: true,
          children: <Widget>[],
        )
    ));

    return steps;
  }

  Future _setConfiguration() async {
    var configurationService = new ConfigurationService();
    await configurationService.setDeviceOwner(_selectedDeviceOwner);
    await configurationService.setSchoolStartAt(_schoolStartsAt);
    await configurationService.setSchoolEndAt(_schoolEndsAt);
    await configurationService.setSchoolLocation(LatLong(_latitude,_longitude));
    await configurationService.setAppConfigured(true);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Pierwsze uruchomienie"),
        automaticallyImplyLeading: false,
      ),
      body: SingleChildScrollView(
        child: Stepper(
          currentStep: _currentStep,
          onStepContinue: () {
            setState(() {
              if(_currentStep == 1) {
                if(_canContinueAdditionalQuestion) {
                  _currentStep++;
                }
              }
              else if(_currentStep == (_steps.length-1)) {
                _setConfiguration().whenComplete(() {
                  Navigator.pushNamed(context, '/home');
                });
              }
              else if(_currentStep < (_steps.length-1)){
                _currentStep++;
              }
            });
          },
          onStepCancel: (){
            setState(() {
              _currentStep--;
              if(_currentStep < 0) {
                _currentStep = 0;
              }
            });
          },
          steps: _steps
        ),
      )
    );
  }

}