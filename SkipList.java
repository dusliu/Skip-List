import java.util.Random;
import java.lang.Comparable;

// Starter code for Project 2: skip lists

// Do not rename the class, or change names/signatures of methods that are declared to be public.


// change to your netid
//package zxl170011;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SkipList<T extends Comparable<? super T>> 
{
    static final int PossibleLevels = 33;
    int size;
 
    Entry<T> head,tail;
    int maxLevel;
    Entry<T>[] last;
    Random random;
    
    static class Entry<E> {
    			//						  []
	E element;//						  []
	Entry[] next; // act like a stack []->[]
	Entry prev;

	public Entry(E x, int lev) {
	    // No change
		element = x;
	    next = new Entry[lev]; // creates the next array with lev rows
	    // add more code as needed
	}
	
	public E getElement() 
	{
	    return element;
	}
	
	public int getLevel()
	{
		return next.length;
	}
    }

    // DONE
    // This method will generate the number of rows an element will have
    int chooseLevel()
    {	
    	int lvl = 1 + Integer.numberOfTrailingZeros(random.nextInt()); // This generates the # of levels for each element
    	if(lvl > maxLevel)
    		maxLevel = lvl;
    	return lvl;
    }
    
    // Constructor
    // DONE
    public SkipList() {
        head = new Entry<T>(null,PossibleLevels);
        tail = new Entry<T>(null,PossibleLevels);
        size = 0;
        maxLevel = 1;
        last = new Entry[PossibleLevels];
        for (int i = 0; i < PossibleLevels; i++) 
        {
            head.next[i] = tail; // For every row, assign head --> next [32]... head --> next [31]...
            
        }
        random = new Random();

    }

    // Add x to list. If x already exists, reject it. Returns true if new node is added to list
   // Done
    public boolean add(T x) 
    {	
    	if(contains(x))
    		return false;
    	int lvl = chooseLevel();
    	Entry<T> entry = new Entry<>(x, lvl); // Creates the node and sets the height(number of rows)
    	// For loop will place the element's value into each row 
    	for(int i= 0; i <= lvl - 1; i++) 
    	{
    		entry.next[i] = last[i].next[i];
    		last[i].next[i] = entry;
    	}
        entry.next[0].prev = entry;
        entry.prev = last[0];
    	size++;
	return true;
    }

    // Find smallest element that is greater or equal to x
    public T ceiling(T x) 
    {
    	findPred(x);
    	// returns current.next
    	return (T) last[0].next[0].element;
    }

    public void findPred(T x)
    {
    	Entry<T> current = head;
    	for(int i = current.getLevel() - 1; i >= 0; i--)
    	{//	i = 2
    		while (current.next[i].element != null && ((T) current.next[i].element).compareTo(x) < 0)
    		{// next[3]
    			current = current.next[i];
    		}
    		
    		// last[] will contain the node with the element before desired element
    		last[i] = current;
    	}
    }
    // Does list contain x?
    // DONE
    public boolean contains(T x) 
    {
    	findPred(x);
    	if(last[0].next[0].element != null && ((Comparable<? super T>) last[0].next[0].element).compareTo(x) == 0)
    		return true;
    	else
    		return false;
    }

    // Return first element of list
    // DONE
    public T first() 
    {
    	if(head.next[0] != null)
    		return (T)head.next[0].element;
	return null;
    }

    // Find largest element that is less than or equal to x
    // Done
    public T floor(T x) 
    {
    	findPred(x);
        if (((T) last[0].next[0].element).compareTo(x) == 0)
            return x;
        else
            return last[0].element;	
    }

    // Return element at index n of list.  First element is at index 0.
    // DONE
    public T get(int n) 
    {
    	if(n > size - 1)
    		throw new NoSuchElementException();
    	Entry<T> current = head;
    	int i = 0;
    	while(i<=n)
    	{
    		current=current.next[0]; // go through the entire bottom row
    		i++;
    	}
    	if(current != null)
    		return current.element;
    	return null;
    }

  
    
    // Is the list empty?
    // DONE
    public boolean isEmpty() {
    	if(size == 0)
    		return true;
	return false;
    }

    // Iterate through the elements of list in sorted order
    public Iterator<T> iterator() 
    {
    	return new newIterator();
    }

    class newIterator implements Iterator<T>
    {
    	Entry<T> current;
    	newIterator()
    	{
    		 current = head;
    	}
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			if(current.next[0] != null)
				return true;
			return false;
		}

		@Override
		public T next() {
			current = current.next[0];
			if(current != null)
				return current.element;
			return null;
		}
    	
    }
    // Return last element of list
    // DONE
    public T last() 
    {
    	if(tail.prev != null)
    		return (T)tail.prev.element;
    	return null;
    }

    // Remove x from list.  Removed element is returned. Return null if x not in list
// DONE    
    public T remove(T x)
    {
    	if(!contains(x))
    		return null;
    	Entry<T>entry = last[0].next[0];
    	int lvl = entry.getLevel();
    	for(int i = 0; i<= lvl - 1; i++)
    		last[i].next[i] = entry.next[i];
    	size--;
    	return entry.element;
    }

    // Return the number of elements in the list
    // DONE
    public int size() 
    {
    	return size;
    }
}

