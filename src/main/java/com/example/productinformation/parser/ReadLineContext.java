package com.example.productinformation.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReadLineContext<T> { // csv 파일을 읽는 제너릭 클래스

  private Parser<T> parser;
  private List<T> lines;

  public ReadLineContext(Parser<T> parser) {
    this.parser = parser;
    this.lines = new ArrayList<>();
  }

  public List<T> readLines(String filename) throws IOException {
    String str = "";
    BufferedReader reader = null;

    reader = Files.newBufferedReader(Paths.get(filename));

    while ((str = reader.readLine()) != null) {
      try {
        this.lines.add(parser.parse(str));
      } catch (Exception e) {
        System.out.printf("파일 내용: %s 에 문제가 생겨 넘어갑니다.\n", str);
      }
    }

    reader.close();
    return this.lines;
  }

}
