
package crypto;

import java.util.ArrayList;
import java.util.List;


public class Coding {
  
    public static final int coding_bit_size = 6 ;
    
    public Coding() {
    }
    
    
  public static String convertTo_StringOfZeroAndOne_InEncoding(byte[] buff,int  block_size ){
       
       String result="" ; 
       for (int k = 0 ; k < block_size ; k ++ ) {
          for(int i=7 ; i >= 0 ; i--) {
             result += (buff[k] & (1 << i))==0 ? "0" : "1" ;  
          }
       }
        
       return result ; 
    }//end method
  
  
  public static String convertTo_StringOfZeroAndOne_InDecoding(List<Integer> alist , int  read_size){
       
       String result="" ; 
       for (int k = 0 ; k < read_size ; k ++ ) {
              for(int i=5 ; i >= 0 ; i--) {
                 result += (alist.get(k) & (1 << i))==0 ? "0" : "1" ;  
              }
           }
        return result ;
  }  
        
  public static String removePadding_InDecoding (Integer aint , int paddingBites){  
      String result="" ;  
      if (paddingBites == 2) {
          for(int i=3 ; i >= 0 ; i--) {
                   result += (aint & (1 << i))==0 ? "0" : "1" ;  
                  }
           }
      else { //padding is 4
          for(int i=1 ; i >= 0 ; i--) {
                   result += (aint & (1 << i))==0 ? "0" : "1" ;  
                  }
      }
     return result ; 
    }//end method
  
  
  
  //******************************************************************************************
  //Text parameter must have a lenght with this condition [(text.lenght % coding_bit_size) = 0] that means text have lenght that is multiplier of coding_bit_size or multiplier of read_block_size
  //if Text parameter have lenght equal to 8 or 18 bit (one or two byte) then last string part will have lenght smaller thann coding_bit_size 
  //*******************************************************************************************
  public static List<String> splitEqually_InEncoding(String text) {
      int size = (text.length()+ coding_bit_size - 1)/ coding_bit_size ;
      List<String> ret = new ArrayList<>(size);
      for (int k = 0; k < text.length(); k += coding_bit_size) {
         ret.add(text.substring(k, Math.min(text.length(), k + coding_bit_size)));
       }
      
      String last_part_str = ret.get(size - 1);
      switch (last_part_str.length()){
          case 6: //equal to coding_bit_size
                  break ;     
          case 2:
                 //add 4 character 0 as padding to begining of last_part_str
                 ret.remove(size - 1);
                 ret.add("0000" + last_part_str );
                 break ;
          case 4:    
                 //add 2 character 0 as padding to begining of last_part_str
                 ret.remove(size - 1);
                 ret.add("00" + last_part_str );
                 break ;
          }//end switch    
    return ret; 
   }
  
 public static List<String> splitEqually_InDecoding(String text) throws Exception {
      if ((text.length() % 8) != 0 ) throw new Exception("Error");
      int size = text.length()/ 8 ;
      List<String> ret = new ArrayList<>(size);
      for (int k = 0; k < text.length(); k += 8) {
         ret.add(text.substring(k,k + 8));
       }
    return ret; 
   }       
  
   public static List<String> encodeIntegers (List<Integer> spInt){
     
       List<String> result = new ArrayList<>();
       
       for (Integer anum : spInt){ 
          switch (anum){
              case 0:
                     result.add("A");
                     break ;
              case 1:
                     result.add("B");
                     break ;
              case 2:
                     result.add("C");
                     break ;
              case 3:
                     result.add("D");
                     break ;
              case 4:
                     result.add("E");
                     break ;
              case 5:
                     result.add("F");
                     break ;
              case 6:
                     result.add("G");
                     break ;
              case 7:
                     result.add("H");
                     break ;
              case 8:
                     result.add("I");
                     break ;
              case 9:
                     result.add("J");
                     break ;
              case 10:
                     result.add("K");
                     break ;
              case 11:
                     result.add("L");
                     break ;
              case 12:
                     result.add("M");
                     break ;
              case 13:
                     result.add("N");
                     break ;
              case 14:
                     result.add("O");
                     break ;
              case 15:
                     result.add("P");
                     break ;
              case 16:
                     result.add("Q");
                     break ;
              case 17:
                     result.add("R");
                     break ;
              case 18:
                     result.add("S");
                     break ;
              case 19:
                     result.add("T");
                     break ;
              case 20:
                     result.add("U");
                     break ;
              case 21:
                     result.add("V");
                     break ;
              case 22:
                     result.add("W");
                     break ;
              case 23:
                     result.add("X");
                     break ;
              case 24:
                     result.add("Y");
                     break ;
              case 25:
                     result.add("Z");
                     break ;
              case 26:
                     result.add("a");
                     break ;
              case 27:
                     result.add("b");
                     break ;
              case 28:
                     result.add("c");
                     break ;
              case 29:
                     result.add("d");
                     break ;
              case 30:
                     result.add("e");
                     break ;
              case 31:
                     result.add("f");
                     break ;
              case 32:
                     result.add("g");
                     break ;
              case 33:
                     result.add("h");
                     break ;
              case 34:
                     result.add("i");
                     break ;
              case 35:
                     result.add("j");
                     break ;
              case 36:
                     result.add("k");
                     break ;
              case 37:
                     result.add("l");
                     break ;
              case 38:
                     result.add("m");
                     break ;
              case 39:
                     result.add("n");
                     break ;
              case 40:
                     result.add("o");
                     break ;
              case 41:
                     result.add("p");
                     break ;
              case 42:
                     result.add("q");
                     break ;
              case 43:
                     result.add("r");
                     break ;
              case 44:
                     result.add("s");
                     break ;
              case 45:
                     result.add("t");
                     break ;
              case 46:
                     result.add("u");
                     break ;
              case 47:
                     result.add("v");
                     break ;
              case 48:
                     result.add("w");
                     break ;
              case 49:
                     result.add("x");
                     break ;
              case 50:
                     result.add("y");
                     break ;
              case 51:
                     result.add("z");
                     break ;
              case 52:
                     result.add("0");
                     break ;
              case 53:
                     result.add("1");
                     break ;
              case 54:
                     result.add("2");
                     break ;
              case 55:
                     result.add("3");
                     break ;
              case 56:
                     result.add("4");
                     break ;
              case 57:
                     result.add("5");
                     break ;
              case 58:
                     result.add("6");
                     break ;
              case 59:
                     result.add("7");
                     break ;
              case 60:
                     result.add("8");
                     break ;
              case 61:
                     result.add("9");
                     break ;
              case 62:
                     result.add("+");
                     break ;
              case 63:
                     result.add("/");              
                     break ;
             }//end switch
       
        }//end for   
        return(result) ;
   }  
   
      public static List<Integer> decodeCharacters (byte[] spByte ,int size) throws Exception{
       List<Integer> result = new ArrayList<>();
       byte[] one_part = new byte[1] ;
       for (int i = 0 ; i < size ; i++){
           one_part[0] = spByte[i];
           String str = new String(one_part,"UTF-8") ;
           switch (str){
              case "A":
                     result.add(0);
                     break ;
              case "B":
                     result.add(1);
                     break ;
              case "C":
                     result.add(2);
                     break ;
              case "D":
                     result.add(3);
                     break ;
              case "E":
                     result.add(4);
                     break ;
              case "F":
                     result.add(5);
                     break ;
              case "G":
                     result.add(6);
                     break ;
              case "H":
                     result.add(7);
                     break ;
              case "I":
                     result.add(8);
                     break ;
              case "J":
                     result.add(9);
                     break ;
              case "K":
                     result.add(10);
                     break ;
              case "L":
                     result.add(11);
                     break ;
              case "M":
                     result.add(12);
                     break ;
              case "N":
                     result.add(13);
                     break ;
              case "O":
                     result.add(14);
                     break ;
              case "P":
                     result.add(15);
                     break ;
              case "Q":
                     result.add(16);
                     break ;
              case "R":
                     result.add(17);
                     break ;
              case "S":
                     result.add(18);
                     break ;
              case "T":
                     result.add(19);
                     break ;
              case "U":
                     result.add(20);
                     break ;
              case "V":
                     result.add(21);
                     break ;
              case "W":
                     result.add(22);
                     break ;
              case "X":
                     result.add(23);
                     break ;
              case "Y":
                     result.add(24);
                     break ;
              case "Z":
                     result.add(25);
                     break ;
              case "a":
                     result.add(26);
                     break ;
              case "b":
                     result.add(27);
                     break ;
              case "c":
                     result.add(28);
                     break ;
              case "d":
                     result.add(29);
                     break ;
              case "e":
                     result.add(30);
                     break ;
              case "f":
                     result.add(31);
                     break ;
              case "g":
                     result.add(32);
                     break ;
              case "h":
                     result.add(33);
                     break ;
              case "i":
                     result.add(34);
                     break ;
              case "j":
                     result.add(35);
                     break ;
              case "k":
                     result.add(36);
                     break ;
              case "l":
                     result.add(37);
                     break ;
              case "m":
                     result.add(38);
                     break ;
              case "n":
                     result.add(39);
                     break ;
              case "o":
                     result.add(40);
                     break ;
              case "p":
                     result.add(41);
                     break ;
              case "q":
                     result.add(42);
                     break ;
              case "r":
                     result.add(43);
                     break ;
              case "s":
                     result.add(44);
                     break ;
              case "t":
                     result.add(45);
                     break ;
              case "u":
                     result.add(46);
                     break ;
              case "v":
                     result.add(47);
                     break ;
              case "w":
                     result.add(48);
                     break ;
              case "x":
                     result.add(49);
                     break ;
              case "y":
                     result.add(50);
                     break ;
              case "z":
                     result.add(51);
                     break ;
              case "0":
                     result.add(52);
                     break ;
              case "1":
                     result.add(53);
                     break ;
              case "2":
                     result.add(54);
                     break ;
              case "3":
                     result.add(55);
                     break ;
              case "4":
                     result.add(56);
                     break ;
              case "5":
                     result.add(57);
                     break ;
              case "6":
                     result.add(58);
                     break ;
              case "7":
                     result.add(59);
                     break ;
              case "8":
                     result.add(60);
                     break ;
              case "9":
                     result.add(61);
                     break ;
              case "+":
                     result.add(62);
                     break ;
              case "/":
                     result.add(63);              
                     break ;
             }//end switch
       
        }//end for   
        return(result) ;
   } 
      
   public static Integer ConvertKey(char keyChar) {
      switch (keyChar){
              case 'A':
                     return(1);
              case 'B':
                     return(2);
              case 'C':
                     return(3);
              case 'D':
                     return(4);
              case 'E':
                     return(5);
              case 'F':
                     return(6);
              case 'G':
                     return(7);
              case 'H':
                     return(8);
              case 'I':
                     return(9);
              case 'J':
                     return(10);
              case 'K':
                     return(11);
              case 'L':
                     return(12);
              case 'M':
                     return(13);
              case 'N':
                     return(14);
              case 'O':
                     return(15);
              case 'P':
                     return(16);
              case 'Q':
                     return(17);
              case 'R':
                     return(18);
              case 'S':
                     return(19);
              case 'T':
                     return(20);
              case 'U':
                     return(21);
              case 'V':
                     return(22);
              case 'W':
                     return(23);
              case 'X':
                     return(24);
              case 'Y':
                     return(25);
              case 'Z':
                     return(26);
              case 'a':
                     return(27);
              case 'b':
                     return(28);
              case 'c':
                     return(29);
              case 'd':
                     return(30);
              case 'e':
                     return(31);
              case 'f':
                     return(32);
              case 'g':
                     return(33);
              case 'h':
                     return(34);
              case 'i':
                     return(35);
              case 'j':
                    return(36);
              case 'k':
                     return(37);
              case 'l':
                     return(38);
              case 'm':
                     return(39);
              case 'n':
                     return(40);
              case 'o':
                     return(41);
              case 'p':
                     return(42);
              case 'q':
                     return(43);
              case 'r':
                    return(44);
              case 's':
                     return(45);
              case 't':
                     return(46);
              case 'u':
                     return(47);
              case 'v':
                     return(48);
              case 'w':
                     return(49);
              case 'x':
                     return(50);
              case 'y':
                     return(51);
              case 'z':
                     return(52);
              case '0':
                     return(53);
              case '1':
                     return(54);
              case '2':
                     return(55);
              case '3':
                     return(56);
              case '4':
                     return(57);
              case '5':
                     return(58);
              case '6':
                     return(59);
              case '7':
                     return(60);
              case '8':
                     return(61);
              case '9':
                     return(62);
              case '+':
                    return(63);
              case '/':
                     return(64);
              default: return(-1) ;    
             }//end switch
       
                 
}
   
}
