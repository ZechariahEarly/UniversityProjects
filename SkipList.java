/*
 * @author Rohan Ramavajjala
 * Professor Alagar - 3/15/2021
 * NET ID: RAR180008
 */

//package dsa;

import java.util.Iterator;
import java.util.Stack;
import java.util.Random;

public class SkipList<T extends Comparable<? super T>> {
    Entry<T> head;
    Entry<T> tail;
    int size;
    static final int PossibleLevels = 33;

    static class Entry<E> {
        E element;
        Entry<E>[] next;

        public Entry(E x, int lev) {
            element = x;
            next = (Entry<E>[]) new Entry[lev];
            // add more code as needed
        }

        public E getElement() {

            return element;
        }

        public int levels() {

            return next.length;
        }
    }

    // Constructor
    public SkipList(){
        head = new Entry<T>(null, PossibleLevels);
        tail = new Entry<T>(null, PossibleLevels);
        for (int i=0; i<PossibleLevels; i++){
            head.next[i] = tail;
        }
    }

    // Add x to list. If x already exists, reject it. Returns true if new node is added to list
    public boolean add(T x) {
        if (contains(x)) //duplicate check
            return false;
        int level = chooseLevel();
        Entry<T> entry = new Entry<T>(x, level);
        Entry<T>[] pred = findPred(x);
        for (int i=0; i<level-1; i++){ //insert at every randomly created level
            entry.next[i] = pred[i].next[i];
            pred[i].next[i] = entry;
        }
        size++;

        return true;
    }

    // Find smallest element that is greater or equal to x
    public T ceiling(T x) {
        if (contains(x))
            return x;
        Entry<T> entry = head.next[0];
        while (entry.element.compareTo(x) < 0) {
            entry = entry.next[0];
        }

        return entry.element;
    }

    // Does list contain x?
    public boolean contains(T x) {
        Entry<T>[] entryArr = findPred(x);

        return entryArr[0].next[0].element == x;
    }

    // Return first element of list
    public T first() {

        return head.next[0].element;
    }

    // Find largest element that is less than or equal to x
    public T floor(T x) {
        if (contains(x))
            return x;

        return findPred(ceiling(x))[0].element;
    }

    // Return element at index n of list.  First element is at index 0.
    public T get(int n) {
        if (n > size - 1 || n < 0)
            throw new NullPointerException();
        Entry<T> entry = head.next[0];
        for (int i=0; i<n; i++){
            entry = entry.next[0];
        }

        return entry.element;
    }

    // Is the list empty?
    public boolean isEmpty() {

        return size == 0;
    }

    // Iterate through the elements of list in sorted order
    public Iterator<T> iterator() {
        return null;
    }

    // Return last element of list
    public T last() {
        Entry<T> last = head.next[0];
        while (last.next[0] != tail)
            last = last.next[0];

        return last.element;
    }

    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(T x) {
        if (!contains(x))
            return null;
        Entry<T>[] pred = findPred(x);
        Entry<T> entry = pred[0].next[0];
        int level = entry.levels();
        for (int i=0; i<level-1; i++){
            pred[i].next[i] = entry.next[i];
        }
        size--;

        return x;
    }

    // Return the number of elements in the list
    public int size() {

        return size;
    }

    public Entry<T>[] findPred(T x) {
        Entry<T> current = head;
        Entry<T>[] pred = (Entry<T>[]) new Entry[current.levels()]; //holds last visited entry (predecessor) in each level
        for (int i=current.levels()-1; i>=0; i--){
            while (current.next[i].element != null && current.next[i].element.compareTo(x) < 0)
                current = current.next[i];
            pred[i] = current;
        }

        return pred;
    }

    public int chooseLevel() {
        Random rand = new Random();
        int randLevel = Integer.numberOfTrailingZeros(rand.nextInt()) + 1;

        return Math.min(randLevel, PossibleLevels - 1);
    }
}