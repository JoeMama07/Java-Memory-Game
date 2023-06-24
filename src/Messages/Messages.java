package Messages;

public class Messages {
    public void defaultErrorMessage(String error){
        System.out.println("Something went wrong, Please try again " + error);
    }

    public void errorMessage(String message) {
        System.out.println(message);
    }
    public void successMessage(String message){
        System.out.println(message);
    }

    public void invalidMessage(){
        System.out.println("Invalid character");
    }
}
