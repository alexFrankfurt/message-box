
import 'dart:html';
import 'dart:convert';

void main() {
  DivElement content = querySelector('.content');
  HttpRequest request = new HttpRequest();
  WebSocket ws = new WebSocket("ws://localhost:9000/socket");
  request.open("GET", "getconversations");
  request.onLoad.listen((Event e) {
    List parsedJson = JSON.decode(e.target.responseText);
    List ids = parsedJson[0];
    ids.forEach( (Element e) =>
      content.append((new DivElement())..text = e)
    );
    querySelectorAll('.content>div').onClick.listen((MouseEvent e) {
      HttpRequest request = new HttpRequest();
      request.open('GET', 'conversation?id=${e.target.text}');
      request.onLoad.listen((ProgressEvent e){
        querySelector('.content').innerHtml = e.target.responseText;
        querySelector('.content>input[type=submit]').onClick.listen((MouseEvent e){
          print(querySelector('.content>input[type=text]').value);

          var data = {"id" : "100","message" : querySelector('.content>input[type=text]').value };
          ws.send(JSON.encode(data));
        });
      });
      request.send();
    });
  });
  request.send();
  ws.onOpen.listen((Event e) => print("Connection established"));
  ws.onMessage.listen((MessageEvent e) => print("Received message: ${e.data}"));
  ws.onClose.listen((Event e) => print("Connection lost"));

}
