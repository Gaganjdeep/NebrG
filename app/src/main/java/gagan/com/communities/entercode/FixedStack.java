package gagan.com.communities.entercode;

import java.util.Stack;

/**
 * @author Gagan
 */
public class FixedStack<T> extends Stack<T> {

  int maxSize = 0;

  @Override public T push(T object) {
    if (maxSize > size()) {
      return super.push(object);
    }

    return object;
  }

  public int getMaxSize() {
    return maxSize;
  }

  public void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }
}
