package yasuaki.kyoto.com.sqlmultitabletodoexp;

import java.util.ArrayList;
import java.util.List;

public class Utility {

  public static List<String> dummyTodoCreater(){
    ArrayList<String> dummyTodo = new ArrayList<>();
    dummyTodo.add("カフェオレを作る");
    dummyTodo.add("トイレに行く");
    dummyTodo.add("顔を洗う");
    dummyTodo.add("髭を剃る");

    return dummyTodo;
  }
}
