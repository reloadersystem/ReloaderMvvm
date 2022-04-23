
package pe.lecordonbleu.reloader.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class Codificador {
    
    public byte[] byteObtener(String textoCompreso1){
            String[] byteValues = textoCompreso1.substring(1, textoCompreso1.length() - 1).split(",");
            byte[] bytes = new byte[byteValues.length];

            for (int i = 0, len = bytes.length; i < len; i++) {
                bytes[i] = Byte.parseByte(byteValues[i].trim());
            }
            return bytes;
            }
            
  public static byte[] compress(final String str) throws IOException {
    if ((str == null) || (str.length() == 0)) {
      return null;
    }
    ByteArrayOutputStream obj = new ByteArrayOutputStream();
    GZIPOutputStream gzip = new GZIPOutputStream(obj);
    gzip.write(str.getBytes("UTF-8"));
    gzip.flush();
    gzip.close();
    return obj.toByteArray();
  }

  public static String decompress(final byte[] compressed) throws IOException {
    final StringBuilder outStr = new StringBuilder();
    if ((compressed == null) || (compressed.length == 0)) {
      return "";
    }
    if (isCompressed(compressed)) {
      final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
      final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, "UTF-8"));

      String line;
      while ((line = bufferedReader.readLine()) != null) {
        outStr.append(line);
      }
    } else {
      outStr.append(compressed);
    }
    return outStr.toString();
  }

  public static boolean isCompressed(final byte[] compressed) {
    return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
  }

    public String asciiToHex(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch));
        }

        return hex.toString();
    }

    public String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        try {
            for (int i = 0; i < hexStr.length(); i += 2) {
                String str = hexStr.substring(i, i + 2);
                output.append((char) Integer.parseInt(str, 16));
            }
            return output.toString();
        } catch (Exception e) {
            return "";
        }

    }

    public String encode(String txt) {
        String encoded = new String(Base64.getEncoder().encode(txt.getBytes()));
        return encoded;

    }

    public String decode(String txt) {
        String decoded = new String(Base64.getDecoder().decode(txt));
        return decoded;

    }
}
