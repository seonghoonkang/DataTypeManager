package sandbox.velocity.example.types;
import java.io.Serializable;
import java.util.List;
public class UserProfile implements Serializable {
/** Serial Version UID. */
private static final long serialVersionUID = 1L;
private int userId;
private String name;
private String office;
private int age;
private java.time.LocalDate dob;
public int getUserId() {
return this.userId;
}
public void setUserId(int userId) {
this.userId = userId;
}
public String getName() {
return this.name;
}
public void setName(String name) {
this.name = name;
}
public String getOffice() {
return this.office;
}
public void setOffice(String office) {
this.office = office;
}
public int getAge() {
return this.age;
}
public void setAge(int age) {
this.age = age;
}
public java.time.LocalDate getDob() {
return this.dob;
}
public void setDob(java.time.LocalDate dob) {
this.dob = dob;
}
}