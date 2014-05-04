
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Exercise25_21 extends JApplet {

    private Heap<Integer> heap = new Heap<Integer>();
    private Canvas canvas = new Canvas();
    private JTextField jtfEnterKey;

    public Exercise25_21() {
        JPanel buttonPanel = new JPanel();
        JButton jbtInsert = new JButton("Insert");
        JButton jbtRemove = new JButton("Remove the root");
        jtfEnterKey = new JTextField("", 10);
        JLabel jlblEnterKey = new JLabel("Enter a key");
        add(canvas);
        add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(jlblEnterKey);
        buttonPanel.add(jtfEnterKey);
        buttonPanel.add(jbtInsert);
        buttonPanel.add(jbtRemove);

        jbtInsert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String a = jtfEnterKey.getText();
                if (a.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a key");
                } else {
                    // add the number to the heap and draw the new node
                    heap.add(Integer.parseInt(a));
                }
                repaint();
            }
        });
        jbtRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                heap.remove();
                repaint();
            }
        });

    }

    public class Heap<E extends Comparable<E>> {

        private java.util.ArrayList<E> list = new java.util.ArrayList<E>();

        /**
         * Create a default heap
         */
        public Heap() {
        }

        /**
         * Create a heap from an array of objects
         */
        public Heap(E[] objects) {
            for (int i = 0; i < objects.length; i++) {
                add(objects[i]);
            }
        }

        /**
         * Add a new object into the heap
         */
        public void add(E newObject) {
            list.add(newObject); // Append to the heap
            int currentIndex = list.size() - 1; // The index of the last node

            while (currentIndex > 0) {
                int parentIndex = (currentIndex - 1) / 2;
                // Swap if the current object is greater than its parent
                if (list.get(currentIndex).compareTo(
                        list.get(parentIndex)) > 0) {
                    E temp = list.get(currentIndex);
                    list.set(currentIndex, list.get(parentIndex));
                    list.set(parentIndex, temp);
                } else {
                    break; // the tree is a heap now
                }
                currentIndex = parentIndex;
            }
        }

        /**
         * Remove the root from the heap
         */
        public E remove() {
            if (list.size() == 0) {
                return null;
            }

            E removedObject = list.get(0);
            list.set(0, list.get(list.size() - 1));
            list.remove(list.size() - 1);

            int currentIndex = 0;
            while (currentIndex < list.size()) {
                int leftChildIndex = 2 * currentIndex + 1;
                int rightChildIndex = 2 * currentIndex + 2;

                // Find the maximum between two children
                if (leftChildIndex >= list.size()) {
                    break; // The tree is a heap
                }
                int maxIndex = leftChildIndex;
                if (rightChildIndex < list.size()) {
                    if (list.get(maxIndex).compareTo(
                            list.get(rightChildIndex)) < 0) {
                        maxIndex = rightChildIndex;
                    }
                }

                // Swap if the current node is less than the maximum
                if (list.get(currentIndex).compareTo(
                        list.get(maxIndex)) < 0) {
                    E temp = list.get(maxIndex);
                    list.set(maxIndex, list.get(currentIndex));
                    list.set(currentIndex, temp);
                    currentIndex = maxIndex;
                } else {
                    break; // The tree is a heap
                }
            }

            return removedObject;
        }

        /**
         * Get the number of nodes in the tree
         */
        public int getSize() {
            return list.size();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Exercise25_21");
        frame.add(new Exercise25_21());
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    class Canvas extends JPanel {

        private int vGap = 50;
        private int radius = 20;
// draw method

        public void draw(Graphics g) {
            // display "heap is empty" when there is no key
            int x1 = (getWidth() / 2) - radius;
            int y1 = getHeight() - (getHeight() - radius);
            if (heap.equals("")) {
                JOptionPane.showMessageDialog(null, "Enter a key");
            } else {
                g.drawOval(x1, y1, radius * 2, radius * 2);
            }

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);

            if (heap.getSize() == 0) {
                g.drawString("heap is empty", (getWidth() / 2 - 30), 15);
            } else {
                int x = getWidth() / 2;
                int y = 30;
                drawTree(g, x, y, 0, getWidth() / 4);
            }
        }

        // drawTree method- sending it the graphics g, x,y coord, radius, index n
        // and the gap size
        public void drawTree(Graphics g, int x, int y, int i, int hGap) {

            if (2 * i + 1 < heap.list.size()) {
                //connectCircles(g,x,y,x-hGap,y+vGap);
                drawConnectLine(g, x, y, x - hGap, y + vGap);
                drawOverlap(g, x - hGap, y + vGap);
                drawTree(g, x - hGap, y + vGap, 2 * i + 1, hGap / 2);


            }
            if (2 * i + 2 < heap.list.size()) {
                //connectCircles(g,x,y,x+hGap,y+vGap);
                drawConnectLine(g, x, y, x + hGap, y + vGap);
                drawOverlap(g, x + hGap, y + vGap);
                drawTree(g, x + hGap, y + vGap, 2 * i + 2, hGap / 2);

            }
            drawOverlap(g, x, y);
            g.setColor(Color.WHITE);
            g.drawString(heap.list.get(i) + "", x - 5, y + 5);
            g.drawOval(x - radius, y - radius, radius * 2, radius * 2);

        }

        public void drawOverlap(Graphics g, int x1, int y1) {
            g.setColor(Color.black);
            g.fillOval(x1 - radius, y1 - radius, radius * 2, radius * 2);

        }

        // drawLine
        public void drawConnectLine(Graphics g, int x1, int y1, int x2, int y2) {

            g.setColor(Color.WHITE);
            g.drawLine(x1, y1, x2, y2);

        }
        // heap class
    }
}