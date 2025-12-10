package API;

import java.util.ArrayList;

public interface Manager<T> {

    boolean add(T object);

    boolean remove(T object);

    ArrayList<T> getList();

    int getSize();

    void print();

    //void readCSV();

    void writeCSV();
}