
import 'package:flutter/material.dart';
import 'package:wbudy_apka/model/Event.dart';
import 'package:wbudy_apka/service/EventLogService.dart';
import 'package:wbudy_apka/widgets/EventDisplay.dart';

class _EventListPageState extends State<EventListPage> {

  int id = -1;
  List<Widget> eventsWidget = List<Widget>();

  @override
  void initState() {
    super.initState();
    widget.eventLogService.getLastestEvent().then(displayEvents);
  }

  void displayEvents(List<Event> events) {
    if(events.isEmpty) {
      widget.eventLogService.getLastestEvent().then(displayEvents);
      return;
    }
    id = events.first.id;
    var widgetList = List<Widget>();
    events.forEach((event) {
      widgetList.add(EventDisplay(event: event));
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
