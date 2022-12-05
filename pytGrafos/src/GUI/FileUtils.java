package GUI;

import static GUI.MainFrm.areaP;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class FileUtils {

    private static File file = null;
    private static BufferedWriter bw = null;
    private static BufferedReader br = null;
    private static FileReader fr = null;

    private static void create_syntax(BufferedWriter bw) throws IOException {
        bw.write("<" + MainFrm.inicio.getText() + " , " + MainFrm.fin.getText() + ">");
        bw.newLine();
        NodosFactory.getListaNodos().forEach(nodo -> {
            try {
                bw.write("*{" + nodo.getID() + "(" + nodo.getLocation().x + "," + nodo.getLocation().y + ")->{");
                bw.newLine();
                nodo.getNodosApuntadores().forEach(apuntador -> {
                    try {
                        bw.write("(" + apuntador + "," + nodo.getCosto(apuntador) + ")");
                        bw.newLine();
                    } catch (IOException ex) {
                    }
                });
                bw.write("}");
                bw.newLine();
            } catch (IOException ex) {
            }
        });
    }

    public static void read_ANB_File(String path) throws FileNotFoundException, IOException {
        fr = new FileReader(path);
        br = new BufferedReader(fr);
        String currentString = "";
        String nodo = "", xy[];
        ArrayList<String> lista = new ArrayList<>();
        while ((currentString = br.readLine()) != null) {
            if (currentString.startsWith("*{")) {
                nodo = StringUtils.Between(currentString, "*{", "->{");
                xy = StringUtils.Between(nodo, "(", ")").split(",");
                MainFrm.areaP.add(NodosFactory.addNodo(StringUtils.Before(nodo, "("), Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
            } else if (currentString.startsWith("(")) {
                lista.add(StringUtils.Before(nodo, "(") + currentString);
            } else if (currentString.startsWith("<")) {
                xy = StringUtils.Between(currentString, "<", ">").split(",");
                MainFrm.inicio.setText(xy[0].replace(" ", ""));
                MainFrm.fin.setText(xy[1].replace(" ", ""));
            }
        }
        lista.forEach(action -> {
            String node = StringUtils.Before(action, "(");
            String array[] = StringUtils.Between(action, "(", ")").split(",");
            NodosFactory.getNodo(node).addApuntadores(array[0], Integer.parseInt(array[1]));
        });
        areaP.updateUI();
        fr.close();
        br.close();
    }

    public static void create_ANB_File(String path) throws IOException {
        if (path.contains(".anb")) {
            file = new File(path);
        } else {
            file = new File(path + ".anb");
        }
        if (file.exists()) {
            int o = JOptionPane.showConfirmDialog(null, "El archivo ya existe.\nDesea remplazarlo?");
            if (o == JOptionPane.OK_OPTION) {
                bw = new BufferedWriter(new FileWriter(file));
                create_syntax(bw);
            }
        } else {
            bw = new BufferedWriter(new FileWriter(file));
            create_syntax(bw);
        }
        bw.close();
    }

}
