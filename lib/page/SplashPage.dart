import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:wbudy_apka/service/ConfigurationService.dart';
import 'package:wbudy_apka/service/OtherService.dart';
import './../service/PermissionService.dart';

class SplashPage extends StatefulWidget {
  SplashPage({Key key}) : super(key: key);
  PermissionService _permissionService = PermissionService();
  ConfigurationService _configurationService = ConfigurationService();
  @override
  StatefulElement createElement() {
    return super.createElement();
  }

  @override
  _SplashPageState createState() => _SplashPageState();
}

class _SplashPageState extends State<SplashPage> {



  @override
  void initState() {
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      this.detectWhatPageShouldSee();
    });
  }

  Future detectWhatPageShouldSee() async {
    var appConfigured = await widget._configurationService.isAppConfigured();
    print({"appConfigured":appConfigured});
    if(!appConfigured) {
      Navigator.pushNamed(context, '/firstStart');
    }
    else {
      var deviceOwner = await widget._configurationService.getDeviceOwner();
      if(deviceOwner == ConfigurationService.OwnerParent) {
        Navigator.pushNamed(context, '/home');
      } else if(deviceOwner == ConfigurationService.OwnerChild) {
        await requestPermissionAndStartService();
        var isCalibratedAllSensors = await widget._configurationService.isConfiguredAll();
        if(isCalibratedAllSensors) {
          Navigator.pushNamed(context, '/home');
        } else {
          Navigator.pushNamed(context, '/configureChild');
        }
      } else {
        Navigator.pushNamed(context, '/firstStart');
      }
    }
  }

  Future requestPermissionAndStartService() async {
    var permissionsService = PermissionService();
    bool granted = await permissionsService.askForGPSPermissions();
    if(granted) {
      var otherService = new OtherService();
      otherService.startService();
    }
  }

  @override
  Widget build(BuildContext context) {
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

  @override
  void didUpdateWidget(SplashPage oldWidget) {
    super.didUpdateWidget(oldWidget);
  }
}