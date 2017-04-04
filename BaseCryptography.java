
package crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;


public abstract class BaseCryptography {
   
    List<Integer> cypherKey ;
    int cypherKey_Size ;
    
    int block_size ;
    ArrayList<String> input_File_Name ;
    String input_File_Directory  ;
    String output_Directory_Path ; 
   
    
    int remaining_File_Count = 0 ;
   
    long currentFile_RemainingBlocks  ;
    long currentRead_Block_Number ; 
    int currentFile_LastBytesCount = 0 ; 
    boolean currentFile_IS_RemainingLastBytes = false ;
    int currentFile_PadingBits = 0 ;
    int current_input_File_index = 0 ;
    int last_reading_byte_size = 0 ;
    
    String intermediate_Str ;
    List<Integer> intermediate_integerResults ;
    List<String> intermediate_stringResults ;
    byte[] intermediate_byteResults ;
    
    byte[] buffer ;
       
    FileInputStream current_FileinputStream = null  ;
    BufferedInputStream current_BufferedInputStream = null ;
    File current_inputFilePointer = null ;
    String current_inputFile_AbsolutePath ;
    String current_outputFile_AbsolutePath ;
    
    FileOutputStream current_FileoutputStream = null  ;
    BufferedOutputStream current_BufferedOutputStream = null ;
    
    boolean removeInputFlag ;
    int encryptDecryptFlag = 0 ; 
     
    public BaseCryptography(int block_size, String input_File_Directory,ArrayList<String> input_File_Name, String output_Directory_Path,boolean removeInputFlag) {
        this.block_size = block_size;
        this.input_File_Name = input_File_Name;
        this.input_File_Directory = input_File_Directory;
        this.output_Directory_Path = output_Directory_Path;
        this.removeInputFlag = removeInputFlag ;
        this.remaining_File_Count = input_File_Name.size() ;
        this.current_input_File_index = 0 ;
        this.buffer = new byte[block_size];
    }
    
   public boolean Is_there_File_For_Read(){
      if (remaining_File_Count > 0) 
          return true ;
      else return false ;
       
   }
    
   public void setEncryptDecryptFlag(int aflag){
       //set flag zero for encrypt and 1 for decrypt
       encryptDecryptFlag = aflag ;
       return ;
      }
   
   public void setCypherKey(List<Integer> acypherKey){
       cypherKey = acypherKey ;
       cypherKey_Size = acypherKey.size() ;
        }
    
   public boolean Is_there_Part_For_Read(){
      if ( currentFile_RemainingBlocks > 0 || currentFile_IS_RemainingLastBytes) 
          return true ;
      else return false ;
      }
   
   
   public boolean initialize_currentInputFile(){
      
      current_inputFile_AbsolutePath = input_File_Directory + File.separator + input_File_Name.get(current_input_File_index);
      try { 
         File file = new File(current_inputFile_AbsolutePath);
         current_inputFilePointer = file ;
         current_FileinputStream = new FileInputStream(file);
         current_BufferedInputStream = new BufferedInputStream(current_FileinputStream);
         System.out.println("Message: Start Reading From Input File , FileName = " + current_inputFile_AbsolutePath);
         return true;
          }
       catch (Exception e){
          System.out.println("Error: Input File Not Found , FileName= " + current_inputFile_AbsolutePath );
          return false ;
       }
      
          }
  
 public boolean initialize_currentOutputFile(){
     
    current_outputFile_AbsolutePath="" ;
    try { 
      //Check existance of a directory
      File dir_file = new File(output_Directory_Path);
      if (!dir_file.exists()) {
          System.out.print("Message: Output Directory Not Exist , DirectoryName= " + output_Directory_Path);
          dir_file.mkdir();
          System.out.print("Message: Output Directory Created , DirectoryName= " + output_Directory_Path);
        }
      current_outputFile_AbsolutePath = output_Directory_Path + File.separator + defineOutputFileName(); 
      File file = new File(current_outputFile_AbsolutePath);
      if (! file.createNewFile()){
          System.out.println("Message: Output File Exist , FileName = " + current_outputFile_AbsolutePath);
          if(! file.delete()){
              System.out.println("Error: Existed Output File Can not Delete , FileName = " +  current_outputFile_AbsolutePath);
              return false ;   
             }
          else if (!file.createNewFile()){
              System.out.println("Error: Output File Can not create , FileName = " + current_outputFile_AbsolutePath);
              return false ;
             }
         }//end if
        //outputfile created	    
	current_FileoutputStream =  new FileOutputStream(file);
        current_BufferedOutputStream = new BufferedOutputStream(current_FileoutputStream) ;
        System.out.println("Message: Output File Created , FileName = " + current_outputFile_AbsolutePath);
        return true ;
        }                                        
     catch (Exception e){
          System.out.println("Error: Can not Create Output File , FileName = " + current_outputFile_AbsolutePath );
          return false ;
       }
      
      
     }
 
   public String defineOutputFileName() throws Exception{
       if (encryptDecryptFlag == 0)
           return (input_File_Name.get(current_input_File_index) + ".crypt" );
       else {
           if (input_File_Name.get(current_input_File_index).contains(".crypt")){
             //then remove .crypt from end of file name
             String[] parts = input_File_Name.get(current_input_File_index).split(".crypt", 2);
             return parts[0] ;
            }
           else {
               System.out.println("Error: Input File Extension Name is Incorrect , FileName = " + input_File_Name.get(current_input_File_index) );
               throw new Exception("Output File Name Error");
                 }
                   
       }
   }     
   
   
   
   public boolean initialize_CurrentFileBlockCount() {
      try{  
         if (this.encryptDecryptFlag == 0) {// encryption Process
             currentFile_RemainingBlocks = current_BufferedInputStream.available() /block_size ;
             currentFile_LastBytesCount = current_BufferedInputStream.available() % block_size ;
              }
         else { //decryption Process
             //we must minus header lenght from actual file size
             currentFile_RemainingBlocks = (current_BufferedInputStream.available()-2) /block_size ;
             currentFile_LastBytesCount = (current_BufferedInputStream.available() -2) % block_size ;
             }
         currentRead_Block_Number = 0 ;
          }
       catch (Exception e){
          System.out.println("Error: Can not Calculate Current Input File Block Size" );
          return false ;
       }
      
      
      if (currentFile_LastBytesCount == 0) 
          currentFile_IS_RemainingLastBytes = false ;
      else 
          currentFile_IS_RemainingLastBytes = true ;
        
      return true ;
   }
   
   public boolean  verifyingHeader_in_CurrentInputFile(String encryption_type){
      try {
           System.out.println("Message: Starting Decryption Process ");
           System.out.println("Message: Reading Header from Input File ");
           byte[] header_part1 = new byte[1] ;
           byte[] header_part2 = new byte[1] ;
           current_BufferedInputStream.read(header_part1,0,1) ;
           current_BufferedInputStream.read(header_part2,0,1) ;
           
           String  encryption_type_of_inputFile =  new String(header_part1, "UTF-8");
           currentFile_PadingBits = Integer.valueOf(new String(header_part2, "UTF-8")) ;
         
           //if Input file not have aproprate header for Simple Encoding
           if ((! encryption_type_of_inputFile.equals(encryption_type)) || ! (currentFile_PadingBits == 0 ||  currentFile_PadingBits == 2 || currentFile_PadingBits == 4 )){
               System.out.println("Error: Header of Encrypted Input File is Inaproprate ");
               return false ;
            }  
           return true ;
           }
      catch (Exception e) {
         System.out.println("Error: Can not Read Header from Input File , FileName= " + current_inputFile_AbsolutePath);
         return false ; 
      }
     }
   
   public boolean  writingHeader_In_CurrentOutputFile(String encryption_type){
      try {
           System.out.println("Message: Starting Encryption Process ");
           current_BufferedOutputStream.write(encryption_type.getBytes("UTF-8")[0]);
           
            if (currentFile_LastBytesCount == 0) 
               currentFile_PadingBits = 0 ;
           else {
               if (currentFile_LastBytesCount == 1) 
                   currentFile_PadingBits = 2 ; 
               else 
                   currentFile_PadingBits = 4 ;
                }   
           current_BufferedOutputStream.write(String.valueOf(currentFile_PadingBits).getBytes("UTF-8")[0]);
           System.out.println("Message: Writing Header in Output File ");
           return true ;
        }
      catch (Exception e) {
         System.out.println("Error: Can not Write Header in Output File , FileName= " + current_outputFile_AbsolutePath);
         return false ; 
      }
     }
   
   public boolean write_A_Part_InEncoding(){
      
      try {
            String str = "" ;
            for (String element : intermediate_stringResults) str = str + element ;
            current_BufferedOutputStream.write(str.getBytes("UTF-8"));
            return true ;
        }
      catch (Exception e) {
           System.out.println("Error: Can not Write Blocks into Output File , FileName= " + current_outputFile_AbsolutePath);
           return false ; 
      }
     
    }
   
    public boolean write_A_Part_InDecoding(){
      
      try {
           current_BufferedOutputStream.write(intermediate_byteResults);
            return true ;
        }
      catch (Exception e) {
           System.out.println("Error: Can not Write Blocks into Output File , FileName= " + current_outputFile_AbsolutePath);
           return false ; 
      }
     
    }
     
   public boolean read_A_Part_InEncoding(){
      try{ 
        if (currentFile_RemainingBlocks != 0 ) {    
           last_reading_byte_size = block_size ;
           //read a block of 3 byte for encode process
           currentRead_Block_Number ++ ;
           current_BufferedInputStream.read(buffer) ;
           //convert read bytes of input file to a string of 0 and 1 characters  
           intermediate_Str = Coding.convertTo_StringOfZeroAndOne_InEncoding(buffer,last_reading_byte_size); 
           currentFile_RemainingBlocks -- ;
           
             }
        else { //there is last bytes for read
            current_BufferedInputStream.read(buffer,0 , currentFile_LastBytesCount) ;
            last_reading_byte_size = currentFile_LastBytesCount ;
            intermediate_Str = Coding.convertTo_StringOfZeroAndOne_InEncoding(buffer,last_reading_byte_size); 
            currentFile_IS_RemainingLastBytes = false ;
          }  
        return true ;
      }
       catch (Exception e){
          System.out.println("Error: Can not Read From Current Input File" );
          return false ;
       }
   }
   
  public boolean read_A_Part_InDecoding(){
     try { 
        if (currentFile_RemainingBlocks != 0 ) {    
           //read a block of 4 byte for decode process
           currentRead_Block_Number ++ ;
           current_BufferedInputStream.read(buffer) ;
           last_reading_byte_size = block_size ;
           currentFile_RemainingBlocks -- ; 
          
         }
        else { //there is last bytes for read   
           current_BufferedInputStream.read(buffer,0 , currentFile_LastBytesCount) ;
           last_reading_byte_size = currentFile_LastBytesCount ;
           currentFile_IS_RemainingLastBytes = false ;
          }  
        return true ;
         }
      catch (Exception e){
          System.out.println("Error: Can not Read From Current Input File" );
          return false ;
       }
   }
    
 
  
  public void splite_A_Part_InEncoding(){
      
       //splite previous string to array of strings (include 0 and 1) with coding_bit_size lenght (6 in this project) 
       //and then convert every splited string to integer 
       intermediate_stringResults = Coding.splitEqually_InEncoding(intermediate_Str); 
    
    }
  
  public boolean splite_A_Part_InDecoding(){
      
     //splite previous string to array of strings (include 0 and 1) with 8 bit size lenght 
     try{  
       intermediate_stringResults = Coding.splitEqually_InDecoding(intermediate_Str); 
       return true ;
     }
     catch (Exception e){
       System.out.println("Error : Encrypted Input File Is Corrupted");
       return false ;
     }
    
    }
  
   public void convert_A_Part_ToByte(){
        int i = 0 ;
        intermediate_byteResults = new byte[intermediate_stringResults.size()];
        for (String element : intermediate_stringResults){
             intermediate_byteResults[i] = (byte) Integer.parseInt(element, 2);
             i++ ;
        } 
   }
  
   public void convert_A_Part_ToInteger(){
      
      //convert every splited string to integer 
     intermediate_integerResults = new ArrayList<>(intermediate_stringResults.size()) ;
     for (String listItem: intermediate_stringResults) {
        intermediate_integerResults.add(Integer.parseInt(listItem,2)) ;
           }
    
   }
  
  
   public void shiftSimple_A_Part(){
       
       UnaryOperator<Integer> shift_unaryOperator = n -> { int new_n = n + cypherKey.get(0) ; 
                                                           if (new_n >= 0 && new_n <= 63) return new_n;
                                                           else {
                                                              if (new_n < 0) return (64 + new_n) ; 
                                                              else return (new_n - 64);
                                                               }
                                                            };
       
      
       intermediate_integerResults.replaceAll(shift_unaryOperator);
           
         
   }  
  
   public void shiftComplex_A_Part(){
       /*
       UnaryOperator<Integer> shift_unaryOperator = n -> { 
                                                           //notice that you should not multiply in block size becaue every block convert to 4 character finally
                                                           long real_index = ((currentRead_Block_Number - 1) * 4) ;                                                                   
                                                           real_index += intermediate_integerResults.indexOf(n)  ;
                                                           long size = shiftCount.size();
                                                           int new_n = n + shiftCount.get((int)(real_index % size)) ; 
                                                           if (new_n >= 0 && new_n <= 63) return new_n;
                                                           else {
                                                              if (new_n < 0) return (64 + new_n) ; 
                                                              else return (new_n - 64);
                                                               }
                                                            };
       */
       
     long real_index = ((currentRead_Block_Number - 1) * 4) ;                                                                   
     Integer tempInt ;
     for (int i = 0 ; i < intermediate_integerResults.size() ; i++) {
        tempInt = intermediate_integerResults.get(i) + cypherKey.get((int)((real_index + i)% cypherKey_Size)) ; 
        intermediate_integerResults.remove(i);
        if (tempInt >= 0 && tempInt <= 63) {
            intermediate_integerResults.add(i,tempInt) ;
        }
        else {
            if (tempInt  < 0) 
               intermediate_integerResults.add(i,64 + tempInt) ;
            else 
               intermediate_integerResults.add(i, tempInt - 64);
             }
        }
     
        
       //intermediate_integerResults.replaceAll(shift_unaryOperator);
           
         
   }  
  
   
   
   public void convert_A_Part_toZeroAndOne(){
       //convert integers to a string of 0 and 1 characters  
       if (last_reading_byte_size == 4)
          intermediate_Str = Coding.convertTo_StringOfZeroAndOne_InDecoding(intermediate_integerResults ,last_reading_byte_size); 
       else { 
          intermediate_Str = Coding.convertTo_StringOfZeroAndOne_InDecoding(intermediate_integerResults ,last_reading_byte_size -1 );  
          intermediate_Str = intermediate_Str + Coding.removePadding_InDecoding(intermediate_integerResults.get(last_reading_byte_size -1) , currentFile_PadingBits) ;
       }
    }
   
   public void encode_A_Part(){
      
       //convert every integer to related character string 
       //and finally return a character for every splited integer in previous step
       intermediate_stringResults = Coding.encodeIntegers(intermediate_integerResults); 
    }
   
   
   public boolean decode_A_Part(){
       //convert every character that read in previous step to integer 
       try {
          intermediate_integerResults = Coding.decodeCharacters(buffer,last_reading_byte_size); 
          return true ; 
       }
       catch(Exception e){
         System.out.println("Error: Encrypted Input File is Corrupted");
         return false ;
       }
       
    }
   
   
 public void removeInputResource (){
    if (this.removeInputFlag) { 
        try { 
           current_inputFilePointer.delete() ;
           System.out.println("Message: Delete Input File , FileName= " + current_inputFile_AbsolutePath);   
         }
        catch (Exception e){
           System.out.println("Error: Can Not Delete Input File , FileName= " + current_inputFile_AbsolutePath );  
           }

       }
     
    }
   
   
 public boolean closeResources(){
     try{      
         if (current_BufferedInputStream != null)
           current_BufferedInputStream.close() ;
         
         if (current_FileinputStream != null)
           current_FileinputStream.close() ;
         
          if (current_BufferedOutputStream != null)
           current_BufferedOutputStream.close() ;
         
         if (current_FileoutputStream != null)
           current_FileoutputStream.close() ;
         
         System.out.println("Message: Ending Encryption Process ");
         return true;
       }
     catch(Exception e){
          System.out.println("Error: Can not Close Resources" );
          return false ;
     }
 }

    public void reCalculate_RemainingFiles(){
        current_input_File_index ++ ;
        remaining_File_Count -- ;  
      }

    
    public abstract void encrypt(String cipherText);
    public abstract void decrypt(String cipherText);
    
   
    
    

}//end class
