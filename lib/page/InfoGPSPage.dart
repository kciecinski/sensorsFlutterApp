import 'package:flutter/material.dart';
import 'package:wbudy_apka/widgets/LocationDisplay.dart';

class InfoGPSPage extends StatefulWidget {
  InfoGPSPage({Key key}) : super(key: key);

  @override
  _InfoGPSPageState createState() => _InfoGPSPageState();
}

class _InfoGPSPageState extends State<InfoGPSPage> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("GPS"),
      ),
      body: SingleChildScrollView(child:
      ListView(
          shrinkWrap: true,
          children: [
            LocationDisplay()
          ]
      )
      ),
    );
  }

}