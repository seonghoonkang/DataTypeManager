package sandbox.velocity.example;

import sandbox.velocity.example.module.Compiler;
import sandbox.velocity.example.module.Transformer;
import sandbox.velocity.example.vo.Field;
import sandbox.velocity.example.vo.RowData;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
public class DataTypeManager {

    private static final String DEFAULT_PACKAGE_NAME = "sandbox.velocity.example.types";

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //-- 1. RowData 생성
        RowData rowData = getRowData();

        //-- 2. Transformer 에서 Java Source Code 생성
        Transformer transformer = new Transformer();
        String javaSourceCode = transformer.generate(rowData);

        //-- 3. Compiler 에서 Java Class 생성
        Compiler compiler = new Compiler(rowData);
        Path sourceLocation = compiler.saveSource(javaSourceCode);

        //-- 4.
        Path classLocation = compiler.compileSource(sourceLocation);


        //--- TEST Code
        Map<String, Object> param = new HashMap<>();
        param.put("setUserId", 1004);
        param.put("setOffice", "bizflow");
        param.put("setAge", 34);
        param.put("setDob", LocalDate.now());
        param.put("setName", "Paul,Kang");

        Class clazz = compiler.getClass(classLocation);
        Object dataObj = clazz.getDeclaredConstructor().newInstance();
        Method methods[] = clazz.getDeclaredMethods();

        for(int i=0;i<methods.length;i++) {
            if(methods[i].getName().contains("set")){
                methods[i].invoke(dataObj, param.get(methods[i].getName()));
            }
        }

        for(int i=0;i<methods.length;i++) {
            if(methods[i].getName().contains("get")){
                System.out.println(methods[i].getName() + " :: " + methods[i].invoke(dataObj));
            }
        }
    }

    private static RowData getRowData() {
        List<Field> properties = new ArrayList<>();
        properties.add(new Field("userId", "int"));
        properties.add(new Field("name", "String"));
        properties.add(new Field("office", "String"));
        properties.add(new Field("age", "int"));
        properties.add(new Field("dob", "java.time.LocalDate"));

        RowData rowData = new RowData();
        rowData.setPackageName(DEFAULT_PACKAGE_NAME);
        rowData.setClassName("UserProfile");
        rowData.setProperties(properties);
        return rowData;
    }
}
