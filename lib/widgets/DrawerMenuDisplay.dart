import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/service/ConfigurationService.dart';
import 'package:wbudy_apka/service/OtherService.dart';

class DrawerMenuDisplay extends StatefulWidget{
  DrawerMenuDisplay();
  bool child = false;
  ConfigurationService configurationService = ConfigurationService();
  @override
  _DrawerMenuDisplayState createState() => _DrawerMenuDisplayState();
}

class _DrawerMenuDisplayState extends State<DrawerMenuDisplay> {


  List<Widget> getChildDrawer(BuildContext context) {
    return [
      ListTile(
        title: Text("Sensory"),
        onTap: () {
          Navigator.pushNamed(context, '/sensors');
        },
      ),
      ListTile(
          title: Text("GPS"),
          onTap: () {
            Navigator.pushNamed(context, '/infoGps');
          }
      ),
      ListTile(
          title: Text("Ustawienia"),
          onTap: () {
            Navigator.pushNamed(context, '/configureChild');
          }
      ),
      ListTile(
          title: Text("Zamknij aplikację i procesy w tle"),
          onTap: () {
            _otherService.stopService();
            SystemNavigator.pop();
          }
      )
    ];
  }

  List<Widget> getParentDrawer(BuildContext context) {
    return [
      ListTile(
          title: Text("Zamknij aplikację"),
          onTap: () {
            SystemNavigator.pop();
          }
      )
    ];
  }

  OtherService _otherService = OtherService();
  @override
  build(BuildContext context) {
    this.widget.configurationService.getDeviceOwner().then((value) => {
      setState((){
        widget.child = (value == ConfigurationService.OwnerChild);
      })
    });
    List<Widget> children = [
      DrawerHeader(
        child: Text(""),
        decoration: BoxDecoration(
            color: Colors.blueGrey
        ),
      )];
    List<Widget> forOwnerChildren = [];
    if(widget.child) {
      forOwnerChildren = getChildDrawer(context);
    } else {
      forOwnerChildren = getParentDrawer(context);
    }
    children.addAll(forOwnerChildren);
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: children
      )
    );
  }
}