package tests.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lexer.Lexer;

public class LexerOutputTest {

  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @BeforeEach
  public void setUp() {
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @AfterEach
  public void tearDown() {
    System.setOut(standardOut);
  }

  @Test
  public void testLexerOutput() throws Exception {
    File temp = File.createTempFile("atestfileforme", null);
    String path = temp.getAbsolutePath().toString();

    FileWriter writer = new FileWriter(temp);
    writer.write("int i\n");
    writer.write("program");
    writer.close();

    Lexer.main(new String[] { path });

    List<String> output = Arrays.asList(outputStreamCaptor.toString().split(System.lineSeparator()));

    assertEquals("int                  left: 0        right: 2        line: 1        IntType", output.get(0));
    assertEquals("i                    left: 4        right: 4        line: 1        Identifier", output.get(1));
    assertEquals("program              left: 0        right: 6        line: 2        Program", output.get(2));
    assertEquals("", output.get(3));
    assertEquals("  1: int i", output.get(4));
    assertEquals("  2: program", output.get(5));
  }

  @Test
  public void testLexerOutputSimpleX() throws Exception {
    File temp = File.createTempFile("atestfileforme", null);
    String path = temp.getAbsolutePath().toString();

    FileWriter writer = new FileWriter(temp);
    writer.write("program { int i int j\n");
    writer.write("   i = i + j + 7\n");
    writer.write("   j = write(i)\n");
    writer.write("}\n");
    writer.close();
    // this should get overwritten

    Lexer.main(new String[] { path });

    List<String> output = Arrays.asList(outputStreamCaptor.toString().split(System.lineSeparator()));

    assertEquals("program              left: 0        right: 6        line: 1        Program", output.get(0));
    assertEquals("{                    left: 8        right: 8        line: 1        LeftBrace", output.get(1));
    assertEquals("int                  left: 10       right: 12       line: 1        IntType", output.get(2));
    assertEquals("i                    left: 14       right: 14       line: 1        Identifier", output.get(3));
    assertEquals("int                  left: 16       right: 18       line: 1        IntType", output.get(4));
    assertEquals("j                    left: 20       right: 20       line: 1        Identifier", output.get(5));
    assertEquals("i                    left: 3        right: 3        line: 2        Identifier", output.get(6));
    assertEquals("=                    left: 5        right: 5        line: 2        Assign", output.get(7));
    assertEquals("i                    left: 7        right: 7        line: 2        Identifier", output.get(8));
    assertEquals("+                    left: 9        right: 9        line: 2        Plus", output.get(9));
    assertEquals("j                    left: 11       right: 11       line: 2        Identifier", output.get(10));
    assertEquals("+                    left: 13       right: 13       line: 2        Plus", output.get(11));
    assertEquals("7                    left: 15       right: 15       line: 2        IntLit", output.get(12));
    assertEquals("j                    left: 3        right: 3        line: 3        Identifier", output.get(13));
    assertEquals("=                    left: 5        right: 5        line: 3        Assign", output.get(14));
    assertEquals("write                left: 7        right: 11       line: 3        Identifier", output.get(15));
    assertEquals("(                    left: 12       right: 12       line: 3        LeftParen", output.get(16));
    assertEquals("i                    left: 13       right: 13       line: 3        Identifier", output.get(17));
    assertEquals(")                    left: 14       right: 14       line: 3        RightParen", output.get(18));
    assertEquals("}                    left: 0        right: 0        line: 4        RightBrace", output.get(19));
    assertEquals("", output.get(20));
    assertEquals("  1: program { int i int j", output.get(21));
    assertEquals("  2:    i = i + j + 7", output.get(22));
    assertEquals("  3:    j = write(i)", output.get(23));
    assertEquals("  4: }", output.get(24));
  }
}
