import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/model/BtDevice.dart';
import 'package:wbudy_apka/service/BtService.dart';
import 'package:wbudy_apka/widgets/BtDeviceDisplay.dart';
import 'package:wbudy_apka/widgets/DrawerMenuDisplay.dart';

class HomeParentPage extends StatefulWidget {
  HomeParentPage({Key key}) : super(key: key);
  BtService btService = BtService();
  @override
  _HomeParentPageState createState() => _HomeParentPageState();
}

class _HomeParentPageState extends State<HomeParentPage> {

  Timer _everySecond;
  List<BtDevice> devices = List<BtDevice>();
  @override
  void initState() {
    super.initState();
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
      var _devices = await widget.btService.getDevices();
      setState(() {
        devices = _devices;
      });
    });
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
        body: Scrollbar(
            child: ListView(
                shrinkWrap: true,
                children: [
                  for (int index = 0; index < devices.length; index++)
                    BtDeviceDisplay(device: devices[index])
                ]
                )
        ),
      ),
    );
  }

}