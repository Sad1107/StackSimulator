public class Driver {
    public static void main(String[] args){
        Stack stack = new Stack(10);

        stack.push(5);
        stack.pop("r9");
        stack.mov(3,"r10");
        stack.add("r10","r9");

        stack.push(3);
        stack.add(5,0);
        stack.add(1,"r9");
        stack.push(13);
        stack.add("r9",1); // add to index 1 ( ie. -%8(rbp) )
        stack.mov(113, "rax");
        stack.push(13);
        stack.idiv(1);

        stack.mov(25, "rax");
        stack.idiv("r9");
        stack.push(2);
        stack.push(5);
        stack.push(5);
        stack.pop(0);
        stack.mov(100,"r9");
        stack.imul(10,"r9","r10");
        stack.mov("r10",4);
        stack.displayStack();
    }
}
