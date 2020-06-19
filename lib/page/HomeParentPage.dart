import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/widgets/DrawerMenuDisplay.dart';

class HomeParentPage extends StatefulWidget {
  HomeParentPage({Key key}) : super(key: key);

  @override
  _HomeParentPageState createState() => _HomeParentPageState();
}

class _HomeParentPageState extends State<HomeParentPage> {

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
            ]
        )
        ),
      ),
    );
  }

}