package de.reichertit.brainfuck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * The Brainfuck class represents an interpreter for the Brainfuck programming language.
 */
public class Brainfuck {

    /**
     * The ALLOWED_OPERATORS variable represents an array of allowed operators in the Brainfuck programming language.
     *
     * <p>
     * The memory of the Brainfuck interpreter is an array of 30,000 bytes initialized to zero.
     * The pointer initially points to the first element of the array and can be moved using the '<' and '>' operators.
     * The '.' operator outputs the byte at the current position as an ASCII-encoded character.
     * The ',' operator reads a single character from input and stores it at the current position.
     * The '+' and '-' operators increment and decrement the value in the current position.
     * The '[' and ']' operators are used for loops, where the loop is executed as long as the value at the current position is not zero.
     * </p>
     *
     * <p>
     * The ALLOWED_OPERATORS static variable is of type char[] and contains the allowed operators in the Brainfuck language.
     * The operators are represented as single characters:
     * - '>' moves the pointer to the next memory cell
     * - '<' moves the pointer to the previous memory cell
     * - '.' outputs the byte at the current memory cell as an ASCII-encoded character
     * - ',' reads a single character from input and stores it in the current memory cell
     * - '+' increments the value at the current memory cell
     * - '-' decrements the value at the current memory cell
     * - '[' begins a loop, jumping forward to the corresponding ']' if the value at the current memory cell is zero
     * - ']' jumps back to the corresponding '[' if the value at the current memory cell is non-zero
     * </p>
     */
    private static final char[] ALLOWED_OPERATORS = new char[]{'>', '<', '.', ',', '+', '-', '[', ']'};

    /**
     * The memory variable represents the memory of the Brainfuck interpreter.
     *
     * <p>
     * The memory is an array of 30,000 bytes initialized to zero. It is used to store data
     * during the execution of Brainfuck programs. Each element in the memory array can hold
     * a value between 0 and 255.
     * </p>
     */
    private final byte[] memory = new byte[30000];

    /**
     * The pointer variable represents the current position of the memory pointer in the Brainfuck interpreter.
     *
     * <p>
     * The memory of the Brainfuck interpreter is an array of 30,000 bytes initialized to zero.
     * The pointer represents the current position in the memory array.
     * It can be moved using the '<' and '>' operators to access different memory cells.
     * </p>
     */
    private int pointer = 0;

    /**
     * The main method is the entry point of the program.
     *
     * @param args The command line arguments.
     *             args[0] - The file path to interpret.
     * @throws FileNotFoundException If the file path provided does not exist.
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            String filePath = args[0];
            File file = new File(filePath);

            if (file.exists()) {
                Brainfuck brainfuck = new Brainfuck();
                brainfuck.interpret(file);
            } else {
                throw new FileNotFoundException("The file at path " + filePath + " does not exist");
            }
        } else {
            System.out.println("Please provide a file path as an argument");
        }
    }

    public void interpret(File fileToRead) throws IOException {
        char[] program = this.getProgramOperators(fileToRead);
        Stack<Integer> loopIndexStack = new Stack<>();

        for (int i = 0; i < program.length; i++) {
            char operator = program[i];

            switch (operator) {
                case '>':
                    incrementPointer();
                    break;
                case '<':
                    decrementPointer();
                    break;
                case '+':
                    incrementMemory();
                    break;
                case '-':
                    decrementMemory();
                    break;
                case '.':
                    outputMemory();
                    break;
                case ',':
                    inputMemory();
                    break;
                case '[':
                    if (memory[pointer] == 0) {
                        i = jumpToMatchingBracket(i, program, 1);
                    } else {
                        loopIndexStack.push(i);
                    }
                    break;
                case ']':
                    if (memory[pointer] != 0) {
                        i = loopIndexStack.peek();
                    } else {
                        loopIndexStack.pop();
                    }
                    break;
            }
        }
    }

    /**
     * Jumps to the matching bracket of a given bracket index in a program.
     * The direction parameter indicates whether to search forward (+1) or backward (-1).
     *
     * @param startIndex The index of the bracket to find the matching bracket for.
     * @param program The char array representing the program to search in.
     * @param direction The direction of the search (1 for forward, -1 for backward).
     * @return The index of the matching bracket. If no matching bracket is found, returns -1.
     */
    private int jumpToMatchingBracket(int startIndex, char[] program, int direction) {
        int bracketCount = direction;

        while (bracketCount != 0) {
            startIndex += direction;
            if (program[startIndex] == '[') {
                bracketCount++;
            } else if (program[startIndex] == ']') {
                bracketCount--;
            }
        }
        return startIndex;
    }

    private void incrementPointer() {
        if(pointer < memory.length - 1) {
            pointer++;
        } else {
            throw new IndexOutOfBoundsException(String.format("Pointer is out of bounds. (pointer: %s, memory_size: %s)\n)", pointer, memory.length));
        }
    }

    private void decrementPointer() {
        if(pointer > 0) {
            pointer--;
        } else {
            throw new IndexOutOfBoundsException(String.format("Pointer is out of bounds. (pointer: %s, memory_size: %s)\n)", pointer, memory.length));
        }
    }

    private void incrementMemory() {
        memory[pointer]++;
    }

    private void decrementMemory() {
        memory[pointer]--;
    }

    private void outputMemory() {
        System.out.print((char) memory[pointer]);
    }

    private void inputMemory() {
        System.out.println("Waiting for Input");
        try {
            int input = System.in.read();
            // Skip over any Leftover newline characters after the input.
            while(System.in.available() > 0) {
                System.in.read();
            }
            memory[pointer] = (byte) input;
        } catch (IOException e) {
            System.out.println("An error occurred while reading input: " + e.getMessage());
        }
    }

    /**
     * Retrieves the program operators from a given file.
     *
     * @param fromFile The file to read the program from.
     * @return An array of characters representing the program operators found in the file.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    private char[] getProgramOperators(File fromFile) throws IOException {
        Path path = fromFile.toPath();
        byte[] bytes = Files.readAllBytes(path);
        String str = new String(bytes);
        StringBuilder sb = new StringBuilder();

        Set<Character> operatorsSet = new HashSet<>();
        for (char operator : ALLOWED_OPERATORS) {
            operatorsSet.add(operator);
        }

        for (char c : str.toCharArray()) {
            if(operatorsSet.contains(c)) {
                sb.append(c);
            }
        }

        return sb.toString().toCharArray();
    }
}
