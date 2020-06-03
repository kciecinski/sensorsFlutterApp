import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/service/LocationService.dart';
import 'package:wbudy_apka/service/PermissionService.dart';
import 'package:wbudy_apka/widgets/ChildStateDisplay.dart';
import 'package:wbudy_apka/widgets/DrawerMenuDisplay.dart';

class HomePage extends StatefulWidget {
  HomePage({Key key}) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {

  @override
  void initState() {
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      requestPermissionAndStartLocationService();
    });
  }

  Future requestPermissionAndStartLocationService() async {
    var permissionsService = PermissionService();
    bool granted = await permissionsService.askForGPSPermissions();
    if(granted) {
      var locationService = new LocationService();
      locationService.startGps();
    }
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async => true,
      child: Scaffold(
        drawer: DrawerMenuDisplay(),
        appBar: AppBar(
          title: Text("")
        ),
        body: SingleChildScrollView(child:
        ListView(
            shrinkWrap: true,
            children: [
              Text("Jeśli dziecko to tutaj jakiś status że dziecko się nie bawi czy bawi telefonem, czy ma być na zajęciach w danej chwili czy jest w szkole"),
              ChildStateDisplay()
            ]
        )
        ),
      ),
    );
  }

}