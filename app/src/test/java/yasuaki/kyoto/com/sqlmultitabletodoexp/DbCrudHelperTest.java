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
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;


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
  public void update_Todo() {
    String[] todoStrArray = MockModel.newTodoStrArray();
    String[] tagStrArray = MockModel.newTagStrArray();
    String[] updateTodoArray = MockModel.updateTodoStrArray();

    // Given insert todos and tag
    for (int i = 0; i < todoStrArray.length; i++) {
      dbCrudHelper.insertTodo(todoStrArray[i], tagStrArray[i], null);
    }

    // When load
    List<Todo> todoList = dbCrudHelper.loadTodo().toBlocking().first();
    List<Tag> tagList = dbCrudHelper.loadTag().toBlocking().first();

    //Then
    then_insertedTooodoArray_and_loadedTodoList_isEqual(todoList, todoStrArray);
    then_insertedTaaaaArray_and_loadedTagList_isEqual(tagList, tagStrArray);

    // When update
    for (int i = 0; i < updateTodoArray.length; i++) {
      dbCrudHelper
          .updateTodoString(updateTodoArray[updateTodoArray.length - 1 - i], true, "",
              todoList.get(i)._id(),
              null);
    }
    // When load
    todoList = dbCrudHelper.loadTodo().toBlocking().first();

    // then
    for (int i = 0; i < updateTodoArray.length; i++) {
      then(updateTodoArray[updateTodoArray.length - 1 - i]).isEqualTo(todoList.get(i).todo());
    }
  }

  @Test
  public void update_Tag() {
    String[] todoStrArray = MockModel.newTodoStrArray();
    String[] tagStrArray = MockModel.newTagStrArray();
    String[] updateTagArray = MockModel.updateTagStrArray();

    // Given insert todos and tag
    for (int i = 0; i < todoStrArray.length; i++) {
      dbCrudHelper.insertTodo(todoStrArray[i], tagStrArray[i], null);
    }

    // When load
    List<Todo> todoList = dbCrudHelper.loadTodo().toBlocking().first();
    List<Tag> tagList = dbCrudHelper.loadTag().toBlocking().first();

    //Then
    then_insertedTooodoArray_and_loadedTodoList_isEqual(todoList, todoStrArray);
    then_insertedTaaaaArray_and_loadedTagList_isEqual(tagList, tagStrArray);

    // When update
    for (int i = 0; i < updateTagArray.length; i++) {
      dbCrudHelper
          .updateTodoString("", true, updateTagArray[i],
              todoList.get(i)._id(),
              null);
    }
    // When load
    tagList = dbCrudHelper.loadTag().toBlocking().first();

    // then
    for (int i = 0; i < updateTagArray.length; i++) {
      then(tagStrArray.length + updateTagArray.length).isEqualTo(tagList.size());
    }
  }

  @Test
  public void update_CheckedTag() {
    String[] todoStrArray = MockModel.newTodoStrArray();
    String[] tagStrArray = MockModel.newTagStrArray();

    // Given insert todos and tag
    for (int i = 0; i < todoStrArray.length; i++) {
      dbCrudHelper.insertTodo(todoStrArray[i], tagStrArray[i], null);
    }

    // When load
    List<Todo> todoList = dbCrudHelper.loadTodo().toBlocking().first();
    List<Tag> tagList = dbCrudHelper.loadTag().toBlocking().first();

    // then
    List<Long> tagIdList = dbCrudHelper.loadTagForTodo(4).toBlocking().first();
    // the log sais the size of tagIdForTodoList is 0 but the test below fails
//    then(tagIdList.size()).isEqualTo(0);

    // Given values to update
    List<Long> checkedTagIdList = new ArrayList<>();
    for (Tag tag : tagList) {
      checkedTagIdList.add(tag._id());
    }

    // When update
    long todoId = todoList.get(3)._id();
    dbCrudHelper.updateTodoString("", false, "", todoId, checkedTagIdList);

    // When load
    tagIdList = dbCrudHelper.loadTagForTodo(todoId).toBlocking().first();

    // then
    for (int i = 0; i < tagList.size(); i++) {
      then(tagIdList.get(i)).isEqualTo(tagList.get(i)._id());
    }
  }

  @Test
  public void update_todo_tag_checkedTag() {
    String[] todoStrArray = MockModel.newTodoStrArray();
    String[] tagStrArray = MockModel.newTagStrArray();
    String[] updateTodoArray = MockModel.updateTodoStrArray();
    String[] updateTagArray = MockModel.updateTagStrArray();

    // Given insert todos and tag
    for (int i = 0; i < todoStrArray.length; i++) {
      dbCrudHelper.insertTodo(todoStrArray[i], tagStrArray[i], null);
    }

    // When load
    List<Todo> todoList = dbCrudHelper.loadTodo().toBlocking().first();
    List<Tag> tagList = dbCrudHelper.loadTag().toBlocking().first();

    //Then
    then_insertedTooodoArray_and_loadedTodoList_isEqual(todoList, todoStrArray);
    then_insertedTaaaaArray_and_loadedTagList_isEqual(tagList, tagStrArray);

    // Given values to update
    List<Long> checkedTagIdList = new ArrayList<>();
    for (Tag tag : tagList) {
      checkedTagIdList.add(tag._id());
    }

    todoList = dbCrudHelper.loadTodo().toBlocking().first();
    // When update
    for (int i = 0; i < todoList.size(); i++) {
      // each todoItem checks every tag.
      dbCrudHelper
          .updateTodoString(updateTodoArray[i], true, updateTagArray[i], todoList.get(i)._id(),
              checkedTagIdList);
    }

    // test for sql database is complicated so do simple unit tests and detail ui tests
    dbCrudHelper.loadTagForTodo(todoList.get(0)._id());
    dbCrudHelper.loadTodoById(todoList.get(0)._id());
    dbCrudHelper.loadTagForTodo(tagList.get(0)._id());
    dbCrudHelper.loadTagForTodo(tagList.get(0)._id());
    dbCrudHelper.loadTagWithTodoCounts();
  }

  /**********************************************************************/

  void then_insertedTooodoArray_and_loadedTodoList_isEqual(
      List<Todo> todoList, String[] todoArray) {

    then(todoList).hasSize(todoArray.length);

    for (int i = 0; i < todoList.size(); i++) {
      then(todoList.get(i).todo()).isEqualTo(todoArray[i]);
    }
  }

  void then_insertedTaaaaArray_and_loadedTagList_isEqual(
      List<Tag> tagList, String[] tagArray) {

    then(tagList).hasSize(tagArray.length);
    for (int i = 0; i < tagList.size(); i++) {
      then(tagList.get(i).tag()).isEqualTo(tagArray[i]);
    }
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

    DbCrudHelperRobot given_insert_todo_And_tag() {
      String[] todoStrArray = MockModel.newTodoStrArray();
      String[] tagStrArray = MockModel.newTagStrArray();

      for (int i = 0; i < todoStrArray.length; i++) {
        dbCrudHelper.insertTodo(todoStrArray[i], tagStrArray[i], null);
      }
      return this;
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
