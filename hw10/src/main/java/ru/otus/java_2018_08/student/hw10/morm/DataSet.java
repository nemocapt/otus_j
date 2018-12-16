package ru.otus.java_2018_08.student.hw10.morm;

import java.lang.reflect.Field;

public abstract class DataSet {
    private long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    abstract protected String getTableName();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("id: ");
        builder.append(id);
        builder.append("; ");

        for (Field item : this.getClass().getFields()) {
            builder.append(item.getName());
            builder.append(": ");
            try {
                builder.append(item.get(this).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            builder.append("; ");
        }

        builder.append("}");

        return builder.toString();
    }
}
