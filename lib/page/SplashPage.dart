import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

class SplashPage extends StatefulWidget {
  SplashPage({Key key}) : super(key: key);

  @override
  _SplashPageState createState() => _SplashPageState();
}

class _SplashPageState extends State<SplashPage> {
  @override
  void initState() {
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) async {
      await requestPermission();
      Navigator.pushNamed(context, '/home');
    });
  }



  Future requestPermission() async {
    bool locationGranted = true;
    List<PermissionGroup> permissonsList = List.of({PermissionGroup.location,PermissionGroup.locationAlways,PermissionGroup.locationWhenInUse});
    var permissions = await PermissionHandler().requestPermissions(permissonsList);
    permissions.forEach((p, val) {
      if(val != PermissionStatus.granted && p != PermissionGroup.location) {
        locationGranted = false;
      }
    });
  }


  @override
  Widget build(BuildContext context) {
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
}