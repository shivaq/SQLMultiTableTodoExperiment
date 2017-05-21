package yasuaki.kyoto.com.sqlmultitabletodoexp;

import java.util.ArrayList;
import java.util.List;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;

/**
 * Created by Yasuaki on 2017/05/19.
 */

public class MockModel {

  public static List<Tag> newTag(){

    List<Tag> tagList = new ArrayList();
    tagList.add(Tag.create(1, "a", 22));
    tagList.add(Tag.create(2, "b", 22));
    tagList.add(Tag.create(3, "c", 22));
    tagList.add(Tag.create(4, "d", 22));
    tagList.add(Tag.create(5, "e", 22));

    return tagList;
  }

  public static List<Todo> newTodo(){

    List<Todo> todoList = new ArrayList();
    todoList.add(Todo.create(1, "h", 22, 33, false));
    todoList.add(Todo.create(2, "i", 22, 33, false));
    todoList.add(Todo.create(3, "j", 22, 33, false));
    todoList.add(Todo.create(4, "k", 22, 33, false));
    todoList.add(Todo.create(5, "l", 22, 33, false));

    return todoList;
  }
  public static String[] newTagStrArray(){

    return new String[]{"a", "b", "c", "d", "e"};
  }

  public static String[] newTodoStrArray(){

    return new String[]{"gg", "sdfasd", "asdfawe", "saerwefaserw4era", "adsfas"};
  }
}
