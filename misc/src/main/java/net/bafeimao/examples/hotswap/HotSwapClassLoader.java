package net.bafeimao.examples.hotswap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Administrator on 2015/10/19.
 */
public class HotSwapClassLoader extends ClassLoader {
    /** 加载的类文件根目录目录,存放加载的.class文件 */
    private URI reloadBaseDir;
    /** 加载的类名集合，Full-Name，不在此集合的类委托给系统加载器来完成 */
    private Set<String> reloadClazzs;

    public HotSwapClassLoader(URI loadDir) {
        // 指定父加载器为null
        super(null);

        this.reloadBaseDir = loadDir;

        reloadClazzs = new HashSet<String>();
    }

    /**
     * 指定加载的类
     *
     * <p>
     * 通过根目录+Full-Name找到.class
     *
     * @param clazzNames
     */
    public void assignLoadedClazzs(String... clazzNames) {
        for (String clazzName : clazzNames) {
            defineClassFromPath(getLoadedClassPath(clazzName), clazzName);
        }

        // 添加至加载的集合
        reloadClazzs.addAll(Arrays.asList(clazzNames));
    }

    /**
     * 根据类名获取所在路径
     *
     * @param clazzName
     * @return
     */
    private Path getLoadedClassPath(String clazzName) {
        String pathName = clazzName.replace('.', File.separatorChar);
        String classPathName = pathName + ".class";

        return Paths.get(reloadBaseDir).resolve(classPathName);
    }

    /**
     * 从指定的Path接收类字节码->转换为Class实例
     *
     * @param path
     * @param clazzFullName
     * @return
     */
    private Class<?> defineClassFromPath(Path path, String clazzFullName) {
        File classFile = path.toFile();
        int fileLength = (int) classFile.length();

        byte[] rawBytes = new byte[fileLength];

        try {
            InputStream in = Files.newInputStream(path);
            in.read(rawBytes);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Converts an array of bytes into an instance of class Class. Before
        // the Class can be used it must be resolved.
        return defineClass(clazzFullName, rawBytes, 0, fileLength);
    }

    // Loads the class
    // 本类加载器只加载reloadClazzs中的类，其余的类委托给系统类加载器进行加载
    // Foo的接口类IFoo加载也会调用此方法，不过其实用系统类加载器进行加载的
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = null;

        // 每个类加载器都维护有自己的一份已加载类名字空间,其中不能出现两个同名的类。
        // 凡是通过该类加载器加载的类，无论是直接的还是间接的，都保存在自己的名字空间.
        // 该方法即在该名字空间中寻找指定的类是否存在，如果存在旧返回给类的引用；
        // 否则就返回null;这里的直接是指存在于该类加载器的加载路径上并由该加载器完成加载,
        // 间接是指由类加载器把类的加载工作委托给其他类加载器完成类的实际加载

        // landon:1.因在loadClass之前，我们调用了{@link #defineClassFromPath}->通过调用{@link
        // #defineClass}->已经实现了将reloadClazzs中的class的装载工作->所以如果通过loadClass方法加载的class是reloadClazzs中的，
        // 则通过findLoadedClass方法可直接获取
        // 2.当然还有一种做法是直接覆盖findClass方法，在该方法中调用defineClass方法.而findClass的调用时机则是在没有找到加载的类时调用的.
        // 其默认实现是直接抛出一个ClassNotFoundException
        clazz = findLoadedClass(name);

        if (!this.reloadClazzs.contains(name) && clazz == null) {
            clazz = getSystemClassLoader().loadClass(name);
        }

        if (clazz == null) {
            throw new ClassNotFoundException(name);
        }

        if (resolve) {
            // Links the specified class
            resolveClass(clazz);
        }

        return clazz;
    }
}