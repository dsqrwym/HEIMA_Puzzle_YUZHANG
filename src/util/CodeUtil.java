package util;
import java.util.Random;
public class CodeUtil {
    private CodeUtil() {
    }

    public static String getCode(){
        //1.创建一个数组
        char[] caracteres = new char[52];//52  索引的范围：0 ~ 51
        //2.添加字母 a - z  A - Z
        for (byte i = 0; i < 26; i++) {
            caracteres[i] = (char)('a' + i);//a - z
            caracteres[i+26] = (char)('A' + i);//A - Z
        }
        //3.生成4个随机字母
        StringBuilder resultado = new StringBuilder();//StringBuilder会更高效，避免了不必要的内存分配和垃圾回收。
        Random aleatorio = new Random();
        for (byte i = 0; i < 4; i++) {
            resultado.append(caracteres[(byte)aleatorio.nextInt(caracteres.length)]);
        }
        //4.在后面拼接数字 0~9
        //5.把随机数字拼接到result的后面
        resultado.append((byte)aleatorio.nextInt(10));
        //System.out.println(result);//ABCD5
        //6.把字符串变成字符数组
        caracteres = resultado.toString().toCharArray();//[A,B,C,D,5]
        //7.拿着索引上的数字，跟随机索引上的数字进行交换 --> 洗牌。
        for (byte i = (byte)(caracteres.length-1); i > 0; i--){
            byte posi = (byte)aleatorio.nextInt(i+1);
            char aux = caracteres[i];
            caracteres[i] = caracteres[posi];
            caracteres[posi] = aux;
        }
        //8.把字符数组再变回字符串
        resultado = new StringBuilder(new String(caracteres));
        return resultado.toString();
    }
}
