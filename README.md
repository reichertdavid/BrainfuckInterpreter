# Brainfuck Interpreter

This project is a simple interpreter for the Brainfuck programming language, implemented in Java. Brainfuck is a minimalist programming language known for its minimalistic syntax and challenging code readability. This interpreter reads Brainfuck source code from a file and executes it.

## Project Structure

The main class of this project is:

- **`Brainfuck`**: This class contains methods to interpret and execute Brainfuck programs. It initializes a memory array and a pointer, processes the input code, and performs operations as specified by Brainfuck's syntax.

## How to Use

To use the Brainfuck interpreter, you need to provide a file containing Brainfuck code as a command-line argument. The program will read the file, parse the Brainfuck commands, and execute them.

### Running the Interpreter

1. **Compile the Java code**:

   ```bash
    javac -d . Brainfuck.java```
2. Run the interpreter with a Brainfuck source file:

```bash
java de.reichertit.brainfuck.Brainfuck path/to/your/brainfuck_code.bf
```
Replace path/to/your/brainfuck_code.bf with the actual path to the Brainfuck file you want to interpret.

## Brainfuck Language Overview
Brainfuck uses a minimalistic set of 8 commands to manipulate a memory array of 30,000 bytes (initialized to zero). The commands are as follows:

```bash
>: Move the memory pointer to the next cell.
<: Move the memory pointer to the previous cell.
+: Increment the byte at the current memory cell by 1.
-: Decrement the byte at the current memory cell by 1.
.: Output the byte at the current memory cell as an ASCII-encoded character.
,: Read a single character from input and store it in the current memory cell.
[: Begin a loop. If the byte at the current memory cell is zero, jump to the matching ].
]: End a loop. If the byte at the current memory cell is non-zero, jump back to the matching [.
```
## How It Works
**Memory Management**: The interpreter uses a fixed-size array of 30,000 bytes as its memory. A pointer is used to navigate through this array.  
  
**Input/Output**: The . command outputs the current memory cell's value as a character, while the , command waits for a single character input from the user.  
  
**Loops**: Using [ and ], the interpreter supports loops. It uses a stack to manage nested loops, ensuring that the program can handle complex looping structures.  
## Error Handling
If the file path provided does not exist, the interpreter throws a FileNotFoundException.
The interpreter checks for pointer bounds and throws an IndexOutOfBoundsException if the pointer moves beyond the allocated memory range.

## Example
Here's a simple **`"Hello World!"`** program in Brainfuck:

```brainfuck
>+++++++++[<++++++++>-]<.>++++[<+++++++>-]<+.+++++++..+++.>>>++++++++[<++++>-]<.>>>++++++++++[<+++++++++>-]<---.<<<<.+++.<.>>++++++++++++[<+++++>-]<.>>>.+++.
```

Save this code in a file, for example, `hello.bf`, and run the interpreter as described above to see the output.

## License
This project is licensed under the MIT License - see the LICENSE file for details.
