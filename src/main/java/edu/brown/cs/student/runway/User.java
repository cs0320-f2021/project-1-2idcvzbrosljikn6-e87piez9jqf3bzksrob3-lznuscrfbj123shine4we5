package edu.brown.cs.student.runway;

public class User {

  private int user_id;
  private String weight; // String?
  private String bust_size;
  private String height;
  private int age;
  private String body_type;
  private String horoscope;

  public String getWeight() {
    return this.weight;
  }

  public int getUserId() {
    return user_id;
  }

  public String getBustSize() {
    return bust_size;
  }

  public String getHeight() {
    return height;
  }

  public int getAge() {
    return age;
  }

  public String getBodyType() {
    return body_type;
  }

  public String getHoroscope() {
    return horoscope;
  }
}
