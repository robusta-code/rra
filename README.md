What is it ?
===

Robusta Rest Adapter is a non-intrusive productivity library for RESTful projects, with special conveniance for GWT.

<div style="color:red">
Warning : This is Alpha state. Do not use in Production. Nevertheless, stable and tested Beta is coming very soon. I'll need people to propose the main features before RC release.
</div>


Why REST ?
===

RESTful web services allows communication between entities such as browser, Http servers or mobile phone. Robusta is written in Java but, because of REST, can talk with entities written in any other languages.

![](www.robusta.io/img/restful.png)


Robusta's Contents
===

The library contains :

* Http Clients, enabling easy and uniform RESTful requests
* A simple Resource interface for Domain Objects, with an id, representation and uri
* A Representation interface for Resources, or even any Class
* Xml and Json Representation implementations based on Jdom, JsonSimple, Gson
    * Or extends it to the technology you want
* JAX-RS ResourceController, that receives and handles Http requests
* Utilities and productivity classes

These five parts are independants! So you can create and handle REST requests with your own legacy domain classes. Or use productivity classes, such as XmlRepresentation? or CoupleList?<L,R>, for a non REST project.

A Framework ? Spilling out of the frame is OK
===

There is very loose Coupling between different Robusta's concepts. You can add Robusta to an existing project, and you won't be locked after a few years.

![ ](http://lh4.ggpht.com/_D-Y4QMlvJ98/TS8RUvAWpSI/AAAAAAAAA1o/xEAaToEMLP0/robustaUML.png&nonsense=something.png)

This diagram means that you can use Representation without using Robusta's Resource, ResourceController? without using Robusta's Representation. Pick what you need !

Simple creation of a Resource Representation
===

Let's say we have :

    User johnDoe = new User(1L, "john.doe@robusta.io", "John", "Doe");

where User implements the `Resource` interface.

Creating new representation of User like:

    Representation representation = new GsonRepresentation( johnDoe );

`representation.toString()` will return :

    {
        "id": 1,
        "email": "john.doe@robusta.io",
        "firstname": "John",
        "lastname": "Doe"
    }

This is often enough to get informations on the precious concept of User. But you still make easy changes with :

    representation.remove( "email" );
    System.out.println( representation );

This deletes the email in the Json representation :

    {
        "id": 1,
        "firstname": "John",
        "lastname": "Doe"
    }


