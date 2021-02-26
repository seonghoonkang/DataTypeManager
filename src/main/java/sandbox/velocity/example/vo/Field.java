package sandbox.velocity.example.vo;

public class Field {
    private String fieldName;
    private String fieldType;


    public Field(String fieldName, String fieldType) {
        super();
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getGetterAndSetterField(){
        char[] arr = this.fieldName.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }
}
