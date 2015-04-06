
import 'dart:html';
import 'dart:convert';

void main() {
  DivElement content = querySelector('.content');
  AElement usersSearch = querySelector('.users-search');
  usersSearch.onClick.listen( (MouseEvent e) =>
    HttpRequest.getString("users/search").then(processJson)
  );
  document.cookie = "start=now/1";
  var request = new HttpRequest();
  var c = document.cookie.isEmpty;

  print(request.getAllResponseHeaders());
//  c.forEach((elem) {
//    content.innerHtml += "jkfdlsj";
//    content.innerHtml += elem.toString();
//  });
}

processJson(String json) {
  DivElement content = querySelector('.content');
  content.text = "";
  List parsedJson = JSON.decode(json);
  List a = parsedJson['users'][0].toList();
  a.forEach( (Element e) {
    content.append(new BRElement());
    content.append((new DivElement())..text = e['login']);
  });
  conversateUser();
}

conversateUser() {
  querySelectorAll('.content>div').onClick.listen((Event e){
    DivElement that = (e.target as DivElement);
    print(that.text);
    HttpRequest request = new HttpRequest();
    request.open("POST", "createconversation?that=${that.text}");
    request.onLoad.listen((Event e) => querySelector('.content').text = e.target.responseText);
    request.send();
  });
}