package cliente;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
        
public class ThreadSend implements Runnable {
    private final ChatClient main; 
    private ObjectOutputStream outputStream;
    private String message;
    private Socket connection; 
   
    public ThreadSend(Socket connection, final ChatClient main){
        this.connection = connection;
        this.main = main;
        
        //Evento que ocurre al escribir en el campo de texto
        main.txtField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                message = event.getActionCommand();
                sendData(message); 
                main.txtField.setText(""); 
            } //Fin metodo actionPerformed
        } 
        );
    } 
    
   //enviar objeto a cliente 
   private void sendData(String message){
      try {
         outputStream.writeObject("Client>>> " + message);
         outputStream.flush(); 
         main.printMessage("Client>>> " + message);
      } //Fin try
      catch (IOException ioException){ 
         main.printMessage("Error typing message");
      } //Fin catch  
      
   } 

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
