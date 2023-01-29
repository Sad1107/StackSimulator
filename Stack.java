import java.util.HashMap;

public class Stack {

    private final int[] stack;
    private int rsp;
    HashMap<String, Integer> registers = new HashMap<>();

    final int size;
    public Stack(int size){
        stack = new int[size];
        rsp = -1;
        registers.put("rax",0);
        registers.put("rdx",0);
        registers.put("r8",0);
        registers.put("r9",0);
        registers.put("r10",0);
        this.size = size;
    }

    public void push(int value){
        if(rsp<size) {
            stack[++rsp] = value;
        }
        else throw new IllegalStateException("full stack");
    }

    public int pop() throws IllegalStateException{
        if(!isEmpty()){

            int value = stack[rsp];
            stack[rsp--] = 0;
            return value;
        }
        else throw new IllegalStateException("empty stack");
    }
    public void pop(String register){
        if(registers.containsKey(register)){
            registers.put(register,pop());
        }
        else throw new IllegalArgumentException();
    }

    public void pop(int index){
        if(!isEmpty() && validIndex(index) && index!=rsp){
            stack[index] = pop();
        }
        else throw new IllegalArgumentException();

    }

    public void view(int index){
        if(!isEmpty() && index<=rsp) {
            System.out.println(stack[index]);
        }
        else System.out.println("invalid index");

    }
    public void view(String register){
        if(registers.containsKey(register))
        {
            System.out.println(registers.get(register));
        }
        else System.out.println("invalid register");
    }

    public boolean isEmpty(){
        return rsp<0;
    }

    public void mov(int value, int index){
        if(index>=0 && index<=rsp){
            stack[index]=value;
        }
        else throw new IllegalArgumentException("invalid index");
    }

    public void mov(int value, String register){
        if(registers.containsKey(register)){
            registers.put(register,value);
        }
        else throw new IllegalArgumentException("invalid register");
    }

    public void mov(String registerFrom, String registerTo){
        if(registers.containsKey(registerFrom) &&
                registers.containsKey(registerTo)){
            registers.put(registerTo, registers.get(registerFrom));
        }
        else throw new IllegalArgumentException("invalid register");
    }

    public void mov(String register, int index){
        if(registers.containsKey(register) &&
                index>=0 && index<=rsp
                ){
            stack[index]=registers.get(register);
        }
        else throw new IllegalArgumentException("invalid register");
    }

    public boolean validIndex(int index){
        return index >= 0 && index <= rsp;
    }
    public void add(int value, String register){
        if(registers.containsKey(register)){
            registers.compute(register,
                    (key, val) -> value+val);
        }
        else throw new IllegalArgumentException("invalid register");
    }

    public void add(String registerFrom, String registerTo){

        if(registers.containsKey(registerFrom)
                && registers.containsKey(registerTo)){
            registers.compute(registerTo,
                    (key, val) -> registers.get(registerFrom)+val);
        }
        else throw new IllegalArgumentException("invalid register");
    }

    public void add(int value, int index){
        if(validIndex(index) && !isEmpty()){
            stack[rsp] +=value;
        }
        else throw new IllegalArgumentException("invalid index");
    }

    public void add(String register, int index){

        if(registers.containsKey(register)
                && validIndex(index) && !isEmpty()){
            stack[index] += registers.get(register);
        }
        else throw new IllegalArgumentException();

    }


    public void sub(int value, String register){
        if(registers.containsKey(register)){
            registers.compute(register,
                    (key, val) -> val-value);
        }
        else throw new IllegalArgumentException();
    }

    public void sub(String registerFrom, String registerTo){
        if(registers.containsKey(registerFrom)
                && registers.containsKey(registerTo)){
            registers.compute(registerTo,
                    (key, val) -> val-registers.get(registerFrom));
        }
        else throw new IllegalArgumentException();
    }

    public void sub(int value, int index){
        if(validIndex(index) && !isEmpty()){
            stack[rsp] -=value;
        }
        else throw new IllegalArgumentException();
    }
    public void sub(String register, int index){
        if(registers.containsKey(register)
                && validIndex(index) && !isEmpty()){
            stack[index] -= registers.get(register);
        }
        else throw new IllegalArgumentException("invalid index or register");
    }


    public void imul(String registerFrom, String registerTo){
        if(registers.containsKey(registerFrom)
                && registers.containsKey(registerTo)){
            registers.compute(registerTo,
                    (key, val) -> val*registers.get(registerFrom));
        }
        else throw new IllegalArgumentException("invalid register");
    }


    public void imul(int index, String register){

        if(registers.containsKey(register)
                && validIndex(index) && !isEmpty()){
            registers.compute(register,
                    (key, val) -> stack[index]*val);
        }
        else throw new IllegalArgumentException("invalid index or register");
    }

    public void imul(int constant, String register1, String register2){
        if(registers.containsKey(register1) && registers.containsKey(register2)){
            int valueOfRegister2 = registers.get(register2);
            registers.compute(register2,
                    (key, val) -> constant*valueOfRegister2);
        }
        else throw new IllegalArgumentException("invalid register");
    }

    public void imul(int constant, int index, String register){
        if(registers.containsKey(register) && validIndex(index)){
            registers.compute(register,
                    (key, val) -> constant*val*stack[index]);
        }
        else throw new IllegalArgumentException("invalid index or register");
    }

    public void idiv(String register){
        if(registers.containsKey(register)){
            int oldValue = registers.get("rax");
            registers.put("rax", oldValue/registers.get(register));
            registers.put("rdx", oldValue%registers.get(register));
        }
        // 0 division check
        else throw new IllegalArgumentException("invalid register");
    }

    public void idiv(int index){
        if(validIndex(index)){ // og nonempty?
            int oldValue = registers.get("rax");
            registers.put("rax", oldValue/stack[index]);
            registers.put("rdx", oldValue%stack[index]);
        }
        // 0 division check

    }

    public void displayStack(){
        String format = "| %-6d | %-3d |%n";
        String formatPointer = "| %-6d | %-3d | <--stack pointer%n";
        System.out.format("+--------+-----+%n");
        System.out.format("|  Data  |Index|%n");
        System.out.format("+--------+-----+%n");
        for(int i = 0; i<=rsp;i++){
            if(i==rsp) {
                System.out.format(formatPointer, stack[i], i);
                System.out.println("+--------+-----+");

            }
            else{
                System.out.format(format, stack[i], i);
                if(i!=rsp) {
                    System.out.println("+--------+-----+");
                }
            }
        }

        System.out.println("Registers:");
        registers.forEach((key,val)->System.out.print(key+": "+val+", "));
        System.out.println();
    }

    //TODO: error handling, check input, implement logical operators, mem->register add/sub,
    // display relative locations
}
