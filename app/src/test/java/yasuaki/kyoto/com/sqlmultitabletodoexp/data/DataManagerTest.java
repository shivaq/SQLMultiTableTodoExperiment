package yasuaki.kyoto.com.sqlmultitabletodoexp.data;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import rx.Observable;
import yasuaki.kyoto.com.sqlmultitabletodoexp.MockModel;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.local.DbCrudHelper;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;

/**
 * No need to mock DbCrudHelper when a method just relay values.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

  @Mock
  DbCrudHelper mockDbCrudHelper;
  private DataManager dataManager;

  @Before
  public void setUp() throws Exception {
    dataManager = new DataManager(mockDbCrudHelper);
  }


  @Test
  public void loadTodo() throws Exception {
    // Given
    List<Todo> todoList = MockModel.newTodoList();
    given(mockDbCrudHelper.loadTodo()).willReturn(Observable.just(todoList));

    // When
    dataManager.loadTodo();
    // Then
    verify(mockDbCrudHelper).loadTodo();
  }


  @Test
  public void loadTag() throws Exception {
    // Given
    List<Tag> tagList = MockModel.newTagList();
    given(mockDbCrudHelper.loadTag()).willReturn(Observable.just(tagList));

    // When
    dataManager.loadTag();
    //Then
    verify(mockDbCrudHelper).loadTag();

  }

  @Test
  public void loadTagForTodo() throws Exception {
    // Given
    long todoId = MockModel.newSomeIdList().get(0);
//    List<Long> tagIdList = MockModel.newSomeIdList();
//    given(mockDbCrudHelper.loadTagForTodo(todoId)).willReturn(Observable.just(tagIdList));

    // When
    dataManager.loadTagForTodo(todoId);
    // Then
    verify(mockDbCrudHelper).loadTagForTodo(todoId);
  }

  @Test
  public void loadTodoForTag() throws Exception {
    // Given
    long someId = MockModel.newSomeIdList().get(0);

    // When
    dataManager.loadTodoForTag(someId);
    // Then
    verify(mockDbCrudHelper).loadTodoForTag(someId);

  }

  @Test
  public void loadTagWithTodoCounts() throws Exception {

    // When
    dataManager.loadTagWithTodoCounts();
    // Then
    verify(mockDbCrudHelper).loadTagWithTodoCounts();
  }

  @Test
  public void insertTodo() throws Exception {
    // Given
    List<Long> someIdList = MockModel.newSomeIdList();

    // When
    dataManager.insertTodo("todo", "tag", someIdList);
    // Then
    verify(mockDbCrudHelper).insertTodo("todo", "tag", someIdList);
  }

  @Test
  public void updateTodoIsChecked() throws Exception {

    // When
    dataManager.updateTodoIsChecked(true, 1L);
    // Then
    verify(mockDbCrudHelper).updateTodoIsChecked(true, 1L);
  }

  @Test
  public void updateTodo() throws Exception {
    // Given
    List<Long> someIdList = MockModel.newSomeIdList();

    // When
    dataManager.updateTodo("todo", true, "tag", 1L, someIdList);
    // Then
    verify(mockDbCrudHelper).updateTodo("todo", true, "tag", 1L, someIdList);
  }

  @Test
  public void deleteTodo() throws Exception {

    // When
    dataManager.deleteTodo(1L);
    // Then
    verify(mockDbCrudHelper).deleteTodo(1L);
  }

}