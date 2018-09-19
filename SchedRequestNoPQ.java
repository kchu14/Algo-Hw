import java.util.Scanner;
import java.util.Collections;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.HashMap;

public class test2 {

  static int MAX_MINUTE = 24 * 60;

  public static void main(String[] args) {
    // initializes arraylists that will be implemented later
    Scanner myScanner = new Scanner(System.in);
    ArrayList<String> schedule = new ArrayList<String>();
    ArrayList<Request> requests = new ArrayList<Request>();
    ArrayList<Request> removedRequests = new ArrayList<Request>();
    Request lastRequest = null;
    Request requestToAddBack = null;
    ArrayList<Request> canceledRequests = new ArrayList<Request>();

    // last request compare to the requests
    // cancel list and cancel when time is called
    // remove requests from requests list as they are printed
    // store the last request and compare it to the requests list

    // to keep track of what time the schedule is at
    int time = 0;

    // creates a list of strings from the file
    while (myScanner.hasNextLine()) {
      schedule.add(myScanner.nextLine());
    }

    // iterates through the list of strings created from the file
    for (String s : schedule) {
      // handles the canceled requests and adds them to canceled requests list
      if (s.contains("cancel")) {
        String s1 = s.substring(7);
        Request r = new Request(s1);
        canceledRequests.add(r);
        removedRequests.remove(r);
        // if a canceled request overlapped with a request that was removed prior
        // the overlapped request is added back into the requests list
        for (Request r1 : removedRequests) {
          if (r1.overlaps(r) && !r1.equals(r) && !requests.contains(r1)) {
            if (requestToAddBack == null) {
              requestToAddBack = new Request(r1.toString());
            }
            if (requestToAddBack != null && r1.getEndMinute() < requestToAddBack.getEndMinute()) {
              requestToAddBack = new Request(r1.toString());
            }
          }
        }
        if (requestToAddBack != null) {
          requests.add(requestToAddBack);
          requestToAddBack = null;
        }

      }
      // adds requests to the request list
      if (!s.contains("cancel") && s.contains(",")) {
        requests.add(new Request(s));
      }

      // sorts the requests list at the given time
      if (!s.contains(",")) {
        ArrayList<Request> toRemove = new ArrayList<Request>();
        // converts the time from a string
        String[] timeParts = s.split(":");
        int hour = Integer.valueOf(timeParts[0]);
        int minute = Integer.valueOf(timeParts[1]);
        time = hour * 60 + minute;
        for (Request r : canceledRequests) {
          requests.remove(r);
        }
        canceledRequests.clear();

        // sorts the requests list
        Collections.sort(requests);
        
        for (int i = 0; i < requests.size(); i++) {
          Request r1 = requests.get(i);
          for (int j = i + 1; j < requests.size(); j++) {
            Request r2 = requests.get(j);
            // if there is an overlap the request with the earliest finish time is kept
            // the other overlapped request is added to toRemove
            if (lastRequest != null && lastRequest.overlaps(r1)) {
              toRemove.add(r1);
              removedRequests.add(r1);
              continue;
            }

            if (r1.overlaps(r2)) {
            
              toRemove.add(r2);
              removedRequests.add(r2);

            }
          }
        }
        for (Request r : toRemove) {
          requests.remove(r);
        }
        toRemove.clear();
        // prints out the required requests at the given time
        for (Request r : requests) {
          if (time >= r.getStartMinute()) {
            System.out.println(r + "");
            lastRequest = new Request(r.toString());
            toRemove.add(r);
          }
        }
        
        ArrayList<Request> toRemove1 = new ArrayList<Request>();
        for (Request r : removedRequests) {
          if (time >= r.getStartMinute()) {
            toRemove1.add(r);
          }
        }
        for (Request r : toRemove1) {
          removedRequests.remove(r);
        }
        // removes requests that have finished

        for (Request r : toRemove) {
          requests.remove(r);
        }
      }

    }

  }

  // Here's a handy class for requests that implements some tricky bits for
  // you, including a compareTo method (so that it's a Comparable, so that
  // it can be sorted with Collections.sort()), a hashCode method
  // (so that identical time ranges are treated as identical keys in
  // a hashMap), and an overlaps() method (which students have often
  // gotten wrong in the past by omitting cases). Parsing, equals(), and
  // toString() are also handled for you.
  public static class Request implements Comparable {
    private int startMinute;
    private int endMinute;

    // Constructor that takes the request format specified in the
    // assignment (startTime,endTime using 24-hr clock)
    public Request(String inputLine) {
      String[] inputParts = inputLine.split(",");
      startMinute = toMinutes(inputParts[0]);
      endMinute = toMinutes(inputParts[1]);
    }

    // Convert time to an integer number of minutes; mostly
    // for internal use by the class
    private static int toMinutes(String time) {
      String[] timeParts = time.split(":");
      int hour = Integer.valueOf(timeParts[0]);
      int minute = Integer.valueOf(timeParts[1]);
      return hour * 60 + minute;
    }

    // Don't feel like you need to use these accesssors, but they're
    // here in case I decide to change the internal representation
    // someday
    public int getStartMinute() {
      return startMinute;
    }

    public int getEndMinute() {
      return endMinute;
    }

    // Did you toString() gets called automatically when your object
    // is put in a situation that expects a String?
    public String toString() {
      return timeToString(startMinute) + "," + timeToString(endMinute);
    }

    // Mostly for use by toString() - format number of minutes as 24hr time
    private static String timeToString(int minutes) {
      if ((minutes % 60) < 10) {
        return (minutes / 60) + ":0" + (minutes % 60);
      }
      return (minutes / 60) + ":" + (minutes % 60);
    }

    // Check whether two Requests overlap in time.
    public boolean overlaps(Request r) {
      // Four kinds of overlap...
      // r starts during this request:
      if (r.getStartMinute() >= getStartMinute() && r.getStartMinute() < getEndMinute()) {
        return true;
      }
      // r ends during this request:
      if (r.getEndMinute() > getStartMinute() && r.getEndMinute() < getEndMinute()) {
        return true;
      }
      // r contains this request:
      if (r.getStartMinute() <= getStartMinute() && r.getEndMinute() >= getEndMinute()) {
        return true;
      }
      // this request contains r:
      if (r.getStartMinute() >= getStartMinute() && r.getEndMinute() <= getEndMinute()) {
        return true;
      }
      return false;
    }

    // Allows use of Collections.sort() on this object
    // (implements Comparable interface)
    public int compareTo(Object o) {
      if (!(o instanceof Request)) {
        throw new ClassCastException();
      }
      Request r = (Request) o;
      if (r.getEndMinute() > getEndMinute()) {
        return -1;
      }
      else if (r.getEndMinute() < getEndMinute()) {
        return 1;
      }
      else if (r.getStartMinute() < getStartMinute()) {
        // Prefer later start times, so sort these first
        return -1;
      }
      else if (r.getStartMinute() > getStartMinute()) {
        return 1;
      }
      else {
        return 0;
      }
    }

    // The hash function for the hashMap, without which our scheme
    // of counting requests with the same range would not work.
    // You don't need to call this yourself; it's used every time
    // get(), contains(), or something similar is called
    public int hashCode() {
      return MAX_MINUTE * startMinute + endMinute;
    }

    // Determine whether two objects are equal. If we're not in a hashing
    // context, other generics will use this to implement functions like
    // contains() or remove().
    public boolean equals(Object o) {
      if (!(o instanceof Request)) {
        return false;
      }
      Request that = (Request) o;
      return (this.startMinute == that.startMinute && this.endMinute == that.endMinute);
    }

  }
}