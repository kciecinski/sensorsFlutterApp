import 'dart:async';
import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/model/Event.dart';
import 'package:wbudy_apka/service/EventLogService.dart';

class _EventListPageState extends State<EventListPage> {
  //Timer _everySecond;

  /*@override
  void initState() {
    super.initState();
    HashMap result = {};
    _everySecond = Timer.periodic(Duration(seconds: 1), (Timer t) async {
      try {
        result = await widget.eventLogService.getLastEvent();
        print(result);
      } on PlatformException catch (e) {
        print(e);
      }
    });
  }*/

  int id = -1;
  List<Widget> eventsWidget = List<Widget>();

  @override
  void initState() {
    super.initState();
    widget.eventLogService.getLastestEvent().then(displayEvents);
  }

  void displayEvents(List<Event> events) {
    if(events.length < 1) {
      widget.eventLogService.getLastestEvent().then(displayEvents);
      return;
    }
    id = events.first.id;
    var widgetList = List<Widget>();
    events.forEach((event) {
      widgetList.add(createWidgetForEvent(event));
    });
    setState(() {
      eventsWidget = widgetList;
    });
  }

  Widget createWidgetForEvent(Event event) {
    return Card(
      child: Column(
        children: <Widget>[
          Text("id: "+event.id.toString()),
          Text("Czas: "+event.timestamp.toString()),
          Text(event.isWithoutEtui),
          Text("Dystans do szkoły: "+ (event.distanceToSchool*1000).toStringAsFixed(2)),
          Text((event.isInSchool ? "W szkole" : "Poza szkołą")),
          Text((event.isShouldBeInSchool ? "Powinnien być w szkole" : "Nie musi być w szkole")),
          Text((event.isPhoneHidden ? "Telefon schowany" : "Telefon nie schowany")),
          Text((event.isInMotion ? "W ruchu" : "Nie porusza się"))
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Lista zdarzeń"),
      ),
      body: Scrollbar(
        child: ListView(
          padding: const EdgeInsets.symmetric(vertical: 8),
          children: [
            ListTile(
              title: Text("pokaż starsze"),
              onTap: () {
                widget.eventLogService.getLoadEventsFrom(id-10).then(displayEvents);
              },
            ),
            for (int index = 0; index < eventsWidget.length; index++)
              eventsWidget[index]
            ,
            ListTile(
              title: Text("pokaż nowsze / odśwież"),
              onTap: () {
                widget.eventLogService.getLoadEventsFrom(id+10).then(displayEvents);
              },
            ),
          ],
        ),
      )
    );
  }

}

class EventListPage extends StatefulWidget {
  EventListPage({Key key}) : super(key: key);

  EventLogService eventLogService = EventLogService();
  @override
  _EventListPageState createState() => _EventListPageState();
}
