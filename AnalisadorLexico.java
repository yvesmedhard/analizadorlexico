import java.io.File;
import java.nio.file.Files;
import java.io.IOException;

public class AnalisadorLexico {

  private String[] reservedWords = { "programa", "declare", "escreva", "leia", "inicio" };
  private char[] content;
  private int pos;

  public AnalisadorLexico(String filename) {
    try {
      byte[] bContent = Files.readAllBytes(new File(filename).toPath());
      this.content = new String(bContent).toCharArray();
      this.pos = 0;
    } catch (IOException ex) {

      System.err.println("Erro ao ler arquivo");
    }

  }

  public boolean eof() {
    return pos == content.length;
  }

  private boolean isReservedWord(String text) {
    for (String s : reservedWords) {
      if (text.equals(s)) {
        return true;
      }
    }
    return false;
  }

  private char nextChar() {
    return content[pos++];
  }

  private boolean isLetter(char c) {
    return c >= 'a' && c <= 'z';
  }

  private boolean isBlank(char c) {
    return c == ' ' || c == '\n' || c == '\t' || c == '\r';
  }

  private boolean isPonctuation(char c) {
    return c == ',' || c == '.' || (c == ':' && lookupNext() != '=');
  }

  private boolean isOperator(char c) {
    return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || (c == ':' && lookupNext() == '=');
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private void retroceder() {
    pos--;
  }

  private char lookupNext(){
    return content[pos];
  }

  public Token nextToken() {
    int s = 0;
    String text = "";
    Token token;
    while (true) {
      if (eof()) {
        return new Token(Token.END_OF_FILE, text);
      }
      switch (s) {
      case 0:
        char c = nextChar();
        if (isLetter(c)) {
          s = 1;
          text += c;
        } else if (isOperator(c)) {
          text += c;
          s = 2;
        } else if (isBlank(c)) {
          s = 0;
        } else if (isPonctuation(c)){
          text += c;
          return new Token(Token.PONTUACTION, text);
        } else {
          return null;
        }
        break;
      case 1:
        c = nextChar();
        if (isLetter(c) || isDigit(c)) {
          s = 1;
          text += c;
        } else if (isBlank(c)) {
          if (isReservedWord(text))
            return new Token(Token.RESERVERD_WORD, text);
          else
            return new Token(Token.ID, text);
        } else if (isOperator(c)) {
          retroceder();
          return new Token(Token.ID, text);
        } else if (isPonctuation(c)) {
          retroceder();
          return new Token(Token.ID, text);
        } else {
          return null;
        }
        break;
      case 2:
        return new Token(Token.OPERATOR, text);
      }
    }
  }
}