

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/service/CalibrateService.dart';
import 'package:wbudy_apka/service/ConfigurationService.dart';

class ConfigureEtui extends StatefulWidget {
  ConfigureEtui({Key key}) : super(key: key);
  ConfigurationService configurationService = ConfigurationService();
  CalibrateService calibrateService = CalibrateService();
  @override
  _ConfigureEtuiState createState() => _ConfigureEtuiState();
}

class _ConfigureEtuiState extends State<ConfigureEtui> {
  int configurationViewStep = 0;
  int configurationSteps = 1;
  bool loadedAll = false;
  bool haveEtui = false;
  bool calibratingWithoutEtui = false;
  bool calibratedWithoutEtui = false;
  bool startedCalibrationWithoutEtui = false;
  bool get disabledAll {
    return startedCalibrationWithoutEtui;
  }
  Timer _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {});

  void setConfigurationViewStep(int stepIndex) {
    _everySecond.cancel();
    switch(stepIndex) {
      case 1:
        _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
          var _calibratingWithoutEtui = await widget.calibrateService.isCalibratingWithoutEtui();
          var _calibratedWithoutEtui = await widget.calibrateService.isCalibratedWithoutEtui();
          setState(() {
            if(!_calibratingWithoutEtui) {
              startedCalibrationWithoutEtui = false;
            }
            calibratingWithoutEtui = _calibratingWithoutEtui;
            calibratedWithoutEtui = _calibratedWithoutEtui;
          });
        });
        break;
    }
    bool lastStep = (stepIndex == configurationSteps-1);
    if(lastStep) {
      widget.configurationService.setHaveEtui(haveEtui);
      widget.configurationService.setConfiguredEtui(true);
    }
    setState(() {
      configurationViewStep = stepIndex;
    });
  }

  @override
  void initState() {
    super.initState();
    loadedAll = false;
    _everySecond.cancel();
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {});
  }

  @override
  void dispose() {
    super.dispose();
    _everySecond.cancel();
  }

  Future loadAll() async {
    bool _haveEtui = await widget.configurationService.isHaveEtui();
    if(!loadedAll) {
      setState(() {
        haveEtui = _haveEtui;
        loadedAll = true;
      });
    }
  }

  Widget get waitForLoadView {
    return ListView(
      shrinkWrap: true,
      children: <Widget>[
        ListTile(
          title: Text("Jeszcze chwila ..."),
        )
      ],
    );
  }
  Widget get configurationView {
    List<Step> steps = [];
    steps.add(Step(
      isActive: true,
      title: Text(""),
      content: ListView(
        shrinkWrap: true,
        children: <Widget>[
          Text("Czy telefon posiada etui"),
          Row(
            children: <Widget>[
              Text("z klapką i magnesem"),
              Switch(
                  value: haveEtui,
                  onChanged: (value){
                    setState(() {
                      haveEtui = value;
                    });
                  },
                  activeTrackColor: Colors.lightGreenAccent,
                  activeColor: Colors.green
              )
            ],
          )
        ],
      )
    ));
    steps.add(Step(
        isActive: haveEtui,
        title: Text(""),
        content: ListView(
          shrinkWrap: true,
          children: <Widget>[
            ListTile(
              title: Text("Wyjmnij telefon z etui i wciśnij przycisk"),
            ),
            FlatButton(
              onPressed: disabledAll ? null : () {
                setState(() {
                  startedCalibrationWithoutEtui = true;
                });
                widget.calibrateService.startCalibrateWithoutEtui();
              },
              color: Colors.blueGrey,
              disabledColor: Colors.grey,
              disabledTextColor: Colors.white,
              textColor: Colors.white,
              textTheme: ButtonTextTheme.normal,
              child: Text("Telefon wyciągnięty"),
            ),
            ListTile(
              title: Text(calibratingWithoutEtui ? "proszę czekać":""),
            )
          ],
        )
    ));
    steps.add(Step(
      title: Text(""),
      isActive: true,
      content: ListView(
        shrinkWrap: true,
        children: <Widget>[
          ListTile(
            title: Text("Gotowe"),
          )
        ],
      )
    ));
    configurationSteps = steps.length;
    Stepper stepper = Stepper(
      controlsBuilder: (BuildContext context, {VoidCallback onStepCancel, VoidCallback onStepContinue}) {
        List<Widget> children = [];
        bool lastStep = (configurationViewStep == configurationSteps-1);
        bool firstStep = (configurationViewStep == 0);
        if(!lastStep) {
          children.add(FlatButton(
            onPressed: disabledAll ? null : onStepContinue,
            disabledColor: Colors.grey,
            child: Text(lastStep ? "Zapisz" : "Dalej",
                style: TextStyle(color: Colors.white)),
            color: Colors.blueGrey,
          ));
          children.add(Padding(
            padding: new EdgeInsets.all(10),
          ));
        }
        if(!firstStep) {
          children.add(FlatButton(
            onPressed: disabledAll ? null : onStepCancel,
            disabledColor: Colors.grey,
            child: const Text(
              'Wstecz',
              style: TextStyle(color: Colors.white),
            ),
            color: Colors.blueGrey,
          ));
        }
        return Row(
          children: children
        );
      },
      currentStep: configurationViewStep,
      onStepContinue: (){
        for(var i = configurationViewStep+1; i < steps.length;i++) {
          if(steps[i].isActive) {
            setConfigurationViewStep(i);
            break;
          }
        }
      },
      onStepCancel: (){
        for(var i = configurationViewStep-1; i >= 0;i--) {
          if(steps[i].isActive) {
            setConfigurationViewStep(i);
            break;
          }
        }
      },
      steps: steps,
    );
    return stepper;
  }

  @override
  Widget build(BuildContext context) {
    loadAll();
    Widget view = ListView(
      shrinkWrap: true,
      children: <Widget>[],
    );
    if(loadedAll) {
      view = configurationView;
    } else {
      view = waitForLoadView;
    }
    return WillPopScope(
      onWillPop: () async => !disabledAll,
      child: Scaffold(
        appBar: AppBar(
          title: Text("Konfiguracja ETUI"),
          automaticallyImplyLeading: !disabledAll,
        ),
        body: SingleChildScrollView(
            child:view
        ),
      ),
    );
  }

}