import java.util.Arrays;
import java.util.List;

import ui.AbstractUI;

public class SimpleMachine {

  /**
   * Generic entry-point for executing the simple machine.
   * @param args command-line arguments, using the following syntax:
   *             "-i [cli|gui] -a [sm213|y86seq|y86pipeminus|y86pipe|y86pipesuper] -v [solution|student]".
   *             additional arguments are defined by specific user-interface implementation.
   */

  public final static class Sm213 {
    public static void main (String[] args) {
      SimpleMachine.main (new String[] {"-a", "sm213", "-v", "solution"}, args);
    }
  }

  public final static class Sm213Vm {
    public static void main (String[] args) {
      SimpleMachine.main (new String[] {"-a", "sm213-vm", "-v", "solution"}, args);
    }
  }

  public final static class Y86Seq {
    public static void main (String[] args) {
      SimpleMachine.main (new String[] {"-a", "y86-seq", "-v", "solution"}, args);
    }
  }

  public final static class Y86PipeMinus {
    public static void main (String[] args) {
      SimpleMachine.main (new String[] {"-a", "y86-pipe-minus", "-v", "solution"}, args);
    }
  }

  public final static class Y86Pipe {
    public static void main (String[] args) {
      SimpleMachine.main (new String[] {"-a", "y86-pipe", "-v", "solution"}, args);
    }
  }

  public final static class Y86PipeSuper {
    public static void main (String[] args) {
      SimpleMachine.main (new String[] {"-a", "y86-pipe-super", "-v", "solution"}, args);
    }
  }

  public final static class Sm213Student {
    public static void main (String[] args) {
      SimpleMachine.main (new String[] {"-a", "sm213", "-v", "student"}, args);
    }
  }

  public final static class Sm213VmStudent {
    public static void main (String[] args) {
      SimpleMachine.main (new String[] {"-a", "sm213-vm", "-v", "student"}, args);
    }
  }

  public final static class Y86SeqStudent {
    public static void main (String[] args) {
      SimpleMachine.main (new String[] {"-a", "y86-seq", "-v", "student"}, args);
    }
  }

  public final static class Y86PipeMinusStudent {
    public static void main (String[] args) {
      SimpleMachine.main (new String[] {"-a", "y86-pipe-minus", "-v", "student"}, args);
    }
  }

  public final static class Y86PipeStudent {
    public static void main (String[] args) {
      SimpleMachine.main (new String[] {"-a", "y86-pipe", "-v", "student"}, args);
    }
  }

  public final static void main (String[] args) {
    AbstractUI.main (args);
  }

  private final static void main (String[] args0, String[] args1) {
    List <String> argsList = Arrays.asList (args0);
    argsList.addAll (Arrays.asList (args1));
    String[] args = argsList.toArray (new String[0]);
    SimpleMachine.main (args);
  }
}