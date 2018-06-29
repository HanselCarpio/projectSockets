package servidor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class ChatServer extends JFrame{
    public JTextField txtField; 
    public JTextArea txtZone; 
    private static ServerSocket server; //
    private static Socket connection; 
    
    public static ChatServer main; 
    
    public ChatServer(){
        super("Server"); 
        
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
    
    //Para mostrar texto en displayArea
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
        ChatServer main = new ChatServer(); 
        main.setLocationRelativeTo(null);  
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ExecutorService executor = Executors.newCachedThreadPool(); 
 
        try {
            //main.mostrarMensaje("No se encuentra Servidor");
            server = new ServerSocket(5100, 100); 
            main.printMessage("Wait Client...");

            
            while (true){
                try {
                    connection = server.accept();       

                    //main.mostrarMensaje("Conexion Establecida");
                    main.printMessage("Connecting to : " + connection.getInetAddress().getHostName());

                    main.enableText(true); 

                    //Ejecucion de los threads
                    executor.execute(new ThreadReceive(connection, main)); //client
                    executor.execute(new ThreadSend(connection, main));
                } catch (IOException ex) {
                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } //Fin del catch //Fin del catch
        finally {
        }
        executor.shutdown();
    }
}
