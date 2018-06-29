package servidor;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadReceive implements Runnable {
    private final ChatServer main;
    private String message; 
    private ObjectInputStream inputStream;
    private Socket client;
   
    
   //Inicializar chatServer y configurar GUI
   public ThreadReceive(Socket client, ChatServer main){
       this.client = client;
       this.main = main;
   }  

    public void mostrarMensaje(String message) {
        main.txtZone.append(message);
    } 
   
    public void run() {
        try {
            inputStream = new ObjectInputStream(client.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ThreadReceive.class.getName()).log(Level.SEVERE, null, ex);
        }
        do { 
            try {//leer el mensaje y mostrarlo 
                message = (String) inputStream.readObject(); //leer nuevo mensaje
                main.printMessage(message);
            } //fin try
            catch (SocketException ex) {
            }
            catch (EOFException eofException) {
                main.printMessage("End of the connection");
                break;
            } //fin catch
            catch (IOException ex) {
                Logger.getLogger(ThreadReceive.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException classNotFoundException) {
                main.printMessage("Unknown object");
            } //fin catch               

        } while (!message.equals("Server>>> TERMINATE")); //Ejecuta hasta que el server escriba TERMINATE

        try {
            inputStream.close();
            client.close(); 
        } //Fin try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } //fin catch

        main.printMessage("End of the connection");
        System.exit(0);
    } 
} 
