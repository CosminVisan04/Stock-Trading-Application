# Question 1

In the assignment, you had to create a `MessageHandler` interface. Please answer the following two questions:

1. Describe the benefits of using this `MessageHandler` interface. (~50-100 words)
2. Instead of creating an implementation of `MessageHandler` that invokes a command handler, we could also pass the command handler to the client/server directly without the middle man of the `MessageHandler` implementation. What are the implications of this? (~50-100 words)

___

**Answer**:
1) The MessageHandler interface provides a clear and standardized way for handling messages, promoting a modular.
 By encapsulating message handling functionality, it facilitates easy swapping of different message 
 processing implementations without affecting the client/server logic. This abstraction enhances code readability, 
 maintainability, and enables the incorporation of new message handling strategies seamlessly.


2) Directly passing the command handler to the client/server without an intermediary MessageHandler will lead to tighter coupling between components. The MessageHandler acts as a decoupling layer, allowing for flexibility in changing or extending the message handling process without modifying the client/server. Without it, modifications to the command handler could directly impact the client/server implementation.

___

# Question 2

One of your colleagues wrote the following class:

```java
public class RookieImplementation {

    private final Car car;

    public RookieImplementation(Car car) {
        this.car = car;
    }

    public void carEventFired(String carEvent) {
        if("steer.left".equals(carEvent)) {
            car.steerLeft();
        } else if("steer.right".equals(carEvent)) {
            car.steerRight();
        } else if("engine.start".equals(carEvent)) {
            car.startEngine();
        } else if("engine.stop".equals(carEvent)) {
            car.stopEngine();
        } else if("pedal.gas".equals(carEvent)) {
            car.accelerate();
        } else if("pedal.brake".equals(carEvent)) {
            car.brake();
        }
    }
}
```

This code makes you angry. Briefly describe why it makes you angry and provide the improved code below.

___

**Answer**:

It provides scalability and maintainability by allowing easy addition or removal of commands without modifying existing code. Additionally, it incorporates error handling for invalid commands, promoting robustness. The code's flexibility allows easy modification of command processing logic. This approach is based on the Single Responsibility Principle, resulting in a more modular and understandable implementation.

Improved code:

```java
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ImprovedRookieImplementation {

    private final Car car;
    private final Map<String, Consumer<Car>> actions;

    public ImprovedRookieImplementation(Car car) {
        this.car = car;
        this.actions = new HashMap<>();
        putPossibleCommands();
    }

    public void putPossibleCommands() {
        actions.put("steer.left", Car::steerLeft);
        actions.put("steer.right", Car::steerRight);
        actions.put("engine.start", Car::startEngine);
        actions.put("engine.stop", Car::stopEngine);
        actions.put("pedal.gas", Car::accelerate);
        actions.put("pedal.brake", Car::brake);
    }

    public void acceptEvent(String command) {
        Consumer<Car> action = actions.get(command);
        if (action != null) {
            action.accept(car);
        } else {
            System.out.println("Invalid command: " + command);
        }
    }
}
```
___

# Question 3

You have the following exchange with a colleague:

> **Colleague**: "Hey, look at this! It's super handy. Pretty simple to write custom experiments."

```java
class Experiments {
    public static Model runExperimentA(DataTable dt) {
        CommandHandler commandSequence = new CleanDataTableCommand()
            .setNext(new RemoveCorrelatedColumnsCommand())
            .setNext(new TrainSVMCommand())

        Config config = new Options();
        config.set("broadcast", true);
        config.set("svmdatatable", dt);

        commandSequence.handle(config);

        return (Model) config.get("svmmodel");
    }

    public static Model runExperimentB() {
        CommandHandler commandSequence = new CleanDataTableCommand()
            .setNext(new TrainSGDCommand())

        Config config = new Options();
        config.set("broadcast", true);
        config.set("sgddatatable", dt);

        commandSequence.handle(config);

        return (Model) config.get("sgdmodel");
    }
}
```

> **Colleague**: "I could even create this method to train any of the models we have. Do you know how Jane did it?"

```java
class Processor {
    public static Model getModel(String algorithm, DataTable dt) {
        CommandHandler commandSequence = new TrainSVMCommand()
            .setNext(new TrainSDGCommand())
            .setNext(new TrainRFCommand())
            .setNext(new TrainNNCommand())

        Config config = new Options();
        config.set("broadcast", false);
        config.set(algorithm + "datatable", dt);

        commandSequence.handle(config);

        return (Model) config.get(algorithm + "model");
    }
}
```

> **You**: "Sure! She is using the command pattern. Easy indeed."
>
> **Colleague**: "Yeah. But look again. There is more; she uses another pattern on top of it. I wonder how it works."

1. What is this other pattern? What advantage does it provide to the solution? (~50-100 words)

2. You know the code for `CommandHandler` has to be a simple abstract class in this case, probably containing four methods:
- `CommandHandler setNext(CommandHandler next)` (implemented in `CommandHandler`),
- `void handle(Config config)` (implemented in `CommandHandler`),
- `abstract boolean canHandle(Config config)`,
- `abstract void execute(Config config)`.

Please provide a minimum working example of the `CommandHandler` abstract class.

___

**Answer**:

1. The code uses the `Chain of Responsibility` pattern, by having improved flexibility and maintainability. This pattern decouples the client from various handlers, allowing easy updates without affecting the client. It ensures the customization of handler chains, promoting modular, flexible and bug-free code.
2.
	```java
    public abstract class CommandHandler {
        private CommandHandler next;

        public CommandHandler setNext(CommandHandler next) {
            this.next = next;
            return next;
        }

        public void handle(Config config) {
            if (canHandle(config)) {
                execute(config);
            } else if (next != null) {
                next.handle(config);
            }
        }

        protected abstract boolean canHandle(Config config);

        protected abstract void execute(Config config);
    }
	```
___
