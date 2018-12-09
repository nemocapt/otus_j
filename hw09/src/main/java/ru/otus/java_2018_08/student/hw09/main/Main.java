package ru.otus.java_2018_08.student.hw09.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java_2018_08.student.hw09.json.JsonParser;

import java.util.Arrays;
import java.util.List;

public class Main {
    static final private Logger log = LoggerFactory.getLogger(Main.class);

    static public void main(String[] args) {
        System.out.println(JsonParser.serialize(new TestPojo()).prettify());
    }

    static public class TestPojo {
        public int i = 30;
        public Integer cI = 42;
        public List<String> list = Arrays.asList("qwe", "asd", "zxc");
        public byte[] byteArr = {1, 1, 2, 3, 5, 8};
        public String line = "some string";
        public InnerPojo inner = new InnerPojo();
    }

    static public class InnerPojo {
        public char c = 'q';
        public Character cC = 'e';
        public String nullString;
        public boolean b = true;
        public Boolean bB = false;
    }
}
