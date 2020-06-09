import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class DrawerMenuDisplay extends StatefulWidget{
  DrawerMenuDisplay();


  @override
  _DrawerMenuDisplayState createState() => _DrawerMenuDisplayState();
}

class _DrawerMenuDisplayState extends State<DrawerMenuDisplay> {

  @override
  build(BuildContext context) {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: <Widget>[
          DrawerHeader(
            child: Text(""),
            decoration: BoxDecoration(
                color: Colors.blueGrey
            ),
          ),
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
          )
        ]
      )
    );
  }
}