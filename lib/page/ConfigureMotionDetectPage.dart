import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/service/CalibrateService.dart';

class ConfigureMotionDetectPage extends StatefulWidget {
  ConfigureMotionDetectPage({Key key}) : super(key: key);
  CalibrateService calibrateService = CalibrateService();
  @override
  _ConfigureMotionDetectPageState createState() =>
      _ConfigureMotionDetectPageState();
}

class _ConfigureMotionDetectPageState extends State<ConfigureMotionDetectPage> {

  bool calibratingMotionDetect = false;
  bool startedCalibrationMotionDetectAccelerometr = false;
  bool get disabledAll {
    return startedCalibrationMotionDetectAccelerometr;
  }

  Timer _everySecond = null;


  @override
  void dispose() {
    super.dispose();
    if(_everySecond != null) {
      _everySecond.cancel();
    }
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async => !disabledAll,
      child: Scaffold(
        appBar: AppBar(
          title: Text("Wykrywanie ruchu"),
          automaticallyImplyLeading: !disabledAll,
        ),
        body: SingleChildScrollView(child:
        ListView(
            shrinkWrap: true,
            children: [
              ListTile(
                title: Text("Połóż telefon i wciśnij przycisk"),
              ),
              FlatButton(
                onPressed: disabledAll ? null : () async {
                  setState(() {
                    startedCalibrationMotionDetectAccelerometr = true;
                    });
                  await widget.calibrateService.startCalibrateMotionDetect();
                  if(_everySecond != null) {
                    _everySecond.cancel();
                  }
                  _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
                    bool _isCalibratingMotionDetect = await widget.calibrateService.isCalibratingMotionDetect();
                    print(_isCalibratingMotionDetect);
                    setState(() {
                      startedCalibrationMotionDetectAccelerometr = _isCalibratingMotionDetect;
                    });
                  });
                },
                color: Colors.blueGrey,
                disabledColor: Colors.grey,
                disabledTextColor: Colors.white,
                textColor: Colors.white,
                textTheme: ButtonTextTheme.normal,
                child: Text("Telefon wyciągnięty"),
              ),
              ListTile(
                title: Text(calibratingMotionDetect ? "proszę czekać":""),
              )
            ]
        )
        ),
      ),
    );
  }

}