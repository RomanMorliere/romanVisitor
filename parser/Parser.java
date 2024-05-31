package parser;

import java.util.EnumSet;

import ast.AST;
import ast.trees.*;
import exceptions.LexicalException;
import exceptions.SyntaxErrorException;
import lexer.ILexer;
import lexer.Lexer;
import lexer.daos.Token;
import lexer.daos.TokenKind;
import visitor.PrintVisitor;

public class Parser {
  private Token currentToken;
  private ILexer lexer;

  private EnumSet<TokenKind> relationalOperators = EnumSet.of(
      TokenKind.Equal,
      TokenKind.NotEqual,
      TokenKind.Less,
      TokenKind.LessEqual,
      TokenKind.Greater,
      TokenKind.GreaterEqual);
  private EnumSet<TokenKind> additionOperators = EnumSet.of(
      TokenKind.Plus,
      TokenKind.Minus,
      TokenKind.Or);
  private EnumSet<TokenKind> multiplicationOperators = EnumSet.of(
      TokenKind.Multiply,
      TokenKind.Divide,
      TokenKind.And);

  public Parser(String sourceProgramPath) throws LexicalException {
    this(new Lexer(sourceProgramPath));
  }

  public Parser(ILexer lexer) throws LexicalException {
    this.lexer = lexer;
    scan();
  }

  private void expect(TokenKind expected) throws SyntaxErrorException, LexicalException {
    if (this.currentToken.getTokenKind() == expected) {
      scan();
    } else {
      error(currentToken.getTokenKind(), expected);
    }
  }

  private void error(TokenKind actual, TokenKind... expected) throws SyntaxErrorException {
    throw new SyntaxErrorException(actual, expected);
  }

  private void scan() throws LexicalException {
    this.currentToken = lexer.nextToken();
  }

  private boolean match(TokenKind... kinds) {
    for (TokenKind tokenKind : kinds) {
      if (this.currentToken.getTokenKind() == tokenKind) {
        return true;
      }
    }

    return false;
  }

  public AST execute() throws SyntaxErrorException, LexicalException {
    return program();
  }

  /**
   * PROGRAM → 'program' BLOCK
   *
   * @throws LexicalException
   * @throws SyntaxErrorException
   */
  private AST program() throws SyntaxErrorException, LexicalException {
    AST node = new ProgramTree();

    expect(TokenKind.Program);
    node.addChild(block());

    return node;
  }

  /**
   * BLOCK → '{' DECLARATIONS* STATEMENTS* '}'
   *
   * @throws LexicalException
   * @throws SyntaxErrorException
   */
  private AST block() throws SyntaxErrorException, LexicalException {
    AST node = new BlockTree();

    expect(TokenKind.LeftBrace);

    while (startingDeclaration()) {
      node.addChild(declaration());
    }

    while (startingStatement()) {
      node.addChild(statement());
    }

    expect(TokenKind.RightBrace);

    return node;
  }

  private boolean startingDeclaration() {
    return match(
        TokenKind.IntType,
        TokenKind.BooleanType,
        TokenKind.Number,
        TokenKind.CharType);
  }

  private boolean startingStatement() {
    return match(
        TokenKind.If,
        TokenKind.While,
        TokenKind.Return,
        TokenKind.LeftBrace,
        TokenKind.Identifier,
        TokenKind.From);
  }

  /**
   * DECLARATION → TYPE NAME
   * DECLARATION → TYPE NAME '(' FORMALS ')' BLOCK
   */
  private AST declaration() throws SyntaxErrorException, LexicalException {
    AST type = type();
    AST name = name();

    if (match(TokenKind.LeftParen)) {
      AST function = new FunctionDeclarationTree().addChild(type).addChild(name);

      function.addChild(formals());

      return function.addChild(block());
    } else {
      return new DeclarationTree().addChild(type).addChild(name);
    }
  }

  /**
   * TYPE → 'int'
   * TYPE → 'boolean'
   */
  private AST type() throws LexicalException, SyntaxErrorException {
    AST node = null;

    switch (this.currentToken.getTokenKind()) {
      case IntType:
        node = new IntTypeTree();
        break;
      case BooleanType:
        node = new BoolTypeTree();
        break;
      case Number:
        node = new NumberTree();
        break;
      case CharType:
        node = new CharTypeTree();
        break;
      default:
        error(currentToken.getTokenKind(),
            TokenKind.IntType,
            TokenKind.BooleanType,
            TokenKind.Number,
            TokenKind.CharType);
        break;
    }

    scan();
    return node;
  }

  /*
   * NAME → <id>
   */
  private AST name() throws LexicalException, SyntaxErrorException {
    AST node = null;

    if (match(TokenKind.Identifier)) {
      node = new IdentifierTree(currentToken);

      expect(TokenKind.Identifier);
    }

    return node;
  }

  /**
   * FORMALS → 𝜀
   * FORMALS → DECLARATION (',' DECLARATION)*
   */
  private AST formals() throws SyntaxErrorException, LexicalException {
    AST node = new FormalsTree();

    expect(TokenKind.LeftParen);

    if (!match(TokenKind.RightParen)) {
      node.addChild(declaration());
    }
    while (!match(TokenKind.RightParen)) {
      expect(TokenKind.Comma);
      node.addChild(declaration());
    }

    expect(TokenKind.RightParen);

    return node;
  }

  /**
   * STATEMENT → 'if' E 'then' BLOCK 'else' BLOCK
   * STATEMENT → 'while' E BLOCK
   * STATEMENT → 'return' E
   * STATEMENT → BLOCK
   * STATEMENT → NAME '=' E
   */
  private AST statement() throws SyntaxErrorException, LexicalException {

    switch (currentToken.getTokenKind()) {
      case If:
        return ifStatement();
      case While:
        return whileStatement();
      case Return:
        return returnStatement();
      case LeftBrace:
        return block();
      case Identifier:
        return assignStatement();
      case From:
        return fromStatement();
      default:
        error(
            currentToken.getTokenKind(),
            TokenKind.If,
            TokenKind.While,
            TokenKind.Return,
            TokenKind.LeftBrace,
            TokenKind.Identifier);
        return null;
    }
  }

  /**
   * STATEMENT → 'if' E 'then' BLOCK 'else' BLOCK
   */
  private AST ifStatement() throws SyntaxErrorException, LexicalException {
    AST node = new IfTree();

    expect(TokenKind.If);
    node.addChild(expression());
    expect(TokenKind.Then);
    node.addChild(block());

    if (match(TokenKind.Else)) {
      expect(TokenKind.Else);
      node.addChild(block());
    }

    return node;
  }

  /**
   * STATEMENT → 'while' E BLOCK
   */
  private AST whileStatement() throws SyntaxErrorException, LexicalException {
    AST node = new WhileTree();

    expect(TokenKind.While);
    node.addChild(expression()).addChild(block());

    return node;
  }

  /**
   * STATEMENT → 'return' E
   */
  private AST returnStatement() throws SyntaxErrorException, LexicalException {
    AST node = new ReturnTree();

    expect(TokenKind.Return);

    node.addChild(expression());
    return node;
  }

  /**
   * STATEMENT → NAME '=' E
   */
  private AST assignStatement() throws SyntaxErrorException, LexicalException {
    AST node = new AssignmentTree();

    node.addChild(name());
    expect(TokenKind.Assign);
    node.addChild(expression());

    return node;
  }

  /**
   * S → 'from' RANGE STEP BLOCK
   *
   * @throws LexicalException
   * @throws SyntaxErrorException
   */
  private AST fromStatement() throws SyntaxErrorException, LexicalException {
    AST node = new FromTree();

    expect(TokenKind.From);
    node.addChild(range());
    node.addChild(step());
    node.addChild(block());

    return node;
  }

  /*
   * RANGE → '(' E 'to' E ')'
   */
  private AST range() throws SyntaxErrorException, LexicalException {
    AST node = new RangeTree();

    expect(TokenKind.LeftParen);
    node.addChild(expression());
    expect(TokenKind.To);
    node.addChild(expression());
    expect(TokenKind.RightParen);

    return node;
  }

  /*
   * STEP → 'step' E
   */
  private AST step() throws SyntaxErrorException, LexicalException {
    AST node = new StepTree();

    expect(TokenKind.Step);
    node.addChild(expression());

    return node;
  }

  /**
   * E → SE
   * E → SE '==' SE
   * E → SE '!=' SE
   * E → SE '<' SE
   * E → SE '<=' SE
   */
  private AST expression() throws LexicalException, SyntaxErrorException {
    AST tree, child = simpleExpression();

    tree = getRelopTree();
    if (tree == null) {
      return child;
    }

    tree.addChild(child);
    tree.addChild(simpleExpression());

    return tree;
  }

  private AST getRelopTree() throws LexicalException {
    if (relationalOperators.contains(currentToken.getTokenKind())) {
      AST tree = new RelOpTree(currentToken);
      scan();

      return tree;
    } else {
      return null;
    }
  }

  /**
   * SE → T
   * SE → SE '+' T
   * SE → SE '-' T
   * SE → SE '|' T
   */
  private AST simpleExpression() throws LexicalException, SyntaxErrorException {
    AST tree, child = term();

    while ((tree = getAddOpTree()) != null) {
      tree.addChild(child);
      tree.addChild(term());

      child = tree;
    }

    return child;
  }

  private AST getAddOpTree() throws LexicalException {
    if (additionOperators.contains(currentToken.getTokenKind())) {
      AST tree = new AddOpTree(currentToken);
      scan();

      return tree;
    } else {
      return null;
    }
  }

  /**
   * T → F
   * T → T '*' F
   * T → T '/' F
   * T → T '&' F
   */
  private AST term() throws SyntaxErrorException, LexicalException {
    AST tree, child = factor();

    while ((tree = getMultOpTree()) != null) {
      tree.addChild(child);
      tree.addChild(factor());

      child = tree;
    }

    return child;
  }

  private AST getMultOpTree() throws LexicalException {
    if (multiplicationOperators.contains(currentToken.getTokenKind())) {
      AST tree = new MultOpTree(currentToken);
      scan();

      return tree;
    } else {
      return null;
    }
  }

  /**
   * F → '(' E ')'
   * F → <int>
   * F → NAME
   * F → NAME '(' E_LIST ')'
   */
  private AST factor() throws SyntaxErrorException, LexicalException {
    switch (currentToken.getTokenKind()) {
      case LeftParen: {
        expect(TokenKind.LeftParen);
        AST node = expression();
        expect(TokenKind.RightParen);

        return node;
      }
      case IntLit: {
        AST node = new IntTree(currentToken);
        expect(TokenKind.IntLit);

        return node;
      }
      case NumberLit: {
        AST node = new NumberLitTree(currentToken);
        expect(TokenKind.NumberLit);

        return node;
      }
      case CharLit: {
        AST node = new CharLitTree(currentToken);
        expect(TokenKind.CharLit);

        return node;
      }
      case Identifier: {
        AST node = new IdentifierTree(currentToken);
        expect(TokenKind.Identifier);

        if (match(TokenKind.LeftParen)) {
          node = new CallTree().addChild(node);
          node.addChild(actualArguments());
        }

        return node;
      }
      default:
        error(
            currentToken.getTokenKind(),
            TokenKind.LeftParen);
        return null;
    }
  }

  /**
   * ACTUAL_ARGUMENTS → 𝜀
   * ACTUAL_ARGUMENTS → E (',' E)*
   */
  private AST actualArguments() throws SyntaxErrorException, LexicalException {
    AST node = new ActualArgumentsTree();

    expect(TokenKind.LeftParen);

    if (!match(TokenKind.RightParen)) {
      node.addChild(expression());
    }
    while (match(TokenKind.Comma) && !match(TokenKind.RightParen)) {
      expect(TokenKind.Comma);
      node.addChild(expression());
    }

    expect(TokenKind.RightParen);

    return node;
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("usage: java parser.Parser <filepath>");
      System.exit(1);
    }

    try {
      ILexer lexer = new Lexer(args[0]);
      Parser parser = new Parser(lexer);

      AST ast = parser.execute();

      System.out.println(lexer);
      System.out.println();

      PrintVisitor printVisitor = new PrintVisitor();
      printVisitor.visitProgramTree(ast);

    } catch (LexicalException e) {
      e.printStackTrace();
    } catch (SyntaxErrorException e) {
      e.printStackTrace();
    }

  }
}
