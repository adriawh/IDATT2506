import 'dart:convert';
import 'dart:io';

import 'package:to_do/model/objects.dart';

import 'package:path_provider/path_provider.dart';

class FileHandler {
  Future<String> get _localPath async {
    final directory = await getApplicationDocumentsDirectory();
    return directory.path;
  }

  Future<File> _localFile(String name) async {
    final path = await _localPath;
    return File('$path/$name.json');
  }

  Future<void> deleteFile(String name) async {
    final file = await _localFile(name);
    file.delete();
  }

  Future<File> writeList(ToDoList list) async {
    final file = await _localFile(list.name);
    // Write the file
    return file.writeAsString(json.encode(list));
  }

  Future<List<ToDoList>> readLists() async {
    final path = await _localPath;
    List<ToDoList> lists = [];
    List<FileSystemEntity> files = Directory(path).listSync();
    for (FileSystemEntity file in files) {
      if (file.path.endsWith('.json')) {
        File f = File(file.path);
        String contents = await f.readAsString();
        lists.add(ToDoList.fromJson(json.decode(contents)));
      }
    }
    return lists;
  }
}
