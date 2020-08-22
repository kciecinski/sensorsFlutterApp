import 'dart:math';

class LatLong
{
  double latitude;
  double longtitude;

  LatLong(this.latitude, this.longtitude);
  double degreesToRadians(degrees) {
    return degrees*(pi/180);
  }
  double distanceInKilometers(LatLong other) {
    var dlat = degreesToRadians(other.latitude-this.latitude);
    var dlong = degreesToRadians(other.longtitude-this.longtitude);
    var lat1 = degreesToRadians(other.latitude);
    var lat2 = degreesToRadians(this.latitude);
    var a = 0.5 - cos(dlat)/2 + cos(lat1)*cos(lat2)*(1-cos(dlong))/2;
    return asin(sqrt(a))*12742;
  }

  @override
  String toString() {
    return 'LatLong{latitude: $latitude, longtitude: $longtitude}';
  }

  String displayStringCordinante() {
    int latHours=0,latMinutes=0;
    double latSeconds = 0.0;
    int longHours=0,longMinutes=0;
    double longSeconds = 0;
    double latAll = (this.latitude*3600);
    double longAll = (this.longtitude*3600);
    latSeconds = latAll % 60;
    longSeconds = longAll % 60;
    latAll = ((latAll-latSeconds)/60).round().toDouble();
    longAll = ((longAll-longSeconds)/60).round().toDouble();
    latMinutes = (latAll % 60).toInt();
    longMinutes = (longAll % 60).toInt();
    latAll = ((latAll-latMinutes)/60).round().toDouble();
    longAll = ((longAll-longMinutes)/60).round().toDouble();
    latHours = latAll.toInt();
    longHours = longAll.toInt();
    String latLetter = this.latitude >= 0 ? "N" : "S";
    String longLetter = this.longtitude >= 0 ? "E" : "W";
    String cordinate = latHours.toString()+"°"+latMinutes.toString()+"'"+latSeconds.toString()+ " "+latLetter;
    cordinate += "  "+longHours.toString()+"°"+longMinutes.toString()+"'"+longSeconds.toString()+ " "+longLetter;
    return cordinate;
  }


}