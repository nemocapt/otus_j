package ru.otus.java_2018_08.student.hw01;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class LanternaMain {
    final static private String text = "Hello world";

    static public void main(String[] args) throws IOException, InterruptedException {
        DefaultTerminalFactory termFactory = new DefaultTerminalFactory();
        Terminal terminal = termFactory.createTerminal();

        Screen screen = new TerminalScreen(terminal);
        TerminalSize size = screen.getTerminalSize();
        TerminalPosition textPosition = getCenterBy(size, text.length());

        screen.startScreen();
        screen.setCursorPosition(null);
        screen.newTextGraphics().putString(textPosition, text);
        screen.refresh();

        Thread.sleep(2000);

        screen.stopScreen();
        screen.close();
    }

    static private TerminalPosition getCenter(TerminalSize size) {
        int y = size.getRows() / 2;
        int x = size.getColumns() / 2;

        return new TerminalPosition(x, y);
    }

    static private TerminalPosition getCenterBy(TerminalSize size, int stringLength) {
        TerminalPosition position = getCenter(size);

        int x = position.getColumn() - (stringLength / 2);
        position.withColumn(x <= 0 ? 0 : x);

        return position;
    }
}
