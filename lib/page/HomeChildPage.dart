import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/service/LocationService.dart';
import 'package:wbudy_apka/service/OtherService.dart';
import 'package:wbudy_apka/service/PermissionService.dart';
import 'package:wbudy_apka/widgets/ChildStateDisplay.dart';
import 'package:wbudy_apka/widgets/DrawerMenuDisplay.dart';

import 'ConfigureChildPage.dart';

class HomeChildPage extends StatefulWidget {
  HomeChildPage({Key key}) : super(key: key);
  PermissionService permissionService = PermissionService();
  OtherService otherService = new OtherService();

  @override
  _HomeChildPageState createState() => _HomeChildPageState();
}

class _HomeChildPageState extends State<HomeChildPage> {

  Future configurationChildDialog() async {
    print("configurationChildDialog");
    await showDialog<bool>(context: context, builder: (BuildContext ctx) => Container(
      decoration: BoxDecoration(),
      child: ConfigureChildPage(),
    ));
  }
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
              ChildStateDisplay ()
            ]
        )
        ),
      ),
    );
  }

}