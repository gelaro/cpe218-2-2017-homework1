/**
 * Created by user on 24/1/2561.
 */
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class TreeIconDemo extends JPanel implements TreeSelectionListener {
    private JEditorPane htmlPane;
    private JTree tree;
    DefaultMutableTreeNode root; //tree node of root node
    DefaultMutableTreeNode node; //tree node of child node

    public TreeIconDemo(Node n) {
        super(new GridLayout(1,0));
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(n.getNodeName());
        createNodes(n, top);

        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        //Set the icon for leaf nodes.
        ImageIcon leafIcon = createImageIcon("middle.gif");
        if (leafIcon != null) {
            DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
            renderer.setOpenIcon(leafIcon); //set open leaf icon
            renderer.setClosedIcon(leafIcon); //set close leaf icon
            tree.setCellRenderer(renderer);
        } else {
            System.err.println("Leaf icon missing; using default.");
        }

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);

        //Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(tree);

        //Create the HTML viewing pane.
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        JScrollPane htmlView = new JScrollPane(htmlPane);

        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(htmlView);
        Dimension minimumSize = new Dimension(100, 100);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100); //XXX: ignored in some releases
        //treeView.setPreferredSize(new Dimension(100, 100));
        splitPane.setPreferredSize(new Dimension(500, 300));
        //Add the split pane to this panel.
        add(splitPane);
    }
    public boolean isOperate(char string) {
        if(string == '+' || string == '-' ||
                string == '*' || string == '/') {
            return true;
        }else {
            return false;
        }
    }
    public String inorder(DefaultMutableTreeNode n) {
        if (n == null) return "";
        if(n == node && !n.isLeaf()) {
            return 	inorder(n.getNextNode()) + n.toString() + inorder(n.getNextNode().getNextSibling());
        }else if(isOperate(n.toString().charAt(0)) && n != root) {
            return "(" + inorder(n.getNextNode()) + n.toString() + inorder(n.getNextNode().getNextSibling()) + ")";
        }else {
            return 	n.toString();
        }
    }

    public int calculate(DefaultMutableTreeNode n) {
        if(n.isLeaf()) return Integer.parseInt(n.toString());
        int left_value = calculate(n.getNextNode());
        int right_value = calculate(n.getNextNode().getNextSibling());
        int result = 0;
        String operand = n.toString();
        switch(operand) {
            case "+" :{
                result = left_value + right_value;
                break;
            }
            case "-" :{
                result = left_value - right_value;
                break;
            }
            case "*" :{
                result = left_value * right_value;
                break;
            }
            case "/" :{
                result = left_value / right_value;
                break;
            }
        }
        return result;
    }
    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if (node == null) return;
//        Object nodeInfo = node.getUserObject();
        htmlPane.setFont(new Font("Angsana New", 1, 60));
        String text = inorder(node);
        if(!node.isLeaf()) text += "=" + calculate(node);
        htmlPane.setText(text);
    }

    private void createNodes(Node n, DefaultMutableTreeNode m) {
        DefaultMutableTreeNode right_child = null;
        DefaultMutableTreeNode left_child = null;

        NodeList list = n.getChildNodes();
        if(list.item(0) == null || list.item(1) == null) return;
        right_child = new DefaultMutableTreeNode(list.item(1).getNodeName());
        createNodes(list.item(1), right_child);
        m.add(right_child);
        left_child = new DefaultMutableTreeNode(list.item(0).getNodeName());
        createNodes(list.item(0), left_child);
        m.add(left_child);
    }
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = TreeIconDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI(Node n) {
        //Create and set up the window.
        JFrame frame = new JFrame("Binary Tree Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        TreeIconDemo newContentPane = new TreeIconDemo(n);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(Node n) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(n);
            }
        });
    }
}