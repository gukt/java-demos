package demos.demo2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * NetworkClassLoader class
 *
 * @author https://github.com/gukt
 */
public class NetworkClassLoader extends ClassLoader {

    private final String url;

    public NetworkClassLoader(String url) {
        // 设置 parent 为 ExtClassLoader
        // NOTE: getSystemClassLoader() 返回的是 AppClassLoader，而 AppClassLoader 的 parent 是 ExtClassLoader
        super(getSystemClassLoader().getParent());
        // 也可以设为 null，那么就会默认使用 AppClassLoader 作为它的 parent
        // super(null);
        this.url = url;
    }

    /**
     * 重写 findClass 方法，一般不建议重写 loadClass 方法，
     * 请见 {@link java.net.URLClassLoader#loadClass(String, boolean) URLClassLoader#loadClass} 方法注释。
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz;
        byte[] content = download(name);
        if (content == null) {
            throw new ClassNotFoundException("url: " + url + ", name: " + name);
        }
        clazz = defineClass(name, content, 0, content.length);
        return clazz;
    }

    private byte[] download(String name) {
        String url = this.url + (this.url.endsWith("/") ? "" : "/");
        url += name.replace(".", "/") + ".class";
        System.out.println("Downloading: " + url + " from the '" + this + "' class loader.");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            byte[] bytes = new byte[4096]; // Or whatever size you want to read in at a time.
            int len;
            while ((len = is.read(bytes)) > 0) {
                baos.write(bytes, 0, len);
            }
            System.out.println("Downloaded, content size: " + baos.size());
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.printf("Failed while reading bytes from %s: %s", url, e.getMessage());
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // private byte[] getClassData(String name) {
    //     InputStream is = null;
    //     try {
    //         String path = name + "/" + name.replace(".", "/") + ".class";
    //         URL url = new URL(path);
    //         byte[] buff = new byte[1024 * 4];
    //         int len;
    //         is = url.openStream();
    //         ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //         while ((len = is.read(buff)) != -1) {
    //             baos.write(buff, 0, len);
    //         }
    //         return baos.toByteArray();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     } finally {
    //         if (is != null) {
    //             try {
    //                 is.close();
    //             } catch (IOException e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     }
    //     return null;
    // }
}
