package ru.otus.java_2018_08.student.hw09.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;

public class JsonParser {
    static private List<BiPredicate<JsonParser, Object>> chain = new ArrayList<>();
    static {
        chain.add(JsonParser::writeNull);
        chain.add(JsonParser::writeNumber);
        chain.add(JsonParser::writeBoolean);
        chain.add(JsonParser::writeString);
        chain.add(JsonParser::writeArray);
        chain.add(JsonParser::checkFields);
    }

    private StringBuilder builder;

    private JsonParser() {
    }

    static public <T> JsonParser serialize(T object) {
        JsonParser parser = new JsonParser();
        parser.builder = new StringBuilder();

        parser.follow(object);

        return parser;
    }

    private <T> void follow(T object) {
        for (BiPredicate<JsonParser, Object> check : chain) {
            if (check.test(this, object)) {
                break;
            }
        }
    }

    static private <T> boolean checkFields(JsonParser parser, T object) {
        parser.builder.append('{');

        Field[] fields = object.getClass().getFields();
        for (int i = 0; i < fields.length; i++) {
            Field item = fields[i];

            if (Modifier.isTransient(item.getType().getModifiers())) {
                continue;
            }

            parser.builder.append('"');
            parser.builder.append(item.getName());
            parser.builder.append("\":");

            Object value = null;
            try {
                value = item.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            parser.follow(value);

            if (i < fields.length - 1) {
                parser.builder.append(',');
            }
        }

        parser.builder.append('}');

        return true;
    }

    static private boolean writeNull(JsonParser parser, Object object) {
        if (object == null) {
            parser.builder.append("null");

            return true;
        }

        return false;
    }

    static private boolean writeBoolean(JsonParser parser, Object object) {
        if (object instanceof Boolean) {
            parser.builder.append(object.toString());

            return true;
        }

        return false;
    }

    static private boolean writeNumber(JsonParser parser, Object object) {
        if (object instanceof Number) {
            parser.builder.append(object.toString());

            return true;
        }

        return false;
    }

    static private boolean writeString(JsonParser parser, Object object) {
        if (object.getClass().equals(String.class) || object instanceof Character) {
            parser.builder.append('"');
            parser.builder.append(object.toString());
            parser.builder.append('"');

            return true;
        }

        return false;
    }

    static private boolean writeArray(JsonParser parser, Object object) {
        if (object instanceof Collection) {
            object = ((Collection<?>)object).toArray();
        }

        if (object.getClass().isArray()) {
            parser.builder.append('[');

            int length = Array.getLength(object);
            for (int j = 0; j < length; j++) {
                parser.follow(Array.get(object, j));
                if (j < length - 1) {
                    parser.builder.append(',');
                }
            }

            parser.builder.append(']');

            return true;
        }

        return false;
    }

    private void addTabs(StringBuilder stringBuilder, int depth) {
        for (int i = 0; i < depth; i++) {
            stringBuilder.append('\t');
        }
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    public String prettify() {
        int depth = 0;

        StringBuilder prettyString = new StringBuilder();

        for (int i = 0; i < builder.length(); i++) {
            char symbol = builder.charAt(i);

            switch (symbol) {
                case '{':
                    prettyString.append(symbol);
                    prettyString.append('\n');
                    addTabs(prettyString, ++depth);
                    break;
                case '}':
                    prettyString.append('\n');
                    addTabs(prettyString, --depth);
                    prettyString.append(symbol);
                    break;
                case '[':
                    prettyString.append(symbol);
                    prettyString.append('\n');
                    addTabs(prettyString, ++depth);
                    break;
                case ']':
                    prettyString.append('\n');
                    addTabs(prettyString, --depth);
                    prettyString.append(symbol);
                    break;
                case ':':
                    prettyString.append(symbol);
                    prettyString.append(' ');
                    break;
                case ',':
                    prettyString.append(symbol);
                    prettyString.append('\n');
                    addTabs(prettyString, depth);
                    break;
                default:
                    prettyString.append(symbol);
                    break;
            }

        }

        return prettyString.toString();
    }
}
