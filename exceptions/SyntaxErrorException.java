package exceptions;

import java.util.Arrays;
import java.util.stream.Collectors;

import lexer.daos.TokenKind;

public class SyntaxErrorException extends Exception {
  public SyntaxErrorException(TokenKind actualKind, TokenKind... expectedTokens) {
    super(
        String.format(
            "Syntax error, unexpected token %s (expected %s)",
            actualKind,
            String.join(", ",
                Arrays.stream(expectedTokens).map(token -> token.toString())
                    .collect(Collectors.toList()))));
  }

  public SyntaxErrorException(TokenKind actualKind, TokenKind expectedKind) {
    this(actualKind, Arrays.asList(expectedKind).toArray(new TokenKind[] {}));
  }
}
