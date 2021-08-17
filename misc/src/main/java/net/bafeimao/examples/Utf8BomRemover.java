package net.bafeimao.examples;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("rawtypes")
public class Utf8BomRemover extends DirectoryWalker {

    public static void main(String[] args) throws IOException {
        //ɾ��ָ���ļ����£������ļ��У�����java�ļ���BOM�����������в���Ϊnull��ɾ�������ļ�ͷ��BOM
        new Utf8BomRemover("java").start(new File("D:\\workspace\\InuServer\\src"));
    }

    private String extension = null;

    public Utf8BomRemover(String extension) {
        super();
        this.extension = extension;
    }

    /**
     * ������ĳ���ļ��е�ɸѡ
     */
    @SuppressWarnings("unchecked")
    public void start(File rootDir) throws IOException {
        walk(rootDir, null);
    }

    protected void handleFile(File file, int depth, Collection results) throws IOException {
        if (extension == null
                || extension.equalsIgnoreCase(FilenameUtils.getExtension(file.toString()))) {
            //���þ���ҵ���߼�����ʵ���ﲻ������ʵ��ɾ��BOM�����������ܶ���ɵ����顣
            remove(file);
        }
    }

    /**
     * �Ƴ�UTF-8��BOM
     */
    private void remove(File file) throws IOException {
        byte[] bs = FileUtils.readFileToByteArray(file);
        if (bs[0] == -17 && bs[1] == -69 && bs[2] == -65) {
            byte[] nbs = new byte[bs.length - 3];
            System.arraycopy(bs, 3, nbs, 0, nbs.length);
            FileUtils.writeByteArrayToFile(file, nbs);
            System.out.println("Remove BOM: " + file);
        }
    }
}
