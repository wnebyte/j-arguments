# j-arguments
java-library

## About
A library for creating, validating, and parsing arguments. 

## Sample
Below code demonstrates how to create a collection of arguments.

    public class Sample {
        List<Argument> arguments = ArgumentFactory.builder().build()
                .setName("-a", "--a);
                .setIsRequired()
                .append(String.class)
                .setName("-b", "--b")
                .setIsOptional()
                .append(int.class)
                .get();
    }
