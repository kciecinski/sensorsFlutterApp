import 'dart:async';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:wbudy_apka/service/ConfigurationService.dart';
import 'package:wbudy_apka/service/OtherService.dart';
import 'package:wbudy_apka/service/PermissionService.dart';

class SplashPage extends StatefulWidget {
  SplashPage({Key key}) : super(key: key);
  @override
  StatefulElement createElement() {
    return super.createElement();
  }

  @override
  _SplashPageState createState() => _SplashPageState();
}

class _SplashPageState extends State<SplashPage> with NavigatorObserver{

  PermissionService permissionService = PermissionService();
  ConfigurationService configurationService = ConfigurationService();
  Future detectWhatPageShouldSee() async {
    var appConfigured = await configurationService.isAppConfigured();
    print({"appConfigured":appConfigured});
    if(!appConfigured) {
      Navigator.pushNamed(context, '/firstStart');
    }
    else {
      var deviceOwner = await configurationService.getDeviceOwner();
      if(deviceOwner == ConfigurationService.OwnerParent) {
        Navigator.pushNamed(context, '/homeParent');
      } else if(deviceOwner == ConfigurationService.OwnerChild) {
        await requestPermissionAndStartService();
        var isCalibratedAllSensors = await configurationService.isConfiguredAll();
        if(isCalibratedAllSensors) {
          Navigator.pushNamed(context, '/homeChild');
        } else {
          Navigator.pushReplacementNamed(context, '/homeChild');
          Navigator.pushNamed(context, '/configureChild');
        }
      } else {
        Navigator.pushNamed(context, '/firstStart');
      }
    }
  }

  Future requestPermissionAndStartService() async {
    bool granted = await permissionService.askForGPSPermissions();
    if(granted) {
      var otherService = new OtherService();
      otherService.startService();
    }
  }



  @override
  void initState() {
    super.initState();
    print("SplashPage::initState");
    detectWhatPageShouldSee();
  }

  @override
  void dispose() {
    print("SplashPage::dispose");
    super.dispose();
  }


  @override
  void deactivate() {
    super.deactivate();
    print("SplashPage::deactivate");
  }

  @override
  Widget build(BuildContext context) {
    print("SplashPage::build");
    return Scaffold(
      body: Center(
        child: ListView(
          shrinkWrap: true,
          children: <Widget>[
            FlutterLogo(),
            Center(child: Text('wbudy_apka'))
          ],
        ),
      ),
    );
  }

}