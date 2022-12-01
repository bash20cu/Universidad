/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author MSaqib
 */
public class NewJFrame extends javax.swing.JFrame {
    
public static int graph[][];  
public static int size;
    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
    }
 // algorithm for a graph represented using adjacency matrix
    // representation
    
    int minDistance(int dist[], Boolean sptSet[])
    {
        // Initialize min value
        int min = Integer.MAX_VALUE, min_index=-1;
 
        for (int v = 0; v < size; v++)
            if (sptSet[v] == false && dist[v] <= min)
            {
                min = dist[v];
                min_index = v;
            }
 
        return min_index;
    }
 
    // A utility function to print the constructed distance array
    void printSolution(int dist[], int n)
    {
        System.out.println("Vertex   Distance from Source");
        for (int i = 0; i < size; i++)
            System.out.println(i+" \t\t "+dist[i]);
    }
    void dijkstra(int graph[][], int src,int dstn)
    {
        NewJPanel.SRC = src;
        NewJPanel.DSTN = dstn;
        int dist[] = new int[size]; // The output array. dist[i] will hold
                                 // the shortest distance from src to i
 
        // sptSet[i] will true if vertex i is included in shortest
        // path tree or shortest distance from src to i is finalized
        Boolean Set[] = new Boolean[size];
        ArrayList<Integer> permanentOrder = new ArrayList<>();
        
        // Initialize all distances as INFINITE and stpSet[] as false
        for (int i = 0; i < size; i++)
        {
            dist[i] = Integer.MAX_VALUE;
            Set[i] = false;
        }
 
        // Distance of source vertex from itself is always 0
        dist[src] = 0;
 
        // Find shortest path for all vertices
        for (int count = 0; count < size-1; count++)
        {
            // Pick the minimum distance vertex from the set of vertices
            // not yet processed. u is always equal to src in first
            // iteration.
            int u = minDistance(dist, Set);
                System.out.println("u = "+u);
            // Mark the picked vertex as processed
            Set[u] = true;
            if(!Set[dstn])
                permanentOrder.add(u);
            // Update dist value of the adjacent vertices of the
            // picked vertex.
            for (int v = 0; v < size; v++)
 
                // Update dist[v] only if is not in sptSet, there is an
                // edge from u to v, and total weight of path from src to
                // v through u is smaller than current value of dist[v]
                if (!Set[v] && graph[u][v]!=0 &&
                        dist[u] != Integer.MAX_VALUE &&
                        dist[u]+graph[u][v] < dist[v])
                    dist[v] = dist[u] + graph[u][v];
        }
 
        // print the constructed distance array
        printSolution(dist, size);
        for(int i=0;i<size;i++)  
            System.out.println(Set[i]);
        if(dist[dstn]!=Integer.MAX_VALUE)
           jTextField1.setText(Integer.toString(dist[dstn]));
        else{
            JFrame frame  = new JFrame();
           frame.setSize(30,20);
           frame.setLocation(50,50); 
         JOptionPane.showMessageDialog(frame,"Source and Destination are not connected","Error",
                 JOptionPane.ERROR_MESSAGE);
         jTextField1.setText("");
         return;
        }
        permanentOrder.add(dstn);
        findLines(permanentOrder,dist);
        for(int i=0;i<permanentOrder.size();i++){
            System.out.println("order"+permanentOrder.get(i));
            //NewJPanel.nodes.get(permanentOrder.get(i)).inPath = true;
        }
        NewJPanel.nodes.get(src).inPath = true;
    }
    void findLines(ArrayList<Integer> po,int distance[]){
         int n1,n2;
            n1 = po.size()-1;
            n2 = n1-1;
            int m = 1;
            boolean found = false;
        for(int i=0;i<po.size()-1;i++){
            System.out.println("n1"+n1+" "+"n2"+n2);
            for(int j=0;j<NewJPanel.lines.size();j++){
                System.out.println("SRc"+NewJPanel.lines.get(j).src+" DSTN"+NewJPanel.lines.get(j).dstn);
                if(((NewJPanel.lines.get(j).dstn == po.get(n1)+65 && NewJPanel.lines.get(j).src == po.get(n2)+65)
                    || (NewJPanel.lines.get(j).src == po.get(n1)+65 && NewJPanel.lines.get(j).dstn == po.get(n2)+65))
                        &&(distance[po.get(n1)]-distance[po.get(n2)] == NewJPanel.lines.get(j).weight)){
                    NewJPanel.lines.get(j).toHighlight = true;
                    found = true;
                    System.out.println("Found");
                    for(int k=0;k<NewJPanel.nodes.size();k++){
                        if(NewJPanel.nodes.get(k).name == NewJPanel.lines.get(j).src )
                            NewJPanel.nodes.get(k).inPath = true;
                         if(NewJPanel.nodes.get(k).name == NewJPanel.lines.get(j).dstn)
                            NewJPanel.nodes.get(k).inPath = true;
                    }
                    break;
                }
            }
            if(found){
                n1 -= m;
                    n2 -= 1;
                    m = 1;
                    found  =  false;
            }else{
                 m++;
                    n2 -= 1;
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        newJPanel1 = new dj.NewJPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dijkstra Algorithm");
        setResizable(false);

        newJPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Source");

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jComboBox1.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setText("Destination");

        jComboBox2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jComboBox2.setEnabled(false);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton1.setText("CALCULATE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton2.setText("CLEAR");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setText("Distance ");

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(newJPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1284, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(newJPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
       for(int i=0;i<NewJPanel.lines.size();i++){
           NewJPanel.lines.get(i).toHighlight = false;
           NewJPanel.lines.get(i).isHighlighted = false;
       }
       for(int i=0;i<NewJPanel.nodes.size();i++){
           NewJPanel.nodes.get(i).inPath = false;
       }
        createGraph();
       NewJPanel.found = true;
       repaint();
       
       
        //System.out.println(NewJPanel.lines.get(0).dstn);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        NewJPanel.lines.removeAll(NewJPanel.lines);
         NewJPanel.nodes.removeAll(NewJPanel.nodes);
         NewJPanel.Name = 'A';
        jTextField1.setText("");
        jComboBox1.removeAllItems();
        jComboBox2.removeAllItems();
        jComboBox2.setEnabled(false);
        jComboBox1.setEnabled(false);
        repaint();
    }//GEN-LAST:event_jButton2ActionPerformed
void createGraph(){
    size = NewJPanel.nodes.size();
    //System.out.println(size);
    graph = new int[size][size];
    for(int i=0;i<size;i++){
        for(int j=0;j<size;j++){
                graph[i][j] = 0;
        }
    }
    for(int i=0;i<size;i++){
        System.out.println(NewJPanel.nodes.get(i).info.size()+".");
        for(int j=0;j<NewJPanel.nodes.get(i).info.size();j++){
          graph[i][NewJPanel.nodes.get(i).info.get(j).toNode] = NewJPanel.nodes.get(i).info.get(j).lineNo;
          //System.out.println(NewJPanel.nodes.get(i).info.get(j).lineNo);
          //System.out.println(NewJPanel.nodes.get(i).info.get(j).toNode);
        }
    }
    for(int i=0;i<size;i++){
        for(int j=0;j<size;j++){
            System.out.print(graph[i][j]+" ");
        }
        System.out.println("");
    }
    dijkstra(graph, jComboBox1.getSelectedItem().toString().charAt(0)-65,jComboBox2.getSelectedItem().toString().charAt(0)-65);
}
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NewJFrame myFrame = new NewJFrame();
                        myFrame.setVisible(true);
                        
                myFrame.setExtendedState(myFrame.getExtendedState() | NewJFrame.MAXIMIZED_BOTH);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    public static javax.swing.JComboBox<String> jComboBox1;
    public static javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    public static javax.swing.JTextField jTextField1;
    private dj.NewJPanel newJPanel1;
    // End of variables declaration//GEN-END:variables
}
