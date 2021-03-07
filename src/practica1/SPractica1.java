/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Rodrigo
 */
public class SPractica1 {

    /**
     * @param args the command line arguments
     */
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
            
            //Enviando los nombres de los archivos al cliente
            String [] listaNombres = f2.list();
            int tamCarpeta = listaNombres.length;
            pw.println(ruta_archivos);
            pw.println(tamCarpeta);
            pw.flush();
            
            File[] paths;
            paths = f2.listFiles();
         
            for(File path:paths) {
                System.out.println("Envio nombres");
               pw.println(path.getName());
               if (path.isFile()){
                   pw.println("Archivo");
               }
               if(path.isDirectory()){
                   pw.println("Directorio");
               }
               pw.flush();
            }
            
            while(true){
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
