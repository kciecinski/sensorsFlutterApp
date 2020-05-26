import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wbudy_apka/service/ConfigurationService.dart';

class FirstStartPage extends StatefulWidget {
  FirstStartPage({Key key}) : super(key: key);

  @override
  _FirstStartPageState createState() => _FirstStartPageState();
}

class _FirstStartPageState extends State<FirstStartPage> {

  void _handlerChangedDeviceOwner(String deviceOwner) {
    setState(() {
      _selectedDeviceOwner = deviceOwner;
    });
  }

  int _currentStep = 0;
  String _selectedDeviceOwner = ConfigurationService.OwnerChild;

  List<Step> get _steps {
    var s = [
      Step(
          title: Text('Kim jesteś ?'),
          content: ListView(
            shrinkWrap: true,
            children: [
              Row(
                children: <Widget>[
                  Text("Rodzic"),
                  Radio(
                      value: ConfigurationService.OwnerParent,
                      groupValue: _selectedDeviceOwner,
                      onChanged: _handlerChangedDeviceOwner
                  )
                ],
              ),
              Row(
                children: <Widget>[
                  Text("Uczeń"),
                  Radio(
                      value: ConfigurationService.OwnerChild,
                      groupValue: _selectedDeviceOwner,
                      onChanged: _handlerChangedDeviceOwner
                  )
                ],
              )
            ],
          )
      ),
      Step(
          title: Text("Gotowe"),
          content: ListView(
            shrinkWrap: true,
            children: <Widget>[],
          )
      )
    ];
    return s;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Pierwsze uruchomienie"),
        automaticallyImplyLeading: false,
      ),
      body: SingleChildScrollView(
        child: Stepper(
          currentStep: _currentStep,
          onStepContinue: () {
            setState(() {
              if(_currentStep == (_steps.length-1)) {
                var configurationService = new ConfigurationService();
                configurationService.setDeviceOwner(_selectedDeviceOwner).whenComplete(() => {
                  configurationService.setAppConfigured(true).whenComplete(() => {
                    Navigator.pushNamed(context, '/home')
                  })
                });
              }else if(_currentStep < (_steps.length-1)){
                _currentStep++;
              }
            });
          },
          onStepCancel: (){
            setState(() {
              _currentStep--;
              if(_currentStep < 0) {
                _currentStep = 0;
              }
            });
          },
          steps: _steps
        ),
      )
    );
  }

}