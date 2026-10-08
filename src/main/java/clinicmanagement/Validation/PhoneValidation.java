package clinicmanagement.Validation;


public class PhoneValidation {


    public static void validatePhone(String phone){

        if(phone == null || phone.isEmpty()){
            throw new IllegalArgumentException("Phone number is required !");
        }
    }
}
