package sandbox.velocity.example.vo;

import java.io.Serializable;
import java.util.List;
public class RowData implements Serializable {
    private String packageName;
    private String className;
    private List<Field> properties;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Field> getProperties() {
        return properties;
    }

    public void setProperties(List<Field> properties) {
        this.properties = properties;
    }
}
