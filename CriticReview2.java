import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CriticReview2 {
  public static void main(String args[]) {
    Scanner myScanner = new Scanner(System.in);
    boolean addC1 = false;
    boolean addC2 = false;
    LinkedHashMap<String, int[]> c1Table = new LinkedHashMap<String, int[]>();
    LinkedHashMap<String, int[]> c2Table = new LinkedHashMap<String, int[]>();

    int rating = 0;
    // adds the document's lines into 2 hashmaps c1Table and c2Table
    while (myScanner.hasNextLine()) {
      String next = myScanner.nextLine();
      if (next.equals("Critic 1")) {
        addC1 = true;
        addC2 = false;
      }
      else if (next.equals("Critic 2")) {
        addC1 = false;
        addC2 = true;
      }
      if (addC1 && !next.equals("Critic 1") && !next.equals("Critic 2")) {
        int stars;
        // checks the stars and makes 5 stars 1 and anything else 0
        if (next.startsWith("5")) {
          stars = 1;
        }
        else {
          stars = 0;
        }
        c1Table.put(next.substring(2), new int[] { rating, stars });
        rating++;
      }
      else if (addC2 && !next.equals("Critic 1") && !next.equals("Critic 2")) {
        int stars;
        // checks the stars and makes 5 stars 1 and anything else 0
        // also checks the movie in critic 1's list
        if (next.startsWith("5") || c1Table.get(next.substring(2))[1] == 1) {
          stars = 1;
        }
        else {
          stars = 0;
        }
        // adds the movie with the index based off of where the movie was found
        // in critic 1's list
        c2Table.put(next.substring(2), new int[] { c1Table.get(next.substring(2))[0], stars });
      }
    }
    // converts the table into linked list
    LinkedList<int[]> c2ReviewsInt = new LinkedList<int[]>();
    Set<String> keys = c2Table.keySet();
    for (String key : keys) {
      int[] temp = new int[] { c2Table.get(key)[0], c2Table.get(key)[1] };
      c2ReviewsInt.add(temp);
    }

    System.out.println(countAndSort(c2ReviewsInt));

  }

  private static int countAndSort(LinkedList<int[]> l) {
    // base case list of size 1 (no inversions)
    if (l.size() <= 1) {
      return 0;
    }
    else {
      int mid = l.size() / 2; // finds the mid
      int size = l.size();
      LinkedList<int[]> firstHalf = new LinkedList<int[]>();
      LinkedList<int[]> secondHalf = new LinkedList<int[]>();
      // adds the first half of the list to firstHalf and the second half to
      // secondHalf
      for (int i = 0; i < size; i++) {
        int[] top = l.poll();
        if (i < mid) {
          firstHalf.add(top);
        }
        else {
          secondHalf.add(top);
        }

      }

      // runs the recursive call on the left, the right and merges the two together
      // gets the inversions for the left, then the right and the inversions in
      // between the two
      return countAndSort(firstHalf) + countAndSort(secondHalf)
          + mergeAndCount(l, firstHalf, secondHalf);

    }
  }

  private static int mergeAndCount(LinkedList<int[]> l, LinkedList<int[]> firstHalf,
      LinkedList<int[]> secondHalf) {
    // initializes result (the sorted version of given list)
    LinkedList<int[]> result = new LinkedList<int[]>();
    int count = 0;
    int[] topFirst = firstHalf.peek();
    int[] topSecond = secondHalf.peek();
    while (!firstHalf.isEmpty() && !secondHalf.isEmpty()) {
      // sees which value is smaller and adds the smaller one to the result list
      if (topFirst[0] < topSecond[0]) {
        result.add(topFirst);
        // changes the pointer in first list
        topFirst = firstHalf.poll();
        if (firstHalf.isEmpty()) {
          break;
        }
        topFirst = firstHalf.peek();
      }
      else {
        result.add(topSecond);
        // checks to see if 5 star rating and if there is doubles the count
        // else does a single count
        if (topFirst[1] == 1 || topSecond[1] == 1) {
          count = count + 2 * firstHalf.size();

        }
        else {
          // the number of inversions is the length of what's left in list 1
          count = count + firstHalf.size();
        }
        // changes the pointer in second list
        topSecond = secondHalf.poll();
        if (secondHalf.isEmpty()) {
          break;
        }
        topSecond = secondHalf.peek();
      }
    }
    // if one list is empty adds the rest of the next list
    if (firstHalf.isEmpty()) {
      result.addAll(secondHalf);
    }
    if (secondHalf.isEmpty()) {
      result.addAll(firstHalf);
    }
    // mutates given list l to be the sorted list
    l.addAll(result);
    return count;
  }
}
