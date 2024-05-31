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

public class VisitorBaseClassGenerator extends FileGeneratorTool {

  public VisitorBaseClassGenerator(Path specificationFile) throws CompilerToolException {
    super(specificationFile);
  }

  @Override
  public void generate() throws CompilerToolException {
    File visitor = this.getGeneratedFilePath().toFile();

    try (FileWriter visitorWriter = new FileWriter(visitor);) {

      visitorWriter.write(this.getHeaderContent());

      while (this.hasNext()) {
        String[] spec = this.next().split("\\s+");
        String className = spec[0];

        visitorWriter.write(this.formatLine(this.getVisitorLine(className), 1, 2));
      }

      visitorWriter.write("}");
    } catch (IOException exc) {
      System.err.println("Failed to create the base Visitor class.");
    }
  }

  private String getHeaderContent() {
    return String.join("",
        this.formatLine(String.format(
            "package %s;",
            VisitorConfiguration.VISITOR_PACKAGE), 0, 2),
        this.formatLine(String.format(
            "import %s.AST;",
            ParserConfiguration.AST_PACKAGE), 0, 2),
        this.formatLine(String.format(
            "public abstract class %s {",
            VisitorConfiguration.VISITOR_BASE_CLASS_NAME), 0, 2),
        this.formatLine("public void visitChildren(AST parentNode) {", 1, 1),
        this.formatLine("for (AST child : parentNode.getChildren()) {", 2, 1),
        this.formatLine("child.accept(this);", 3, 1),
        this.formatLine("}", 2, 1),
        this.formatLine("}", 1, 2));
  }

  private String getVisitorLine(String className) {
    return String.format("public abstract Object visit%sTree(AST node);", className);
  }

  @Override
  public Path getGeneratedFilePath() {
    return Paths.get(
        VisitorConfiguration.VISITOR_PACKAGE,
        String.format("%s.java", VisitorConfiguration.VISITOR_BASE_CLASS_NAME));
  }

}
