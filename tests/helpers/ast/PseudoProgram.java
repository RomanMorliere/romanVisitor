package tests.helpers.ast;

import java.util.List;
import java.util.StringTokenizer;
import ast.AST;
import ast.trees.AssignmentTree;
import ast.trees.BlockTree;
import ast.trees.DeclarationTree;
import ast.trees.IdentifierTree;
import ast.trees.IntTree;
import ast.trees.IntTypeTree;
import ast.trees.ProgramTree;
import ast.trees.RelOpTree;
import ast.trees.ReturnTree;
import lexer.ILexer;
import lexer.daos.Token;
import tests.helpers.lexer.TestLexer;

public class PseudoProgram {

  public static ILexer lexerFromPseudoProgram(String program) throws Exception {
    StringTokenizer tokenizer = new StringTokenizer(program);

    try (TestLexer lexer = new TestLexer();) {
      while (tokenizer.hasMoreTokens()) {
        String tokenString = tokenizer.nextToken();
        Token token = PseudoProgramTokens.getTestToken(tokenString);

        if (token == null) {
          // This should only happen if I have created an invalid test case
          throw new Exception(String.format("Unrecognized token: %s", tokenString));
        }

        lexer.addToken(token);
      }

      return lexer;
    }
  }

  public static ILexer lexerForRelationalOperator(String relOp) throws Exception {
    return PseudoProgram.lexerFromPseudoProgram(
        String.format(
            String.join(
                System.lineSeparator(),
                List.of(
                    "program {",
                    "  return <int> %s <int>",
                    "}")),
            relOp));
  }

  public static List<AST> expectedAstForRelationalOperator(String relOp) {
    return List.of(
        new ProgramTree(),
        new BlockTree(),
        new ReturnTree(),
        new RelOpTree(PseudoProgramTokens.getTestToken(relOp)),
        new IntTree(PseudoProgramTokens.getTestToken("<int>")),
        new IntTree(PseudoProgramTokens.getTestToken("<int>")));
  }

  public static ILexer lexerForMathematicalOperator(String mathOp) throws Exception {
    return PseudoProgram.lexerFromPseudoProgram(
        String.format(
            String.join(
                System.lineSeparator(),
                List.of(
                    "program { int <id>",
                    "  <id> = <int> %s <int>",
                    "}")),
            mathOp));
  }

  public static List<AST> expectedAstForMathematicalOperator(AST opTree) {
    return List.of(
        new ProgramTree(),
        new BlockTree(),
        new DeclarationTree(),
        new IntTypeTree(),
        new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
        new AssignmentTree(),
        new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
        opTree,
        new IntTree(PseudoProgramTokens.getTestToken("<int>")),
        new IntTree(PseudoProgramTokens.getTestToken("<int>")));
  }

  public static ILexer lexerForType(String typeString) throws Exception {
    return PseudoProgram.lexerFromPseudoProgram(
        String.format(
            String.join(
                System.lineSeparator(),
                List.of(
                    "program { %s <id>",
                    "  <id> = <%s>",
                    "}")),
            typeString, typeString));
  }

  public static List<AST> expectedAstForType(String typeString) {
    return List.of(
        new ProgramTree(),
        new BlockTree(),
        new DeclarationTree(),
        PseudoProgramAsts.get(typeString),
        new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
        new AssignmentTree(),
        new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
        PseudoProgramAsts.get(String.format("<%s>", typeString)));
  }
}
