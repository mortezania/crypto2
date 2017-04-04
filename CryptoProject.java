
package crypto;


public class CryptoProject {
 
    public static void main(String[] args) {
        AnalyseAndAction ai = new AnalyseAndAction() ;
        if (ai.analyseArgs(args) && ai.verify_Files_pathes()){
           ai.takeAction();
        } 
        
     }
   
}
