
import 'package:flutter/material.dart';
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

  List<BtDevice> devices = List<BtDevice>();
  @override
  void initState() {
    super.initState();
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