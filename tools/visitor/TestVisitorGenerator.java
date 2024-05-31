package tools.visitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import exceptions.CompilerToolException;
import tools.FileGeneratorTool;
import tools.configuration.VisitorConfiguration;

public class TestVisitorGenerator extends FileGeneratorTool {

  public TestVisitorGenerator(Path specificationFile) throws CompilerToolException {
    super(specificationFile);
  }

  @Override
  public void generate() throws CompilerToolException {
    File visitor = Paths.get("tests", "helpers", "visitor", "TestVisitor.java").toFile();

    try (
        FileWriter writer = new FileWriter(visitor);) {
      writer.write(this.getHeaderContent());

      while (this.hasNext()) {
        String[] spec = this.next().split("\\s+");
        String className = spec[0];
        boolean isSymbolTree = spec.length == 2 && spec[1].contains("s");

        writer.write(getVisitorLine(className, isSymbolTree));
      }

      writer.write(this.formatLine("}"));
    } catch (IOException exc) {
      System.err.println("Failed to create the PrintVisitor.");
    }
  }

  private String getHeaderContent() {
    return String.join("",
        this.formatLine("package tests.helpers.visitor;", 0, 2),
        this.formatLine("import ast.*;", 0, 1),
        this.formatLine("import java.util.List;", 0, 1),
        this.formatLine(String.format(
            "import visitor.%s;", VisitorConfiguration.VISITOR_BASE_CLASS_NAME), 0, 2),
        this.formatLine(String.format("public class TestVisitor extends %s {",
            VisitorConfiguration.VISITOR_BASE_CLASS_NAME), 0, 2),
        this.formatLine("private List<AST> expected;", 1, 1),
        this.formatLine("private int index;", 1, 2),
        this.formatLine("public TestVisitor(final List<AST> expected) {", 1, 1),
        this.formatLine("this.expected = expected;", 2, 1),
        this.formatLine("this.index = 0;", 2, 1),
        this.formatLine("}", 1, 2),
        this.formatLine("public Object test(AST t) {", 1, 2),
        this.formatLine("try {", 2, 1),
        this.formatLine("expected.get(index).getClass().cast(t);", 3, 2),
        this.formatLine("index++;", 3, 2),
        this.formatLine("return testChildren(t);", 3, 1),
        this.formatLine("} catch (ClassCastException exception) {", 2, 1),
        this.formatLine("return String.format(", 3, 1),
        this.formatLine("\"Expected [%s] but got [%s]\",", 4, 1),
        this.formatLine("expected.get(index).getClass().getSimpleName(),", 4, 1),
        this.formatLine("t.getClass().getSimpleName());", 4, 1),
        this.formatLine("}", 2, 1),
        this.formatLine("}", 1, 2),
        this.formatLine("private Object test(AST t, String expectedSymbol, String actualSymbol) {", 1, 1),
        this.formatLine("try {", 2, 1),
        this.formatLine("expected.get(index).getClass().cast(t);", 3, 2),
        this.formatLine("if (!expectedSymbol.equals(actualSymbol)) {", 3, 1),
        this.formatLine("throw new Exception(", 4, 1),
        this.formatLine("String.format(", 5, 1),
        this.formatLine("\"Expected [%s] but got [%s]\",", 6, 1),
        this.formatLine("expectedSymbol,", 7, 1),
        this.formatLine("actualSymbol));", 7, 1),
        this.formatLine("}", 3, 2),
        this.formatLine("index++;", 3, 1),
        this.formatLine("return testChildren(t);", 3, 1),
        this.formatLine("} catch (ClassCastException exception) {", 2, 1),
        this.formatLine("return String.format(", 3, 1),
        this.formatLine("\"Expected [%s] but got [%s]\",", 4, 1),
        this.formatLine("expected.get(index).getClass().getSimpleName(),", 4, 1),
        this.formatLine("t.getClass().getSimpleName());", 4, 1),
        this.formatLine("} catch (Exception exception) {", 2, 1),
        this.formatLine("return exception.getMessage();", 3, 1),
        this.formatLine("}", 2, 1),
        this.formatLine("}", 1, 2),
        this.formatLine("private Object testChildren(AST t) {", 1, 1),
        this.formatLine("for (AST child : t.getChildren()) {", 2, 1),
        this.formatLine("Object result = child.accept(this);", 3, 2),
        this.formatLine("if (result != null) {", 3, 1),
        this.formatLine("return result;", 4, 1),
        this.formatLine("}", 3, 1),
        this.formatLine("}", 2, 2),
        this.formatLine("return null;", 2, 1),
        this.formatLine("}", 1, 2));
  }

  private String getVisitorLine(String className, boolean isSymbolTree) {
    StringBuffer buffer = new StringBuffer();

    buffer.append(this.formatLine("@Override", 1, 1));
    buffer.append(this.formatLine(String.format(
        "public Object visit%sTree(AST node) {",
        className), 1, 1));

    if (isSymbolTree) {
      buffer.append(this.formatLine(
          "String actualSymbol = ((ISymbolTree) node).getSymbol().toString();", 2, 1));
      buffer.append(this.formatLine(
          "String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();", 2, 2));
      buffer.append(this.formatLine(
          "return test(node, expectedSymbol, actualSymbol);", 2, 1));
    } else {
      buffer.append(this.formatLine("return test(node);", 2, 1));
    }

    buffer.append(this.formatLine("}", 1, 2));

    return buffer.toString();
  }

  @Override
  public Path getGeneratedFilePath() {
    return Paths.get("tests", "helpers", "visitor", "TestVisitor.java");
  }

}
