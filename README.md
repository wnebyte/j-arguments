# j-arguments

java-core-library
            
## About

This module contains classes for representing, building, creating, parsing and formatting 
options and arguments.<br>
            
## Documentation

This section provides documentation for 
the com.github.wnebyte.jarguments.util.ArgumentFactory class.
<h5>create parameters</h5>

- [name](#name)
- [description](#description)
- [required](#required)
- [choices](#choices)
- [metavar](#metavar)
- [defaultValue](#defaultvalue)
- [type](#type)
- [typeAdapter](#typeadapter)
- [constraints](#constraints)

### name

If name is set to <code>null</code> and [required](#required) is set to <code>true</code> a positional argument will be created.<br>
If name is not <code>null</code> then <code>required</code> will determine whether an optional or 
required argument is created.<br>
If name is not <code>null</code> and <code>required</code> is <code>false</code> and [type](#type) is set to 
<code>boolean</code>, a flag argument (special optional argument) will be created.<br>
Multiple names can be specified by separating them with a ','.<br>
            
Create a positional argument of type <code>String</code>: 

    factory.create(null, null, true,
            null, null, null, String.class);
            
Create an optional argument of type <code>String</code>: 

    factory.create("-f, --foo", null, false,
            null, null, null, String.class);
            
Create a required argument of type <code>String</code>: 

    factory.create("-f, --foo", null, true,
            null, null, null, String.class);

Create a flag argument:

    factory.create("-f, --foo", null, false,
            null, null, null, boolean.class);

### description

The description value is a string containing a brief description of the given argument. When 
a user requests help (), these descriptions will be displayed with each argument.

    factory.create("-f, --foo", "this is a brief description", false,
            null, null, null, String.class);

### required

Required arguments have to be included at the command line. Optional arguments on the other hand 
can be omitted.

    factory.create("-f, --foo", null, true,
            null, null, null, String.class);

### choices

Is used to constrain the value of an argument to a set of string values. Will not be applied on 
default values or on flag arguments.

    factory.create(null, null, true,
            String[]{"rock", "paper", "scissors"}, null, null, String.class);

### metavar

Is used by the help message formatter when referencing the given argument.

    factory.create(null, null, true,
            null, null, "FOO", String.class);

### defaultValue

All optional arguments may be omitted at the command line. 
If omitted the argument will be initialized using a default value.<br>
If this flag is not set; 
a default value returned by [typeAdapter](#typeadapter) will be used instead.

### type

The type of the argument.

### typeAdapter

Is an interface used to convert a string value into an instance of [type](#type).
The <code>ArgumentFactory</code> class's default <code>AbstractTypeAdapterRegistry</code> has 
implementations for and can convert the following types: <br>

<ul>
    <li>primitive types</li>
    <li>wrapper classes</li>
    <li>arrays where the component type is a primitive type</li>
    <li>arrays where the component type is a wrapper class</li>
</ul>

### constraints

Is used to apply arbitrary constraints on a value. Will not be applied on default values 
or on flag arguments.

    factory.create("-f, --foo", null, true,
            null, null, null, String.class, new ConstrintCollectionBuilder<String>()
                .addConstraint(new Constraint<String>() {
                    @Override
                    public boolean test(String s) {
                        return (s.length() == 3);
                    }
                    @Override
                    public String errorMessage() {
                        return "values length is not equal to 3."
                    }
                })
                .build());
                