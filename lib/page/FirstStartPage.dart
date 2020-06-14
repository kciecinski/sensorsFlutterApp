
import 'package:flutter/material.dart';
import 'package:wbudy_apka/service/ConfigurationService.dart';

class FirstStartPage extends StatefulWidget {
  FirstStartPage({Key key}) : super(key: key);

  @override
  _FirstStartPageState createState() => _FirstStartPageState();
}

class _FirstStartPageState extends State<FirstStartPage> {

  void chanegeDeviceOwner(String deviceOwner) {
    setState(() {
      selectedDeviceOwner = deviceOwner;
    });
  }

  String selectedDeviceOwner = ConfigurationService.OwnerChild;

  Future setConfiguration() async {
    var configurationService = new ConfigurationService();
    await configurationService.setDeviceOwner(selectedDeviceOwner);
    await configurationService.setAppConfigured(true);
  }

  @override
  Widget build(BuildContext context) {
    /*

     */
    return WillPopScope(
        onWillPop: () async => false,
        child: Scaffold(
            appBar: AppBar(
              title: Text("Pierwsze uruchomienie"),
              automaticallyImplyLeading: false,
            ),
            body: SingleChildScrollView(
              child: ListView(
                shrinkWrap: true,
                children: <Widget>[
                  ListTile(
                    title: Text("Do kogo należy ten telefon?"),
                  ),
                  ListTile(
                    title: Row(
                      children: <Widget>[
                        Text("Rodzic"),
                        Radio(
                            value: ConfigurationService.OwnerParent,
                            groupValue: selectedDeviceOwner,
                            onChanged: chanegeDeviceOwner
                        )
                      ],
                    ),
                  ),
                  ListTile(
                    title: Row(
                      children: <Widget>[
                        Text("Uczeń"),
                        Radio(
                            value: ConfigurationService.OwnerChild,
                            groupValue: selectedDeviceOwner,
                            onChanged: chanegeDeviceOwner
                        )
                      ],
                    )
                  ),
                  ListTile(
                    title: FlatButton(
                      onPressed: (){
                        setConfiguration().then((value) => {
                          Navigator.pushNamed(context, '/splashPage')
                        });
                      },
                      color: Colors.blueGrey,
                      textColor: Colors.white,
                      child: Text("Dalej"),
                    ),
                  )
                ],
              )
            )
        )
    );
  }
}