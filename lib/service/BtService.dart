

import 'package:wbudy_apka/model/Event.dart';

import '../model/BtDevice.dart';
import 'dart:collection';

import 'package:flutter/services.dart';

class BtService {
  static BtService _instance = null;

  factory BtService() {
    if (_instance == null) {
      _instance = new BtService._constructor();
    }
    return _instance;
  }

  BtService._constructor();
  static const _getDevices = "getDevices";
  static const _getLastEvent = "getLastEvent";
  static const _getNextEvent = "getNextEvent";
  static const _getPrevEvent = "getPrevEvent";
  static const MethodChannel _platform = const MethodChannel('samples.flutter.dev/bt');

  bool _isBtInUsage = false;
  bool get isBtInUsage => _isBtInUsage;

  List<Event> listEvent = List<Event>();

  BtDevice _usedDevice = null;
  BtDevice get usedDevice => _usedDevice;
  set usedDevice(BtDevice device) {
    listEvent.clear();
    _usedDevice = device;
  }

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


  Future getLastEvent() async {
    _isBtInUsage = true;
    var result = await _platform.invokeMethod(_getLastEvent,{"addres":usedDevice.addres});
    var event = _resultMapToEvent(result);
    listEvent.clear();
    listEvent.add(event);
    _isBtInUsage = false;
  }
  Future getPrevEvent() async {
    _isBtInUsage = true;
    var id = -2;
    if(listEvent.isNotEmpty) {
      id = listEvent.first.id;
    }
    var result = await _platform.invokeMethod(_getPrevEvent,{"addres":usedDevice.addres,"id":id});
    var event = _resultMapToEvent(result);
    listEvent.insert(0, event);
    _isBtInUsage = false;
  }
  Future getNextEvent() async {
    _isBtInUsage = true;
    var id = -2;
    if(listEvent.isNotEmpty) {
      id = listEvent.last.id;
    }
    var result = await _platform.invokeMethod(_getNextEvent,{"addres":usedDevice.addres,"id":id});
    var event = _resultMapToEvent(result);
    listEvent.add(event);
    _isBtInUsage = false;
  }

  Future<List<BtDevice>> getDevices() async {
    List<BtDevice> devices = List<BtDevice>();
    LinkedHashMap<dynamic,dynamic> result = await _platform.invokeMethod(_getDevices);
    result.forEach((key, value) {
      var dev = BtDevice(key.toString(),value.toString());
      devices.add(dev);
    });
    return devices;
  }

}