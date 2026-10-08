package clinicmanagement.Validation;

public class CinValidation {


    public static void ValidateCin(String cin){

        if(cin.isEmpty() || cin.isBlank()){
            throw new IllegalArgumentException("CIN is required !");
        }
        if(cin.length() <= 8){
            throw new IllegalArgumentException("CIN must be more than 7 and less than 9 !");
        }
    }
}
