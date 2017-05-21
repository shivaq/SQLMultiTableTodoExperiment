package yasuaki.kyoto.com.sqlmultitabletodoexp;

import org.assertj.core.api.AbstractObjectAssert;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Yasuaki on 2017/05/19.
 */

public class TestSubscriberAssert<T>
    extends AbstractObjectAssert<TestSubscriberAssert<T>, TestSubscriber<T>> {

  //引数のsubscriber を引数とする TestSubscriberAssert のインスタンスを返す
  public static <T> TestSubscriberAssert<T> assertThat(TestSubscriber<T> subscriber) {
    return new TestSubscriberAssert<T>(subscriber);
  }


  //引数の Observable を subscribe する subscriber を引数とする
  // アサーション OBJ を返すファクトリーメソッド
  public static <T> TestSubscriberAssert<T> assertThatASubscriberTo(
      Observable<T> observable) {

    TestSubscriber<T> subscriber = new TestSubscriber<>();

    observable
        .subscribeOn(Schedulers.immediate())
        .subscribe(subscriber);

    return new TestSubscriberAssert<T>(subscriber);

  }

  public TestSubscriberAssert(TestSubscriber<T> actual) {
    //AbstractObjectAssert(A actual, Class<?> selfType)
    super(actual, TestSubscriberAssert.class);
  }

  public TestSubscriberAssert received(T... values) {
    actual.assertValues(values);
    return this;
  }
}

