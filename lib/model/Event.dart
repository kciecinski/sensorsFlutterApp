

class Event {

  int timestamp;
  String isWithoutEtui;
  double distanceToSchool;
  bool isInSchool;
  bool isShouldBeInSchool;
  bool isPhoneHidden;
  bool isInMotion;
  int id;

  Event(this.timestamp, this.isWithoutEtui, this.distanceToSchool,
      this.isInSchool, this.isShouldBeInSchool, this.isPhoneHidden,
      this.isInMotion, {this.id = -1});

  @override
  String toString() {
    return 'Event{timestamp: $timestamp, isWithoutEtui: $isWithoutEtui, distanceToSchool: $distanceToSchool, isInSchool: $isInSchool, isShouldBeInSchool: $isShouldBeInSchool, isPhoneHidden: $isPhoneHidden, isInMotion: $isInMotion, id: $id}';
  }


}