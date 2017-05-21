package yasuaki.kyoto.com.sqlmultitabletodoexp;


import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.Java6Assertions.assertThat;

import android.os.Build;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.local.DbCrudHelper;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.local.DbOpenHelper;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag.TagWithTodoCounts;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo.TodoForTag;


@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP_MR1)
@RunWith(RobolectricTestRunner.class)
public class DbCrudHelperTest {

  private static final String TEST_DB_NAME = DbOpenHelper.DATABASE_NAME;
  private static final String TODO_DATA_TYPE = "todo";
  private static final String TAG_DATA_TYPE = "tag";

  // RuntimeEnvironment.application) →Context の代わり
  private final DbOpenHelper openHelper =
      new DbOpenHelper(RuntimeEnvironment.application);
  private final DbCrudHelper dbCrudHelper = new DbCrudHelper(openHelper);

  private DbCrudHelperRobot crudHelperRobot;

  @Before
  public void setUp() {
    dbCrudHelper.clearTables().subscribe();
    crudHelperRobot = new DbCrudHelperRobot();
  }

  @After
  public void tearDown() {
    dbCrudHelper.clearTables();
  }

  @Test
  public void whenDbOpenHelperCreated_thenDBNameShouldBeSet() {
    assertThat(TEST_DB_NAME).isEqualTo(openHelper.getDatabaseName());
  }

  @Test
  public void insertTodoReturns_InsertedNumOfRows() {
    long todoId = 0;
    // Given
    String[] todoStrArray = MockModel.newTodoStrArray();

    // When
    for (int i = 0; i < todoStrArray.length; i++) {
      todoId = dbCrudHelper.insertTodo(todoStrArray[i], "", null);
    }
    // then
    then(todoId).isEqualTo(todoStrArray.length);
  }

  @Test
  public void When_insertTodoAndNewTag_Then_NoError() throws Exception {

    String[] todoStrArray = MockModel.newTodoStrArray();
    String[] tagStrArray = MockModel.newTagStrArray();

    for (int i = 0; i < todoStrArray.length; i++) {
      dbCrudHelper.insertTodo(todoStrArray[i], tagStrArray[i], null);
    }
  }

  @Test
  public void loadTodoReturns_correctValues() throws Exception {

    // Given
    String[] todoStrArray = crudHelperRobot
        .given_insert_todo_And_tag_Then_returns_insertedTodo_StringArray(TODO_DATA_TYPE);

    // When
    List<Todo> todoList = dbCrudHelper.loadTodo().toBlocking().first();

    //Then
    crudHelperRobot
        .then_insertedToooooodoArray_and_loadedTodoList_isEqual(todoList, todoStrArray);
  }

  @Test
  public void loadTag_Then_returnsCorrectValues() throws Exception {
    //Given
    String[] tagStrArray = crudHelperRobot
        .given_insert_todo_And_tag_Then_returns_insertedTodo_StringArray(TAG_DATA_TYPE);

    //When
    List<Tag> tagList = dbCrudHelper.loadTag().toBlocking().first();

    //then
    crudHelperRobot
        .then_insertedTaaaaaaaaaagArray_and_loadedTagList_isEqual(tagList, tagStrArray);

  }

  @Test
  public void updateTodo_Then_check_each_loadMethods() {

    List<Long> checkedTagIdList_1 = new ArrayList<>();
    List<Long> checkedTagIdList_2 = new ArrayList<>();
    List<Long> tagListForTodo;
    String updateTodoStr_1 = "cycling";
    String updateTodoStr_2 = "eat";
    String updatedTagStr_1 = "hate";
    String updatedTagStr_2 = "old";

    // Given
    String[] todoStrArray = crudHelperRobot
        .given_insert_todo_And_tag_Then_returns_insertedTodo_StringArray(TODO_DATA_TYPE);

    // When
    List<Todo> todoList = dbCrudHelper.loadTodo().toBlocking().first();

    //Then
    crudHelperRobot
        .then_insertedToooooodoArray_and_loadedTodoList_isEqual(todoList, todoStrArray);

    // Given values to update
    List<Tag> tagList = dbCrudHelper.loadTag().toBlocking().first();
    long todoId_1 = todoList.get(0)._id();
    long todoId_2 = todoList.get(4)._id();
    long tagId_1 = tagList.get(0)._id();
    long tagId_2 = tagList.get(1)._id();
    checkedTagIdList_1.add(tagId_1);
    checkedTagIdList_2.add(tagId_1);
//    checkedTagIdList_2.add(tagId_2);
//    checkedTagIdList_1.add(tagId_2);

    // When update
    dbCrudHelper
        .updateTodoString(updateTodoStr_1, true, updatedTagStr_1, todoId_1, checkedTagIdList_1);
//    dbCrudHelper
//        .updateTodoString(updateTodoStr_2, true, updatedTagStr_2, todoId_2, checkedTagIdList_2);
    todoList = dbCrudHelper.loadTodo().toBlocking().first();
    tagList = dbCrudHelper.loadTag().toBlocking().first();
    List<String> todoStrList = retrieveStringListFromTooodoooList(todoList);
    List<String> tagStrList = retrieveStringListFromTaaaaaaaaagList(tagList);

    // Then updatedValue could be loaded
//    then(todoStrList).contains(updateTodoStr_1, updateTodoStr_2);
//    then(tagStrList).contains(updatedTagStr_1, updatedTagStr_2);

//    // When loadTagForTodo
//    tagListForTodo = dbCrudHelper.loadTagForTodo(todoId_1).toBlocking().first();
//    // Then
//    then(tagListForTodo).contains(tagId_1, tagId_2);
//    then(tagListForTodo.size()).isEqualTo(2);
//    // when
//    tagListForTodo = dbCrudHelper.loadTagForTodo(todoId_2).toBlocking().first();
//    // then
//    then(tagListForTodo).contains(tagId_1);
//    then(tagListForTodo.size()).isEqualTo(1);

    // When loadTodoForTag
    List<TodoForTag> todoIdForTheTag_1 = dbCrudHelper.loadTodoForTag(tagId_1).toBlocking().first();
    List<TodoForTag> todoIdForTheTag_2 = dbCrudHelper.loadTodoForTag(tagId_2).toBlocking().first();

    // Then
    then(todoIdForTheTag_1.size()).isEqualTo(1);
    then(todoIdForTheTag_2.size()).isEqualTo(1);
    then(todoIdForTheTag_2.get(0).todo()).isEqualTo("ddd");

    then(todoId_1).isEqualTo(todoIdForTheTag_1.get(0).todo()._id());
    then(todoId_1).isEqualTo(todoIdForTheTag_2.get(0).todo()._id());

    // When
    List<TagWithTodoCounts> tagWithTodoCountsList =
        dbCrudHelper.loadTagWithTodoCounts().toBlocking().first();

    // Then
    then(tagWithTodoCountsList.get(0).tag_count()).isEqualTo(1);
    then(tagWithTodoCountsList.get(1).tag_count()).isEqualTo(2);

    // Given
    checkedTagIdList_1 = new ArrayList<>();

    // When uncheck all tag for the todoId
    dbCrudHelper.updateTodoString("", false, "", todoId_1, checkedTagIdList_1);
    tagListForTodo = dbCrudHelper.loadTagForTodo(todoId_1).toBlocking().first();

    // Then
    then(tagListForTodo).doesNotContain(tagId_1, tagId_2);
  }


  /**********************************************************************/
  List<String> retrieveStringListFromTaaaaaaaaagList(List<Tag> tagList) {
    List<String> tagStrList = new ArrayList<>();
    for (Tag tag : tagList) {
      tagStrList.add(tag.tag());
    }
    return tagStrList;
  }

  List<String> retrieveStringListFromTooodoooList(List<Todo> todoList) {
    List<String> todoStrList = new ArrayList<>();
    for (Todo todo : todoList) {
      todoStrList.add(todo.todo());
    }
    return todoStrList;
  }

  /***************************  Robot  **************************************/
  class DbCrudHelperRobot {

    String[] given_insert_todo_And_Returns_insertedTodo_StringArray() {
      String[] todoStrArray = MockModel.newTodoStrArray();
      for (int i = 0; i < todoStrArray.length; i++) {
        dbCrudHelper.insertTodo(todoStrArray[i], "", null);
      }
      return todoStrArray;
    }

    String[] given_insert_todo_And_tag_Then_returns_insertedTodo_StringArray(String returnType) {
      String[] todoStrArray = MockModel.newTodoStrArray();
      String[] tagStrArray = MockModel.newTagStrArray();

      for (int i = 0; i < todoStrArray.length; i++) {
        dbCrudHelper.insertTodo(todoStrArray[i], tagStrArray[i], null);
      }
      if (returnType.equals(TODO_DATA_TYPE)) {
        return todoStrArray;
      } else {
        return tagStrArray;
      }
    }



    /*******************************  When ****************************************/


    /*******************************  then ****************************************/
    DbCrudHelperRobot then_insertedTaaaaaaaaaagArray_and_loadedTagList_isEqual(
        List<Tag> tagList, String[] tagArray) {

      then(tagList).hasSize(tagArray.length);
      for (int i = 0; i < tagList.size(); i++) {
        then(tagList.get(i).tag()).isEqualTo(tagArray[i]);
      }
      return this;
    }

    DbCrudHelperRobot then_insertedToooooodoArray_and_loadedTodoList_isEqual(
        List<Todo> todoList, String[] todoArray) {

      then(todoList).hasSize(todoArray.length);
      for (int i = 0; i < todoList.size(); i++) {
        then(todoList.get(i).todo()).isEqualTo(todoArray[i]);
      }
      return this;
    }
  }
}
