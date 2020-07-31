import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:collection';
import 'dart:ffi';

import 'package:wbudy_apka/model/Event.dart';

class EventLogService {
  static EventLogService _instance = null;

  factory EventLogService() {
    if (_instance == null) {
      _instance = new EventLogService._constructor();
    }
    return _instance;
  }

  EventLogService._constructor() {}
  static const MethodChannel _platform = const MethodChannel('samples.flutter.dev/eventlog');
  static const _getLastEvent = "getLastEvent";
  static const _getNextEventById = "getNextEventById";
  static const _getPrevEventById = "getPrevEventById";
  static const _getEventById = "getEventById";

  Event _resultMapToEvent(dynamic result) {
    print(result);
    Event x = Event(
        int.parse(result['timestamp']),
        result['isWithoutEtui'],
        double.parse(result['distanceToSchool']),
        (result['isInSchool'] == "true" ? true : false),
        (result['isShouldBeInSchool'] == "true" ? true : false),
        (result['isPhoneHidden'] == "true" ? true : false),
        (result['isInMotion'] == "true" ? true : false),
        id:int.parse(result['id'])
    );
    return x;
  }

  Future<Event> getLastEvent() async {
    var result = await _platform.invokeMethod(_getLastEvent);
    return _resultMapToEvent(result);
  }
  Future<Event> getNextEventById(int id) async {
    var result = await _platform.invokeMethod(_getNextEventById,{"id":id});
    print(result);
    return _resultMapToEvent(result);
  }
  Future<Event> getPrevEventById(int id) async {
    var result = await _platform.invokeMethod(_getPrevEventById,{"id":id});
    return _resultMapToEvent(result);
  }
  Future<Event> getEventById(int id) async {
    var result = await _platform.invokeMethod(_getEventById,{"id":id});
    return _resultMapToEvent(result);
  }
  Future<List<Event>> getLastestEvent() async {
    var lastEvent = await getLastEvent();
    int id = (lastEvent.id / 10).toInt();
    id *= 10;
    id += 1;
    var list = List<Event>();
    for(int i = id; i < id + 10; i ++) {
      var e = await getEventById(i);
      if(e.id < 0) {
        continue;
      }
      list.add(e);
    }
    return list;
  }

  Future<List<Event>> getLoadEventsFrom(int _id) async {
    int id = (_id / 10).toInt();
    id *= 10;
    id += 1;
    var list = List<Event>();
    for(int i = id; i < id + 10; i ++) {
      var e = await getEventById(i);
      if(e.id < 0) {
        continue;
      }
      list.add(e);
    }
    return list;
  }
}