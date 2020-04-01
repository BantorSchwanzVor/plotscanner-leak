package org.apache.commons.net.nntp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Threader {
  public Threadable thread(List<? extends Threadable> messages) {
    return thread(messages);
  }
  
  public Threadable thread(Iterable<? extends Threadable> messages) {
    if (messages == null)
      return null; 
    HashMap<String, ThreadContainer> idTable = new HashMap<>();
    for (Threadable t : messages) {
      if (!t.isDummy())
        buildContainer(t, idTable); 
    } 
    if (idTable.isEmpty())
      return null; 
    ThreadContainer root = findRootSet(idTable);
    idTable.clear();
    idTable = null;
    pruneEmptyContainers(root);
    root.reverseChildren();
    gatherSubjects(root);
    if (root.next != null)
      throw new RuntimeException("root node has a next:" + root); 
    for (ThreadContainer r = root.child; r != null; r = r.next) {
      if (r.threadable == null)
        r.threadable = r.child.threadable.makeDummy(); 
    } 
    Threadable result = (root.child == null) ? null : root.child.threadable;
    root.flush();
    return result;
  }
  
  private void buildContainer(Threadable threadable, HashMap<String, ThreadContainer> idTable) {
    String id = threadable.messageThreadId();
    ThreadContainer container = idTable.get(id);
    int bogusIdCount = 0;
    if (container != null)
      if (container.threadable != null) {
        bogusIdCount++;
        id = "<Bogus-id:" + bogusIdCount + ">";
        container = null;
      } else {
        container.threadable = threadable;
      }  
    if (container == null) {
      container = new ThreadContainer();
      container.threadable = threadable;
      idTable.put(id, container);
    } 
    ThreadContainer parentRef = null;
    String[] references = threadable.messageThreadReferences();
    byte b;
    int i;
    String[] arrayOfString1;
    for (i = (arrayOfString1 = references).length, b = 0; b < i; ) {
      String refString = arrayOfString1[b];
      ThreadContainer ref = idTable.get(refString);
      if (ref == null) {
        ref = new ThreadContainer();
        idTable.put(refString, ref);
      } 
      if (parentRef != null && 
        ref.parent == null && 
        parentRef != ref && 
        !ref.findChild(parentRef)) {
        ref.parent = parentRef;
        ref.next = parentRef.child;
        parentRef.child = ref;
      } 
      parentRef = ref;
      b++;
    } 
    if (parentRef != null && (
      parentRef == container || container.findChild(parentRef)))
      parentRef = null; 
    if (container.parent != null) {
      ThreadContainer prev = null, rest = container.parent.child;
      for (; rest != null; 
        prev = rest, rest = rest.next) {
        if (rest == container)
          break; 
      } 
      if (rest == null)
        throw new RuntimeException(
            "Didnt find " + 
            container + 
            " in parent" + 
            container.parent); 
      if (prev == null) {
        container.parent.child = container.next;
      } else {
        prev.next = container.next;
      } 
      container.next = null;
      container.parent = null;
    } 
    if (parentRef != null) {
      container.parent = parentRef;
      container.next = parentRef.child;
      parentRef.child = container;
    } 
  }
  
  private ThreadContainer findRootSet(HashMap<String, ThreadContainer> idTable) {
    ThreadContainer root = new ThreadContainer();
    Iterator<Map.Entry<String, ThreadContainer>> iter = idTable.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry<String, ThreadContainer> entry = iter.next();
      ThreadContainer c = entry.getValue();
      if (c.parent == null) {
        if (c.next != null)
          throw new RuntimeException(
              "c.next is " + c.next.toString()); 
        c.next = root.child;
        root.child = c;
      } 
    } 
    return root;
  }
  
  private void pruneEmptyContainers(ThreadContainer parent) {
    ThreadContainer prev = null, container = parent.child, next = container.next;
    for (; container != null; 
      prev = container, 
      container = next, 
      next = (container == null) ? null : container.next) {
      if (container.threadable == null && container.child == null) {
        if (prev == null) {
          parent.child = container.next;
        } else {
          prev.next = container.next;
        } 
        container = prev;
      } else if (container.threadable == null && 
        container.child != null && (
        container.parent != null || 
        container.child.next == null)) {
        ThreadContainer kids = container.child;
        if (prev == null) {
          parent.child = kids;
        } else {
          prev.next = kids;
        } 
        ThreadContainer tail;
        for (tail = kids; tail.next != null; tail = tail.next)
          tail.parent = container.parent; 
        tail.parent = container.parent;
        tail.next = container.next;
        next = kids;
        container = prev;
      } else if (container.child != null) {
        pruneEmptyContainers(container);
      } 
    } 
  }
  
  private void gatherSubjects(ThreadContainer root) {
    int count = 0;
    for (ThreadContainer c = root.child; c != null; c = c.next)
      count++; 
    HashMap<String, ThreadContainer> subjectTable = new HashMap<>((int)(count * 1.2D), 0.9F);
    count = 0;
    for (ThreadContainer threadContainer1 = root.child; threadContainer1 != null; threadContainer1 = threadContainer1.next) {
      Threadable threadable = threadContainer1.threadable;
      if (threadable == null)
        threadable = threadContainer1.child.threadable; 
      String subj = threadable.simplifiedSubject();
      if (subj != null && subj.length() != 0) {
        ThreadContainer old = subjectTable.get(subj);
        if (old == null || (
          threadContainer1.threadable == null && old.threadable != null) || (
          old.threadable != null && 
          old.threadable.subjectIsReply() && 
          threadContainer1.threadable != null && 
          !threadContainer1.threadable.subjectIsReply())) {
          subjectTable.put(subj, threadContainer1);
          count++;
        } 
      } 
    } 
    if (count == 0)
      return; 
    ThreadContainer prev = null, threadContainer2 = root.child, rest = threadContainer2.next;
    for (; threadContainer2 != null; 
      prev = threadContainer2, threadContainer2 = rest, rest = (rest == null) ? null : rest.next) {
      Threadable threadable = threadContainer2.threadable;
      if (threadable == null)
        threadable = threadContainer2.child.threadable; 
      String subj = threadable.simplifiedSubject();
      if (subj != null && subj.length() != 0) {
        ThreadContainer old = subjectTable.get(subj);
        if (old != threadContainer2) {
          if (prev == null) {
            root.child = threadContainer2.next;
          } else {
            prev.next = threadContainer2.next;
          } 
          threadContainer2.next = null;
          if (old.threadable == null && threadContainer2.threadable == null) {
            ThreadContainer tail = old.child;
            while (tail != null && tail.next != null)
              tail = tail.next; 
            if (tail != null)
              tail.next = threadContainer2.child; 
            for (tail = threadContainer2.child; tail != null; tail = tail.next)
              tail.parent = old; 
            threadContainer2.child = null;
          } else if (old.threadable == null || (
            threadContainer2.threadable != null && 
            threadContainer2.threadable.subjectIsReply() && 
            !old.threadable.subjectIsReply())) {
            threadContainer2.parent = old;
            threadContainer2.next = old.child;
            old.child = threadContainer2;
          } else {
            ThreadContainer newc = new ThreadContainer();
            newc.threadable = old.threadable;
            newc.child = old.child;
            ThreadContainer tail = newc.child;
            for (; tail != null; 
              tail = tail.next)
              tail.parent = newc; 
            old.threadable = null;
            old.child = null;
            threadContainer2.parent = old;
            newc.parent = old;
            old.child = threadContainer2;
            threadContainer2.next = newc;
          } 
          threadContainer2 = prev;
        } 
      } 
    } 
    subjectTable.clear();
    subjectTable = null;
  }
  
  @Deprecated
  public Threadable thread(Threadable[] messages) {
    if (messages == null)
      return null; 
    return thread(Arrays.asList(messages));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\nntp\Threader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */