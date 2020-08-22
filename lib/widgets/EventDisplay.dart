import 'package:flutter/material.dart';
import 'package:wbudy_apka/model/Event.dart';

class EventDisplay extends StatefulWidget {
  EventDisplay({Key key,this.event}) : super(key: key);
  Event event;
  @override
  _EventDisplayState createState() => _EventDisplayState();
}

class _EventDisplayState extends State<EventDisplay> {
  @override
  Widget build(BuildContext context) {
    return Card(
      child: Column(
        children: <Widget>[
          Text("id: "+widget.event.id.toString()),
          Text("Czas: "+widget.event.timestamp.toString()),
          Text(widget.event.isWithoutEtui),
          Text("Dystans do szkoły: "+ (widget.event.distanceToSchool*1000).toStringAsFixed(2)),
          Text((widget.event.isInSchool ? "W szkole" : "Poza szkołą")),
          Text((widget.event.isShouldBeInSchool ? "Powinnien być w szkole" : "Nie musi być w szkole")),
          Text((widget.event.isPhoneHidden ? "Telefon schowany" : "Telefon nie schowany")),
          Text((widget.event.isInMotion ? "W ruchu" : "Nie porusza się"))
        ],
      ),
    );
  }

}