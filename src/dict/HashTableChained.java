/* HashTableChained.java */

package dict;
import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
  public DList[] table;
  public int numBuckets;



  public static boolean isPrime(int n) {
    int divisor = 2;
    while (divisor < n) {
      if (n%divisor==0) {
        return false;
      }
      divisor++;
    }
    return true;
  }

  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    // Your solution here.
    int i, tableSize=0;
    for (i=sizeEstimate; i<=2*sizeEstimate; i++) {
      if (isPrime(i)) {
        tableSize = i;
        break;
      }
    }
    table = new DList[tableSize];
    for (i=0;i<tableSize;i++) {
      table[i] = new DList();
    }
    numBuckets = tableSize;
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    // Your solution here.
    int i;
    table = new DList[101];
    for (i=0;i<101;i++) {
      table[i] = new DList();
    }
    numBuckets = 101;
  }
  
  // resize if the load factor is getting over 1, every time resize() will double the num of buckets;
  public void resize(){
	    if (this.size()/numBuckets>0.75){
	      DList[] temp = this.table;
	      this.table = new DList[numBuckets*2];
	      numBuckets = numBuckets * 2;
	      for (int i=0;i<numBuckets;i++){
	        table[i] = new DList();
	      }
	      try {
	        for (int i=0;i<temp.length;i++){
	          if (temp[i].length()>0){
	            DListNode cur = (DListNode)(temp[i].front());
	            while (cur.item()!=null) {
	              this.insert(((Entry)(cur.item())).key, ((Entry)(cur.item())).value);
	              if (cur==(DListNode)(temp[i].back())){
	                break;
	              }
	              cur=(DListNode)(cur.next());
	            }
	          }
	        }
	      } catch(InvalidNodeException e){
	        e.printStackTrace();
	      }
	    }
	  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
    // Replace the following line with your solution.
    int position;
    position = ((10*code+30)%14657)%(numBuckets);
    if (position<0) {
      position+=numBuckets;
    }
    return position;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    // Replace the following line with your solution.
    int i, numEntries=0;
    for (i=0;i<numBuckets;i++) {
      numEntries+=table[i].length();
    }
    return numEntries;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    // Replace the following line with your solution.
    int i;
    for (i=0;i<numBuckets;i++) {
      if (table[i].length()>=1) {
        return false;
      }
    }
    return true;
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
    // Replace the following line with your solution.
    int position;
    Entry newEntry = new Entry();
    newEntry.key = key;
    newEntry.value = value;
    position=compFunction(key.hashCode());
    table[position].insertBack(newEntry);
    return newEntry;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
	    // Replace the following line with your solution.
	    int position;
	    DListNode temp;
	    position=compFunction(key.hashCode());
	    if (table[position].length()>0){
	      try{
	        temp = (DListNode)(table[position].front());
	        while (temp.item()!=null) {
	          if (key.equals(((Entry)(temp.item())).key)) {
	            return (Entry)(temp.item());
	          }
	          if (temp==(DListNode)(table[position].back())){
	            break;
	          }
	          temp=(DListNode)(temp.next());
	        }
	      }
	      catch(InvalidNodeException e){
	        e.printStackTrace();
	      }
	    }
	    return null;
	  }
	  /** 
	   *  Remove an entry with the specified key.  If such an entry is found,
	   *  remove it from the table and return it; otherwise return null.
	   *  If several entries have the specified key, choose one arbitrarily, then
	   *  remove and return it.
	   *
	   *  This method should run in O(1) time if the number of collisions is small.
	   *
	   *  @param key the search key.
	   *  @return an entry containing the key and an associated value, or null if
	   *          no entry contains the specified key.
	   */
	  public Entry remove(Object key) {
	    // Replace the following line with your solution.
	    int position;
	    DListNode temp;
	    position=compFunction(key.hashCode());
	    if (table[position].length()>0){
	      try{
	        temp = (DListNode)(table[position].front());
	        while (temp.item()!=null) {
	          if (key.equals(((Entry)(temp.item())).key)) {
	            Entry removed = (Entry)(temp.item());
	            temp.remove();
	            return removed;
	          }
	          if (temp==(DListNode)(table[position].back())){
	            break;
	          }
	          temp=(DListNode)(temp.next());
	        }
	      }
	      catch(InvalidNodeException e){
	        e.printStackTrace();
	      }
	    }
	    return null;
	  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
    // Your solution here.
    int i;
    table = new DList[numBuckets];
    for (i=0;i<numBuckets;i++) {
      table[i] = new DList();
    }

  }
  public int collisions(){
    int i, num=0;
    for (i=0;i<numBuckets;i++) {
      if (table[i].length()>1) {
        num+=table[i].length()-1;
      }
    }
    return num;
  }

  public static void main(String[] args) {
    HashTableChained t1 = new HashTableChained(90);
    t1.insert("cs61b", "hardcore");
    System.out.println(t1.find("cs61b").value);
    System.out.println(t1.size());
    t1.makeEmpty();
    System.out.println(t1.isEmpty());
    t1.insert("cscscs", "lalala");
    System.out.println(t1.find("cscscs").value);
  }

}
