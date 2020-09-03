package Utils;

import java.io.*;

public class Utils
{
    /**
     * Deserializa o array de bytes  recebido para um objeto
     * @param yourBytes bytes a serem deserializados
     * @return objeto deserializado
     * @throws IOException
     */
    public static Object deserializeObject(byte[] yourBytes) throws IOException
    {
        Object result = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            result = in.readObject();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return result;
    }

    /**
     * Serializa para bytes o objeto recebido como parâmetro.
     * @param obj a ser serializado
     * @return array de bytes correspodente ao objeto serializado
     * @throws IOException
     */
    public static byte[] serializeObject(Object obj) throws IOException
    {
        byte[] yourBytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            yourBytes = bos.toByteArray();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return yourBytes;
    }
}
