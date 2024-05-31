package tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ast.AST;
import exceptions.LexicalException;
import exceptions.SyntaxErrorException;
import parser.Parser;
import visitor.PrintVisitor;
import visitor.images.DrawOffsetVisitor;
import visitor.images.OffsetVisitor;

public class AstRenderer {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("usage: java tools.AstRenderer <path to code file>");
      System.exit(1);
    }

    try {
      Parser parser = new Parser(args[0]);
      AST tree = parser.execute();

      PrintVisitor printVisitor = new PrintVisitor();
      tree.accept(printVisitor);

      OffsetVisitor offsetVisitor = new OffsetVisitor();
      tree.accept(offsetVisitor);

      DrawOffsetVisitor drawOffsetVisitor = new DrawOffsetVisitor(
          offsetVisitor.getOffsets(),
          offsetVisitor.getUnitWidth(),
          offsetVisitor.getUnitHeight());
      tree.accept(drawOffsetVisitor);

      BufferedImage image = drawOffsetVisitor.getImage();
      ImageIO.write(image, "png", (new File(args[0] + ".png")));

    } catch (LexicalException e) {
      System.err.println(e.getMessage());
    } catch (SyntaxErrorException e) {
      System.err.println(e.getMessage());
    } catch (IOException e) {
      System.err.println("Failed to save image.");
      e.printStackTrace();
    }
  }
}
