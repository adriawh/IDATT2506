import 'package:flutter/material.dart';
import 'package:to_do/model/objects.dart';
import 'package:to_do/manager/file_handler.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => FocusManager.instance.primaryFocus?.unfocus(),
      child: MaterialApp(
        title: 'Flutter Demo',
        theme: ThemeData(
          primarySwatch: Colors.cyan,
        ),
        home: const MyHomePage(),
      ),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> with TickerProviderStateMixin {
  List<ToDoList> toDoLists = [];

  late TabController tabController =
      TabController(length: 0, vsync: this, initialIndex: 0);
  int currentListIndex = 0;

  late TabBar bar;

  FileHandler fileHandler = FileHandler();

  final taskInputController = TextEditingController();
  final newListInputController = TextEditingController();

  final focusNode = FocusNode();

  @override
  void initState() {
    super.initState();
    fileHandler.readLists().then((value) {
      setState(() {
        toDoLists = value;
        tabController = TabController(
            length: toDoLists.length,
            vsync: this,
            initialIndex: currentListIndex);
      });
    });
  }

  sortList() {
    setState(() {
      toDoLists[currentListIndex].items.sort((a, _) {
        return a.isDone ? 1 : -1;
      });
    });
    fileHandler.writeList(toDoLists[currentListIndex]);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('ToDo'),
        bottom: TabBar(
          controller: tabController,
          isScrollable: true,
          tabs: [
            for (ToDoList toDoList in toDoLists) ...[
              Tab(
                child: Text(
                  toDoList.name,
                  style: const TextStyle(fontSize: 20),
                ),
              )
            ],
          ],
          onTap: (index) {
            setState(() {
              currentListIndex = index;
            });
          },
        ),
        actions: [
          TextButton.icon(
            style: ButtonStyle(
              foregroundColor: MaterialStateProperty.all(Colors.white),
            ),
            label: const Text("New List"),
            icon: const Icon(Icons.add),
            onPressed: newListDialog,
          ),
        ],
      ),
      backgroundColor: Colors.cyan[50],
      body: Column(
        children: [
          Container(
            padding: const EdgeInsets.all(15),
            child: Row(
              children: <Widget>[
                if (toDoLists.isNotEmpty) ...[
                  Expanded(
                      child: TextField(
                    controller: taskInputController,
                    focusNode: focusNode,
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Enter a task',
                    ),
                    onSubmitted: (value) {
                      setState(() {
                        if (taskInputController.text.isNotEmpty) {
                          toDoLists[currentListIndex]
                              .items
                              .add(ListItem(taskInputController.text, false));
                          taskInputController.clear();
                          sortList();
                        }
                      });
                      focusNode.requestFocus();
                    },
                  )),

                  //square add button matchin textfield on the left
                  Container(
                    margin: const EdgeInsets.only(left: 10),
                    child: IconButton(
                      icon: const Icon(Icons.add),
                      onPressed: () {
                        setState(() {
                          if (taskInputController.text.isNotEmpty) {
                            toDoLists[currentListIndex]
                                .items
                                .add(ListItem(taskInputController.text, false));
                            taskInputController.clear();
                            sortList();
                          }
                        });
                        focusNode.requestFocus();
                      },
                    ),
                  ),
                ]
              ],
            ),
          ),
          Flexible(
            child: ListView(
              children: [
                if (toDoLists.isNotEmpty) ...[
                  for (ListItem item in toDoLists[currentListIndex].items) ...[
                    Container(
                      margin: const EdgeInsets.all(5),
                      child: CheckboxListTile(
                        title: Text(item.name, style: textStyle(item)),
                        value: item.isDone,
                        onChanged: (bool? value) {
                          setState(() {
                            item.isDone = value!;
                            sortList();
                          });
                        },
                        tileColor: Colors.white,
                        controlAffinity: ListTileControlAffinity.leading,
                        secondary: IconButton(
                          icon: const Icon(Icons.delete),
                          color: Colors.red,
                          onPressed: () {
                            setState(() {
                              toDoLists[currentListIndex].items.remove(item);
                              fileHandler
                                  .writeList(toDoLists[currentListIndex]);
                            });
                          },
                        ),
                      ),
                    ),
                  ]
                ] else ...[
                  Center(
                    child: Container(
                      padding: const EdgeInsets.only(top: 100),
                      child: const Text("No Lists",
                          style: TextStyle(
                              fontSize: 25,
                              fontWeight: FontWeight.bold,
                              color: Colors.grey)),
                    ),
                  )
                ]
              ],
            ),
          )
        ],
      ),
      floatingActionButton: FloatingActionButton(
        backgroundColor: Colors.red,
        onPressed: deleteListDialog,
        child: const Icon(Icons.delete),
      ),
    );
  }

  TextStyle textStyle(ListItem item) {
    if (item.isDone) {
      return const TextStyle(
        decoration: TextDecoration.lineThrough,
        color: Colors.grey,
        decorationColor: Colors.grey,
        decorationThickness: 2,
        fontSize: 20,
      );
    } else {
      return const TextStyle(
        fontSize: 20,
        fontWeight: FontWeight.w500,
      );
    }
  }

//dialog for deleting list
  void deleteListDialog() {
    showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            title: const Text("Delete List"),
            content: const Text("Are you sure you want to delete this list?"),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.of(context).pop();
                },
                child: const Text("Cancel"),
              ),
              TextButton(
                onPressed: () {
                  setState(() {
                    fileHandler
                        .deleteFile(toDoLists.elementAt(currentListIndex).name);
                    toDoLists.removeAt(currentListIndex);
                    tabController = TabController(
                        length: toDoLists.length,
                        vsync: this,
                        initialIndex: currentListIndex);
                    currentListIndex = 0;
                  });
                  Navigator.pop(context);
                },
                child: const Text("Delete"),
              ),
            ],
          );
        });
  }

//dialog for adding new list
  void newListDialog() {
    showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            title: const Text("New List"),
            content: TextField(
              controller: newListInputController,
              decoration: const InputDecoration(
                border: OutlineInputBorder(),
                labelText: 'Enter a name for the list',
              ),
              //ad list when submitted
              onSubmitted: (value) {
                newList();
              },
            ),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.of(context).pop();
                },
                child: const Text("Cancel"),
              ),
              TextButton(
                onPressed: () {
                  newList();
                },
                child: const Text("Add"),
              ),
            ],
          );
        });
  }

  newList() {
    if (newListInputController.text != "" &&
        toDoLists
            .where((element) => element.name == newListInputController.text)
            .isEmpty) {
      setState(() {
        toDoLists.add(ToDoList(newListInputController.text));
        currentListIndex = toDoLists.length - 1;
        tabController = TabController(
            length: toDoLists.length,
            vsync: this,
            initialIndex: currentListIndex);
      });
      fileHandler.writeList(toDoLists[currentListIndex]);
      Navigator.pop(context);
      newListInputController.clear();
    } else {
      Navigator.pop(context);
      listExistDialog();
    }
  }

  listExistDialog() {
    showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            title: const Text("Error"),
            content: const Text("A list with that name already exists"),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.of(context).pop();
                },
                child: const Text("Ok"),
              ),
            ],
          );
        });
  }
}
