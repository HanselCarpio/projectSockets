package servidor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
        
public class ThreadSend implements Runnable {
    private final ChatServer main; 
    private ObjectOutputStream outputStream;
    private String message;
    private Socket connection; 
   
    public ThreadSend(Socket connection, final ChatServer main){
        this.connection = connection;
        this.main = main;
        
        //Evento que ocurre al escribir en el areaTexto
        main.txtField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                message = event.getActionCommand();
                sendData(message); 
                main.txtField.setText(""); 
            } //Fin metodo actionPerformed
        } 
        );//Fin llamada a addActionListener
    } 
    
   //enviar objeto a cliente 
   private void sendData(String message){
      try {
         outputStream.writeObject("Server>>> " + message);
         outputStream.flush(); 
         main.printMessage("Server>>> " + message);
      } //Fin try
      catch (IOException ioException){ 
         main.printMessage("Error typing message");
      } //Fin catch  
      
   } //Fin methodo enviarDatos

   //manipula areaPantalla en el hilo despachador de eventos
    public void mostrarMensaje(String message) {
        main.txtZone.append(message);
    } 
   
    public void run() {
         try {
            outputStream = new ObjectOutputStream(connection.getOutputStream());
            outputStream.flush(); 
        } catch (SocketException ex) {
        } catch (IOException ioException) {
          ioException.printStackTrace();
        } catch (NullPointerException ex) {
        }
    }   
   
} 
