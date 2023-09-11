package com.example.productinformation.parser;

public interface Parser<T> {

  /**
   * 주어진 line 에서 특정 데이터를 추출하여 POJO 객체를 만드는데에 활용하는 메서드
   * @param line
   * @return
   */
  T parse(String line);
}
