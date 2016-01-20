import 'dart:html';

final SM = "Dart!";

void main() {
  ButtonElement logIn = querySelector('.log_in>button');
  ButtonElement signUp = querySelector('.sign_up>button');
  var status = querySelector(".status>em");
  logIn.onClick.listen(userLogIn);
  signUp.onClick.listen(userSignUp);

//  HttpRequest.getString('users').then( (String result) =>
//    print(result)
//  );

//  WebSocket ws = new WebSocket("ws://localhost:9000/socket");
//  ws.onOpen.listen((Event e) =>print("Connection established"));
//  ws.onMessage.listen((MessageEvent e) => print("on message ${e.data}"));
//  ws.onClose.listen((Event e) => print("Connection lost"));
//  status.onClick.listen((MouseEvent e) => ws.send("lalala"));
}

userLogIn(MouseEvent e) {
  HttpRequest LIRequest = new HttpRequest();
  LIRequest.open('GET', 'http://localhost:9000/login');
  LIRequest.onLoad.listen((e) =>
    querySelector('.content').innerHtml = e.target.responseText
  );
  LIRequest.send();
}

userSignUp(MouseEvent e) {
  HttpRequest.getString('http://localhost:9000/signup').then( (String cont) =>
    querySelector('.content').innerHtml = cont
  );
}
