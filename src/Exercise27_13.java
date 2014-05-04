
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Exercise27_13 extends JApplet {

    private BST<Integer> tree; 
    private JTextField jtfKey = new JTextField(5);
    private TreeView view = new TreeView();
    private JButton jbtInsert = new JButton("Insert");
    private JButton jbtDelete = new JButton("Delete");
    private JButton jbtInorder = new JButton("Show Inorder");
    private JButton jbtPreorder = new JButton("Show Preorder");
    private JButton jbtPostorder = new JButton("Show Postorder");

    /**
     * Construct a view for a binary tree
     */
    public Exercise27_13(BST<Integer> tree) {
        this.tree = tree; 
        setUI();

    }

    /**
     * Initialize UI for binary tree
     */
    private void setUI() {
        this.setLayout(new BorderLayout());
        add(view, BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter a key: "));
        panel.add(jtfKey);
        panel.add(jbtInsert);
        panel.add(jbtDelete);
        panel.add(jbtInorder);
        panel.add(jbtPreorder);
        panel.add(jbtPostorder);
        add(panel, BorderLayout.SOUTH);

        jbtInsert.addActionListener(new ActionListener() {
            @Override  
            public void actionPerformed(ActionEvent e) {
                int key = Integer.parseInt(jtfKey.getText());
                if (tree.search(key)) { 
                    JOptionPane.showMessageDialog(null,
                            key + " is already in the tree");
                } else {
                    tree.insert(key);
                    view.repaint(); 
                }
            }
        });

        jbtDelete.addActionListener(new ActionListener() {
            @Override  
            public void actionPerformed(ActionEvent e) {
                int key = Integer.parseInt(jtfKey.getText());
                if (!tree.search(key)) { 
                    JOptionPane.showMessageDialog(null,
                            key + " is not in the tree");
                } else {
                    tree.delete(key); 
                    view.repaint(); 
                }
            }
        });
        //show inorder
        jbtInorder.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                if (tree.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "List is empty");
                } else {
                    JOptionPane.showMessageDialog(null, "inorder is "
                            + tree.inorderList());
                }

            }
        });
        //show preorder
        // current node is visited first
        // the left subtree is visited recusively, then the
        // right subtree is visited recursively
        // and finally, the current node itself
        jbtPreorder.addActionListener(new ActionListener() {
            @Override  
            public void actionPerformed(ActionEvent e) {
                if (tree.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "List is empty");
                } else {

                    JOptionPane.showMessageDialog(null, "Preoder is "
                            + tree.preorderList());
                }
            }
        });
        //show postorder
        // the left subtree is visited recusively, then the
        // right subtree is visited recursively
        // and finally, the current node itself
        jbtPostorder.addActionListener(new ActionListener() {
            @Override  
            public void actionPerformed(ActionEvent e) {
                if (tree.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "List is empty");
                } else {

                    JOptionPane.showMessageDialog(null, "Postorder is "
                            + tree.postorderList());
                }
            }
        });

    }
    // Inner class TreeView for displaying a tree on a panel

    class TreeView extends JPanel {

        private int radius = 20;
        private int vGap = 50;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (tree.getRoot() != null) {
                // Display tree recursively    
                displayTree(g, tree.getRoot(), getWidth() / 2, 30,
                        getWidth() / 4);
            }
        }

        /**
         * Display a subtree rooted at position (x, y)
         */
        private void displayTree(Graphics g,
                BST.TreeNode<Integer> root, int x, int y, int hGap) {
            // Display the root
            g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
            g.drawString(root.element + "", x - 6, y + 4);

            if (root.left != null) {
                // Draw a line to the left node
                connectTwoCircles(g, x - hGap, y + vGap, x, y);
                // Draw the left subtree recursively
                displayTree(g, root.left, x - hGap, y + vGap, hGap / 2);
            }

            if (root.right != null) {
                // Draw a line to the right node
                connectTwoCircles(g, x + hGap, y + vGap, x, y);
                // Draw the right subtree recursively
                displayTree(g, root.right, x + hGap, y + vGap, hGap / 2);
            }
        }

        /**
         * Connect two circles centered at (x1, y1) and (x2, y2)
         */
        private void connectTwoCircles(Graphics g,
                int x1, int y1, int x2, int y2) {
            double d = Math.sqrt(vGap * vGap + (x2 - x1) * (x2 - x1));
            int x11 = (int) (x1 - radius * (x1 - x2) / d);
            int y11 = (int) (y1 - radius * (y1 - y2) / d);
            int x21 = (int) (x2 + radius * (x1 - x2) / d);
            int y21 = (int) (y2 + radius * (y1 - y2) / d);
            g.drawLine(x11, y11, x21, y21);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Exercise27_13");
        frame.add(new Exercise27_13(new BST<Integer>()));
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}

class BST<E extends Comparable<E>>
        extends AbstractTree<E> {

    protected TreeNode<E> root;
    protected int size = 0;

    /**
     * Create a default binary tree
     */
    public BST() {
    }

    /**
     * Create a binary tree from an array of objects
     */
    public BST(E[] objects) {
        for (int i = 0; i < objects.length; i++) {
            insert(objects[i]);
        }
    }

    @Override
    /**
     * Returns true if the element is in the tree
     */
    public boolean search(E e) {
        TreeNode<E> current = root; // Start from the root

        while (current != null) {
            if (e.compareTo(current.element) < 0) {
                current = current.left;
            } else if (e.compareTo(current.element) > 0) {
                current = current.right;
            } else // element matches current.element
            {
                return true; // Element is found
            }
        }

        return false;
    }

    @Override
    /**
     * Insert element o into the binary tree Return true if the element is
     * inserted successfully
     */
    public boolean insert(E e) {
        if (root == null) {
            root = createNewNode(e); // Create a new root
        } else {
            // Locate the parent node
            TreeNode<E> parent = null;
            TreeNode<E> current = root;
            while (current != null) {
                if (e.compareTo(current.element) < 0) {
                    parent = current;
                    current = current.left;
                } else if (e.compareTo(current.element) > 0) {
                    parent = current;
                    current = current.right;
                } else {
                    return false; // Duplicate node not inserted
                }
            }
            // Create the new node and attach it to the parent node
            if (e.compareTo(parent.element) < 0) {
                parent.left = createNewNode(e);
            } else {
                parent.right = createNewNode(e);
            }
        }

        size++;
        return true; // Element inserted
    }

    protected TreeNode<E> createNewNode(E e) {
        return new TreeNode<E>(e);
    }

    @Override
    /**
     * In order traversal from the root
     */
    public void inorder() {
        inorder(root);
    }

    /**
     * In order traversal from a subtree
     */
    protected void inorder(TreeNode<E> root) {
        if (root == null) {
            return;
        }
        inorder(root.left);
        System.out.print(root.element + " ");
        inorder(root.right);
    }

    @Override
    /**
     * Postorder traversal from the root
     */
    public void postorder() {
        postorder(root);
    }

    /**
     * Postorder traversal from a subtree
     */
    protected void postorder(TreeNode<E> root) {
        if (root == null) {
            return;
        }
        postorder(root.left);
        postorder(root.right);
        System.out.print(root.element + " ");
    }

    @Override
    /**
     * Pre order traversal from the root
     */
    public void preorder() {
        preorder(root);
    }

    /**
     * Pre order traversal from a subtree
     */
    protected void preorder(TreeNode<E> root) {
        if (root == null) {
            return;
        }
        System.out.print(root.element + " ");
        preorder(root.left);
        preorder(root.right);
    }

    /**
     * This inner class is static, because it does not access any instance
     * members defined in its outer class
     */
    public static class TreeNode<E extends Comparable<E>> {

        protected E element;
        protected TreeNode<E> left;
        protected TreeNode<E> right;

        public TreeNode(E e) {
            element = e;
        }
    }

    @Override
    /**
     * Get the number of nodes in the tree
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the root of the tree
     */
    public TreeNode<E> getRoot() {
        return root;
    }

    /**
     * Returns a path from the root leading to the specified element
     */
    public java.util.ArrayList<TreeNode<E>> path(E e) {
        java.util.ArrayList<TreeNode<E>> list =
                new java.util.ArrayList<TreeNode<E>>();
        TreeNode<E> current = root; // Start from the root

        while (current != null) {
            list.add(current); // Add the node to the list
            if (e.compareTo(current.element) < 0) {
                current = current.left;
            } else if (e.compareTo(current.element) > 0) {
                current = current.right;
            } else {
                break;
            }
        }

        return list; // Return an array of nodes
    }

    @Override
    /**
     * Delete an element from the binary tree. Return true if the element is
     * deleted successfully Return false if the element is not in the tree
     */
    public boolean delete(E e) {
        // Locate the node to be deleted and also locate its parent node
        TreeNode<E> parent = null;
        TreeNode<E> current = root;
        while (current != null) {
            if (e.compareTo(current.element) < 0) {
                parent = current;
                current = current.left;
            } else if (e.compareTo(current.element) > 0) {
                parent = current;
                current = current.right;
            } else {
                break; 
            }
        }

        if (current == null) {
            return false; 
        }
        // Case 1: current has no left children
        if (current.left == null) {
            // Connect the parent with the right child of the current node
            if (parent == null) {
                root = current.right;
            } else {
                if (e.compareTo(parent.element) < 0) {
                    parent.left = current.right;
                } else {
                    parent.right = current.right;
                }
            }
        } else {
            // Case 2: The current node has a left child
            // Locate the rightmost node in the left subtree of
            // the current node and also its parent
            TreeNode<E> parentOfRightMost = current;
            TreeNode<E> rightMost = current.left;

            while (rightMost.right != null) {
                parentOfRightMost = rightMost;
                rightMost = rightMost.right; 
            }

            // Replace the element in current by the element in rightMost
            current.element = rightMost.element;

            // Eliminate rightmost node
            if (parentOfRightMost.right == rightMost) {
                parentOfRightMost.right = rightMost.left;
            } else {
                // Special case: parentOfRightMost == current
                parentOfRightMost.left = rightMost.left;
            }
        }

        size--;
        return true; // Element inserted
    }

    @Override
    /**
     * Obtain an iterator. Use in order.
     */
    public java.util.Iterator<E> iterator() {
        return new InorderIterator();
    }

    // Inner class InorderIterator
    private class InorderIterator implements java.util.Iterator<E> {
        // Store the elements in a list

        private java.util.ArrayList<E> list =
                new java.util.ArrayList<E>();
        private int current = 0; 

        public InorderIterator() {
            inorder(); 
        }

        /**
         * In order traversal from the root
         */
        private void inorder() {
            inorder(root);
        }

        /**
         * In order traversal from a subtree
         */
        private void inorder(TreeNode<E> root) {
            if (root == null) {
                return;
            }
            inorder(root.left);
            list.add(root.element);
            inorder(root.right);
        }

        @Override
        /**
         * More elements for traversing?
         */
        public boolean hasNext() {
            if (current < list.size()) {
                return true;
            }

            return false;
        }

        @Override
        /**
         * Get the current element and move to the next
         */
        public E next() {
            return list.get(current++);
        }

        @Override
        /**
         * Remove the current element
         */
        public void remove() {
            delete(list.get(current)); 
            list.clear(); 
            inorder(); 
        }
    }

    /**
     * Remove all elements from the tree
     */
    public void clear() {
        root = null;
        size = 0;
    }

    public java.util.List<E> inorderList() {
        return inorderList(root);
    }

    // return a list of nodes from inorder
    // left subtree --> current node --> right subtree 
    public java.util.List<E> inorderList(TreeNode<E> e) {
        java.util.List<E> inList = new ArrayList<E>();

        if (e == null) {
            return inList;
        }
        inList.addAll(inorderList(e.left));
        inList.add(e.element);
        inList.addAll(inorderList(e.right));

        return inList;

    }

    public java.util.List<E> preorderList() {
        return preorderList(root);
    }
    // return a list of nodes from preorder
    // left subtree --> right subtree --> current node
  

    public java.util.List<E> preorderList(TreeNode<E> e) {
        java.util.List<E> preList = new ArrayList<E>();

        if (e == null) {
            return preList;
        }
        preList.addAll(preorderList(e.left));
        preList.addAll(preorderList(e.right));
        preList.add(e.element);

        return preList;
    }

    public java.util.List<E> postorderList() {
        return postorderList(root);
    }

    
    public java.util.List<E> postorderList(TreeNode<E> e) {
        java.util.List<E> postList = new ArrayList<E>();
        if (e == null) {
            return postList;
        }
        postList.add(e.element);
        postList.addAll(postorderList(e.left));
        postList.addAll(preorderList(e.right));

        return postList;
    }
}

abstract class AbstractTree<E extends Comparable<E>>
        implements Tree<E> {

    @Override
    /**
     * In order traversal from the root
     */
    public void inorder() {
    }

    @Override
    /**
     * Postorder traversal from the root
     */
    public void postorder() {
    }

    @Override
    /**
     * Pre order traversal from the root
     */
    public void preorder() {
    }

    @Override
    /**
     * Return true if the tree is empty
     */
    public boolean isEmpty() {
        return getSize() == 0;
    }

    @Override
    /**
     * Return an iterator for the tree
     */
    public java.util.Iterator<E> iterator() {
        return null;
    }
}

interface Tree<E extends Comparable<E>> extends Iterable<E> {
    
    public boolean search(E e);
    
    public boolean insert(E e);

    public boolean delete(E e);

    public void inorder();

    public void postorder();

    public void preorder();

    public int getSize();

    public boolean isEmpty();

    public java.util.Iterator<E> iterator();
}
