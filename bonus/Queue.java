public class Queue<T> {
    private T[] list;
    private int head;
    private int tail;
    private int count;

    @SuppressWarnings("unchecked")
    // Creates an empty queue
    public Queue() {
        list = (T[]) new Object[16];
        head = 0;
        tail = 0;
        count = 0;
    }

    // Checks if queue is empty
    public boolean isEmpty() {
        return count == 0;
    }

    // Returns item count
    public int size() {
        return count;
    }

    @SuppressWarnings("unchecked")
    // Adjusts array capacity
    private void resize(int newCapacity) {
        T[] newList = (T[]) new Object[newCapacity];
        for (int i = 0; i < count; i++) {
            newList[i] = list[(head + i) % list.length];
        }
        list = newList;
        head = 0;
        tail = count;
    }

    // Adds an item to the end
    public void enqueue(T item) {
        if (count == list.length) {
            resize(list.length * 2);
        }
        list[tail] = item;
        tail = (tail + 1) % list.length;
        count++;
    }

    // Removes an item from the front
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        T item = list[head];
        list[head] = null;
        head = (head + 1) % list.length;
        count--;
        return item;
    }
}
