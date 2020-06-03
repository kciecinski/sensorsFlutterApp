
import 'package:flutter/material.dart';

import 'package:wbudy_apka/page/FirstStartPage.dart';
import 'package:wbudy_apka/page/HomePage.dart';
import 'package:wbudy_apka/page/InfoGPSPage.dart';
import 'package:wbudy_apka/page/MyHomePage.dart';
import 'package:wbudy_apka/page/SplashPage.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blueGrey,
      ),
      home: SplashPage(),
      routes: <String,WidgetBuilder> {
        '/splashPage': (BuildContext context) => SplashPage(),
        '/home': (BuildContext context) => HomePage(),
        '/myhome': (BuildContext context) => MyHomePage(),
        '/firstStart': (BuildContext context) => FirstStartPage(),
        '/infoGps': (BuildContext context) => InfoGPSPage()
      }
    );
  }
}
