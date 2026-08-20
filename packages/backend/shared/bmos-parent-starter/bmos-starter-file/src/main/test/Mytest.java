import com.aspose.words.License;
import org.junit.Test;


import java.io.FileOutputStream;
import java.io.InputStream;

public class Mytest {
    @Test
    public void modifyWordsJar() throws Exception {

        InputStream is = Mytest.class.getClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);

        String sourceFile = "C:\\Users\\Administrator\\Desktop\\QMS.PR7.3-CI-SOP001-R01-00  胶原蛋白植入剂生产记录（已处理）.docx";//输入的文件
        String targetFile = "D:\\111.pdf";//输出的文件

        try {
            long old = System.currentTimeMillis();
            FileOutputStream os = new FileOutputStream(targetFile);
            com.aspose.words.Document doc = new com.aspose.words.Document(sourceFile);
            doc.save(os, com.aspose.words.SaveFormat.PDF);
            os.close();
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
