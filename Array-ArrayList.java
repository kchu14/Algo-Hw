import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class HW5 {
  public static void main(String args[]) {
    // checks if right input
    if (args.length == 0) {
      System.out.println("no args given");
    }
    if (args.length > 1) {
      System.out.println("wrong format, input only one number");
    }
    else {
      try {
        Integer.parseInt(args[0]);
      }
      catch (NumberFormatException e) {
        throw new IllegalArgumentException("wrong format, input an int");
      }
      catch (NullPointerException e) {
        throw new IllegalArgumentException("wrong format, input an int");
      }
      ArrayToList test1 = new ArrayToList();

      // runs the given number of adds and deletes
      for (int i = 0; i < Integer.parseInt(args[0]); i++) {
        test1.add(1);
      }
      for (int j = 0; j < Integer.parseInt(args[0]); j++) {
        test1.deleteLast();
      }
      // prints results
      System.out.println("---------------------");
      System.out.println("add no resize: " + average(test1.addNoResizeList));
      System.out.println("add resize: " + average(test1.addResizeList));
      System.out.println(
          "total average of add: " + totalAverage(test1.addResizeList, test1.addNoResizeList));
      System.out.println("delete no resize: " + average(test1.deleteNoResizeList));
      System.out.println("delete resize: " + average(test1.deleteResizeList));
      System.out.println("total average of delete: "
          + totalAverage(test1.deleteResizeList, test1.deleteNoResizeList));
      System.out.println("---------------------");
    }

  }

  // finds the total average
  public static long totalAverage(LinkedList<Long> given, LinkedList<Long> given2) {
    long temp = 0;
    for (long i : given) {
      temp += i;
    }
    for (long j : given2) {
      temp += j;
    }
    System.out.println("total sum of time: " + temp);
    return (temp / (given.size() + given2.size()));
  }

  // finds the average
  public static long average(LinkedList<Long> given) {
    long temp = 0;
    for (long i : given) {
      temp += i;
    }
    return (temp / given.size());
  }

}

class ArrayToList {

  int[] initArray;
  int index;
  boolean stuffInArray;
  LinkedList<Long> addNoResizeList;
  LinkedList<Long> addResizeList;
  LinkedList<Long> deleteNoResizeList;
  LinkedList<Long> deleteResizeList;

  ArrayToList() {
    // initial array size of 50
    this.initArray = new int[50];
    this.index = 0;
    this.stuffInArray = false;
    this.addNoResizeList = new LinkedList<Long>();
    this.addResizeList = new LinkedList<Long>();
    this.deleteNoResizeList = new LinkedList<Long>();
    this.deleteResizeList = new LinkedList<Long>();

  }

  public void add(int x) {
    long startTime = System.nanoTime();
    // increments the index and adds the element resizes if the index reaches the init array length
    if (this.index == this.initArray.length - 1) {
      initArray[this.index] = x;
      index++;
      int[] temp = new int[this.initArray.length * 2];
      for (int i = 0; i < this.index; i++) {
        temp[i] = this.initArray[i];
      }
      this.initArray = temp;
      // this.initArray = Arrays.copyOf(initArray, this.initArray.length * 2);
      long addResize = System.nanoTime();
      this.addResizeList.add(addResize - startTime);
    }
    else {
      initArray[this.index] = x;
      index++;
      if (index == initArray.length / 4) {
        this.stuffInArray = true;
      }
      long addNoResize = System.nanoTime();
      this.addNoResizeList.add(addNoResize - startTime);
    }

  }

  public void deleteLast() {
    long startTime = System.nanoTime();
    //decrements the index and resizes the array if index is 1/4 of init array length
    if (index <= this.initArray.length / 4 - 1 && this.stuffInArray) {
      index--;
      int[] temp = new int[this.initArray.length / 2];
      for (int i = 0; i < this.index; i++) {
        temp[i] = this.initArray[i];
      }
      this.initArray = temp;
      // this.initArray = Arrays.copyOf(initArray, this.initArray.length / 2);
      long deleteResize = System.nanoTime();
      this.deleteResizeList.add(deleteResize - startTime);

    }
    else {
      index--;
      long deleteNoResize = System.nanoTime();
      this.deleteNoResizeList.add(deleteNoResize - startTime);
    }
  }
}