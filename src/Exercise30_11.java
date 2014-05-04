
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Exercise30_11 extends JApplet {
    // Create the initial board

    private InitialNodePanel initialNodePanel = new InitialNodePanel();
    private JButton jbtSolve = new JButton("Solve");
    private JButton jbtStartOver = new JButton("Start Over");
    // solutionPanel holds a sequence of panels for displaying nodes
    private JPanel solutionPanel =
            new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    private NineTailModel model = new NineTailModel();

    
    public Exercise30_11() {
        // Place solutionPanel in a scroll pane
        solutionPanel.add(initialNodePanel);
        add(new JScrollPane(solutionPanel), BorderLayout.CENTER);

        // buttonPanel holds two buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(jbtSolve);
        buttonPanel.add(jbtStartOver);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listener for the Solve button
        jbtSolve.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                solutionPanel.removeAll();
                
                // Get a shortest path
                java.util.List<Integer> list =
                        model.getShortestPath(NineTailModel.getIndex(
                        initialNodePanel.getNode()));

                // var for previous index 
                int previous = -1;

                // Display nodes in the shortest path
                for (int nodeIndex : list) {
                    // if flipped, color set to red
                    if (previous != -1) {
                        solutionPanel.add(
                                new NodePanel(NineTailModel.getNode(nodeIndex), NineTailModel.getNode(previous)));
                        // else, if not flipped color stays black
                    } else {
                        solutionPanel.add(
                                new NodePanel(NineTailModel.getNode(nodeIndex)));
                    }
                    for (char x : NineTailModel.getNode(nodeIndex)) {
                    }
                    previous = nodeIndex;
                }

                solutionPanel.revalidate();
            }
        });

        // Listener for the Start Over button
        jbtStartOver.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                solutionPanel.removeAll();
                solutionPanel.add(initialNodePanel); // Display initial node
                solutionPanel.repaint();
            }
        });
    }

    /**
     * An inner class for displaying a node on a panel
     */
    static class NodePanel extends JPanel {

        public NodePanel(char[] node) {
            this.setLayout(new GridLayout(3, 3));
            // error fixed, displayed correctly
            for (int i = 0; i < 9; i++) {
                add(new Cell(node[i] + ""));
            }
        }

        public NodePanel(char[] node, char[] prevNode) {
            this.setLayout(new GridLayout(3, 3));

            for (int i = 0; i < 9; i++) {
                if (node[i] != prevNode[i]) {
                    // change flipped to red
                    add(new Cell(node[i] + "")).setForeground(Color.red);
                } else {
                    add(new Cell(node[i] + ""));
                }
            }
        }
    }

    /**
     * An inner class for displaying a cell
     */
    static class Cell extends JLabel {

        public Cell(String s) {
            this.setBorder(new LineBorder(Color.black, 1)); // Cell border
            this.setHorizontalAlignment(JLabel.CENTER);
            this.setFont(new Font("TimesRoman", Font.BOLD, 20));

            setText(s);
        }
    }

    /**
     * An inner class for displaying the initial node
     */
    static class InitialNodePanel extends JPanel {
        // Each cell represents a coin, which can be flipped

        ClickableCell[][] clickableCells = new ClickableCell[3][3];

        public InitialNodePanel() {
            this.setLayout(new GridLayout(3, 3));

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    add(clickableCells[i][j] = new ClickableCell("H"));
                }
            }
        }

        /**
         * Get an array of characters for a node from a GUI node
         */
        
        
        public char[] getNode() {
            char[] node = new char[9];
            int k = 0; /// ++++  Error Here ++++
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {

                    node[k] = clickableCells[i][j].getText().charAt(0);
                    k++;
                }
            }

            return node;
        }
    }

    /**
     * An inner class for displaying a click-able cell
     */
    static class ClickableCell extends Cell {

        public ClickableCell(String s) {
            super(s);

            addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    setBackground(Color.red);
                    setOpaque(true);
                    if (getText().equals("H")) {

                        setText("T"); // Flip from H to T

                    } else {

                        setText("H"); // Flip from T to H
                    }
                }
            });
        }
    }

    /**
     * This main method enables the applet to run as an application
     */
    public static void main(String[] args) {
        // Create a frame
        JFrame frame = new JFrame("Nine Tail Problem");

        // Create an instance of the applet
        Exercise30_11 applet = new Exercise30_11();

        // Add the applet instance to the frame
        frame.add(applet, BorderLayout.CENTER);

        // Display the frame
        frame.setSize(500, 280);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class NineTailModel {

    public final static int NUMBER_OF_NODES = 512;
    protected AbstractGraph<Integer>.Tree tree; // Define a tree

    /**
     * Construct a model
     */
    public NineTailModel() {
        // Create edges
        List<AbstractGraph.Edge> edges = getEdges();

        // Create a graph
        UnweightedGraph<Integer> graph = new UnweightedGraph<Integer>(
                edges, NUMBER_OF_NODES);

        // Obtain a BSF tree rooted at the target node
        tree = graph.bfs(511);
    }

    /**
     * Create all edges for the graph
     */
    private List<AbstractGraph.Edge> getEdges() {
        List<AbstractGraph.Edge> edges =
                new ArrayList<AbstractGraph.Edge>(); // Store edges

        for (int u = 0; u < NUMBER_OF_NODES; u++) {
            for (int k = 0; k < 9; k++) {
                char[] node = getNode(u); // Get the node for vertex u
                if (node[k] == 'H') {
                    int v = getFlippedNode(node, k);
                    // Add edge (v, u) for a legal move from node u to node v
                    edges.add(new AbstractGraph.Edge(v, u));
                }
            }
        }

        return edges;
    }

    public static int getFlippedNode(char[] node, int position) {
        int row = position / 3;
        int column = position % 3;

        flipACell(node, row, column);
        flipACell(node, row - 1, column);
        flipACell(node, row + 1, column);
        flipACell(node, row, column - 1);
        flipACell(node, row, column + 1);

        return getIndex(node);
    }

    public static void flipACell(char[] node, int row, int column) {

        if (row >= 0 && row <= 2 && column >= 0 && column <= 2) {
            // Within the boundary
            if (node[row * 3 + column] == 'H') {
                node[row * 3 + column] = 'T'; // Flip from H to T
            } else {
                node[row * 3 + column] = 'H'; // Flip from T to H
            }
        }
    }

    public static int getIndex(char[] node) {
        int result = 0;

        for (int i = 0; i < 9; i++) {
            if (node[i] == 'T') {
                result = result * 2 + 1;
            } else {
                result = result * 2 + 0;
            }
        }

        return result;
    }

    public static char[] getNode(int index) {
        char[] result = new char[9];

        for (int i = 0; i < 9; i++) {
            int digit = index % 2;
            if (digit == 0) {
                result[8 - i] = 'H';
            } else {
                result[8 - i] = 'T';
            }
            index = index / 2;
        }

        return result;
    }

    public List<Integer> getShortestPath(int nodeIndex) {
        return tree.getPath(nodeIndex);
    }

    public static void printNode(char[] node) {
        for (int i = 0; i < 9; i++) {
            if (i % 3 != 2) {
                System.out.print(node[i]);
            } else {
                System.out.println(node[i]);
            }
        }

        System.out.println();
    }
}
