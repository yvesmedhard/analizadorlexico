class Main {
  public static void main(String[] args) {
    AnalisadorLexico l;
    l = new AnalisadorLexico("prog.in");// new String("abcd + ab123 ab@ ").toCharArray());
    while (!l.eof()) {
      System.out.println(l.nextToken());
    }
  }
}