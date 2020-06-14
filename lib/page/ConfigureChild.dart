import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/service/ConfigurationService.dart';
import 'package:wbudy_apka/service/OtherService.dart';
import 'package:wbudy_apka/service/PermissionService.dart';

class ConfigureChild extends StatefulWidget {
  ConfigureChild({Key key}) : super(key: key);
  PermissionService permissionService = PermissionService();
  OtherService otherService = new OtherService();
  ConfigurationService configurationService = ConfigurationService();

  @override
  _ConfigureChildState createState() => _ConfigureChildState();
}

class _ConfigureChildState extends State<ConfigureChild> {

  @override
  void initState() {
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

  @override
  Widget build(BuildContext context) {
    widget.configurationService.isConfiguredAll().then((_configuredAll) => {
      if(_configuredAll != configuredAll) {
        setState((){
          configuredAll = _configuredAll;
        })
      }
    });
    widget.configurationService.isConfiguredEtui().then((_configured) => {
      if(_configured != configuredEtui) {
        setState((){
          configuredEtui = _configured;
        })
      }
    });
    widget.configurationService.isConfiguredSchool().then((_configured) => {
      if(_configured != configuredSchool) {
        setState((){
          configuredSchool = _configured;
        })
      }
    });
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
                    )
                  ]
              )
          ),
        )
    );
  }

}