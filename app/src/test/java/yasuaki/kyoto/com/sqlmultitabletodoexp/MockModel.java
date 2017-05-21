package yasuaki.kyoto.com.sqlmultitabletodoexp;

import java.util.ArrayList;
import java.util.List;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;

/**
 * Created by Yasuaki on 2017/05/19.
 */

public class MockModel {

  public static String[] newTodoStrArray() {

    return new String[]{"assassinate him", "dancing", "go to America", "go shopping",
        "have dinner with Michael"
    };
  }

  public static String[] updateTodoStrArray() {

    return new String[]{"crying", "do meditation", "feed Hachi", "give him some money",
        "go to the park"};
  }

  public static String[] newTagStrArray() {

    return new String[]{"car", "joy", "people", "punishment", "travel"};
  }

  public static String[] updateTagStrArray() {

    return new String[]{"animal", "bicycle", "home", "reward", "sadness"};
  }

  public static List<Todo> newTodoList(){
    List<Todo> todoList = new ArrayList<>();
    todoList.add(Todo.create(1, "assassinate him", 99L, 99L, false));
    todoList.add(Todo.create(2, "dancing", 99L, 99L, false));
    todoList.add(Todo.create(3, "go to America", 99L, 99L, false));
    todoList.add(Todo.create(4, "go shopping", 99L, 99L, false));
    todoList.add(Todo.create(5, "have dinner with Michael", 99L, 99L, false));
    return todoList;
  }

  public static List<Todo> updateTodoList(){
    List<Todo> todoList = new ArrayList<>();
    todoList.add(Todo.create(1, "crying", 99L, 99L, false));
    todoList.add(Todo.create(2, "do meditation", 99L, 99L, false));
    todoList.add(Todo.create(3, "feed Hachi", 99L, 99L, false));
    todoList.add(Todo.create(4, "give him some money", 99L, 99L, false));
    todoList.add(Todo.create(5, "go to the park", 99L, 99L, false));
    return todoList;
  }

  public static List<Tag> newTagList(){
    List<Tag> tagList = new ArrayList<>();
    tagList.add(Tag.create(1, "car", 99L));
    tagList.add(Tag.create(2, "joy", 99L));
    tagList.add(Tag.create(3, "people", 99L));
    tagList.add(Tag.create(4, "punishment", 99L));
    tagList.add(Tag.create(5, "travel", 99L));
    return tagList;
  }

  public static List<Tag> updateTagList(){
    List<Tag> tagList = new ArrayList<>();
    tagList.add(Tag.create(1, "animal", 99L));
    tagList.add(Tag.create(2, "bicycle", 99L));
    tagList.add(Tag.create(3, "home", 99L));
    tagList.add(Tag.create(4, "reward", 99L));
    tagList.add(Tag.create(5, "sadness", 99L));
    return tagList;
  }
  public static List<Long> newSomeIdList(){
    List<Long> tagIdList =new ArrayList<>();
    tagIdList.add(1L);
    tagIdList.add(2L);
    tagIdList.add(3L);
    tagIdList.add(4L);
    tagIdList.add(5L);
    return tagIdList;
  }
}
