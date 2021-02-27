package sandbox.velocity.example;

import org.junit.jupiter.api.*;
import sandbox.velocity.example.module.Compiler;
import sandbox.velocity.example.module.Transformer;
import sandbox.velocity.example.vo.Field;
import sandbox.velocity.example.vo.RowData;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestDataTypeManager {
    private static String javaSourceCode;
    private static Class clazz;

    private RowData rowData = new RowData();
    private Map<String, Object> testData = new HashMap<>();

    @BeforeEach
    public void init() {
        //-- Data Type Metadata
        String packageName = "sandbox.velocity.example.types";
        String className = "UserProfile";
        List<Field> properties = new ArrayList<>();
        properties.add(new Field("userId", "int"));
        properties.add(new Field("name", "String"));
        properties.add(new Field("office", "String"));
        properties.add(new Field("age", "int"));
        properties.add(new Field("dob", "java.time.LocalDate"));

        //-- input DATA
        rowData.setPackageName(packageName);
        rowData.setClassName(className);
        rowData.setProperties(properties);

        //-- Output DATA
        testData.put("userId", 1004);
        testData.put("office", "BizFlow Corp.");
        testData.put("age", 34);
        testData.put("dob", LocalDate.now());
        testData.put("name", "Paul,Kang");
    }

    @Test
    @Order(1)
    public void transformerTest() {
        Transformer transformer = new Transformer();
        javaSourceCode = transformer.generate(rowData);
        assertNotEquals(javaSourceCode, null);
    }

    @Test
    @Order(2)
    public void compilerTest() throws IOException, ClassNotFoundException {
        Compiler compiler = new Compiler(rowData);
        Path sourceLocation = compiler.saveSource(javaSourceCode);
        assertEquals(sourceLocation.getFileName().toString(), rowData.getClassName() + ".java");

        Path classLocation = compiler.compileSource(sourceLocation);
        clazz = compiler.getClass(classLocation);
        assertEquals(rowData.getPackageName() + "." + rowData.getClassName(), clazz.getName());

    }

    @Test
    @Order(3)
    public void dataStorageTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Object dataObj = clazz.getDeclaredConstructor().newInstance();
        Method methods[] = clazz.getDeclaredMethods();

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().contains("set")) {
                String fieldName = lowerCamelCase(methods[i].getName().substring(3));
                methods[i].invoke(dataObj, testData.get(fieldName));
            }
        }

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().contains("get")) {
                String fieldName = lowerCamelCase(methods[i].getName().substring(3));
                assertEquals(testData.get(fieldName), methods[i].invoke(dataObj));
                System.out.println(methods[i].getName() + " :: " + methods[i].invoke(dataObj));
            }
        }

    }

    private static String lowerCamelCase(String name) {
        char[] arr = name.toCharArray();
        arr[0] = Character.toLowerCase(arr[0]);
        return new String(arr);
    }
}
