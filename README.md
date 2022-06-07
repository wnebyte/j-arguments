# j-arguments

java-library

## About

This library declares an abstract <code>Argument</code> class, and the following subclasses: 
<code>Required</code>, <code>Positional</code>, <code>Optional</code> and <code>Flag</code>.<br>
            
### Documentation

- [name](#name)
- [description](#description)
- [required](#required)
- [choices](#choices)
- [metavar](#metavar)
- [defaultValue](#defaultvalue)
- [type](#type)
- [typeAdapter](#typeadapter)
- [constraints](#constraints)

#### name

If name is <code>null</code> and [required](#required) is <code>true</code> a Positional Argument will be created.<br>
If name is not <code>null</code>, <code>required</code> will determine whether an Optional or 
Required Argument is created.<br>
If name is not <code>null</code> and <code>required</code> is <code>false</code> and [type](#type) is set to 
<code>boolean</code>, a Flag Argument will be created.<br>
Multiple names can be specified by separating them with a ','.<br>
            
Create a Positional Argument of type <code>String</code>: 

    factory.create(null, null, true,
            null, null, null, String.class);
            
Create an Optional Argument of type <code>String</code>: 

    factory.create("-f, --foo", null, false,
            null, null, null, String.class);
            
Create a Required Argument of type <code>String</code>: 

    factory.create("-f, --foo", null, true,
            null, null, null, String.class);

Create a Flag Argument of type <code>boolean</code>:

    factory.create("-f, --foo", null, false,
            null, null, null, boolean.class);

#### description

The description value is a string containing a brief description of the given argument. When 
a user requests help (), these descriptions will be displayed with each argument.

    factory.create("-f, --foo", "this is a brief description", false,
            null, null, null, String.class);

#### required

Required Arguments have to be included at the command line. Optional Arguments on the other hand 
can be omitted.

    factory.create("-f, --foo", null, true,
            null, null, null, String.class);

#### choices

Specify that a command line value for a given argument should be constrained 
to a set of string values. 

    factory.create(null, null, true,
            String[]{"rock", "paper", "scissors"}, null, null, String.class);

#### metavar

Is used by the help message to refer to the given argument.

    factory.create(null, null, true,
            null, null, "DATA", String.class);

#### defaultValue

All optional and flag arguments may be omitted at the command line. 
If omitted, they need to be initialized using a default value.<br>
If this attribute is not specified, 
the default value returned [typeAdapter](#typeadapter) will be used 
to initialize the given argument. 

#### type

Specify the <code>Class<?></code> of the given argument when initialized.

#### typeAdapter

Is used to convert the given argument when in the form of a string, into 
an instance of the specified <code>type</code>.<br>

#### constraints

Arbitrary constraints to apply on the initialized argument. 