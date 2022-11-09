class ToDoList {
  String name;
  List<ListItem> items = [];

  ToDoList(this.name);

  ToDoList.fromJson(Map<String, dynamic> json)
      : name = json['name'],
        items = (json['items'] as List<dynamic>)
            .map((e) => ListItem.fromJson(e as Map<String, dynamic>))
            .toList();

  Map<String, dynamic> toJson() => {
        'name': name,
        'items': items,
      };
}

class ListItem {
  String name;
  bool isDone;

  ListItem(this.name, this.isDone);

  ListItem.fromJson(Map<String, dynamic> json)
      : name = json['name'],
        isDone = json['isDone'];

  Map<String, dynamic> toJson() => {
        'name': name,
        'isDone': isDone,
      };
}
