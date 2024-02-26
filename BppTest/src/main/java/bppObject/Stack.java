package bppObject;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class Stack<T> {
    private List<T> data;

    public Stack() {
        data = new ArrayList<>();
    }

    public boolean empty() {
        return data.isEmpty();
    }

    public boolean not_empty() {
        return !data.isEmpty();
    }

    public T pop() {
        if (empty()) {
            throw new EmptyStackException();
        }
        return data.remove(data.size() - 1);
    }

    public void push(T... items) {
        for (T item : items) {
            data.add(item);
        }
    }

    public T top() {
        if (empty()) {
            throw new EmptyStackException();
        }
        return data.get(data.size() - 1);
    }

    public void clear() {
        data.clear();
    }

    public int size() {
        return data.size();
    }
}