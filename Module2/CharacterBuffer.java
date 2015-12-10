package assignment2;

import java.util.ArrayList;

/**
 * Buffer class that hold a value as a single char
 */
public class CharacterBuffer {
    private char charBuffer;

    public CharacterBuffer() {
        charBuffer = 0;
    }

    // Sets a value in the buffer
    public void setBufferValue(char value) {
        charBuffer = value;
    }

    // Returns the buffer value
    public char getBufferValue() {
        return charBuffer;
    }
}
