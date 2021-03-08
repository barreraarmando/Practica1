/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Rodrigo
 */
public class SPractica1 {

    public static void recibirArchivo(File dirPadre, Socket cl){
        try{
            String ruta_archivos = dirPadre.getAbsolutePath();
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            String nombre = dis.readUTF();
            long tam = dis.readLong();
            System.out.println("Comienza descarga del archivo "+nombre+" de "+tam+" bytes\n");
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(ruta_archivos+"\\"+nombre));
            long recibidos=0;
            int l=0, porcentaje=0;
            while(recibidos<tam){
                byte[] b = new byte[1500];
                l = dis.read(b);
                System.out.println("leidos: "+l);
                dos.write(b,0,l);
                dos.flush();
                recibidos = recibidos + l;
                porcentaje = (int)((recibidos*100)/tam);
                System.out.print("\rRecibido el "+ porcentaje +" % del archivo");
            }//while
            System.out.println("\nArchivo recibido..\n");
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void desplegarArchivos(File f2, PrintWriter pw, String ruta_archivos){
        //Enviando los nombres de los archivos al cliente
        String [] listaNombres = f2.list();
        int tamCarpeta = listaNombres.length;
        pw.println(ruta_archivos);
        pw.println(tamCarpeta);
        pw.flush();

        File[] paths;
        paths = f2.listFiles();

        for(File path:paths) {
           pw.println(path.getName());
           if (path.isFile()){
               pw.println("Archivo");
           }
           if(path.isDirectory()){
               pw.println("Directorio");
           }
           pw.flush();
        }
    }
    
    public static void recibirCarpeta(String nombreCarpetaRecibida, String dirPadre, int cantArchivosCarpeta, Socket cl){
        File f2 = new File(dirPadre+"\\"+nombreCarpetaRecibida+"\\");
        f2.mkdirs();
        f2.setWritable(true);
        System.out.println("CantArchivosCarpeta: "+cantArchivosCarpeta);
        for(int i=1; i<=cantArchivosCarpeta; i++){
            System.out.println("Entro");
            recibirArchivo(f2, cl);
        }
    }
    
    public static void decidirArchivoCarpeta(File f2, Socket cl, BufferedReader br, String ruta_archivos){
        try{
            String opcionRecibida = br.readLine();
            if(opcionRecibida.equals("archivo")){
                recibirArchivo(f2, cl);
            }else{
                if(opcionRecibida.equals("carpeta")){
                    String nombreCarpetaRecibida = br.readLine();
                    int cantArchivosCarpeta = Integer.parseInt(br.readLine());
                    recibirCarpeta(nombreCarpetaRecibida, ruta_archivos, cantArchivosCarpeta, cl);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        try{
            int puerto = 3000;
            ServerSocket s = new ServerSocket(puerto);
            s.setReuseAddress(true);
            File f = new File("");
            String ruta = f.getAbsolutePath();
            String carpeta="archivosServidor";
            String ruta_archivos = ruta+"\\"+carpeta+"\\";
            System.out.println("ruta:"+ruta_archivos);
            File f2 = new File(ruta_archivos);
            f2.mkdirs();
            f2.setWritable(true);
            
            Socket cl = s.accept();
            System.out.println("Cliente conectado desde "+cl.getInetAddress()+":"+cl.getPort());
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            
            desplegarArchivos(f2, pw, ruta_archivos);
            
            while(true){
                String opcionS = br.readLine();
                int opcion = Integer.parseInt(opcionS);
                switch(opcion){
                    case(0):
                        desplegarArchivos(f2, pw, ruta_archivos);
                        break;
                    case(1):
                        decidirArchivoCarpeta(f2, cl, br, ruta_archivos);
                        break;
                    case(2):
                        System.out.println("Caso para descargar archivo");
                        break;
                    case(3):
                        System.out.println("Caso para eliminar archivo");
                        break;   
                }
                if(opcion == 4){
                    break;
                }
            }
            br.close();
            pw.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
