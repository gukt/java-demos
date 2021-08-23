package demos;

import java.util.ArrayList;
import java.util.List;

/**
 * GC class
 *
 * @author https://github.com/gukt
 */
public class Foo {

  private static List<Foo> deathlessList = new ArrayList<>();

  private int id;

  private Foo(int id) {
    this.id = id;
    System.out.println("Initialized: " + this);
  }

  @Override
  public String toString() {
    return "Foo#" + id;
  }

  @Override
  protected void finalize() throws Throwable {
    System.out.println("Destroying: " + this);
    //    deathlessList.add(this);
    super.finalize();
  }

  public static void main(String[] args) {
    for (int i = 0; i < 5; i++) {
      new Foo(i);
      //      deathlessSet.add(new Foo(i));
      System.gc();
//      System.runFinalization();
//      System.gc();
    }
    System.out.println("deathlessList: size: " + deathlessList.size() + ": " + deathlessList);
    System.out.println("deathlessList: size: " + deathlessList.size() + ": " + deathlessList);
    System.out.println("deathlessList: size: " + deathlessList.size() + ": " + deathlessList);
    System.out.println("deathlessList: size: " + deathlessList.size() + ": " + deathlessList);
  }
}
