package corridacarros;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author igo
 */
public class Corrida {

    /**
     * @param args the command line arguments
     */
    private int n_carros;
    
    private int altura;
    private int distancia; //430
    private int largura;
    private int espera = 1000;

    private LinkedList<String> Classificacao = new LinkedList<String>();
    private LinkedList<JLabel> Carros = new LinkedList<JLabel>();

    private JFrame janela = new JFrame("Corrida Carros");
    private Container fundo = janela.getContentPane();
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private JButton Start = new JButton("Start!");

    class Carro extends Thread {

        private JLabel Car = null;
        private int Espera = 0;
        private int distancia = 0;

        public Carro(JLabel Car, int espera, int distancia) {
            this.Car = Car;
            this.Espera = espera;
            this.distancia = distancia;
        }

        public void run() {

            // Ver a hora do sistema Parte 1
            Date d_i = new Date();

            double actual = 0;
            int i = 0;

            
                while (actual < distancia) {
                    int espera = (int) (Math.random() * Espera);
                    actual = Car.getLocation().getX();
                    int nova = (int) actual + 10;
                    Car.setLocation(nova, 0);
                    Car.repaint();
		    try {
		         sleep(espera);
		    } catch (InterruptedException ex) {
				    }
                }
           

            // Ver hora do sistema Parte 2
            // Subtrair a Parte 1 à Parte 2 e ver o tempo que o carro demorou a fazer o percurso
            Date d_f = new Date();
            
            double t = (d_f.getTime() - d_i.getTime());
            double tempo = t / 1000;

            //System.out.println(Car.getName() +" -> "+ tempo + " s");
            Classificacao.add(Car.getName()+" - "+tempo+" s");

            if(Classificacao.size() == n_carros){
                String mensagem = "";

                for (int l = 0; l<Classificacao.size();l++)
                    mensagem += (l+1)+"º Lugar - "+Classificacao.get(l)+"\n";
                
                JOptionPane.showMessageDialog(janela, mensagem, "Classificação Final", JOptionPane.INFORMATION_MESSAGE);
                janela.dispose();
            }
        }
    }

    public static void main(String[] args) {
        Corrida C = new Corrida();
        C.inicia();
    }

    private void Conf_Corrida() {

        String NC = JOptionPane.showInputDialog(janela, "Quantos condutores vão correr?", "Configuração da Corrida", JOptionPane.QUESTION_MESSAGE);
        try{
            if(NC.equals("1")){
                NC = "2";
                JOptionPane.showMessageDialog(janela, "Era suposto haver mais do que um condutor.\nA corrida terá "+NC+" condutores.", "Configuração da Corrida", JOptionPane.ERROR_MESSAGE);
            }

            n_carros = Integer.parseInt(NC);
            JOptionPane.showMessageDialog(janela, "Os "+n_carros+" condutores estão concentradíssimos!", "Configuração da Corrida", JOptionPane.INFORMATION_MESSAGE);
        }catch(NumberFormatException e){
            n_carros = 2;
            JOptionPane.showMessageDialog(janela, "Era suposto introduzir o número de condutores.\nA corrida terá "+n_carros+" condutores.", "Configuração da Corrida", JOptionPane.ERROR_MESSAGE);
        }catch (NullPointerException e){
            System.exit(0);
        }

        String Dist = JOptionPane.showInputDialog(janela, "Qual a distância da corrida?\n(Atenção! A distância da corrida deverá ser inferior à resolução do monitor ["+(int) dim.getWidth()+"])\nObs.: A distância minima é 140", "Configuração da Corrida", JOptionPane.QUESTION_MESSAGE);

        try{
            distancia = Integer.parseInt(Dist);

            if(distancia < 140){
                distancia = 140;
                JOptionPane.showMessageDialog(janela, "Erro!\nA distância minima é de 140 metros e será essa a distância desta corrida!" ,"Configuração da Corrida", JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(janela, "A Grande Corrida terá uma distância de "+distancia+" metros!","Configuração da Corrida", JOptionPane.INFORMATION_MESSAGE);
        }catch(NumberFormatException e){
            distancia = 300;
            JOptionPane.showMessageDialog(janela, "Era suposto introduzir um número inteiro para a distância da corrida.\nPor defeito a corrida terá "+distancia+" metros!","Configuração da Corrida", JOptionPane.INFORMATION_MESSAGE);
        }

        JPanel painel_carros = new JPanel();
        painel_carros.setLayout(new GridLayout(n_carros, 1));

        for(int i = 1; i<=n_carros; i++){
           boolean correcto = true;

           int x = ((int) (i%3))+1;

           ClassLoader cl = this.getClass().getClassLoader();
           String popo = "imagens/car"+x+".png";
           URL icon = cl.getResource(popo);
           ImageIcon Imagem;

           if(icon!=null)
            Imagem = new ImageIcon(icon);
           else
            Imagem = new ImageIcon(popo);

           JLabel Car = new JLabel(Imagem);
           String nome = "";

           do{
               correcto = true;
               try{
                nome = JOptionPane.showInputDialog(janela, "Introduz o Nome do Condutor do Carro "+i, "Configuração do Carro "+i, JOptionPane.QUESTION_MESSAGE);
                if(nome.isEmpty())
                    nome = "Condutor "+i;
               }catch(NullPointerException e){
                   nome = "Condutor "+i;
               }

               for(int n = 0; n<Carros.size();n++){

                   if(Carros.get(n).getName().equals(nome)){
                       correcto = false;
                       JOptionPane.showMessageDialog(janela, "Esse condutor já está a conduzir outro carro", "Erro!", JOptionPane.ERROR_MESSAGE);
                   }
                }

               if(Carros.isEmpty())
                   correcto = true;

            }while(!correcto);
           Car.setName(nome);

           Carros.add(Car);

           JPanel Pista = new JPanel();
           Pista.setLayout(new BorderLayout());
           Pista.add(Car, BorderLayout.WEST);

           painel_carros.add(Pista);
           //System.out.println("Carro de "+nome+" Criado!");
        }

        fundo.add(painel_carros, BorderLayout.CENTER);
    }

    private void inicia() {

        fundo.setLayout(new BorderLayout());
        Conf_Corrida();

        altura = 100+(n_carros*15);
        largura = 55+distancia;

        int location_Y = (int) (dim.getHeight() / 2 - altura / 2);
        int location_X = (int) (dim.getWidth() / 2 - largura / 2);

        JPanel painel_controlo = new JPanel();
        painel_controlo.setLayout(new FlowLayout());
        Start.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                for(int t = 0; t<n_carros;t++){
                    Thread Carro = new Carro(Carros.get(t), espera, distancia);
                    Carro.start();
                }
                Start.setEnabled(false);
            }
        });
        painel_controlo.add(Start);

        fundo.add(painel_controlo, BorderLayout.SOUTH);

        janela.setSize(largura, altura);
        janela.setResizable(false);
        janela.setLocation(location_X, location_Y);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setVisible(true);
    }
}
