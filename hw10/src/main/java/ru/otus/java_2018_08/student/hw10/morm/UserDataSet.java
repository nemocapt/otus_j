package ru.otus.java_2018_08.student.hw10.morm;

public class UserDataSet extends DataSet{
    static private final String tableName = "user";

    public String name;
    public int age;

    @Override
    protected String getTableName() {
        return tableName;
    }
}
