import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:wbudy_apka/service/ConfigurationService.dart';
import './../service/PermissionService.dart';

class SplashPage extends StatefulWidget {
  SplashPage({Key key}) : super(key: key);

  @override
  StatefulElement createElement() {
    print("crateElement");
    return super.createElement();
  }

  @override
  _SplashPageState createState() => _SplashPageState();
}

class _SplashPageState extends State<SplashPage> {



  @override
  void initState() {
    print("initState");
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      print("postFrameCallback");
      var permissionsService = new PermissionService();
      permissionsService.askForGPSPermissions().whenComplete(() => this._permissonsAsked());
    });
  }

  void _permissonsAsked() {
    var configurationService = new ConfigurationService();
    configurationService.isAppConfigured().then((configured) => {
      if(configured == true) {
        Navigator.pushNamed(context, '/home')
      }else {
        Navigator.pushNamed(context, '/firstStart')
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    print("build");
    return Scaffold(
      body: Center(
        child: ListView(
          shrinkWrap: true,
          children: <Widget>[
            FlutterLogo(),
            Center(child: Text('Nazwa apki'))
          ],
        ),
      ),
    );
  }

  @override
  void didUpdateWidget(SplashPage oldWidget) {
    super.didUpdateWidget(oldWidget);
    print("didUpdateWidget");
  }
}