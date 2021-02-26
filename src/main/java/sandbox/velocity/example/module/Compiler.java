package sandbox.velocity.example.module;

import sandbox.velocity.example.vo.RowData;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Compiler {
    private static final String UTF_8 = "UTF8";
    private static String classRootPath = System.getProperty("user.dir");
    private final RowData metadata;

    public Compiler(RowData sourceCodeMetadata) {
        this.metadata = sourceCodeMetadata;

        if(ClassLoader.getSystemResource(".") != null){
            classRootPath = ClassLoader.getSystemResource(".").getPath();
        }
    }

    public Path saveSource(String javaSourceCode) throws IOException {
        String aPackage = getPackage();
        Path sourcePath = Paths.get(aPackage, metadata.getClassName() + ".java");
        Files.write(sourcePath, javaSourceCode.getBytes(UTF_8));
        return sourcePath;
    }

    public Path compileSource(Path javaFile) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, javaFile.toFile().getAbsolutePath());
        return javaFile.getParent().resolve(javaFile.toFile().getName().split("\\.")[0] + ".class");
    }

    public Class<?> getClass(Path javaClass)
            throws MalformedURLException, ClassNotFoundException {
        URL classUrl = javaClass.getParent().toFile().toURI().toURL();
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{classUrl});
        return Class.forName(metadata.getPackageName()+"."+metadata.getClassName(), true, classLoader);
    }

    private String getPackage() throws IOException {
        File aPackage = new File(classRootPath + File.separator + metadata.getPackageName().replace(".", File.separator));
        if(!aPackage.exists()){
            if(!aPackage.mkdir())
                throw new IOException("Failed to create directory :: " + aPackage.getPath());
        }
        return aPackage.getPath();
    }
}
