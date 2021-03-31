package com.tomgs.core.base;

import java.util.Objects;

/**
 * @author tomgs
 * @since 2021/3/30
 */
public class EqualsHashCodeTest {

  private String a;

  private String b;

  public String getA() {
    return a;
  }

  public void setA(String a) {
    this.a = a;
  }

  public String getB() {
    return b;
  }

  public void setB(String b) {
    this.b = b;
  }

  @Override
  public String toString() {
    return "Test{" +
        "a='" + a + '\'' +
        ", b='" + b + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EqualsHashCodeTest test = (EqualsHashCodeTest) o;
    return Objects.equals(a, test.a) &&
        Objects.equals(b, test.b);
  }

  @Override
  public int hashCode() {
    return Objects.hash(a, b);
  }

  public static void main(String[] args) {
    EqualsHashCodeTest test = new EqualsHashCodeTest();
    EqualsHashCodeTest test1 = new EqualsHashCodeTest();

    System.out.println("cmp: " + (test == test1));
    System.out.println("cmp: " + (test.equals(test1)));
    System.out.println(test.hashCode());
    System.out.println(test1.hashCode());
  }

}
