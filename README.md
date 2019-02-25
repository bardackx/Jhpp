# Jhpp
A Java serialization/deserialization library to convert Java Objects into .property files and back

## Goals
  * Provide simple `toProperties()` and `fromProperties()` methods to convert Java objects to properties file and vice-versa
  * Allow pre-existing unmodifiable objects to be converted to and from properties file

## Real goals
  * Enable people to use properties even for hierarchical data structures
  * If your company uses JSON, XML or —god forbids— YAML, to give you a reason to stick to plain old properties files
  
## Features

### Lists, Sets, Maps and Arrays

Properties:

This properties file can be parsed...

```properties
list.1.ammount	=	1.0
list.2.ammount	=	2.0
list.3.ammount	=	3.0

set.1.ammount	=	1.0
set.2.ammount	=	2.0
set.3.ammount	=	3.0

map.1.ammount	=	1.0
map.2.ammount	=	2.0
map.3.ammount	=	3.0

array.1.ammount	=	1.0
array.2.ammount	=	2.0
array.3.ammount	=	3.0
```

Java:

Into a ListSetMapArray object.

```java
class ListSetMapArray {
    private List<Measure> list;
    private Set<Measure> set;
    private Map<String, Measure> map;
    private Measure[] array;
    // ARGUMENTLESS CONSTRUCTOR, GETTERS, SETTERS, EQUALS, HASHCODE
}
class Measure {
    private double ammount;
    private String source;
    // ARGUMENTLESS CONSTRUCTOR, GETTERS, SETTERS, EQUALS, HASHCODE
}
```
  
## How to use Jhpp
The primary class to use is Gson which you can just create by calling `new Jhpp()`.

### From properties example

Properties:

```properties
pro.url         = pro.example.com
pro.email.user  = pro
pro.email.pass  = complex$Password123
pro.db.user     = myprosql
pro.db.pass     = different$Password123

qas.url         = qas.example.com
qas.email.user  = qas
qas.email.pass  = 1234<-easyPassword
qas.db.user     = myqassql
qas.db.pass     = 1234<-samePassword
```

Java:

Argumentless constructors, and getter and setter methods for each field are omitted for brevity, but they are required by the library

```java
class Environments {
    private String url;
    private Environment pro, qas;
    // ARGUMENTLESS CONSTRUCTOR, GETTERS, SETTERS, EQUALS, HASHCODE
}
class Environment {
    private Account email, db;
    // ARGUMENTLESS CONSTRUCTOR, GETTERS, SETTERS, EQUALS, HASHCODE
}
class Account {
    private String user, pass;
    // ARGUMENTLESS CONSTRUCTOR, GETTERS, SETTERS, EQUALS, HASHCODE
}

// deserialization
InputStream is = // any input stream, but it also accept Reader objects or java.util.Properties objects
Environments actual = new Jhpp().fromProperties(is, Environments.class);
```

### To properties example

This feature is not ready yet
