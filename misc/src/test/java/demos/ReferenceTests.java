package demos;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.ref.*;

@Slf4j
public class ReferenceTests {

    /**
     * 该循环会在某个时间点跳出循环，因为weakPeson的引用会被GC收集掉，
     * WeakReference的特点是不知道GC何时回收它指向的引用
     */
    @Test
    public void testWeakReference1() {
        WeakReference<Person> weakPerson = new WeakReference<Person>(new Person(1, "tom"));

        int i = 0;

        while (true) {
            if (weakPerson.get() != null) {
                i++;
                System.out.println("Object is alive for " + i + " loops - " + weakPerson);
            } else {
                System.out.println("Object has been collected.");
                break;
            }
        }
    }

    /**
     * 由于person一直指向Person对象，它是一个引用，在没有设置为null的情况下，它将不会被GC回收，循环不会跳出
     */
    @Test
    public void testWeakReference2() {
        Person person = new Person(1, "tom");
        WeakReference<Person> weakPerson = new WeakReference<Person>(person);

        int i = 0;

        while (true) {
            if (weakPerson.get() != null) {
                i++;
                System.out.println("Object is alive for " + i + " loops - " + weakPerson);
            } else {
                System.out.println("Object has been collected.");
                break;
            }
        }
    }

    /**
     * Person对象被手动设置为null，这会导致Person对象没有了任何强引用，而只有一处弱引用（weakPerson)
     * 那么，GC会在其下次运行的时候自动回收掉堆上的Person对象
     */
    @Test
    public void testWeakReference3() {
        Person person = new Person(1, "tom");
        WeakReference<Person> weakPerson = new WeakReference<Person>(person);

        person = null; // 让Person对象不在有其他强引用

        int i = 0;

        while (true) {
            if (weakPerson.get() != null) {
                i++;
                System.out.println("Object is alive for " + i + " loops - " + weakPerson);
            } else {
                System.out.println("Object has been collected.");
                break;
            }
        }
    }

    @Test
    public void testSoftReference() {
        SoftReference<Person> softReference = new SoftReference<Person>(new Person(1));

        // 一直判断弱引用是否已经被释放了
        while (softReference.get() != null) {
            System.gc();

            log.info("Reference is alive!");
        }

        log.info("Reference is released");
    }

    @Test
    public void testReferencesAndReferenceQueue() {
        Reference<?> ref;

        String weakObj = new String("weak object");
        ReferenceQueue<String> weakQueue = new ReferenceQueue<String>();
        WeakReference<String> weakRef = new WeakReference<String>(weakObj, weakQueue);

        String phantomObj = new String("phantom object");
        ReferenceQueue<String> phantomQueue = new ReferenceQueue<String>();
        PhantomReference<String> phantomRef = new PhantomReference<String>(phantomObj, phantomQueue);

        weakObj = null;
        phantomObj = null;

        System.gc(); // invoke gc

        // 此时weakRef应该已经进入队列了
        Assertions.assertTrue(weakRef.isEnqueued());

        if (!phantomRef.isEnqueued()) {
            System.out.println("Requestion finalization.");
            System.runFinalization();
        }

        Assertions.assertTrue(phantomRef.isEnqueued());

        try {
            ref = weakQueue.remove();
            Assertions.assertEquals(ref, weakRef);
            Assertions.assertNull(ref.get());

            ref = phantomQueue.remove();
            Assertions.assertEquals(ref, phantomRef);
            Assertions.assertNull(ref.get());

            ref.clear();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }
}
