package tools.visitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import exceptions.CompilerToolException;
import tools.FileGeneratorTool;
import tools.configuration.ParserConfiguration;
import tools.configuration.VisitorConfiguration;

public class PrintVisitorGenerator extends FileGeneratorTool {

  public PrintVisitorGenerator(Path specificationFile) throws CompilerToolException {
    super(specificationFile);
  }

  @Override
  public void generate() throws CompilerToolException {
    File visitor = new File(
        this.getGeneratedFilePath().toString().replace(
            VisitorConfiguration.VISITOR_BASE_CLASS_NAME,
            "PrintVisitor"));

    try (
        FileWriter writer = new FileWriter(visitor);) {
      writer.write(this.getHeaderContent());

      while (this.hasNext()) {
        String[] spec = this.next().split("\\s+");
        String className = spec[0];
        boolean isSymbolTree = spec.length == 2 && spec[1].contains("s");

        writer.write(this.getVisitorLine(className, isSymbolTree));
      }

      writer.write(this.formatLine("}"));
    } catch (IOException exc) {
      System.err.println("Failed to create the PrintVisitor.");
    }
  }

  private String getHeaderContent() {
    return String.join("",
        this.formatLine(String.format(
            "package %s;",
            VisitorConfiguration.VISITOR_PACKAGE), 0, 2),
        this.formatLine(String.format(
            "import %s.*;",
            ParserConfiguration.AST_PACKAGE), 0, 2),
        this.formatLine(String.format(
            "public class PrintVisitor extends %s {",
            VisitorConfiguration.VISITOR_BASE_CLASS_NAME), 0, 2),
        this.formatLine("private final int INDENT_BY = 2;", 1, 1),
        this.formatLine("private int indentation = INDENT_BY;", 1, 2),
        this.formatLine("private void print(String description, AST node) {", 1, 1),
        this.formatLine("String nodeNumber = String.format(\"%3d:\", node.getNodeNumber());", 2, 1),
        this.formatLine("System.out.println(String.format(\"%-3s %-35.35s\",", 2, 1),
        this.formatLine("nodeNumber,", 4, 1),
        this.formatLine("String.format(\"%\" + indentation + \"s%s\", \"\", description)));", 4, 2),
        this.formatLine("this.indentation += INDENT_BY;", 2, 1),
        this.formatLine("this.visitChildren(node);", 2, 1),
        this.formatLine("this.indentation -= INDENT_BY;", 2, 1),
        this.formatLine("}", 1, 2));
  }

  private String getVisitorLine(String className, boolean isSymbolTree) {
    StringBuffer buffer = new StringBuffer();

    buffer.append(
        this.formatLine(String.format("public Object visit%sTree(AST node) {", className), 1, 1));

    if (isSymbolTree) {
      String printCommand = String.format("%s%s",
          String.format("print(String.format(\"%s: ", className),
          "%s\", ((ISymbolTree) node).getSymbol().getLexeme()), node);");
      buffer.append(this.formatLine(printCommand, 2, 1));
    } else {
      buffer.append(this.formatLine(String.format("print(\"%s\", node);", className), 2, 1));
    }

    buffer.append(this.formatLine("return null;", 2, 1));
    buffer.append(this.formatLine("}", 1, 2));

    return buffer.toString();
  }

  @Override
  public Path getGeneratedFilePath() {
    return Paths.get(VisitorConfiguration.VISITOR_PACKAGE, "PrintVisitor.java");
  }

}
