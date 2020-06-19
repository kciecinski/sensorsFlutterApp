import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/service/ConfigurationService.dart';
import 'package:wbudy_apka/service/OtherService.dart';
import 'package:wbudy_apka/service/PermissionService.dart';

class ConfigureChildPage extends StatefulWidget {
  ConfigureChildPage({Key key}) : super(key: key);
  PermissionService permissionService = PermissionService();
  OtherService otherService = new OtherService();
  ConfigurationService configurationService = ConfigurationService();

  @override
  _ConfigureChildPageState createState() => _ConfigureChildPageState();
}

class _ConfigureChildPageState extends State<ConfigureChildPage> with WidgetsBindingObserver{
  Timer _everySecond;

  @override
  void initState() {
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
      bool isConfiguredAll = await widget.configurationService.isConfiguredAll();
      bool isConfiguredEtui = await widget.configurationService.isConfiguredEtui();
      bool isConfiguredSchool = await widget.configurationService.isConfiguredSchool();
      bool isConfiguredMotionDetector = await widget.configurationService.isConfiguredMotionDetector();
      setState((){
        configuredAll = isConfiguredAll;
        configuredEtui = isConfiguredEtui;
        configuredSchool = isConfiguredSchool;
        configuredMotionDetector = isConfiguredMotionDetector;
      });
    });
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      requestPermissionAndStartService();
    });
  }

  Future requestPermissionAndStartService() async {
    bool granted = await widget.permissionService.askForGPSPermissions();
    if(granted) {
      widget.otherService.startService();
    }
  }

  var configuredAll = false;
  var configuredEtui = false;
  var configuredSchool = false;
  var configuredMotionDetector = false;//TODO: add ask for value

  @override
  void dispose() {
    _everySecond.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
        onWillPop: () async => configuredAll,
        child: Scaffold(
          appBar: AppBar(
            title: Text("Konfiguracja"),
            automaticallyImplyLeading: configuredAll,
          ),
          body: SingleChildScrollView(
              child: ListView(
                  shrinkWrap: true,
                  children: [
                    ListTile(
                      title: Text("Wykrywanie etui"),
                      subtitle: Text(configuredEtui ? "" : "Wymagana konfiguracja"),
                      onTap: () {
                        Navigator.pushNamed(context, '/configureChild_etui');
                      },
                    ),
                    ListTile(
                      title: Text("Szkoła"),
                      subtitle: Text(configuredSchool ? "" : "Wymagana konfiguracja"),
                      onTap: () {
                        Navigator.pushNamed(context, '/configureChild_school');
                      },
                    ),
                    ListTile(
                      title: Text("Wykrywanie ruchu"),
                      subtitle: Text(configuredMotionDetector ? "": "Wymagana konfiguracja"),
                      onTap: () {
                        Navigator.pushNamed(context, '/configureChild_motionDetect');
                      },
                    )
                  ]
              )
          ),
        )
    );
  }

}