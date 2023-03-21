import Ipe.Handler.InputHandler;

public class IpeCG {
    public IpeCG(String[] args) {
        if(args.length > 1) {
            new InputHandler(args);
        }
        else {
            System.out.println("requires valid arguments");
        }
    }
}
