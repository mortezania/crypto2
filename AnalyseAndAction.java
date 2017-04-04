
package crypto;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;



public class AnalyseAndAction {
   
    final int encode_read_block_size = 3 ;
    final int decode_read_block_size = 4 ;
    
    int number_of_pading_bits = 2 ; //2 or 4 related to input file size
    
    int action_number ;// 1:Simple Enecode - 2:Complex Encode - 3:Simple Decode   4:Complex Decode 
    
    boolean Is_ES_switch_InInputParameters = false ;
    boolean Is_DS_switch_InInputParameters = false ;
    boolean Is_EC_switch_InInputParameters = false ;
    boolean Is_DC_switch_InInputParameters = false ;
    boolean Is_I_Switch_InInputParameters = false ;
    boolean Is_R_switch_InInputParameters = false ;
    boolean Is_O_switch_InInputParameters = false ;
   
    boolean Is_correct_to_be_any_switche_now = true ;

    String input_File_Complete_Name = null ;
  
    ArrayList<String> input_File_Name = new ArrayList<>() ;
    
    String input_File_Directory = null ;
    
    boolean input_File_is_Directory = false ;
     
    String output_Directory_Path = null ;
       
    String cypher_key = null ;
    
    public AnalyseAndAction() {
          }
    
    public boolean analyseArgs(String[] args){
       
        if (args.length == 0 || args.length > 7) {
            System.out.println("Error: Invalid Input Parameters");
            return false;
         }
        else {
            for (int i=0 ; i< args.length;i++ ){
              switch (args[i]){
                case "-es":
                         if (Is_there_previous_encriptionSwitches()) {
                            System.out.println("Error: Invalid Input Parameters"); 
                            return false;
                           } 
                         else {
                            Is_ES_switch_InInputParameters = true ;
                            Is_correct_to_be_any_switche_now = false  ;   
                            action_number = 1 ;
                            break ;
                         }
                case "-ds":
                        if (Is_there_previous_encriptionSwitches()) {
                            System.out.println("Error: Invalid Input Parameters"); 
                            return false;
                           } 
                         else {    
                            Is_DS_switch_InInputParameters = true ;
                            Is_correct_to_be_any_switche_now = false  ; 
                            action_number = 3 ;
                            break ;
                        }   
                           
                case "-ec":
                        if (Is_there_previous_encriptionSwitches()) {
                            System.out.println("Error: Invalid Input Parameters"); 
                            return false;
                           } 
                         else {
                            Is_EC_switch_InInputParameters = true ;
                            Is_correct_to_be_any_switche_now = false  ;   
                            action_number = 2 ;
                            break ;
                         }
                
                 
                case "-dc":
                        if (Is_there_previous_encriptionSwitches()) {
                            System.out.println("Error: Invalid Input Parameters"); 
                            return false;
                           } 
                         else {    
                            Is_DC_switch_InInputParameters = true ;
                            Is_correct_to_be_any_switche_now = false  ; 
                            action_number = 4 ;
                            break ;
                        }   
                case "-i":
                           if (Is_correct_to_be_any_switche_now) {
                               Is_I_Switch_InInputParameters = true ;
                               Is_correct_to_be_any_switche_now = false ;
                               break ;
                            }
                           else{
                               System.out.println("Error: Invalid Input Parameters"); 
                               return false;
                           }
                case "-o":
                          if (Is_correct_to_be_any_switche_now) {
                               Is_ES_switch_InInputParameters = true ;
                               Is_correct_to_be_any_switche_now = false ;
                               break ;
                            }
                          else {
                               System.out.println("Error: Invalid Input Parameters"); 
                               return false;
                           }
                case "-r":  
                           if (Is_correct_to_be_any_switche_now) {
                               Is_R_switch_InInputParameters = true ;
                               Is_correct_to_be_any_switche_now = true ;  
                               break ;
                             }
                           else {
                               System.out.println("Error: Invalid Input Parameters"); 
                               return false;
                           }
                default:  
                           if (Is_correct_to_be_any_switche_now){
                                System.out.println("Error: Invalid Input ");
                                return false;
                                }
                           else {
                                if (args[i-1].equals("-i")){
                                    input_File_Complete_Name = args[i] ;
                                    Is_correct_to_be_any_switche_now =  true;
                                    break ; 
                                   }
                                if (args[i-1].equals("-o")) {  
                                    output_Directory_Path = args[i] ;
                                    Is_correct_to_be_any_switche_now =  true;
                                    break ; 
                                }
                                if (args[i-1].equals("-dc") || args[i-1].equals("-ec") || args[i-1].equals("-es") || args[i-1].equals("-ds")){
                                    cypher_key = args[i] ; 
                                    Is_correct_to_be_any_switche_now = true ; 
                                    break ;
                                  }
                                System.out.println("Error: Invalid Input Parameters");
                                return false; 
                              }
                          }//ens switch
                      } //end for
            return true ;
            } //end else   
    }//end method
    
   /////////////////////// 
    
   boolean Is_there_previous_encriptionSwitches(){
        if(Is_ES_switch_InInputParameters || Is_DS_switch_InInputParameters || Is_EC_switch_InInputParameters || Is_DC_switch_InInputParameters) 
            return true ;
        return false ;
  
      }
   
   //////////////////////
   public boolean verify_Files_pathes() {
       //1. Verify Input File(s) Path
       if (input_File_Complete_Name != null) {
          File f = new File(input_File_Complete_Name);
          if(f.exists()){
             if (f.isDirectory()) { 
                input_File_is_Directory = true;
                input_File_Directory = input_File_Complete_Name ;
                File folder = new File(input_File_Directory);
                File[] listOfFiles = folder.listFiles();
                for (int i = 0; i < listOfFiles.length; i++) {
                   if (listOfFiles[i].isFile()) {
                      input_File_Name.add(listOfFiles[i].getName()) ;  
                   }
                }
              }
             else{
               input_File_Directory = f.getParent() ;
               input_File_Name.add(f.getName()) ;
                        }
           }
          else { 
              System.out.println("Error: Invalid Input File Path/Name");
              return false ;
          }
       }
       else {
           System.out.println("Error: Missing Input File Name");
           return false ;
       }
       
       //2. Verify Output Path
       if (output_Directory_Path != null) {
         Path output_path = Paths.get(output_Directory_Path);
         try {
            Files.createDirectories(output_path);
             } 
         catch (IOException e) {
             System.out.println("Error: Invalid Output File Path");
             return false ;
          }
        }
       else {
          output_Directory_Path = input_File_Directory ;
          return true ;
       }
   
       
    return true ; //useless command
   }
   //////////////////////
   public void takeAction(){
       switch(action_number){
           case 1: //simple encode and remove inputfile if required
                   {
                    SimpleCryptography sc = new SimpleCryptography(encode_read_block_size,input_File_Directory,input_File_Name,output_Directory_Path,Is_R_switch_InInputParameters);
                    sc.encrypt(cypher_key);
                   }
                    break ;
           case 2:
                   //Complex encode and remove inputfile if required
                   {
                    ComplexCryptography cc = new ComplexCryptography(encode_read_block_size,input_File_Directory,input_File_Name,output_Directory_Path,Is_R_switch_InInputParameters);
                    cc.encrypt(cypher_key);
                   }
                    break ;

           case 3://simple decode and remove inputfile if required
                   {
                    SimpleCryptography sc = new SimpleCryptography(decode_read_block_size,input_File_Directory,input_File_Name,output_Directory_Path,Is_R_switch_InInputParameters);
                    sc.decrypt(cypher_key);
                   }
                   break ; 
           case 4:   
                  //Complex decode and remove inputfile if required
                   {
                    ComplexCryptography cc = new ComplexCryptography(decode_read_block_size,input_File_Directory,input_File_Name,output_Directory_Path,Is_R_switch_InInputParameters);
                    cc.decrypt(cypher_key);
                   }
                   break ; 
         }
     
        
       
       
       
       
    }//end method
    
}//end class
