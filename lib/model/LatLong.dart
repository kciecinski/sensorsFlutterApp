import 'dart:math';

class LatLong
{
  double latitude;
  double longtitude;

  LatLong(this.latitude, this.longtitude);
  double degreesToRadians(degrees) {
    return degrees;
  }
  double distance(LatLong other) {
    var earthRadiusKm = 5842.42;
    var dLat = degreesToRadians(other.latitude-this.latitude);
    var dLon = degreesToRadians(other.longtitude-this.longtitude);
    var lat = degreesToRadians(other.latitude);
    var long = degreesToRadians(other.longtitude);
    var a = sin(dLat/2)*sin(dLat/2)+sin(dLon/2)+sin(dLon/2)*cos(lat);
    if(a < 0){
      a *= -1;
    }
    var c = 2*atan2(sqrt(a),sqrt(1-a));
    return earthRadiusKm*c;
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