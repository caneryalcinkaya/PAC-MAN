// Caner Yalcinkaya
// 2024400273
public class Stack<T> {
    private T[] list;
    private int top; // Points to the next empty slot

    @SuppressWarnings("unchecked")
    // Creates an empty stack
    public Stack() {
        list = (T[]) new Object[100];
        top = 0;
    }

    // Checks if stack is empty
    public boolean isEmpty() {
        return top == 0;
    }

    // Returns the number of items
    public int size() {
        return top;
    }

    @SuppressWarnings("unchecked")
    // Adjusts array capacity
    private void resize(int newCapacity) {
        T[] newList = (T[]) new Object[newCapacity];
        for (int i = 0; i < top; i++) {
            newList[i] = list[i];
        }
        list = newList;
    }

    // Pushes an item to the top
    public void push(T item) {
        if (top == list.length) {
            resize(list.length * 2);
        }
        list[top++] = item;
    }

    // Pops an item from the top
    public T pop() {
        if (isEmpty()) {
            return null;
        }
        T item = list[--top];
        list[top] = null; // Clear reference to help garbage collection
        return item;
    }

    // Clears all items
    public void clear() {
        for (int i = 0; i < top; i++) {
            list[i] = null;
        }
        top = 0;
    }
}