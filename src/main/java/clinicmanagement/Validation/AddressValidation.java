package clinicmanagement.Validation;

public class AddressValidation {


    public static void ValidateAddress(String address){

        if(address.isEmpty() || address.isBlank()){
            throw new IllegalArgumentException("Address is required !");
        }
        if(address.length() > 255){
            throw new IllegalArgumentException("address must be less than 255 character !");
        }
    }
}
