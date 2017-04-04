
package crypto;

import java.util.ArrayList;
import java.util.List;


public class SimpleCryptography extends BaseCryptography {

    List<Integer> shiftCount_forEncryption ;
    List<Integer> shiftCount_forDecryption ;
    
    boolean removeInputFlag ;
    final String SIMPLE_ENCRYPT ="0" ;
    
    
    public SimpleCryptography(int block_size, String input_File_Directory,ArrayList<String> input_File_Name, String output_File_Path,boolean removeInputFlag) {
        super(block_size, input_File_Directory,input_File_Name, output_File_Path,removeInputFlag);
    }
   

    public void encrypt(String key) {
        if (! is_Key_Correct(key))
           return ;
        setEncryptDecryptFlag(0) ; // 0 foe encode
        setCypherKey(shiftCount_forEncryption);
        while (Is_there_File_For_Read()){
           if (!initialize_currentInputFile()) {reCalculate_RemainingFiles();}
           if (!initialize_currentOutputFile()) {reCalculate_RemainingFiles();}
           if (!initialize_CurrentFileBlockCount()) {reCalculate_RemainingFiles(); continue ;}
           if(! writingHeader_In_CurrentOutputFile(SIMPLE_ENCRYPT)) {reCalculate_RemainingFiles(); continue ;}
           while(Is_there_Part_For_Read()){
                 if (!read_A_Part_InEncoding()) break ; 
                 splite_A_Part_InEncoding();
                 convert_A_Part_ToInteger() ;
                 shiftSimple_A_Part() ;
                 encode_A_Part() ;
                 if (!write_A_Part_InEncoding()) break ;       
               }//end while
           closeResources();
           removeInputResource() ;
           reCalculate_RemainingFiles() ;
          }//end outer While
      }
    
  public void decrypt(String key){
       if (! is_Key_Correct(key))
           return ;
       setEncryptDecryptFlag(1) ; // 1 for decode
       setCypherKey(shiftCount_forDecryption);
       while (Is_there_File_For_Read()){
           if (!initialize_currentInputFile()) {reCalculate_RemainingFiles();}
           if (!initialize_currentOutputFile()) {reCalculate_RemainingFiles();}
           if (!initialize_CurrentFileBlockCount()) {reCalculate_RemainingFiles(); continue ;}
           if(! verifyingHeader_in_CurrentInputFile(SIMPLE_ENCRYPT)) {reCalculate_RemainingFiles(); continue ;}
           while(Is_there_Part_For_Read()){
                 if (!read_A_Part_InDecoding()) break ; 
                 if(!decode_A_Part()) break ;
                 shiftSimple_A_Part() ;
                 convert_A_Part_toZeroAndOne();
                 if(!splite_A_Part_InDecoding()) break;
                 convert_A_Part_ToByte() ;
                 if (!write_A_Part_InDecoding()) break ;       
               }//end while
           closeResources();
           removeInputResource() ;
           reCalculate_RemainingFiles() ;
          }//end outer While
    
      }
  
public boolean is_Key_Correct(String key){
    
    try {
        shiftCount_forEncryption = new ArrayList<>(1) ;
        shiftCount_forDecryption = new ArrayList<>(1) ;
       
        int shift = Integer.valueOf(key) ;
        
        shiftCount_forEncryption.add(shift) ;
        shiftCount_forDecryption.add(-1 * shift) ;
       
        if (shift >=0 || shift < 62) //Correct Shift key is between 0 to 62  
          return true ;
       else {
          System.out.println("Error: Invalid Cypher key for Simple Cryptography");  
          return false ;
         }
       }
    catch (Exception e){
        System.out.println("Error: Invalid Cypher key for Simple Cryptography");  
        return false ;
    }
    
  
   
} 
}
