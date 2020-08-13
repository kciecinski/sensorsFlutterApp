import 'package:flutter/material.dart';
import 'package:wbudy_apka/model/BtDevice.dart';
import 'package:wbudy_apka/page/EventListParentPage.dart';

class BtDeviceDisplay extends StatefulWidget {
  BtDeviceDisplay({Key key,this.device}) : super(key: key);
  BtDevice device;
  @override
  _BtDeviceDisplayState createState() => _BtDeviceDisplayState();
}

class _BtDeviceDisplayState extends State<BtDeviceDisplay> {
  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(widget.device.name),
      subtitle: Text(widget.device.addres),
      onTap: () {
        showDialog(context: context, builder: (BuildContext ctx) {
          return EventListParentPage(
            device: widget.device,
          );
        });
      },
    );
  }

}