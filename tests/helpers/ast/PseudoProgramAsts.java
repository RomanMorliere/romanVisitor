package tests.helpers.ast;

import java.util.HashMap;
import java.util.Map;
import ast.AST;
import ast.trees.*;

public class PseudoProgramAsts {
  private static Map<String, AST> pseudoAsts = new HashMap<>();
  static {
    pseudoAsts.put("int", new IntTypeTree());
    pseudoAsts.put("<int>", new IntTree(PseudoProgramTokens.getTestToken("<int>")));
    pseudoAsts.put("boolean", new BoolTypeTree());
    pseudoAsts.put("<boolean>", new IntTree(PseudoProgramTokens.getTestToken("<boolean>")));

    // Add new types here for current semester
    pseudoAsts.put("num", new NumberTree());
    pseudoAsts.put("<num>", new NumberLitTree(PseudoProgramTokens.getTestToken("<num>")));
    pseudoAsts.put("character", new CharTypeTree());
    pseudoAsts.put("<character>", new CharLitTree(PseudoProgramTokens.getTestToken("<character>")));
  }

  public static AST get(String typeString) {
    return pseudoAsts.get(typeString);
  }
}
