import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/model/BtDevice.dart';
import 'package:wbudy_apka/model/Event.dart';
import 'package:wbudy_apka/service/BtService.dart';
import 'package:wbudy_apka/widgets/EventDisplay.dart';

class EventListParentPage extends StatefulWidget {
  EventListParentPage({Key key,this.device}) : super(key: key);
  BtService btService = BtService();
  BtDevice device;
  @override
  _EventListParentPageState createState() => _EventListParentPageState();
}

class _EventListParentPageState extends State<EventListParentPage> {

  Future start() async {
    widget.btService.usedDevice = widget.device;
    await widget.btService.getLastEvent();
  }
  Future loadPrev() async {
    await widget.btService.getPrevEvent();
  }
  Future loadNext() async {
    await widget.btService.getNextEvent();
  }
  List<Event> events = List<Event>();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.device.name+"("+widget.device.addres+")"),
      ),
      body: Scrollbar(
        child: ListView(
          padding: const EdgeInsets.symmetric(vertical: 8),
          children: [
            ListTile(
              onTap: () {
                loadPrev().whenComplete(() => setState(() {
                  print(widget.btService.listEvent);
                  events = widget.btService.listEvent;
                }));
              },
              title: Text("Wczytaj starsze"),
            ),
            for (int index = 0; index < events.length; index++)
              EventDisplay(event: events[index]),
            ListTile(
              onTap: () {
                loadNext().whenComplete(() => setState(() {
                  print(widget.btService.listEvent);
                  events = widget.btService.listEvent;
                }));},
              title: Text("Wczytaj nowsze"),
            )
          ],
        ),
      )
    );
  }

  @override
  void initState() {
    super.initState();
    start().whenComplete(() => setState(() {
      print(widget.btService.listEvent);
      events = widget.btService.listEvent;
    }));
  }

}