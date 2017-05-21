package yasuaki.kyoto.com.sqlmultitabletodoexp;

/**
 * Created by Yasuaki on 2017/05/19.
 */

public class MockModel {

  public static String[] newTagStrArray() {

    return new String[]{"car", "joy", "people", "punishment", "travel"};
  }

  public static String[] updateTagStrArray() {

    return new String[]{"animal", "bicycle", "home", "reward", "sadness"};
  }

  public static String[] newTodoStrArray() {

    return new String[]{"assassinate the angel", "dancing", "go to America", "go shopping",
        "have dinner with Michael"
    };
  }

  public static String[] updateTodoStrArray() {

    return new String[]{"crying", "do meditation", "feed Hachi", "give him some money",
        "go to the park"};
  }
}
