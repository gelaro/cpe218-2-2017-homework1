import org.w3c.dom.*;
import javax.imageio.metadata.*;
import java.util.*;

public class Homework1 {
    static  Stack stack = new Stack(); //stack
    static  Node root= null; //root node

    public static void infix(Node n) {
        //base case
        if(!isOperate(n.getNodeName().charAt(0))) {
            n.setNodeValue(n.getNodeName());
            return;
        }
        //Constructs an right node with a given stack.pop().toString().
        Node right = new IIOMetadataNode(stack.pop().toString());
        infix(right);
        //Constructs an left node with a given stack.pop().toString().
        Node left = new IIOMetadataNode(stack.pop().toString());
        infix(left);
        n.appendChild(right); //append right node in n
        n.appendChild(left); //append left node in n
        calculate(n);
    }

    public static void inorder(Node n) {
        if (n != null){
            if(isOperate(n.getNodeName().charAt(0)) && n != root) System.out.print("(");
            NodeList list = n.getChildNodes(); //list get child nodes
            inorder(list.item(1)); //recursive left node
            System.out.print(n.getNodeName());   //print itself
            inorder(list.item(0));    //recursive right node
            if(isOperate(n.getNodeName().charAt(0)) && n != root) System.out.print(")");
        }
    }

    public static void calculate(Node n) {
        NodeList list = n.getChildNodes(); //list get child nodes
        int left_value = Integer.parseInt(list.item(1).getNodeValue()); //value of left node (0-9)
        int right_value = Integer.parseInt(list.item(0).getNodeValue()); //value of right node (0-9)
        int result = 0; //result
        String operand = n.getNodeName();
        switch(operand) { //operation
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
        n.setNodeValue(Integer.toString(result));
    }

    public static boolean isOperate(char string) {
        if(string == '+' || string == '-' ||
                string == '*' || string == '/') {
        return true;
        }else {
            return false;
        }
    }

    public static void main(String[] args) {
        String data = "251-*32*+";
        // TODO: Implement your project here
//        Scanner in = new Scanner(System.in);
//        data = in.nextLine();
//        in.close();

        if(args.length > 0) data = args[0];

        for(int i = 0; i < data.length(); i++) {
            stack.push(data.charAt(i));
        }
        root = new IIOMetadataNode(stack.pop().toString());
        inorder(root);
        infix(root);
        System.out.println("=" + root.getNodeValue());
        TreeIconDemo.main(root);
    }
}