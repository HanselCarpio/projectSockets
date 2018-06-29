package cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**Clase que se encarga de correr los threads de enviar y recibir texto
 * y de crear la interfaz grafica.
 * 
 * @author hansel
 */
public class ChatClient extends JFrame{
    public JTextField txtField; 
    public JTextArea txtZone; 
    private static ServerSocket server; 
    private static Socket client; 
    
    public static ChatClient main; 
    
    public ChatClient(){
        super("Client"); 
 
        txtField = new JTextField();
        txtField.setEditable(false);
        add(txtField, BorderLayout.NORTH);
        
        
        txtZone = new JTextArea();
        txtZone.setEditable(false);
        add(new JScrollPane(txtZone), BorderLayout.CENTER);
        txtZone.setBackground(Color.CYAN);
        txtZone.setForeground(Color.BLACK);
        txtField.setForeground(Color.BLACK);
       
        
        //Crea menu Archivo y submenu Salir, ademas agrega el submenu al menu
        JMenu mOptions = new JMenu("Options"); 
        JMenuItem miExit = new JMenuItem("Exit");
        mOptions.add(miExit); 
        
        JMenuBar mbBar = new JMenuBar();
        setJMenuBar(mbBar); 
        mbBar.add(mOptions);
        
        //Accion que se realiza cuando se presiona el submenu Salir
        miExit.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); 
                }
        });
        
        setSize(300, 520); 
        setVisible(true); 
    }
    
    
    public void printMessage(String message) {
        txtZone.append(message + "\n");
    } 
    public void enableText(boolean editable) {
        txtField.setEditable(editable);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ChatClient main = new ChatClient(); 
        main.setLocationRelativeTo(null);   
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        ExecutorService executor = Executors.newCachedThreadPool(); 
 
        try {
            main.printMessage("Searching Server...");
            client = new Socket(InetAddress.getLocalHost(), 5100); //comunicarme con el servidor
            main.printMessage("Connecting to :" + client.getInetAddress().getHostName());
    
            main.enableText(true); 
            
            //Ejecucion de los Threads
            executor.execute(new ThreadReceive(client, main));
            executor.execute(new ThreadSend(client, main)); 
            
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        } //Fin del catch //Fin del catch
        finally {
        }
        executor.shutdown();
    }
}
