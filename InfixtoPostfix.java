import java.util.*;

public class InfixtoPostfix {


   /* Parse input equation and return a postfix expression
    * @param expression is the input equation taken from user
    * @return a String of the postFix equation
    */
   private String generatePostFixExpression(String expression){
      //initialize a list to hold sorted part
      List<String> final_postfix_list = new ArrayList<>();
   
      //use stack as a temporary memory, think of it like a RAM to our method
      Stack<Character> tempCharMemory = new Stack<>();
      //tells us whether the current number has another digit in memory
      boolean operandContainsAnotherDigit = false;
      //remove any spaces from input_equation
      expression = expression.replace(" ", "");
      //convert the input_equation string to an array of characters
      char[] characters = expression.toCharArray();
      //iterate the characters array, skipping spaces
      for(int i=0;i<characters.length;i++){
         //pick a character to perform operation on
         char parsed_char = characters[i];
         //>>-----------> if char is an opening bracket, push it in the stack and iterate till you get a closing bracket
         if(parsed_char=='('){
            tempCharMemory.push(parsed_char);
            operandContainsAnotherDigit = false;
         }
         //if you get to the closing bracket, perform operation on elements in stack till you get to its opening brackets
         else if(parsed_char==')'){
            operandContainsAnotherDigit = false;
            //check if the stack is empty, and performs operation repeatedly till empty or till an opening brace is reached
            while(!tempCharMemory.isEmpty()){
               //if you get to its opening bracket, remove the bracket and break out of while loop
               if(tempCharMemory.peek()=='('){
                  tempCharMemory.pop();
                  break;
               }
               //pop character convert it to string and add it to out postfix list
               else{
                  final_postfix_list.add(String.valueOf(tempCharMemory.pop()));
               }
            }
         }
         //>>-----------> block runs if parsed+char is an operation sign
         else if(parsed_char=='+' || parsed_char=='-' || parsed_char=='*' || parsed_char=='/'){
            operandContainsAnotherDigit = false;
            //check if stack is empty, if empty, add int the operator
            if(tempCharMemory.isEmpty()){
               tempCharMemory.push(parsed_char);
            }
            //if other values are in stack, sort the operands
            else{
               //compare the preferences of operator on top of the stack and parsed operator before adding to stack
               while(!tempCharMemory.isEmpty() && checkOperatorPreference(tempCharMemory.peek())>= checkOperatorPreference(parsed_char)){
                  //if operator on stack should appear before parsed operator, pop the operator and insert it to list
                  final_postfix_list.add(tempCharMemory.pop()+"");
                  //repeat until stack is empty of all operators
               }
               //afterwards, put the operator on the stack to compare it with the next operator
               tempCharMemory.push(parsed_char);
            }
         }
         //>>-----------> if parsed character is not an operator or a brackets, it should then be a number, an operand
         else{
            //check if operand is one digit or not
            if(operandContainsAnotherDigit){
               String lastNumber = final_postfix_list.get(final_postfix_list.size()-1);
               lastNumber+=parsed_char;
               final_postfix_list.set(final_postfix_list.size()-1, lastNumber);
            }else
               final_postfix_list.add(String.valueOf(parsed_char));
            operandContainsAnotherDigit = true;
         }
      }
      //add all characters left in the temporary memory to the list
      while(!tempCharMemory.isEmpty()){
         final_postfix_list.add(String.valueOf(tempCharMemory.pop()));
      }
      //convert the list to a string, then return the string
      return String.join(" ",final_postfix_list);
   }

   /**
    * checks if an expression is valid based on arrangement of parenthesis
    * @param expression is the input equation taken from user
    * @return a String of the postFix equation
    */
   private Boolean checkValidity(String expression){
      //use stack as a temporary memory, think of it like a RAM to our method
      Stack<Character> cache = new Stack<>();
      //remove any spaces from input_equation
      expression = expression.replace(" ", "");
      //convert the input_equation string to an array of characters
      char[] characters = expression.toCharArray();
      // get opening and closing braces only
      String braces = "";
      for (char c : characters){
         if(c == '(' || c == ')'){
            braces = braces+c;
         }
      }
      // if braces is not an even no. return false
      if(braces.length()%2 != 0){
         return false;
      }
      // if number of ( is not equal to number of ) return false
      if(braces.replace("(", "").length() != braces.replace(")","").length()){
         return false;
      }
   
      characters = braces.toCharArray();
   
      //iterate the characters array, skipping spaces
      for(int i=0;i<characters.length;i++){
         //pick a character to perform operation on
         char parsed_char = characters[i];
         //>>-----------> if char is an opening bracket, push it in the stack and iterate till you get a closing bracket
         if(parsed_char=='('){
            cache.push(parsed_char);
         }
         //if you get to the closing bracket, perform operation on elements in stack till you get to its opening brackets
         else if(parsed_char==')'){
            //check if stack contains (
            //if tempChar is empty return false as it is expected to have a (
            if(cache.isEmpty()){
               return false;
            }
         
            if(cache.peek()=='('){
               cache.pop();
            }
         }
      }
      //if temp char is empty return true
      if(cache.isEmpty())
         return true;
      else
         return false;
   }

   /**
    * method solves a postfix equation and returns an answer
    * @param postFixedExpression is the postFix equation
    * @return an answer
    */
   private int getSolution(String postFixedExpression){
      List<String> postFix_List = getChars(postFixedExpression);
      Stack<Integer> tempIntegerMemory = new Stack<>();
   
      for(int i=0;i<postFix_List.size();i++){
         String word = postFix_List.get(i);
         if(word.length()==1 && (word.charAt(0)=='+'||word.charAt(0)=='-'||word.charAt(0)=='*'||word.charAt(0)=='/')){
            int number2 = tempIntegerMemory.pop();
            int number1 = tempIntegerMemory.pop();
            if(word.charAt(0)=='+'){
               int number = number1+number2;
               tempIntegerMemory.push(number);
            }else if(word.charAt(0)=='-'){
               int number = number1-number2;
               tempIntegerMemory.push(number);
            }else if(word.charAt(0)=='*'){
               int number = number1*number2;
               tempIntegerMemory.push(number);
            }else{
               int number = number1/number2;
               tempIntegerMemory.push(number);
            }
         }else{
            int number = Integer.parseInt(word);
            tempIntegerMemory.push(number);
         }
      }
      return tempIntegerMemory.peek();
   }

   /**
    * converts a string into a list. Separation is based on " "(spaces)
    * @param word is the input String to split
    * @return list of strings
    */
   public List<String>  getChars(String word) {
      return Arrays.asList(word.split(" "));
   }

   /**
    * method gives the rank of the certain character
    * @param operator
    * @return
    */
   private int checkOperatorPreference(char operator){
      if(operator=='+'|| operator=='-') 
         return 1;
      else if(operator=='*' || operator=='/') 
         return 2;
      else 
         return -1;
   }

   public static  void main (String ... args){
   
      Scanner sc = new Scanner(System.in);
   
      InfixtoPostfix q= new InfixtoPostfix();
      System.out.print("Enter the mathematical expression: ");
      String input = sc.nextLine();
      if(q.checkValidity(input)){
         String postFixExpression = q.generatePostFixExpression(input);
         System.out.println("The postfix expression: " +postFixExpression );
         System.out.println("The final result: "+q.getSolution(postFixExpression));
      }
      else {
         System.out.println("The expression is invalid");
      }
   
   }
}